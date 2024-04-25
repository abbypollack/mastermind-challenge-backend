package com.mastermind.data.mappers;

import com.mastermind.models.Game;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GameMapper implements RowMapper<Game> {
    @Override
    public Game mapRow(ResultSet rs, int rowNum) throws SQLException {
        Game game = new Game();
        game.setGameId(rs.getInt("game_id"));
        game.setSecretCode(rs.getString("secret_code"));
        game.setGameStartTime(rs.getTimestamp("game_start_time").toLocalDateTime());
        if (rs.getTimestamp("game_end_time") != null) {
            game.setGameEndTime(rs.getTimestamp("game_end_time").toLocalDateTime());
        }
        game.setDifficultyLevel(rs.getString("difficulty_level"));
        game.setWinnerId(rs.getInt("winner_id"));
        game.setHintCount(rs.getInt("hint_count"));
        return game;
    }
}
