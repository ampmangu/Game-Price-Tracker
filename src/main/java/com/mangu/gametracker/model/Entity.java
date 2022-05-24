package com.mangu.gametracker.model;

import com.mangu.gametracker.model.enums.Category;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document("entity")
@Getter
@Setter
@Builder(toBuilder = true)
public class Entity {
    @Id
    private String id;

    private String name;
    private Category category;
    private List<PriceRecord> priceRecordList;
    private String url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<PriceRecord> getPriceRecordList() {
        return priceRecordList;
    }

    public void setPriceRecordList(List<PriceRecord> priceRecordList) {
        this.priceRecordList = priceRecordList;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
