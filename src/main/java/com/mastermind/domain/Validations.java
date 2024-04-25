package com.mastermind.domain;

import com.mastermind.data.GameJdbcRepository;
import com.mastermind.data.PlayerJdbcRepository;
import com.mastermind.models.Game;
import com.mastermind.models.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class Validations {

    @Autowired
    private GameJdbcRepository gameRepository;

    @Autowired
    private PlayerJdbcRepository playerRepository;

    public boolean isValidPlayerId(Integer playerId) {
        if (playerId == null || playerId <= 0) {
            return false;
        }

        Player player = playerRepository.findById(playerId);
        return player != null;
    }

    public boolean isValidGameId(Integer gameId) {
        if (gameId == null || gameId <= 0) {
            return false;
        }

        Game game = gameRepository.findById(gameId);
        return game != null;
    }

    public boolean isValidDifficultyLevel(String difficultyLevel) {
        return StringUtils.hasText(difficultyLevel) &&
                ("easy".equalsIgnoreCase(difficultyLevel) ||
                        "medium".equalsIgnoreCase(difficultyLevel) ||
                        "hard".equalsIgnoreCase(difficultyLevel));
    }

    public boolean isValidGuessSequence(String guessSequence) {
        return guessSequence != null && guessSequence.matches("\\d{4}");
    }
}
