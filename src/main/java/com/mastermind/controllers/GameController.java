package com.mastermind.controllers;

import com.mastermind.domain.GameService;
import com.mastermind.domain.Result;
import com.mastermind.models.Game;
import com.mastermind.models.Guess;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/game")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping
    public ResponseEntity<Object> startGame(@RequestBody Map<String, Object> payload) {
        List<Integer> playerIds = (List<Integer>) payload.get("playerIds");
        String difficultyLevel = (String) payload.get("difficultyLevel");

        Result<Game> result = gameService.startNewGame(playerIds, difficultyLevel);
        return result.isSuccess() ? new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED) : ErrorResponse.build(result);
    }

    @PostMapping("/{gameId}/guess")
    public ResponseEntity<Object> makeGuess(@PathVariable int gameId, @RequestBody Map<String, Object> payload) {
        Integer playerId = (Integer) payload.get("playerId");
        String guessSequence = (String) payload.get("guessSequence");

        Result<Guess> result = gameService.makeGuess(gameId, playerId, guessSequence);
        return result.isSuccess() ? ResponseEntity.ok(result.getPayload()) : ErrorResponse.build(result);
    }

    @GetMapping("/{gameId}/history")
    public ResponseEntity<Object> getGameHistory(@PathVariable int gameId) {
        Result<List<Guess>> result = gameService.getGameHistory(gameId);
        return result.isSuccess() ? ResponseEntity.ok(result.getPayload()) : ErrorResponse.build(result);
    }

    @GetMapping("/{gameId}/hint")
    public ResponseEntity<String> getHint(@PathVariable int gameId) {
        Result<String> hintResult = gameService.getHint(gameId);
        return hintResult.isSuccess() ? ResponseEntity.ok(hintResult.getPayload()) : ResponseEntity.badRequest().body(hintResult.getMessages().get(0));
    }
    @PostMapping("/{gameId}/stop-timer")
    public ResponseEntity<String> stopTimer(@PathVariable int gameId) {
        Result<Boolean> result = gameService.stopTimer(gameId);
        if (result.isSuccess() && result.getPayload()) {
            return ResponseEntity.ok("Timer stopped");
        } else {
            String errorMessage = String.join(", ", result.getMessages());
            if (errorMessage.isEmpty()) {
                errorMessage = "Failed to stop timer or game does not exist.";
            }
            return ResponseEntity.badRequest().body(errorMessage);
        }
    }

    @PutMapping("/{gameId}/settings")
    public ResponseEntity<Object> updateGameSettings(@PathVariable int gameId, @RequestBody Map<String, String> settings) {
        Result<Game> result = gameService.updateSettings(gameId, settings);
        return result.isSuccess() ? ResponseEntity.ok(result.getPayload()) : ErrorResponse.build(result);
    }
}
