package com.mastermind.domain;

import com.mastermind.data.GamePlayerJdbcRepository;
import com.mastermind.data.PlayerJdbcRepository;
import com.mastermind.models.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class PlayerServiceTest {

    @Autowired
    private PlayerService playerService;

    @MockBean
    private PlayerJdbcRepository playerRepository;

    @MockBean
    private GamePlayerJdbcRepository gamePlayerRepository;

    @MockBean
    private Validations validations;

    @BeforeEach
    void setup() {
        when(validations.isValidPlayerId(anyInt())).thenReturn(true);
    }

    @Test
    void shouldCreatePlayerSuccessfully() {
        Player player = new Player(1, "Alice", 0);
        when(playerRepository.create(any())).thenReturn(player);

        Result<Player> result = playerService.createPlayer("Alice");
        assertTrue(result.isSuccess());
        assertNotNull(result.getPayload());
        assertEquals("Alice", result.getPayload().getName());
        verify(playerRepository).create(any(Player.class));
    }

    @Test
    void shouldFailToCreatePlayerWithEmptyName() {
        Result<Player> result = playerService.createPlayer("");
        assertFalse(result.isSuccess());
        assertNull(result.getPayload());
        assertEquals(ResultType.INVALID, result.getType());
        verify(playerRepository, never()).create(any(Player.class));
    }

    @Test
    void shouldRetrievePlayerGameHistorySuccessfully() {
        List<Integer> games = Arrays.asList(1, 2, 3);
        when(gamePlayerRepository.findGamesByPlayerId(1)).thenReturn(games);

        Result<List<Integer>> result = playerService.getPlayerGameHistory(1);
        assertTrue(result.isSuccess());
        assertEquals(3, result.getPayload().size());
    }

    @Test
    void shouldReturnEmptyGameHistoryForValidPlayerWithNoGames() {
        when(gamePlayerRepository.findGamesByPlayerId(1)).thenReturn(Arrays.asList());
        Result<List<Integer>> result = playerService.getPlayerGameHistory(1);
        assertFalse(result.isSuccess());
        assertEquals(ResultType.NOT_FOUND, result.getType());
    }

    @Test
    void shouldFindPlayerById() {
        Player player = new Player(1, "Alice", 0);
        when(playerRepository.findById(1)).thenReturn(player);

        Player result = playerService.findById(1);
        assertNotNull(result);
        assertEquals(1, result.getPlayerId());
    }
}
