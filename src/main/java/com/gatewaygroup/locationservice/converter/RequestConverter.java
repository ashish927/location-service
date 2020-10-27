package com.gatewaygroup.locationservice.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gatewaygroup.locationservice.model.CityRequest;
import org.apache.camel.Converter;
import org.apache.camel.TypeConverters;
import org.apache.camel.converter.stream.InputStreamCache;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class RequestConverter implements TypeConverters {


    @Converter
    public byte[] cityRequestToByteArray(CityRequest source) {
        try {
            return new ObjectMapper().writeValueAsBytes(source);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    @Converter
    public CityRequest byteArrayToCityRequest(byte[] source) {
        try {
            return new ObjectMapper().readValue(source, CityRequest.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Converter
    public Map inputStreamCacheToCityMap(InputStreamCache source) {
        try {
            return new ObjectMapper().readValue(source.readAllBytes(), Map.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
