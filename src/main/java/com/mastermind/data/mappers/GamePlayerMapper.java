package com.mastermind.data.mappers;

import com.mastermind.models.GamePlayer;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GamePlayerMapper implements RowMapper<GamePlayer> {

    @Override
    public GamePlayer mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        GamePlayer gamePlayer = new GamePlayer();
        gamePlayer.setGameId(resultSet.getInt("game_id"));
        gamePlayer.setPlayerId(resultSet.getInt("player_id"));
        return gamePlayer;
    }
}
