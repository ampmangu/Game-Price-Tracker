package com.mangu.gametracker.service;

import com.mangu.gametracker.model.Entity;
import com.mangu.gametracker.model.PriceRecord;
import com.mangu.gametracker.repository.EntityRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

@Service
public class DBServiceImpl implements DBService {

    private final EntityRepository entityRepository;
    private final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public DBServiceImpl(EntityRepository entityRepository) {
        this.entityRepository = entityRepository;
    }

    @Override
    public Entity insert(Entity entity) {
        String url = entity.getUrl();
        List<Entity> byUrl = entityRepository.findByUrl(url);
        if (byUrl.isEmpty()) {
            return entityRepository.insert(entity);
        }
        Entity originalEntity = byUrl.get(0);
        List<PriceRecord> originalRecordList = originalEntity.getPriceRecordList();
        List<PriceRecord> newRecordList = entity.getPriceRecordList();
        List<PriceRecord> merged = mergeRecords(originalRecordList, newRecordList);
        originalEntity.setPriceRecordList(merged);
        return entityRepository.save(originalEntity);
    }

    private List<PriceRecord> mergeRecords(List<PriceRecord> originalRecordList, List<PriceRecord> newRecordList) {
        List<PriceRecord> merged = new ArrayList<>(originalRecordList);
        for (PriceRecord newRecord : newRecordList) {
            String formatedOriginalDate = formatDate(newRecord.date());
            List<String> listOfDates = originalRecordList.stream().map(pr -> formatDate(pr.date()))
                    .filter(date -> date.equalsIgnoreCase(formatedOriginalDate)).toList();
            if (listOfDates.isEmpty()) {
                merged.add(newRecord);
                break;
            }
            AbstractMap.SimpleEntry<String, Float> newPair =
                    new AbstractMap.SimpleEntry<>(formatDate(newRecord.date()), newRecord.price());
            long count = originalRecordList.stream().map(r -> new AbstractMap.SimpleEntry<>(formatDate(r.date()), r.price()))
                    .filter(se -> !se.equals(newPair)).count();
            if (count > 0) {
                merged.add(newRecord);
            }
        }
        return merged;
    }

    private String formatDate(String date) {
        return Instant.ofEpochSecond(Long.parseLong(date))
                .atZone(ZoneId.of("Europe/Madrid")).format(formatter);
    }
}
