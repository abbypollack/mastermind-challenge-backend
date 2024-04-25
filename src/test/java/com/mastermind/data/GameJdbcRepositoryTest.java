package com.mastermind.data;

import com.mastermind.models.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class GameJdbcRepositoryTest {

    @Autowired
    private GameJdbcRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() {
        knownGoodState.set();
    };

    @Test
    void findAllGames() {
        List<Game> games = repository.findAll();
        assertFalse(games.isEmpty(), "Should find all games without error.");
    }

    @Test
    void findGameById() {
        Game game = repository.findById(1);
        assertNotNull(game, "Should find game by ID.");
        assertFalse(game.getPlayerIds().isEmpty(), "Game should have associated player IDs.");
    }

    @Test
    void findGameByInvalidId() {
        assertThrows(EmptyResultDataAccessException.class,
                () -> repository.findById(999),
                "Should throw exception for non-existent game ID.");
    }

    @Test
    void createGameSuccessfully() {
        Game newGame = new Game();
        newGame.setSecretCode("4321");
        newGame.setGameStartTime(LocalDateTime.now());
        newGame.setDifficultyLevel("Easy");
        newGame.setWinnerId(0);
        newGame.setHintCount(0);

        Game createdGame = repository.create(newGame);
        assertNotNull(createdGame, "New game should be created successfully.");
        assertTrue(createdGame.getGameId() > 0, "New game should have a valid ID.");
    }

    @Test
    void createGameFailure() {
        Game invalidGame = new Game();
        invalidGame.setSecretCode(null);
        invalidGame.setGameStartTime(null);
        invalidGame.setDifficultyLevel(null);

        assertThrows(DataIntegrityViolationException.class,
                () -> repository.create(invalidGame),
                "Should fail to create game with invalid data.");
    }


    @Test
    void updateGameDetails() {
        Game gameToUpdate = repository.findById(1);
        gameToUpdate.setWinnerId(2);
        Game updatedGame = repository.update(gameToUpdate);

        assertNotNull(updatedGame, "Game update should succeed.");
        assertEquals(2, updatedGame.getWinnerId(), "Winner ID should be updated.");
    }

    @Test
    void deleteGameById() {
        assertTrue(repository.deleteById(1), "Should successfully delete the game.");
        assertThrows(EmptyResultDataAccessException.class, () -> repository.findById(1), "Game should no longer exist.");
    }

    @Test
    void deleteNonExistentGame() {
        assertFalse(repository.deleteById(999), "Should fail to delete a non-existent game.");
    }

}


