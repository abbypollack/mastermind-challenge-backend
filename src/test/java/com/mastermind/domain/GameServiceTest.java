package com.mastermind.domain;

import com.mastermind.data.GameJdbcRepository;
import com.mastermind.data.GamePlayerRepository;
import com.mastermind.data.GuessJdbcRepository;
import com.mastermind.models.Game;
import com.mastermind.models.Guess;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class GameServiceTest {

    @Autowired
    private GameService gameService;

    @MockBean
    private GameJdbcRepository gameRepository;

    @MockBean
    private GamePlayerRepository gamePlayerRepository;

    @MockBean
    private GuessJdbcRepository guessRepository;

    @MockBean
    private RandomNumberService randomNumberService;

    @MockBean
    private PlayerService playerService;

    @MockBean
    private Validations validations;

    @BeforeEach
    void setup() {
        when(validations.isValidGameId(anyInt())).thenReturn(true);
        when(validations.isValidPlayerId(anyInt())).thenReturn(true);
        when(validations.isValidGuessSequence(anyString())).thenReturn(true);
        when(validations.isValidDifficultyLevel(anyString())).thenReturn(true);
    }

    @Test
    void shouldStartNewGameSuccessfully() {
        List<Integer> playerIds = Arrays.asList(1, 2);
        String difficultyLevel = "easy";
        String secretCode = "1234";
        when(randomNumberService.generateSecretCode()).thenReturn(secretCode);
        Game game = new Game();
        game.setPlayerIds(playerIds);
        game.setSecretCode(secretCode);
        game.setDifficultyLevel(difficultyLevel);
        when(gameRepository.create(any())).thenReturn(game);

        Result<Game> result = gameService.startNewGame(playerIds, difficultyLevel);
        assertTrue(result.isSuccess());
        assertNotNull(result.getPayload());
        assertEquals(playerIds, result.getPayload().getPlayerIds());
        assertEquals(secretCode, result.getPayload().getSecretCode());
        assertEquals(difficultyLevel, result.getPayload().getDifficultyLevel());
    }

    @Test
    void shouldMakeGuessSuccessfully() {
        int gameId = 1;
        int playerId = 1;
        String guessSequence = "1234";
        Game game = new Game();
        game.setGameId(gameId);
        game.setPlayerIds(Arrays.asList(playerId));
        game.setSecretCode(guessSequence);
        when(gameRepository.findById(gameId)).thenReturn(game);
        Guess guess = new Guess();
        guess.setGameId(gameId);
        guess.setPlayerId(playerId);
        guess.setGuessSequence(guessSequence);
        when(guessRepository.create(any())).thenReturn(guess);

        Result<Guess> result = gameService.makeGuess(gameId, playerId, guessSequence);
        assertTrue(result.isSuccess());
        assertNotNull(result.getPayload());
        assertEquals(gameId, result.getPayload().getGameId());
        assertEquals(playerId, result.getPayload().getPlayerId());
        assertEquals(guessSequence, result.getPayload().getGuessSequence());
    }

    @Test
    void shouldStopTimerSuccessfully() {
        int gameId = 1;
        Game game = new Game();
        game.setGameId(gameId);
        game.setGameStartTime(LocalDateTime.now());
        when(gameRepository.findById(gameId)).thenReturn(game);

        Result<Boolean> result = gameService.stopTimer(gameId);
        assertTrue(result.isSuccess());
        assertTrue(result.getPayload());
    }

    @Test
    void shouldGetHintSuccessfully() {
        int gameId = 1;
        String secretCode = "1234";
        Game game = new Game();
        game.setGameId(gameId);
        game.setSecretCode(secretCode);
        game.setDifficultyLevel("easy");
        when(gameRepository.findById(gameId)).thenReturn(game);

        Result<String> result = gameService.getHint(gameId);
        assertTrue(result.isSuccess());
        assertNotNull(result.getPayload());
    }

    @Test
    void shouldGetGameHistorySuccessfully() {
        int gameId = 1;
        List<Guess> guesses = Arrays.asList(new Guess(), new Guess());
        when(guessRepository.findByGameId(gameId)).thenReturn(guesses);

        Result<List<Guess>> result = gameService.getGameHistory(gameId);
        assertTrue(result.isSuccess());
        assertEquals(guesses.size(), result.getPayload().size());
    }

    @Test
    void shouldFindGameById() {
        int gameId = 1;
        Game game = new Game();
        game.setGameId(gameId);
        when(gameRepository.findById(gameId)).thenReturn(game);

        Game result = gameService.findById(gameId);
        assertNotNull(result);
        assertEquals(gameId, result.getGameId());
    }

    @Test
    void shouldCalculateFeedbackSuccessfully() {
        String secretCode = "1234";
        String guess = "1234";

        Result<String> result = gameService.calculateFeedback(secretCode, guess);
        assertTrue(result.isSuccess());
        assertEquals("4 correct number(s) and 4 correct location(s)", result.getPayload());
    }
}