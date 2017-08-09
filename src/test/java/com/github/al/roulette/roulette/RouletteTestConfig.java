package com.github.al.roulette.roulette;


import com.github.al.roulette.bet.Bet;
import com.github.al.roulette.bet.PlayerBet;
import com.github.al.roulette.player.Player;
import com.github.al.roulette.player.PlayersStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Optional;

@Configuration
public class RouletteTestConfig {
	@Autowired
	private PlayersStorage playersStorage;

	@Primary
	@Bean
	public Iterator<PlayerBet> mockBetsIerator() {
		Player somePlayer = new Player("some player");
		playersStorage.addPlayer(somePlayer);
		PlayerBet someBet = new PlayerBet(somePlayer, new Bet(Optional.empty(), new BigDecimal("4"), Bet.Type.EVEN));
		return new PlayerBetTestIterator(new boolean[]{true, false}, new PlayerBet[]{someBet});
	}
}
