package com.devsu.bankapi.config;

import com.devsu.bankapi.properties.MongodbProperties;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

@Configuration
@Slf4j
@EnableMongoRepositories(basePackages = "com.devsu.bankapi.repositories.mongoRepositories")
public class MongoConfig extends AbstractMongoClientConfiguration {

    private final MongodbProperties mongodbProperties;

    public MongoConfig(MongodbProperties mongodbProperties) { this.mongodbProperties = mongodbProperties; }

    @Override
    protected String getDatabaseName() {
        return mongodbProperties.getDatabase();
    }

    @Override
    public MongoClient mongoClient() {
        ConnectionString connectionString = new ConnectionString("mongodb://" + mongodbProperties.getUser() + ":" +
                mongodbProperties.getPassword() + "@" + mongodbProperties.getHost() + ":" +
                mongodbProperties.getPort() + "/" + mongodbProperties.getDatabase()
                + "?connectTimeoutMS=" + mongodbProperties.getTimeOut());
        log.info("mongodb://" + mongodbProperties.getUser() + ":" +
                mongodbProperties.getPassword() + "@" + mongodbProperties.getHost() + ":" +
                mongodbProperties.getPort() + "/" + mongodbProperties.getDatabase());

        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .applyToClusterSettings(builder ->
                        builder.serverSelectionTimeout(mongodbProperties.getTimeOut(), TimeUnit.MILLISECONDS))
                .build();
        return MongoClients.create(mongoClientSettings);
    }

    @Override
    protected Collection<String> getMappingBasePackages() {
        Package mappingBasePackage = getClass().getPackage();
        return Collections.singleton(mappingBasePackage == null ? null : mappingBasePackage.getName());
    }

}
