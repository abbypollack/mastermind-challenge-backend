package com.mastermind.models;
import java.time.LocalDateTime;

public class Guess {
    private int guessId;
    private int gameId;
    private String guessSequence;
    private String feedback;
    private LocalDateTime guessTime;

    public Guess() {
    }

    public Guess(int guessId, int gameId, String guessSequence, String feedback, LocalDateTime guessTime) {
        this.guessId = guessId;
        this.gameId = gameId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Guess guess = (Guess) o;

        return guessId == guess.guessId;
    }

    @Override
    public int hashCode() {
        return guessId;
    }

    @Override
    public String toString() {
        return "Guess{" +
                "guessId=" + guessId +
                ", gameId=" + gameId +
                ", guessSequence='" + guessSequence + '\'' +
                ", feedback='" + feedback + '\'' +
                ", guessTime=" + guessTime +
                '}';
    }
}
