package com.devsu.bankapi.utils.functions.decorators;

import com.devsu.bankapi.mongoEntities.LogDevsu;
import com.devsu.bankapi.repositories.mongoRepositories.LogDevsuRepository;
import com.devsu.bankapi.utils.abstracts.AbstractResponse;
import com.devsu.bankapi.utils.abstracts.AdministrativeAbstractRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;

@Aspect
@Component
@Slf4j
public class LogDevsuAspect {

    private final LogDevsuRepository logDevsuRepository;

    public LogDevsuAspect(
            LogDevsuRepository logDevsuRepository
    ){
        this.logDevsuRepository = logDevsuRepository;
    }

    @Around("@annotation(LogDevsu)")
    public Object trace(ProceedingJoinPoint joinPoint) throws Throwable {

        long startTime = System.currentTimeMillis();
        LocalDateTime start = LocalDateTime.now();

        ResponseEntity<AbstractResponse> result = (ResponseEntity<AbstractResponse>) joinPoint.proceed();

        long endTime = System.currentTimeMillis();
        LocalDateTime end = LocalDateTime.now();

        LogDevsu logDevsu = new LogDevsu(
                ObjectId.get(),
                Objects.requireNonNull(result.getBody()).getSuccess(),
                start,
                end,
                endTime - startTime,
                joinPoint.getSignature().getName(),
                null,
                null,
                result.getBody(),
                Objects.requireNonNull(result.getBody()).getSuccess() ? null :
                        result.getBody().getErrors()
        );

        if (joinPoint.getArgs().length > 0){
            if (joinPoint.getArgs()[0] instanceof AdministrativeAbstractRequest){
                logDevsu.setRequestBody((AdministrativeAbstractRequest)joinPoint.getArgs()[0]);
            }else{
                logDevsu.setRequestParams(joinPoint.getArgs());
            }
        }

        try {
            logDevsuRepository.save(logDevsu);
        }catch (Exception e){
            log.error("Mongo - No se pudo conectar a la base de datos");
            log.info(logDevsu.toString());
        }

        return result;

    }
}
