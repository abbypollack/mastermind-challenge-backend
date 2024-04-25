package com.mastermind.domain;

import com.mastermind.data.GamePlayerJdbcRepository;
import com.mastermind.data.PlayerJdbcRepository;
import com.mastermind.models.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerService {

    @Autowired
    private PlayerJdbcRepository playerRepository;

    @Autowired
    private GamePlayerJdbcRepository gamePlayerRepository;

    @Autowired
    public Validations validations;

    public Result<Player> createPlayer(String name) {
        if (name == null || name.isEmpty()) {
            Result<Player> result = new Result<>();
            result.addMessage("Player name cannot be empty.", ResultType.INVALID);
            return result;
        }

        Player player = new Player();
        player.setName(name);
        player.setScore(0);

        Player createdPlayer = playerRepository.create(player);
        if (createdPlayer != null) {
            Result<Player> result = new Result<>();
            result.setPayload(createdPlayer);
            return result;
        } else {
            Result<Player> result = new Result<>();
            result.addMessage("Failed to create a new player.", ResultType.INVALID);
            return result;
        }
    }

    public Result<List<Integer>> getPlayerGameHistory(int playerId) {
        if (!validations.isValidPlayerId(playerId)) {
            Result<List<Integer>> result = new Result<>();
            result.addMessage("Invalid player ID.", ResultType.INVALID);
            return result;
        }

        List<Integer> games = gamePlayerRepository.findGamesByPlayerId(playerId);
        Result<List<Integer>> result = new Result<>();
        if (games.isEmpty()) {
            result.addMessage("No games found for this player.", ResultType.NOT_FOUND);
            result.setPayload(null);
        } else {
            result.setPayload(games);
        }
        return result;
    }

    public Result<Player> incrementScoreIfWon(int playerId, boolean hasWon) {
        if (!validations.isValidPlayerId(playerId)) {
            Result<Player> result = new Result<>();
            result.addMessage("Invalid player ID.", ResultType.INVALID);
            return result;
        }

        Player player = playerRepository.findById(playerId);
        if (hasWon) {
            player.setScore(player.getScore() + 1);
            Player updatedPlayer = playerRepository.update(player);
            Result<Player> result = new Result<>();
            if (updatedPlayer != null) {
                result.setPayload(updatedPlayer);
                result.addMessage("Player score updated successfully.", ResultType.SUCCESS);
            } else {
                result.addMessage("Failed to update player score.", ResultType.INVALID);
            }
            return result;
        }

        Result<Player> result = new Result<>();
        result.addMessage("Player did not win, score remains the same.", ResultType.SUCCESS);
        return result;
    }

    public Player findById(int playerId) {
        return playerRepository.findById(playerId);
    }
}
