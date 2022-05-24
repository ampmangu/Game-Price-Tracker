package com.mangu.gametracker.web;

import com.mangu.gametracker.model.Entity;
import com.mangu.gametracker.model.UrlRequest;
import com.mangu.gametracker.service.DBService;
import com.mangu.gametracker.service.GameService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2")
public class GameController {
    private final GameService gameService;
    private final DBService dbService;

    public GameController(GameService gameService, DBService dbService) {
        this.gameService = gameService;
        this.dbService = dbService;
    }

    @PostMapping(
            value = "/getgame",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Entity> fetchEntity(@RequestBody UrlRequest urlRequest) {
        Entity entityFetched = this.gameService.fetch(urlRequest.url());
        Entity insert = dbService.insert(entityFetched);
        return ResponseEntity.ok(insert);
    }
}
