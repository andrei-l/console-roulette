package com.github.al.roulette.player;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Optional;

@Component
class PlayersLoaderImpl implements PlayersLoader {
	@Autowired
	private PlayerFactory playerFactory;

	@Autowired
	private PlayersStorage playersStorage;

	@Override
	public void loadPlayers(InputStream inputStream) {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
			String input;
			while ((input = reader.readLine()) != null) {
				Optional<Player> player = playerFactory.createPlayer(input);
				player.ifPresent(playersStorage::addPlayer);
			}
		} catch (IOException e) {
			throw new RuntimeException("Failed to load players " + e.getMessage(), e);
		}
	}
}
