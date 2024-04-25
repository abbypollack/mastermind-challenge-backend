package com.mastermind.models;

import java.util.Objects;

public class GamePlayer {
    private int gameId;
    private int playerId;

    public GamePlayer(int gameId, int playerId) {
        this.gameId = gameId;
        this.playerId = playerId;
    }

    public GamePlayer() {
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GamePlayer that = (GamePlayer) o;
        return gameId == that.gameId && playerId == that.playerId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameId, playerId);
    }

    @Override
    public String toString() {
        return "GamePlayer{" +
                "gameId=" + gameId +
                ", playerId=" + playerId +
                '}';
    }
}