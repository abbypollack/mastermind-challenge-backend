package com.mastermind.data;

import com.mastermind.data.mappers.GuessMapper;
import com.mastermind.models.Guess;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class GuessJdbcRepository implements GuessRepository {

    private final JdbcTemplate jdbcTemplate;

    public GuessJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Guess findById(int guessId) {
        final String sql = "SELECT * FROM guesses WHERE guess_id = ?;";
        return jdbcTemplate.queryForObject(sql, new GuessMapper(), guessId);
    }

    @Override
    public List<Guess> findAll() {
        final String sql = "SELECT * FROM guesses;";
        return jdbcTemplate.query(sql, new GuessMapper());
    }

    @Override
    public List<Guess> findByGameId(int gameId) {
        final String sql = "SELECT * FROM guesses WHERE game_id = ?;";
        return jdbcTemplate.query(sql, new GuessMapper(), gameId);
    }

    @Override
    @Transactional
    public Guess create(Guess guess) {
        final String sql = "INSERT INTO guesses (game_id, player_id, guess_sequence, feedback, guess_time) VALUES (?, ?, ?, ?, ?);";
        jdbcTemplate.update(sql, guess.getGameId(), guess.getPlayerId(), guess.getGuessSequence(), guess.getFeedback(), guess.getGuessTime());
        return guess;
    }

    @Override
    public boolean update(Guess guess) {
        final String sql = "UPDATE guesses SET game_id = ?, player_id = ?, guess_sequence = ?, feedback = ?, guess_time = ? WHERE guess_id = ?;";
        return jdbcTemplate.update(sql, guess.getGameId(), guess.getPlayerId(), guess.getGuessSequence(), guess.getFeedback(), guess.getGuessTime(), guess.getGuessId()) > 0;
    }

    @Override
    public boolean deleteById(int guessId) {
        final String sql = "DELETE FROM guesses WHERE guess_id = ?;";
        return jdbcTemplate.update(sql, guessId) > 0;
    }
}
