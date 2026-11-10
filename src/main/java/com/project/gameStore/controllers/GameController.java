package com.project.gameStore.controllers;

import com.project.gameStore.services.GameService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GameController {
    private final GameService gameService;
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }
}
