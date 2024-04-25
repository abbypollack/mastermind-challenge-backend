package com.mastermind.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Game {
    private int gameId;
    private String secretCode;
    private LocalDateTime gameStartTime;
    private LocalDateTime gameEndTime;
    private String difficultyLevel;
    private int winnerId;
    private int hintCount;
    private List<Guess> guesses = new ArrayList<>();
    private List<Integer> playerIds = new ArrayList<>();

    public Game() {
    }

    public Game(int gameId, String secretCode, LocalDateTime gameStartTime, LocalDateTime gameEndTime, String difficultyLevel, int winnerId, int hintCount, List<Guess> guesses, List<Integer> playerIds) {
        this.gameId = gameId;
        this.secretCode = secretCode;
        this.gameStartTime = gameStartTime;
        this.gameEndTime = gameEndTime;
        this.difficultyLevel = difficultyLevel;
        this.winnerId = winnerId;
        this.hintCount = hintCount;
        this.guesses = guesses;
        this.playerIds = playerIds;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public String getSecretCode() {
        return secretCode;
    }

    public void setSecretCode(String secretCode) {
        this.secretCode = secretCode;
    }

    public LocalDateTime getGameStartTime() {
        return gameStartTime;
    }

    public void setGameStartTime(LocalDateTime gameStartTime) {
        this.gameStartTime = gameStartTime;
    }

    public LocalDateTime getGameEndTime() {
        return gameEndTime;
    }

    public void setGameEndTime(LocalDateTime gameEndTime) {
        this.gameEndTime = gameEndTime;
    }

    public String getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(String difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public int getWinnerId() {
        return winnerId;
    }

    public void setWinnerId(int winnerId) {
        this.winnerId = winnerId;
    }

    public int getHintCount() {
        return hintCount;
    }

    public void setHintCount(int hintCount) {
        this.hintCount = hintCount;
    }

    public List<Guess> getGuesses() {
        return guesses;
    }

    public void setGuesses(List<Guess> guesses) {
        this.guesses = guesses;
    }

    public List<Integer> getPlayerIds() {
        return playerIds;
    }

    public void setPlayerIds(List<Integer> playerIds) {
        this.playerIds = playerIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return gameId == game.gameId && winnerId == game.winnerId && hintCount == game.hintCount && Objects.equals(secretCode, game.secretCode) && Objects.equals(gameStartTime, game.gameStartTime) && Objects.equals(gameEndTime, game.gameEndTime) && Objects.equals(difficultyLevel, game.difficultyLevel) && Objects.equals(guesses, game.guesses) && Objects.equals(playerIds, game.playerIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameId, secretCode, gameStartTime, gameEndTime, difficultyLevel, winnerId, hintCount, guesses, playerIds);
    }

    @Override
    public String toString() {
        return "Game{" +
                "gameId=" + gameId +
                ", secretCode='" + secretCode + '\'' +
                ", gameStartTime=" + gameStartTime +
                ", gameEndTime=" + gameEndTime +
                ", difficultyLevel='" + difficultyLevel + '\'' +
                ", winnerId=" + winnerId +
                ", hintCount=" + hintCount +
                ", guesses=" + guesses +
                ", playerIds=" + playerIds +
                '}';
    }
}

