package com.mastermind.models;

import java.util.Objects;

public class Player {
    private int playerId;
    private String name;
    private int score;

    public Player() {}

    public Player(int playerId, String name, int score) {
        this.playerId = playerId;
        this.name = name;
        this.score = score;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return playerId == player.playerId && score == player.score && Objects.equals(name, player.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerId, name, score);
    }

    @Override
    public String toString() {
        return "Player{" +
                "playerId=" + playerId +
                ", name='" + name + '\'' +
                ", score=" + score +
                '}';
    }
}