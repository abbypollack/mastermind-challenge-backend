package com.mastermind.models;
import java.time.LocalDateTime;
import java.util.Objects;

public class Guess {
    private int guessId;
    private int gameId;
    private int playerId;
    private String guessSequence;
    private String feedback;
    private LocalDateTime guessTime;

    public Guess() {
    }

    public Guess(int guessId, int gameId, int playerId, String guessSequence, String feedback, LocalDateTime guessTime) {
        this.guessId = guessId;
        this.gameId = gameId;
        this.playerId = playerId;
        this.guessSequence = guessSequence;
        this.feedback = feedback;
        this.guessTime = guessTime;
    }

    public int getGuessId() {
        return guessId;
    }

    public void setGuessId(int guessId) {
        this.guessId = guessId;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public String getGuessSequence() {
        return guessSequence;
    }

    public void setGuessSequence(String guessSequence) {
        this.guessSequence = guessSequence;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public LocalDateTime getGuessTime() {
        return guessTime;
    }

    public void setGuessTime(LocalDateTime guessTime) {
        this.guessTime = guessTime;
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
        Guess guess = (Guess) o;
        return guessId == guess.guessId && gameId == guess.gameId && Objects.equals(playerId, guess.playerId) && Objects.equals(guessSequence, guess.guessSequence) && Objects.equals(feedback, guess.feedback) && Objects.equals(guessTime, guess.guessTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(guessId, gameId, playerId, guessSequence, feedback, guessTime);
    }

    @Override
    public String toString() {
        return "Guess{" +
                "guessId=" + guessId +
                ", gameId=" + gameId +
                ", playerId=" + playerId +
                ", guessSequence='" + guessSequence + '\'' +
                ", feedback='" + feedback + '\'' +
                ", guessTime=" + guessTime +
                '}';
    }
}
