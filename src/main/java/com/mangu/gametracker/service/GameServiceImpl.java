package com.mangu.gametracker.service;

import com.mangu.gametracker.model.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GameServiceImpl implements GameService {

    private static final Logger log = LoggerFactory.getLogger(GameServiceImpl.class);

    private final RestTemplate restTemplate;
    private final ParserService parserService;

    public GameServiceImpl(RestTemplate restTemplate, ParserService parserService) {
        this.restTemplate = restTemplate;
        this.parserService = parserService;
    }

    @Override
    public Entity fetch(String url) {

        return parserService.parse(restTemplate.getForObject(url, String.class), url);
    }
}
