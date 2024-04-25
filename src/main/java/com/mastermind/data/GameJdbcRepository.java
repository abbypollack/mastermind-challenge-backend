package com.mastermind.data;

import com.mastermind.data.mappers.GameMapper;
import com.mastermind.models.Game;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class GameJdbcRepository implements GameRepository{
    private final JdbcTemplate jdbcTemplate;
    public GameJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Game> findAll() {
        final String sql = "SELECT * FROM games;";
        return jdbcTemplate.query(sql, new GameMapper());
    }

    @Override
    public Game findById(int gameId) {
        final String sql = "SELECT * FROM games WHERE game_id = ?;";
        Game game = jdbcTemplate.queryForObject(sql, new GameMapper(), gameId);

        if (game != null) {
            List<Integer> playerIds = findPlayerIdsByGameId(gameId);
            game.setPlayerIds(playerIds);
        }
        return game;
    }

    private List<Integer> findPlayerIdsByGameId(int gameId) {
        final String sql = "SELECT player_id FROM game_players WHERE game_id = ?;";
        return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getInt("player_id"), gameId);
    }

    @Override
    public Game create(Game game) {
        final String sql = "INSERT INTO games (secret_code, game_start_time, game_end_time, difficulty_level, winner_id, hint_count) VALUES (?, ?, ?, ?, ?, ?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, game.getSecretCode());
            ps.setTimestamp(2, game.getGameStartTime() != null ? Timestamp.valueOf(game.getGameStartTime()) : null);
            ps.setTimestamp(3, game.getGameEndTime() != null ? Timestamp.valueOf(game.getGameEndTime()) : null);
            ps.setString(4, game.getDifficultyLevel());
            ps.setInt(5, game.getWinnerId());
            ps.setInt(6, game.getHintCount());
            return ps;
        }, keyHolder);

        if (rowsAffected <= 0) {
            return null;
        }

        game.setGameId(keyHolder.getKey().intValue());
        return game;
    };

    @Override
    @Transactional
    public Game update(Game game) {
        final String sql = "UPDATE games SET secret_code = ?, game_start_time = ?, game_end_time = ?, difficulty_level = ?, winner_id = ?, hint_count = ? WHERE game_id = ?;";
        jdbcTemplate.update(sql,
                game.getSecretCode(),
                game.getGameStartTime(),
                game.getGameEndTime(),
                game.getDifficultyLevel(),
                game.getWinnerId(),
                game.getHintCount(),
                game.getGameId());

        List<Integer> existingPlayerIds = jdbcTemplate.query("SELECT player_id FROM game_players WHERE game_id = ?;", (rs, rowNum) -> rs.getInt("player_id"), game.getGameId());

        for (Integer playerId : game.getPlayerIds()) {
            if (!existingPlayerIds.contains(playerId)) {
                jdbcTemplate.update("INSERT INTO game_players (game_id, player_id) VALUES (?, ?);", game.getGameId(), playerId);
            }
        }

        Game updatedGame = findById(game.getGameId());

        return updatedGame;
    }

    @Override
    @Transactional
    public boolean deleteById(int gameId) {
        jdbcTemplate.update("DELETE FROM game_players WHERE game_id = ?;", gameId);
        jdbcTemplate.update("DELETE FROM guesses WHERE game_id = ?;", gameId);
        return jdbcTemplate.update("DELETE FROM games WHERE game_id = ?;", gameId) > 0;
    }
}
