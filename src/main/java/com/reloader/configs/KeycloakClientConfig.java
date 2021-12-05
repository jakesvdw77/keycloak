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



    @Bean
    public Keycloak getKeycloak() {
        Keycloak keycloak = Keycloak.getInstance(
                keycloakServer,
                "ultimate-reloader-realm",
                "admin", // Change to your admin name
                "admin", // Change to your admin password
                "reloader-mananagement",
                "6032e00f-a343-47dd-97aa-68f9f6b353d7");

        return keycloak;
    }


}
