package com.gatewaygroup.locationservice.processor;

import com.gatewaygroup.locationservice.model.CityRequest;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class RequestProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {

        CityRequest cityRequest = (CityRequest) exchange.getIn(CityRequest.class);
        System.out.println(exchange.getMessage().getBody());
    }


}
