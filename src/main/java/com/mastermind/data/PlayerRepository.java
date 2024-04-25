package com.mastermind.data;

import com.mastermind.models.Player;

public interface PlayerRepository {
    Player findById(int playerId);
    Player create(Player player);
    Player update(Player player);
}
