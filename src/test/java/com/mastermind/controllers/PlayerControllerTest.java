package com.mastermind.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mastermind.domain.GameService;
import com.mastermind.domain.PlayerService;
import com.mastermind.domain.Result;
import com.mastermind.models.Game;
import com.mastermind.models.Player;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PlayerControllerTest {

    @MockBean
    private PlayerService playerService;

    @MockBean
    private GameService gameService;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void createPlayerShouldReturn201WhenSuccessful() throws Exception {
        Player player = new Player();
        player.setPlayerId(1);
        player.setName("Alice");

        Result<Player> result = new Result<>();
        result.setPayload(player);

        when(playerService.createPlayer(any(String.class))).thenReturn(result);

        mvc.perform(post("/api/player")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(player)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(player)));
    }

    @Test
    public void createPlayerShouldHandleInvalidInput() throws Exception {
        mvc.perform(post("/api/player")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new Player())))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Player data is missing or invalid"));
    }

    @Test
    public void getPlayerScoreShouldReturn200WhenSuccessful() throws Exception {
        Player player = new Player();
        player.setPlayerId(1);
        player.setScore(100);

        when(playerService.findById(eq(1))).thenReturn(player);

        mvc.perform(get("/api/player/1/score")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(Map.of("score", player.getScore()))));
    }

    @Test
    public void getPlayerScoreShouldHandlePlayerNotFound() throws Exception {
        when(playerService.findById(eq(1))).thenReturn(null);

        mvc.perform(get("/api/player/1/score")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Player not found"));
    }

    @Test
    public void getPlayerGameHistoryShouldReturn200WhenSuccessful() throws Exception {
        List<Map<String, Object>> gameHistories = Collections.singletonList(
                Map.of(
                        "gameId", 1,
                        "gameStartTime", "2022-01-01T00:00:00",
                        "gameEndTime", "2022-01-01T01:00:00",
                        "playerIds", List.of(1),
                        "hintCount", 0,
                        "winnerId", 1
                )
        );

        Result<List<Integer>> result = new Result<>();
        result.setPayload(Collections.singletonList(1));
        when(playerService.getPlayerGameHistory(eq(1))).thenReturn(result);

        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        when(gameService.findById(eq(1))).then(invocation -> {
            Game game = new Game();
            game.setGameId(invocation.getArgument(0));

            game.setGameStartTime(LocalDateTime.parse("2022-01-01T00:00:00", formatter));
            game.setGameEndTime(LocalDateTime.parse("2022-01-01T01:00:00", formatter));

            game.setPlayerIds(List.of(1));
            game.setHintCount(0);
            game.setWinnerId(1);
            return game;
        });

        mvc.perform(get("/api/player/1/games")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(gameHistories)));
    }

    @Test
    public void getPlayerGameHistoryShouldHandleNoGamesFound() throws Exception {
        Result<List<Integer>> result = new Result<>();
        result.setPayload(Collections.emptyList());
        when(playerService.getPlayerGameHistory(eq(1))).thenReturn(result);

        mvc.perform(get("/api/player/1/games")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No games found for this player."));
    }
}
