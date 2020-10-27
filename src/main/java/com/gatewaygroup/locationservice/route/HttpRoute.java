package com.gatewaygroup.locationservice.route;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class HttpRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("direct:httpRoute")
                .log("Http Route started")
                .setHeader("Content-Type", constant("application/json"))
                .setHeader("Accept", constant("application/json"))
                .setHeader(Exchange.HTTP_METHOD, constant("GET"))
                .removeHeader(Exchange.HTTP_PATH)
                .to("log:DEBUG?showBody=true&showHeaders=true")
                .setHeader(Exchange.HTTP_QUERY, simple("&key=${in.headers.key}&q=${in.headers.q}&q=${in.headers.q}"))
                .toD("${in.headers.uri}")
                .streamCaching()
                .convertBodyTo(Map.class)
                .marshal().json(JsonLibrary.Jackson)
                .log("Response : ${body}");
    }
}
