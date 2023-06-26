package com.devsu.bankapi.repositories.mongoRepositories;

import com.devsu.bankapi.mongoEntities.LogDevsu;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LogDevsuRepository
    extends MongoRepository<LogDevsu, ObjectId> {

}
