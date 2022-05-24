package com.mangu.gametracker.service;

import com.mangu.gametracker.model.Entity;
import com.mangu.gametracker.model.PriceRecord;
import com.mangu.gametracker.model.enums.Category;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

@Service
public class ParserServiceImpl implements ParserService {
    private List<String> consoles;
    public ParserServiceImpl() {
        this.consoles = List.of("Series X", "Nintendo Switch", "Playstation");
    }
    @Override
    public Entity parse(String htmlString, String url) {
        Document doc = Jsoup.parse(htmlString);
        String name = doc.getElementsByClass("product-title").text();
        return Entity.builder()
                .name(name)
                .category(getCategory(doc))
                .priceRecordList(getPrices(doc))
                .url(url)
                .build();

    }

    private List<PriceRecord> getPrices(Document doc) {
        Element newElement = null;
        if (!doc.getElementsByClass("buy-xl buy-new").isEmpty()) {
            //is physical game
            newElement = doc.getElementsByClass("buy-xl buy-new").get(0);
        }
        if (!doc.getElementsByClass("buy-xl buy-digital").isEmpty()) {
            //is digital
            newElement = doc.getElementsByClass("buy-xl buy-digital").get(0);
        }
        if (!doc.getElementsByClass("buy-l buy-new").isEmpty()) {
            newElement = doc.getElementsByClass("buy-l buy-new").get(0);
        }
        return getPrices(doc, newElement);
    }

    private List<PriceRecord> getPrices(Document doc, Element newElement) {
        if (newElement == null) {
            throw new RuntimeException("No prices available");
        }
        Element priceNew = newElement.getElementsByClass("buy--price").get(0);
        String priceNewFormed = formPrice(priceNew);
        if(priceNewFormed.contains("mes")) {
            priceNewFormed = searchTotalPrice(newElement);
        }
        PriceRecord priceRecord = new PriceRecord(Long.toString(Instant.now().getEpochSecond()),
                Float.parseFloat(priceNewFormed), true);

        Elements oldElements = doc.getElementsByClass("buy-xl buy-preowned");
        if (oldElements.isEmpty()) {
            return List.of(priceRecord);
        }

        Element oldPrice = oldElements.get(0).getElementsByClass("buy--price").get(0);
        PriceRecord oldRecord = new PriceRecord(Long.toString(Instant.now().getEpochSecond()),
                Float.parseFloat(formPrice(oldPrice)), false);
        return List.of(priceRecord, oldRecord);
    }

    private String searchTotalPrice(Element newElement) {
        //edge case for consoles with financing
        if (newElement.getElementsByClass("buy--info").isEmpty()) {
            throw new RuntimeException("");
        }
        String price = newElement.getElementsByClass("buy--info").text().split(" ")[0];
        if (price.contains(",")) {
            return price.replace(",", ".");
        }
        return price;
    }

    private String formPrice(Element priceElement) {
        //priceElement.getElementsByClass("int").get(0).getElementsByTag("span")
        String intSide = priceElement.getElementsByClass("int").text();
        if (priceElement.getElementsByClass("int").get(0).html().contains("u-line-through")) {
            intSide = priceElement.getElementsByClass("int").get(0).text().split(" ")[0];
        }
        return intSide + "." + priceElement.getElementsByClass("decimal").text().replace("'", "");

    }

    private Category getCategory(Document doc) {
        Element element = doc.getElementsByClass("header-breadcrumbs").get(0);
        int childrenSize = element.childrenSize();
        if (childrenSize == 0) {
            return Category.DEFAULT;
        }
        Element child = element.child(childrenSize - 2);//last one is the title, previous can be category
        Category found = Arrays.stream(Category.values()).filter(v -> v.name().equalsIgnoreCase(child.text()))
                .findFirst().orElse(Category.DEFAULT);
        if (found.equals(Category.DEFAULT)) {
            found = findConsoleCategories(doc);
        }
        return found;
    }

    private Category findConsoleCategories(Document doc) {
        String productInfo = doc.getElementsByClass("product-info").text();
        boolean match = consoles.stream().anyMatch(productInfo::contains);
        return match ? Category.HARDWARE : Category.DEFAULT;
    }
}
