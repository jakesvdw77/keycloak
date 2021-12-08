package com.reloader.configs;

import org.keycloak.admin.client.Keycloak;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakClientConfig {

    @Value("${keycloak.resource}")
    private String keyCloakResource;

    @Value("${keycloak.auth-server-url}")
    private String keycloakServer;

    @Value("${keycloak.realm}")
    private String keyCloakRealm;


    @Value("${admin.keycloak.user}")
    private String user;

    @Value("${admin.keycloak.pwd}")
    private String pwd;


    @Bean
    public Keycloak getKeycloak() {
        Keycloak keycloak = Keycloak.getInstance(
                keycloakServer,
                keyCloakRealm,
                user, // Change to your admin name
                pwd, // Change to your admin password
                keyCloakResource);

        return keycloak;
    }


}
