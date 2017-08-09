package com.github.al.roulette.game;


import com.github.al.roulette.bet.Bet;
import com.github.al.roulette.bet.PlayerBet;
import com.github.al.roulette.player.Player;
import com.github.al.roulette.player.PlayersStorage;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
class GameResultsPublisher {
	private static final String BET_PUBLISHING_PATTERN = "%-15s %5s %10s %10s";
	private static final String PLAYER_TOTAL_PUBLISHING_PATTERN = "%-15s %10s %10s";

	@Autowired
	private PlayersStorage playersStorage;


	public void publishCurrentGameResults(int winningNumber, Set<PlayerBet> playerBets) {
		List<String> betResults = playerBets.stream().map(this::playerBetToResultString).collect(Collectors.toList());
		System.out.println(resultsToString(buildFinalBetsPublishingResult(winningNumber, betResults)));
	}

	@VisibleForTesting
	String playerBetToResultString(PlayerBet playerBet) {
		Player player = playerBet.getPlayer();
		Bet bet = playerBet.getBet();

		String playerName = player.getName();
		String betAmount = bet.getBetAmount().toString();
		String betOutcome = bet.getOutcome().get().map(Bet.Outcome::toString).orElse("");
		String betWinnings = bet.getWinnings().get().map(BigDecimal::toString).orElse("");

		return buildBetResultLine(playerName, betAmount, betOutcome, betWinnings);
	}

	private String buildBetResultLine(String playerName, String betAmount, String betOutcome, String betWinnings) {
		return String.format(BET_PUBLISHING_PATTERN, playerName, betAmount, betOutcome, betWinnings);
	}

	@VisibleForTesting
	ImmutableList<String> buildFinalBetsPublishingResult(int winningNumber, List<String> betResults) {
		return ImmutableList
				.<String>builder()
				.add("Number: " + winningNumber)
				.add(buildBetResultLine("Player", "Bet", "Outcome", "Winnings"))
				.add("---")
				.addAll(betResults)
				.add(System.lineSeparator())
				.build();
	}

	public void publishTotalResults() {
		ImmutableSet<Player> registeredPlayers = playersStorage.getRegisteredPlayers();
		List<String> totalResults = registeredPlayers.stream().map(this::playerToTotalString).collect(Collectors.toList());

		System.out.println(resultsToString(buildFinalPlayersTotalResults(totalResults)));
	}

	@VisibleForTesting
	String playerToTotalString(Player player) {
		String playerName = player.getName();
		String totalWin = player.getTotalWin().get().toString();
		String totalBet = player.getTotalBet().get().toString();

		return buildPlayerTotalResultLine(playerName, totalWin, totalBet);
	}

	private String buildPlayerTotalResultLine(String playerName, String totalWin, String totalBet) {
		return String.format(PLAYER_TOTAL_PUBLISHING_PATTERN, playerName, totalWin, totalBet);
	}

	@VisibleForTesting
	ImmutableList<String> buildFinalPlayersTotalResults(List<String> playersTotalResults) {
		return ImmutableList
				.<String>builder()
				.add(buildPlayerTotalResultLine("Player", "Total Win", "Total Bet"))
				.add("---")
				.addAll(playersTotalResults)
				.add(System.lineSeparator())
				.build();
	}

	private String resultsToString(List<String> results) {
		return Joiner.on(System.lineSeparator()).join(results);
	}
}
