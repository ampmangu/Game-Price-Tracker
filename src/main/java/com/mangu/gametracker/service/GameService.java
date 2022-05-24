package com.mangu.gametracker.service;

import com.mangu.gametracker.model.Entity;

public interface GameService {
    Entity fetch(String url);
}
