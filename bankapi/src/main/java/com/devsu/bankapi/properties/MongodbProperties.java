package com.devsu.bankapi.properties;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "custom.mongo")
public class MongodbProperties {

    @NotNull
    private String host;

    @NotNull
    private Integer port;

    @NotNull
    private String database;

    @NotNull
    private String collectionLog;

    @NotNull
    private String user;

    @NotNull
    private String password;

    @NotNull
    private Integer timeOut;

}