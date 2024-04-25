package com.mastermind.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mastermind.domain.GameService;
import com.mastermind.domain.Result;
import com.mastermind.domain.ResultType;
import com.mastermind.models.Game;
import com.mastermind.models.Guess;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class GameControllerTest {

    @MockBean
    private GameService gameService;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void startGameShouldReturn201WhenSuccessful() throws Exception {
        Game game = new Game();
        game.setGameId(1);

        Result<Game> result = new Result<>();
        result.setPayload(game);

        when(gameService.startNewGame(any(List.class), any(String.class))).thenReturn(result);

        Map<String, Object> payload = Map.of(
                "playerIds", List.of(1),
                "difficultyLevel", "easy"
        );

        mvc.perform(post("/api/game")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(game)));
    }

    @Test
    public void makeGuessShouldReturn200WhenSuccessful() throws Exception {
        Guess guess = new Guess();
        guess.setGuessId(123);

        Result<Guess> result = new Result<>();
        result.setPayload(guess);

        when(gameService.makeGuess(eq(1), anyInt(), anyString())).thenReturn(result);

        Map<String, Object> payload = Map.of(
                "playerId", 1,
                "guessSequence", "1234"
        );

        mvc.perform(post("/api/game/1/guess")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(guess)));
    }

    @Test
    public void makeGuessShouldHandleInvalidGuess() throws Exception {
        Result<Guess> result = new Result<>();
        result.addMessage("Invalid guess sequence", ResultType.INVALID);

        when(gameService.makeGuess(eq(1), anyInt(), anyString())).thenReturn(result);

        Map<String, Object> payload = Map.of(
                "playerId", 1,
                "guessSequence", "wrong"
        );

        mvc.perform(post("/api/game/1/guess")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getHintShouldReturn200WhenSuccessful() throws Exception {
        Result<String> hintResult = new Result<>();
        hintResult.setPayload("Try another combination");

        when(gameService.getHint(eq(1))).thenReturn(hintResult);

        mvc.perform(get("/api/game/1/hint"))
                .andExpect(status().isOk())
                .andExpect(content().string("Try another combination"));
    }

    @Test
    public void getHintShouldHandleNoHintsAvailable() throws Exception {
        Result<String> hintResult = new Result<>();
        hintResult.addMessage("No hints available", ResultType.INVALID);

        when(gameService.getHint(eq(1))).thenReturn(hintResult);

        mvc.perform(get("/api/game/1/hint"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("No hints available"));
    }


    @Test
    public void updateGameSettingsShouldReturn200WhenSuccessful() throws Exception {
        Game game = new Game();
        game.setGameId(1);

        Result<Game> result = new Result<>();
        result.setPayload(game);

        Map<String, String> settings = Map.of("timeLimit", "30");

        when(gameService.updateSettings(eq(1), anyMap())).thenReturn(result);

        mvc.perform(put("/api/game/1/settings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(settings)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(game)));
    }

    @Test
    public void stopTimerShouldReturn200WhenSuccessful() throws Exception {
        Result<Boolean> result = new Result<>();
        result.setPayload(true);

        when(gameService.stopTimer(eq(1))).thenReturn(result);

        mvc.perform(post("/api/game/1/stop-timer"))
                .andExpect(status().isOk())
                .andExpect(content().string("Timer stopped"));
    }

    @Test
    public void stopTimerShouldHandleFailure() throws Exception {
        Result<Boolean> result = new Result<>();
        result.addMessage("Timer not running or game not found", ResultType.INVALID);

        when(gameService.stopTimer(eq(1))).thenReturn(result);

        mvc.perform(post("/api/game/1/stop-timer"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Timer not running or game not found"));
    }
}
