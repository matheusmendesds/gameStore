package com.project.gameStore.controllers;

import com.project.gameStore.dtos.GameResponseDTO;
import com.project.gameStore.entities.Game;
import com.project.gameStore.services.GameService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/games")
public class GameController {
    private final GameService gameService;
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping
    public List<GameResponseDTO> findAll() {
        return gameService.findAll()
                .stream()
                .map(game -> new GameResponseDTO(
                        game.getId(),
                        game.getName(),
                        game.getDescription(),
                        game.getPrice(),
                        game.getProductType(),
                        game.getDigitalStock(),
                        game.getPhysicalStock(),
                        game.getActive()
                ))
                .toList();
    }

    @GetMapping("/{id}")
    public GameResponseDTO findById(@PathVariable UUID id) {
        return gameService.findById(id)
                .map(this::toResponseDTO)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Jogo não encontrado"
                ));
    }



    private GameResponseDTO toResponseDTO(Game game) {
        return new GameResponseDTO(
                game.getId(),
                game.getName(),
                game.getDescription(),
                game.getPrice(),
                game.getProductType(),
                game.getDigitalStock(),
                game.getPhysicalStock(),
                game.getActive()

        );
    }
}
