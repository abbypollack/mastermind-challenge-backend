package com.mastermind.data;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Repository
public class GamePlayerJdbcRepository implements GamePlayerRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GamePlayerJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean addPlayerToGame(int gameId, int playerId) {
        final String sql = "INSERT INTO game_players (game_id, player_id) VALUES (?, ?);";
        return jdbcTemplate.update(sql, gameId, playerId) > 0;
    }

    @Override
    public boolean removePlayerFromGame(int gameId, int playerId) {
        final String sql = "DELETE FROM game_players WHERE game_id = ? AND player_id = ?;";
        return jdbcTemplate.update(sql, gameId, playerId) > 0;
    }

    @Override
    public List<Integer> findPlayersByGameId(int gameId) {
        final String sql = "SELECT player_id FROM game_players WHERE game_id = ?;";
        return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getInt("player_id"), gameId);
    }

    @Override
    public List<Integer> findGamesByPlayerId(int playerId) {
        final String sql = "SELECT game_id FROM game_players WHERE player_id = ?;";
        return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getInt("game_id"), playerId);
    }
}
