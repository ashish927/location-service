package com.gatewaygroup.locationservice.route;

import com.gatewaygroup.locationservice.service.LocationService;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

@Component
public class WebRoute extends RouteBuilder {

    @Override
    public void configure() {
        restConfiguration()
                .contextPath("/gatewaygroup/api/")
                .apiContextPath("/api-doc")
                .apiProperty("api.title", "Location Service REST API")
                .apiProperty("api.version", "1.0.0")
                .apiProperty("cors", "true")
                .apiContextRouteId("doc-api")
                .port( "8080")
                .bindingMode(RestBindingMode.json);


        rest("/location").description("Details of city")
                .consumes("application/json")
                .produces("application/json")

                .post().description("create city details")
                .route().routeId("create-city-details")
                .marshal().json(JsonLibrary.Jackson)
                .bean(LocationService.class, "createCityDetails")
                .endRest()

                .get("/{city}").description("List of city")
                .route().routeId("get-city-details")
                .bean(LocationService.class, "getCityDetails(${header.city})")
                .endRest();

    }

}
