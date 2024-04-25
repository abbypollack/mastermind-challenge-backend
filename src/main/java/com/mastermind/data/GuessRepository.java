package com.mastermind.data;

import com.mastermind.models.Guess;
import java.util.List;

public interface GuessRepository {
    Guess findById(int guessId);
    List<Guess> findAll();
    List<Guess> findByGameId(int gameId);
    Guess create(Guess guess);
    boolean update(Guess guess);
    boolean deleteById(int guessId);
}
