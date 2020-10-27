package com.gatewaygroup.locationservice.util;

import com.gatewaygroup.locationservice.model.CsvData;
import com.opencsv.bean.*;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Component
public class FileUtil {

    static final String CSV_LOCATION = "src/main/resources/city-result.csv";

    public static void writeIntoCsv(List<CsvData> dataList) {
        if(readFromCsv().size() > 0){
            dataList.addAll(readFromCsv());
        }
        FileWriter writer = null;
        try {
            writer = new FileWriter(CSV_LOCATION);
            ColumnPositionMappingStrategy mappingStrategy = new ColumnPositionMappingStrategy();
            mappingStrategy.setType(CsvData.class);
            String[] columns = new String[]{"continent", "country", "state", "city", "latitude", "longitude"};
            mappingStrategy.setColumnMapping(columns);
            StatefulBeanToCsvBuilder<CsvData> builder = new StatefulBeanToCsvBuilder(writer);
            StatefulBeanToCsv beanWriter = builder.withMappingStrategy(mappingStrategy).build();
            beanWriter.write(dataList);
            writer.close();
        } catch (IOException | CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<CsvData> readFromCsv() {
        try (
                Reader reader = Files.newBufferedReader(Paths.get(CSV_LOCATION));
        ) {
            ColumnPositionMappingStrategy strategy = new ColumnPositionMappingStrategy();
            strategy.setType(CsvData.class);
            String[] memberFieldsToBindTo = new String[]{"continent", "country", "state", "city", "latitude", "longitude"};
            strategy.setColumnMapping(memberFieldsToBindTo);

            CsvToBean<CsvData> csvToBean = new CsvToBeanBuilder(reader)
                    .withMappingStrategy(strategy)
                    .withSkipLines(1)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            List<CsvData> csvData = csvToBean.parse();
            return csvData;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
