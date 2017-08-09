package com.github.al.roulette.bet;


import java.util.Set;

public interface PlayerBetsStorage {
	void addBet(PlayerBet playerBet);

	Set<PlayerBet> grabCurrentGameBets();
}
