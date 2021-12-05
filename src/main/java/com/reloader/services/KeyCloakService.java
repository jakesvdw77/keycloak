package com.reloader.services;

import com.reloader.error.AuthorizationError;
import com.reloader.error.KeycloakError;
import com.reloader.error.UserExistsException;
import com.reloader.error.UserNotFoundException;
import com.reloader.models.LoginUser;
import com.reloader.models.NewUser;
import com.reloader.models.UserProfileDTO;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.ws.rs.core.Response;
import java.util.*;

@Service
public class KeyCloakService {

    private final RestTemplate restTemplate;

    private final Keycloak keycloak;

    private final ModelMapper modelMapper;

    @Value("${authentication.keycloak.url}")
    private String authenticationUrl;
    @Value("${keycloak.resource}")
    private String keyCloakResource;
    @Value("${keycloak.realm}")
    private String keyCloakRealm;
    @Value("${reloader.keycloak.admin}")
    private String keyCloakAdmin;

    public KeyCloakService(Keycloak keycloak, RestTemplate restTemplate, ModelMapper modelMapper) {
        this.restTemplate = restTemplate;
        this.modelMapper = modelMapper;
        this.keycloak = keycloak;
    }

    public UserProfileDTO authenticate(LoginUser loginUser) {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("client_id", Arrays.asList(keyCloakResource));
        map.put("password", Arrays.asList(loginUser.getPassword()));
        map.put("username", Arrays.asList(loginUser.getUserName()));
        map.put("grant_type", Arrays.asList("password"));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        try {
            var result = restTemplate.postForEntity(authenticationUrl, request, LinkedHashMap.class).getBody();
            var userInfo = loadProfile(loginUser.getUserName());
            var userProfileDTO = modelMapper.map(userInfo, UserProfileDTO.class);
            userProfileDTO.setToken(result.get("access_token").toString());
            userProfileDTO.setExpires(Long.parseLong(result.get("expires_in").toString()));

            return userProfileDTO;
        } catch (HttpClientErrorException err) {
            if (err.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                throw new AuthorizationError("Email or Password is incorrect");

            } else if (err.getStatusCode() == HttpStatus.BAD_REQUEST) {
                throw new KeycloakError(err.getMessage());
            }
            throw new UserNotFoundException(err.getMessage());
        } catch (Exception err) {

            throw new UserNotFoundException(err.getMessage());
        }

    }

    public void createUser(NewUser newUser) {
        UserRepresentation user = new UserRepresentation();
        List actions = new ArrayList<>();
        user.setEnabled(true);
        user.setEmailVerified(true);
        user.setUsername(newUser.getEmail());
        user.setFirstName(newUser.getName());
        user.setLastName(newUser.getSurname());
        user.setEmail(newUser.getEmail());
        user.setRequiredActions(actions);

        List realmRoles = new ArrayList();
        realmRoles.add("app-user");
        user.setRealmRoles(realmRoles);


//        List clientRoles = new ArrayList();
//        clientRoles.add("user");
//        Map<String,List<String>> clientMap = new HashMap<>();
//        clientMap.put("reloader-management",clientRoles);
//        user.setClientRoles(clientMap);


        //UserConsentRepresentation consentRepresentation = new UserConsentRepresentation();

        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setTemporary(true);
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        credentialRepresentation.setValue(newUser.getPassword());
        user.setCredentials(Collections.singletonList(credentialRepresentation));


        // Get realm
        RealmResource realmResource = keycloak.realm(keyCloakRealm);
        UsersResource userResource = realmResource.users();

// Create user (requires manage-users role)
        Response response = userResource.create(user);


        System.out.println("Repsonse: " + response.getStatusInfo());

        if (response.getStatus() == 409)
            throw new UserExistsException("User already exists");


    }

    public UserRepresentation loadProfile(String userName) {
        // Get realm
        RealmResource realmResource = keycloak.realm(keyCloakRealm);
        UsersResource userResource = realmResource.users();

        if (userName.equals(keyCloakAdmin))
            userName = "admin";

        var result = userResource.search(userName);

        if (result.isEmpty())
            throw new UserNotFoundException("User Profile not found");

        return result.get(0);


    }
}
