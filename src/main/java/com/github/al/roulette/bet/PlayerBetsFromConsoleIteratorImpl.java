package com.github.al.roulette.bet;


import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Optional;

@Component
class PlayerBetsFromConsoleIteratorImpl implements Iterator<PlayerBet>, DisposableBean {
	private final PlayerBetFactory playerBetFactory;

	private final BufferedReader betsBufferedReader;

	@Autowired
	public PlayerBetsFromConsoleIteratorImpl(PlayerBetFactory playerBetFactory) {
		this(new BufferedReader(new InputStreamReader(System.in)), playerBetFactory);
	}

	private PlayerBetsFromConsoleIteratorImpl(BufferedReader bufferedReader, PlayerBetFactory playerBetFactory) {
		this.betsBufferedReader = bufferedReader;
		this.playerBetFactory = playerBetFactory;
	}

	@Override
	public boolean hasNext() {
		return true;
	}

	@Override
	public PlayerBet next() {
		try {
			String input = betsBufferedReader.readLine();
			Optional<PlayerBet> playerBet = playerBetFactory.createPlayerBet(input);
			return playerBet.orElseGet(this::next);
		} catch (IOException | NumberFormatException e) {
			return next();
		}
	}

	@Override
	public void destroy() throws Exception {
		betsBufferedReader.close();
	}
}
