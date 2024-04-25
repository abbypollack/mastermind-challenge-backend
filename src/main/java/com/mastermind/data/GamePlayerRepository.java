package com.mastermind.data;

import java.util.List;

public interface GamePlayerRepository {
    boolean addPlayerToGame(int gameId, int playerId);
    List<Integer> findPlayersByGameId(int gameId);
    List<Integer> findGamesByPlayerId(int playerId);
    boolean removePlayerFromGame(int gameId, int playerId);
}
