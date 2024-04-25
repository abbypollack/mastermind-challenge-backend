package com.mastermind.data;

import com.mastermind.data.mappers.PlayerMapper;
import com.mastermind.models.Player;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.Statement;

@Repository
public class PlayerJdbcRepository implements PlayerRepository {

    private final JdbcTemplate jdbcTemplate;

    public PlayerJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Player findById(int playerId) {
        final String sql = "SELECT * FROM players WHERE player_id = ?;";
        return jdbcTemplate.queryForObject(sql, new PlayerMapper(), playerId);
    }

    @Override
    public Player create(Player player) {
        final String sql = "INSERT INTO players (name, score) VALUES (?, ?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    ps.setString(1, player.getName());
                    ps.setInt(2, player.getScore());
                    return ps;
                }, keyHolder);

        player.setPlayerId(keyHolder.getKey().intValue());
        return player;
    }

    @Override
    @Transactional
    public Player update(Player player) {
        final String sql = "UPDATE players SET name = ?, score = ? WHERE player_id = ?;";
        return jdbcTemplate.update(sql, player.getName(), player.getScore(), player.getPlayerId()) > 0 ? player : null;
    }
}
