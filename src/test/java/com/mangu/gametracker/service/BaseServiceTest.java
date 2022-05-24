package com.mangu.gametracker.service;

import com.mangu.gametracker.model.Entity;
import com.mangu.gametracker.model.PriceRecord;
import com.mangu.gametracker.model.enums.Category;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

public abstract class BaseServiceTest {
    static String getResourceFileAsString(String fileName) {
        InputStream is = getResourceFileAsInputStream(fileName);
        if (is != null) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            return reader.lines().collect(Collectors.joining(System.lineSeparator()));
        } else {
            throw new RuntimeException("resource not found");
        }
    }

    private static InputStream getResourceFileAsInputStream(String fileName) {
        ClassLoader classLoader = ParserServiceTest.class.getClassLoader();
        return classLoader.getResourceAsStream(fileName);
    }

    Entity buildExampleEntity() {
        PriceRecord priceRecord = new PriceRecord(Long.toString(Instant.now().getEpochSecond()), 19.99f, true);
        return Entity.builder()
                .name("Pokemon Gold")
                .category(Category.VIDEOJUEGOS)
                .url("http://example.com")
                .priceRecordList(List.of(priceRecord))
                .build();
    }
}
