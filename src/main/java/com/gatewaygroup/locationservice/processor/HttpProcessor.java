package com.gatewaygroup.locationservice.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

@Component
public class HttpProcessor implements Processor {


    @Override
    public void process(Exchange exchange) throws Exception {
//        Map res = (Map) exchange.getIn().getBody();
        System.out.println(new ObjectMapper().writeValueAsString(exchange.getIn().getBody()));
        //exchange.getMessage().setBody("City record successfully created");
    }
}
