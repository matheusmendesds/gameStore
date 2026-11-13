package com.project.gameStore.services;

import com.project.gameStore.dtos.GameRequestDTO;
import com.project.gameStore.entities.Category;
import com.project.gameStore.entities.Game;
import com.project.gameStore.entities.Platform;
import com.project.gameStore.repositories.CategoryRepository;
import com.project.gameStore.repositories.GameRepository;
import com.project.gameStore.repositories.PlatformRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class GameService {

    private final GameRepository gameRepository;
    private final CategoryRepository categoryRepository;
    private final PlatformRepository platformRepository;

    public GameService(GameRepository gameRepository, CategoryRepository categoryRepository, PlatformRepository platformRepository){
        this.gameRepository = gameRepository;
        this.categoryRepository = categoryRepository;
        this.platformRepository = platformRepository;
    }

    public List<Game> findAll() {
        return gameRepository.findAllByActiveTrue();
    }

    public Optional<Game> findById(UUID id){
        return gameRepository.findById(id);
    }

    public Game save(GameRequestDTO data) {
        if (gameRepository.existsByName(data.name())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Já existe um jogo cadastrado com esse nome"
            );
        }

        Category category = categoryRepository.findById(data.categoryId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Categoria não encontrada"));
        List<Platform> platforms = platformRepository.findAllById(data.platformIds());

        if (platforms.size() != data.platformIds().size()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Uma ou mais plataformas não foram encontradas");
        }

        Game game = new Game();

        game.setName(data.name());
        game.setDescription(data.description());
        game.setPrice(data.price());
        game.setProductType(data.productType());
        game.setDigitalStock(data.digitalStock());
        game.setPhysicalStock(data.physicalStock());
        game.setActive(true);
        game.setCategory(category);
        game.setPlatforms(new HashSet<>(platforms));

        return gameRepository.save(game);
    }

    public Game update(GameRequestDTO data , UUID id) {
        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Jogo não encontrado"));

        if (gameRepository.existsByNameAndIdNot(data.name(),id)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Já existe outro jogo cadastrado com esse nome"
            );
        }
        Category category = categoryRepository.findById(data.categoryId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Categoria não encontrado"));

        List<Platform> platforms = platformRepository.findAllById(data.platformIds());

        if (platforms.size() != data.platformIds().size()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Uma ou mais plataformas não foram encontradas");
        }
        game.setName(data.name());
        game.setDescription(data.description());
        game.setPrice(data.price());
        game.setProductType(data.productType());
        game.setDigitalStock(data.digitalStock());
        game.setPhysicalStock(data.physicalStock());
        game.setCategory(category);
        game.setPlatforms(new HashSet<>(platforms));

        return gameRepository.save(game);
    }

    public void deleteById(UUID id) {
        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Jogo não encontrado"));

        game.setActive(false);

        gameRepository.save(game);

    }

    public List<Game> searchByName(String name) {
        return gameRepository.findAllByActiveTrueAndNameContainingIgnoreCase(name);
    }

    public List<Game> findByCategory(String categoryName) {
        return gameRepository.findAllByActiveTrueAndCategory_NameIgnoreCase(categoryName);
    }

}
