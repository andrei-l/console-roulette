package com.github.al.roulette.bet;


import com.github.al.roulette.bet.Bet.Type;
import com.github.al.roulette.player.Player;
import com.github.al.roulette.player.PlayersStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
class PlayerBetFactory {
	private static final String BET_NUMBER_REGEX = "[1-9]|1[0-9]|2[0-9]|3[0-6]";
	private static final String ODD_NUMBER_REGEX = "ODD";
	private static final String EVEN_NUMBER_REGEX = "EVEN";
	private static final Pattern BET_NUMBER_PATTERN = Pattern.compile(BET_NUMBER_REGEX);
	private static final Pattern PLAYER_BET_PATTERN =
			Pattern.compile(String.format("(\\w+)\\s(%s|%s|%s)\\s(\\d+|\\d+\\.\\d+)", BET_NUMBER_REGEX, ODD_NUMBER_REGEX, EVEN_NUMBER_REGEX));

	@Autowired
	private PlayersStorage playersStorage;

	public Optional<PlayerBet> createPlayerBet(String input) {
		return Optional.ofNullable(input).flatMap(val -> {
			Matcher playerBetMatcher = PLAYER_BET_PATTERN.matcher(val);

			if (playerBetMatcher.matches()) {
				String name = playerBetMatcher.group(1);
				return Optional.of(name).flatMap(this::findPlayer).map(player -> new PlayerBet(player, crateBet(playerBetMatcher)));
			} else {
				return Optional.empty();
			}
		});
	}

	private Optional<Player> findPlayer(String name) {
		return playersStorage.getRegisteredPlayers().stream().filter(p -> Objects.equals(p.getName(), name)).findAny();
	}

	private Bet crateBet(Matcher playerBetMatcher) {
		String betGroup = playerBetMatcher.group(2);
		String betAmountGroup = playerBetMatcher.group(3);

		Optional<Integer> betNumber = parseBetNumber(betGroup);
		Type betType = parseBetType(betGroup);
		BigDecimal betAmount = new BigDecimal(betAmountGroup);

		return new Bet(betNumber, betAmount, betType);
	}

	private Optional<Integer> parseBetNumber(String bet) {
		Matcher betNumberMatcher = BET_NUMBER_PATTERN.matcher(bet);
		if (betNumberMatcher.matches()) {
			return Optional.of(new Integer(betNumberMatcher.group()));
		}
		return Optional.empty();
	}

	private Type parseBetType(String bet) {
		if (Pattern.matches(ODD_NUMBER_REGEX, bet)) {
			return Type.ODD;
		} else if (Pattern.matches(EVEN_NUMBER_REGEX, bet)) {
			return Type.EVEN;
		} else {
			return Type.NUMBER;
		}
	}
}
