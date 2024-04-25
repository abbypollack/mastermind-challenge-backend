package com.mastermind.data;

import com.mastermind.models.Game;

import java.util.List;

public interface GameRepository {
    List<Game> findAll();
    Game findById(int id);
    Game create(Game game);
    Game update(Game game);
    boolean deleteById(int id);
}
