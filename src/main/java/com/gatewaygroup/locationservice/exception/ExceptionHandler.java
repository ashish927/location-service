package com.gatewaygroup.locationservice.exception;

import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import java.util.HashMap;
import java.util.Map;

@Component
public class ExceptionHandler {
     static final Logger logger = LoggerFactory.getLogger(ExceptionHandler.class);
     static Map<Class, HttpStatus> exceptionMap = new HashMap<>();

    static {
        exceptionMap.put(HttpClientErrorException.Unauthorized.class, HttpStatus.UNAUTHORIZED );
        exceptionMap.put(HttpClientErrorException.BadRequest.class, HttpStatus.BAD_REQUEST );
        exceptionMap.put(HttpClientErrorException.UnprocessableEntity.class, HttpStatus.UNPROCESSABLE_ENTITY );
        exceptionMap.put(RuntimeException.class, HttpStatus.INTERNAL_SERVER_ERROR );
    }

    public void prepareResponseBody(Exchange exchange, Throwable e){
        logger.error(e.getMessage(), e);
        exchange.getIn().setHeader(Exchange.CONTENT_TYPE, "application/json");
        if(exceptionMap.containsKey(e.getClass())){
            exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, exceptionMap.get(e.getClass()));
        }
    }

}
