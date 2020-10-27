package com.gatewaygroup.locationservice.service;

import com.gatewaygroup.locationservice.converter.RequestConverter;
import com.gatewaygroup.locationservice.mapper.MapToCsvData;
import com.gatewaygroup.locationservice.model.CityRequest;
import com.gatewaygroup.locationservice.model.CsvData;
import com.gatewaygroup.locationservice.route.HttpRoute;
import com.gatewaygroup.locationservice.util.FileUtil;
import org.apache.camel.Body;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.impl.DefaultCamelContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("LocationService")
public class LocationService {

    @Value("${opencagedata.api.key}")
    private String key;

    @Value("${opencagedata.api.uri}")
    private String uri;

    @Autowired
    private MapToCsvData mapToCsvData;

    ProducerTemplate producerTemplate;

    public LocationService() throws Exception {
        CamelContext context = new DefaultCamelContext();
        HttpRoute httpRoute = new HttpRoute();
        context.addRoutes(httpRoute);
        context.setDebugging(true);
        context.getTypeConverterRegistry().addTypeConverters(new RequestConverter());
        context.start();
        this.producerTemplate = context.createProducerTemplate();

    }

    public ResponseEntity<List<CsvData>> getCityDetails(String city) {
        List<CsvData> csvData = FileUtil.readFromCsv();
        List<CsvData> responseDate = csvData.stream()
                .filter(data-> data.getCity().equalsIgnoreCase(city)).collect(Collectors.toList());
        return new ResponseEntity<>(responseDate, HttpStatus.OK);
    }

    public ResponseEntity<String> createCityDetails(@Body CityRequest cityRequest) {
        Map<String, Object> headers = getHeaders(cityRequest);
        Map result = producerTemplate.requestBodyAndHeaders("direct:httpRoute", null, headers, Map.class);
        List<CsvData> csvData = getCsvData((List) result.get("results"));
        FileUtil.writeIntoCsv(csvData);
        return new ResponseEntity<>("City details successfully created", HttpStatus.CREATED);
    }

    private List<CsvData> getCsvData(List results) {
        List<CsvData> csvDataList = new ArrayList<>();
        results.forEach(result -> {
            csvDataList.add(MapToCsvData.map(result));
        });
        return csvDataList;
    }

    private Map<String, Object> getHeaders(CityRequest cityRequest) {
        Map<String, Object> headers = new HashMap<>();
        headers.put("key", key);
        headers.put("q", cityRequest.getCityName());
        headers.put("uri", uri);
        return headers;
    }
}
