package com.example.xmcyodc.util;

import com.example.xmcyodc.model.CryptoCurrency;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Component
public class CsvUtility {

    @Value("${csv.folder.path}")
    private String csvFolderPath;


    private static CsvMapper mapper;
    private final ResourceLoader resourceLoader;

    public CsvUtility(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

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
        Resource[] resources = ResourcePatternUtils.getResourcePatternResolver(resourceLoader)
                .getResources(csvFolderPath + "/*.csv");
        for (Resource resource : resources) {
            csvFileNames.add(resource.getFilename());
        }
        return csvFileNames;
    }


    public List<CryptoCurrency> readCsvFileOfCrypto(String fileName) throws IOException {
        CsvMapper mapper = getMapper();
        CsvSchema schema = mapper.schemaFor(CryptoCurrency.class).withHeader();
        Resource resource = resourceLoader
                .getResource(csvFolderPath + "/" + fileName.toUpperCase(Locale.US) + "_values.csv");
        if (!resource.exists()) {
            throw new FileNotFoundException("Resource " + fileName + " does not exist");
        }
        if (!resource.isReadable()) {
            throw new IOException("Resource " + fileName + " is not readable");
        }
        File file = resource.getFile();
        MappingIterator<CryptoCurrency> iterator = mapper.readerFor(CryptoCurrency.class)
                .with(schema).readValues(file);
        return iterator.readAll();
    }

}
