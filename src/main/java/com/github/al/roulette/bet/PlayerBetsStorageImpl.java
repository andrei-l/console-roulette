package com.github.al.roulette.bet;


import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

@Component
class PlayerBetsStorageImpl implements PlayerBetsStorage {
	private final AtomicReference<Set<PlayerBet>> playerBetsHolder = new AtomicReference<>(emptySet());

	@Override
	public void addBet(PlayerBet playerBet) {
		playerBetsHolder.updateAndGet(bets -> {
			bets.add(playerBet);
			return bets;
		});
	}

	@Override
	public Set<PlayerBet> grabCurrentGameBets() {
		return playerBetsHolder.getAndSet(emptySet());
	}

	private ConcurrentHashMap.KeySetView<PlayerBet, Boolean> emptySet() {
		return ConcurrentHashMap.newKeySet();
	}
}
