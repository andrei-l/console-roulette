package com.github.al.roulette.player;


import com.google.common.collect.ImmutableSet;

public interface PlayersStorage {
	void addPlayer(Player player);

	ImmutableSet<Player> getRegisteredPlayers();
}
