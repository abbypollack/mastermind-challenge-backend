package com.mastermind.models;
import java.time.LocalDateTime;

public class Game {
    private int gameId;
    private String secretCode;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;

    public Game() {
    }

    public Game(int gameId, String secretCode, LocalDateTime createdAt, LocalDateTime completedAt) {
        this.gameId = gameId;
        this.secretCode = secretCode;
        this.createdAt = createdAt;
        this.completedAt = completedAt;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Game game = (Game) o;

        return gameId == game.gameId;
    }

    @Override
    public int hashCode() {
        return gameId;
    }

    @Override
    public String toString() {
        return "Game{" +
                "gameId=" + gameId +
                ", secretCode='" + secretCode + '\'' +
                ", createdAt=" + createdAt +
                ", completedAt=" + completedAt +
                '}';
    }
}

