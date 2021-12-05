package com.reloader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.reloader")
public class ReloadManagementApplication
{
    public static void main(String[] args) {
        SpringApplication.run(ReloadManagementApplication.class, args);
    }

}
