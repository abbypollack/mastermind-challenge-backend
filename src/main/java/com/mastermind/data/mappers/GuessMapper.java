package com.mastermind.data.mappers;

import com.mastermind.models.Guess;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GuessMapper implements RowMapper<Guess> {
    @Override
    public Guess mapRow(ResultSet rs, int rowNum) throws SQLException {
        Guess guess = new Guess();
        guess.setGuessId(rs.getInt("guess_id"));
        guess.setGameId(rs.getInt("game_id"));
        guess.setPlayerId(rs.getInt("player_id"));
        guess.setGuessSequence(rs.getString("guess_sequence"));
        guess.setFeedback(rs.getString("feedback"));
        guess.setGuessTime(rs.getTimestamp("guess_time") != null ? rs.getTimestamp("guess_time").toLocalDateTime() : null);
        return guess;
    }
}
