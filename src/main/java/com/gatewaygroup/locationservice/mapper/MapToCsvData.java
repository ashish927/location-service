package com.gatewaygroup.locationservice.mapper;

import com.gatewaygroup.locationservice.model.CsvData;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class MapToCsvData {

    public static CsvData map(Object result){
        CsvData csvData = new CsvData();
        Map<String, String> components = (Map<String, String>) ((LinkedHashMap) result).get("components");
        Map<String, String> geometry = (Map<String, String>) ((LinkedHashMap) result).get("geometry");
        csvData.setContinent(components.get("continent"));
        csvData.setCountry(components.get("country"));
        csvData.setState(components.get("state"));
        csvData.setCity(components.get("city"));
        csvData.setLatitude(String.valueOf(geometry.get("lat")));
        csvData.setLongitude(String.valueOf(geometry.get("lng")));
        return csvData;
    }
}
