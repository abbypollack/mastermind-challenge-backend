package com.mastermind.controllers;

import com.mastermind.domain.GameService;
import com.mastermind.domain.PlayerService;
import com.mastermind.domain.Result;
import com.mastermind.models.Game;
import com.mastermind.models.Player;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/player")
public class PlayerController {

    private final PlayerService playerService;
    private final GameService gameService;

    public PlayerController(PlayerService playerService, GameService gameService) {
        this.playerService = playerService;
        this.gameService = gameService;
    }

    @PostMapping
    public ResponseEntity<Object> createPlayer(@RequestBody Player player) {
        if (player == null || player.getName() == null || player.getName().isEmpty()) {
            return new ResponseEntity<>("Player data is missing or invalid", HttpStatus.BAD_REQUEST);
        }
        Result<Player> result = playerService.createPlayer(player.getName());
        return result.isSuccess() ? new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED) : ErrorResponse.build(result);
    }

    @GetMapping("/{playerId}/score")
    public ResponseEntity<Object> getPlayerScore(@PathVariable int playerId) {
        Player player = playerService.findById(playerId);
        if (player == null) {
            return new ResponseEntity<>("Player not found", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(Map.of("score", player.getScore()));
    }

    @GetMapping("/{playerId}/games")
    public ResponseEntity<Object> getPlayerGameHistory(@PathVariable int playerId) {
        Result<List<Integer>> gameIdsResult = playerService.getPlayerGameHistory(playerId);
        if (!gameIdsResult.isSuccess()) {
            return ErrorResponse.build(gameIdsResult);
        }

        List<Map<String, Object>> gameHistories = new ArrayList<>();
        for (Integer gameId : gameIdsResult.getPayload()) {
            Game game = gameService.findById(gameId);
            if (game != null) {
                Map<String, Object> gameHistory = new HashMap<>();
                gameHistory.put("gameId", game.getGameId());
                gameHistory.put("gameStartTime", game.getGameStartTime());
                gameHistory.put("gameEndTime", game.getGameEndTime());
                gameHistory.put("playerIds", game.getPlayerIds());
                gameHistory.put("hintCount", game.getHintCount());
                gameHistory.put("winnerId", game.getWinnerId());
                gameHistories.add(gameHistory);
            }
        }

        return gameHistories.isEmpty() ? new ResponseEntity<>("No games found for this player.", HttpStatus.NOT_FOUND) : ResponseEntity.ok(gameHistories);
    }
}
