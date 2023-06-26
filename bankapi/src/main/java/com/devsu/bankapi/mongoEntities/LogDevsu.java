package com.devsu.bankapi.mongoEntities;

import com.devsu.bankapi.utils.abstracts.AbstractResponse;
import com.devsu.bankapi.utils.abstracts.AdministrativeAbstractRequest;
import com.devsu.bankapi.utils.generics.ErrorList;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Document
public class LogDevsu {

    @Id
    private ObjectId id;

    private boolean success;

    private LocalDateTime timeRequestStart;

    private LocalDateTime timeRequestEnd;

    private long millisecondsToResponse;

    private String url;

    private Object[] requestParams;

    private AdministrativeAbstractRequest requestBody;

    private AbstractResponse response;

    private ErrorList errors;

}
