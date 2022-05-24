package com.mangu.gametracker.service;

import com.mangu.gametracker.model.Entity;
import com.mangu.gametracker.model.enums.Category;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class ParserServiceTest extends BaseServiceTest{

    @Test
    void testParseVideogame() {
        ParserService parserService = new ParserServiceImpl();
        Entity parse = parserService.parse(getParsingSample(), "https://www.game.es/VIDEOJUEGOS/ACCION/PLAYSTATION-4/MARVELS-SPIDER-MAN/139473");
        assertAll(() -> {
            assertNotNull(parse);
            assertEquals(2, parse.getPriceRecordList().size());
            assertTrue(parse.getName().toUpperCase().contains("SPIDER-MAN"));
            assertEquals(Category.VIDEOJUEGOS, parse.getCategory());
        });
    }

    private String getParsingSample() {
        return getResourceFileAsString("samples/game_sample.html");
    }


}
