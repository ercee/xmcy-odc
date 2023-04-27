package com.example.xmcyodc.util;

import com.example.xmcyodc.model.CryptoCurrency;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Component
public class CsvUtility extends DataReader {

    @Value("${csv.folder.path}")
    private String csvFolderPath;


    private static CsvMapper mapper;

    public static CsvMapper getMapper() {
        if (mapper == null) {
            mapper = new CsvMapper();
            mapper.configure(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS, false);
            mapper.registerModule(new JavaTimeModule());
        }
        return mapper;
    }

    public List<String> listCsvFiles() throws IOException {
        List<String> csvFileNames = new ArrayList<>();

        ClassPathResource resource = new ClassPathResource(csvFolderPath);
        File folder = resource.getFile();
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles((dir, name) -> name.endsWith(".csv"));
            for (File file : files) {
                csvFileNames.add(file.getName());
            }
        }
        return csvFileNames;
    }

    public List<CryptoCurrency> readCsvFileOfCrypto(String fileName) throws IOException {
        CsvMapper mapper = getMapper();
        mapper.registerModule(new JavaTimeModule());
        CsvSchema schema = mapper.schemaFor(CryptoCurrency.class).withHeader();
        ClassPathResource resource = new ClassPathResource(csvFolderPath + "/" + fileName.toUpperCase(Locale.US) + "_values.csv");
        File file = resource.getFile();
        MappingIterator<CryptoCurrency> iterator = mapper.readerFor(CryptoCurrency.class)
                .with(schema).readValues(file);
        return iterator.readAll();
    }
}
