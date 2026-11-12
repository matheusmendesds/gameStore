package com.project.gameStore.controllers;

import com.project.gameStore.dtos.GameRequestDTO;
import com.project.gameStore.dtos.GameResponseDTO;
import com.project.gameStore.entities.Game;
import com.project.gameStore.entities.Platform;
import com.project.gameStore.services.GameService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
                        game.getActive(),
                        game.getCategory().getName(),
                        game.getPlatforms().stream().map(Platform::getName).collect(Collectors.toSet())
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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GameResponseDTO save(@Valid @RequestBody GameRequestDTO data) {
        Game game = gameService.save(data);
        return toResponseDTO(game);
    }

    @PutMapping("/{id}")
    public GameResponseDTO update(@RequestBody GameRequestDTO data,@PathVariable UUID id) {
        Game game = gameService.update(data,id);
        return toResponseDTO(game);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id){
        gameService.deleteById(id);
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
                game.getActive(),
                game.getCategory().getName(),
                game.getPlatforms().stream().map(Platform::getName).collect(Collectors.toSet())

        );
    }
}
