package com.mangu.gametracker.service;

import com.mangu.gametracker.model.Entity;
import com.mangu.gametracker.model.PriceRecord;
import com.mangu.gametracker.repository.EntityRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({MockitoExtension.class})
@DataMongoTest
class DbServiceTest extends BaseServiceTest{

    @Autowired
    EntityRepository entityRepository;

    private DBService dbService;

    @BeforeEach
    public void setUp() throws Exception {
        dbService = new DBServiceImpl(entityRepository);
    }
    @Test
    void testOneInsert() {
        dbService.insert(buildExampleEntity());
        assertFalse(entityRepository.findAll().isEmpty());
    }

    @Test
    void testInsertWithSamePriceKeepsSameEntity() {
        dbService.insert(buildExampleEntity());
        assertEquals(1, entityRepository.findAll().size());
        dbService.insert(buildExampleEntity());
        assertFalse(entityRepository.findAll().size() > 1);
    }

    @Test
    void testInsertUpdatedPriceUpdatesEntity() {
        dbService.insert(buildExampleEntity());
        PriceRecord newPriceRecord = new PriceRecord(Long.toString(Instant.now().plusSeconds(86400).getEpochSecond()), 10.99f, true);
        Entity newEntity = buildExampleEntity();
        newEntity.setPriceRecordList(List.of(newPriceRecord));
        Entity updated = dbService.insert(newEntity);
        assertEquals(2, updated.getPriceRecordList().size());
    }

    @AfterEach
    void cleanUpDatabase() {
        entityRepository.deleteAll();
    }
}
