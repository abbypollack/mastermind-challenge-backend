package com.mastermind.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class GamePlayerJdbcRepositoryTest {

    @Autowired
    GamePlayerJdbcRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() {
        knownGoodState.set();
    }

    @Test
    void shouldAddPlayerToGame() {
        assertTrue(repository.addPlayerToGame(3, 3));
    }

    @Test
    void shouldRemovePlayerFromGame() {
        assertTrue(repository.addPlayerToGame(4, 2));
        assertTrue(repository.removePlayerFromGame(4, 2));
        assertFalse(repository.removePlayerFromGame(4, 2));
    }

    @Test
    void shouldFindPlayersByGameId() {
        List<Integer> players = repository.findPlayersByGameId(2);
        assertNotNull(players);
        assertEquals(1, players.size());
    }

    @Test
    void shouldFindGamesByPlayerId() {
        List<Integer> games = repository.findGamesByPlayerId(1);
        assertNotNull(games);
        assertEquals(2, games.size());
    }
}
