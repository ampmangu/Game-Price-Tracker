package com.mangu.gametracker.service;

import com.mangu.gametracker.model.Entity;

public interface ParserService {
    Entity parse(String htmlString, String url);
}
