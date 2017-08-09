package com.github.al.roulette.player;


import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
class PlayerFactory {
	private static final Pattern SIMPLE_PLAYER_PATTERN = Pattern.compile("(\\w+)");
	private static final Pattern COMPLEX_PLAYER_PATTERN = Pattern.compile("(\\w+),(\\d+|\\d+\\.\\d+),(\\d+|\\d+\\.\\d+)");

	public Optional<Player> createPlayer(String input) {
		return Optional.ofNullable(input).flatMap(val -> {
			Matcher simpleMatcher = SIMPLE_PLAYER_PATTERN.matcher(val);
			Matcher complexMatcher = COMPLEX_PLAYER_PATTERN.matcher(val);

			String playerName = null;
			BigDecimal totalWin = null;
			BigDecimal totalBet = null;

			if (simpleMatcher.matches()) {
				playerName = simpleMatcher.group(1);
			} else if (complexMatcher.matches()) {
				playerName = complexMatcher.group(1);
				totalWin = new BigDecimal(complexMatcher.group(2));
				totalBet = new BigDecimal(complexMatcher.group(3));
			} else {
				return Optional.empty();
			}
			return Optional.of(new Player(playerName, Optional.ofNullable(totalWin), Optional.ofNullable(totalBet)));
		});
	}
}
