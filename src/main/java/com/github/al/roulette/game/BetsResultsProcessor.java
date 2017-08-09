package com.github.al.roulette.game;


import com.github.al.roulette.bet.Bet;
import com.github.al.roulette.bet.PlayerBet;
import com.github.al.roulette.player.Player;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;

@Component
class BetsResultsProcessor {
	void processResults(int winningNumber, Set<PlayerBet> playerBets) {
		playerBets.forEach(playerBet -> {
			Player player = playerBet.getPlayer();
			Bet bet = playerBet.getBet();

			Optional<BigDecimal> winnings = bet.getWinnings(winningNumber);
			updateBetStatus(bet, winnings);
			updatePlayerStatistics(player, bet, winnings);
		});
	}

	private void updateBetStatus(Bet bet, Optional<BigDecimal> winnings) {
		bet.getWinnings().set(Optional.of(winnings.orElse(BigDecimal.ZERO)));
		bet.getOutcome().set(Optional.of(winnings.map(amount -> Bet.Outcome.WIN).orElse(Bet.Outcome.LOSE)));
	}

	private void updatePlayerStatistics(Player player, Bet bet, Optional<BigDecimal> winnings) {
		player.getTotalWin().updateAndGet(curWin -> curWin.add(winnings.orElse(BigDecimal.ZERO)));
		player.getTotalBet().updateAndGet(curTotal -> curTotal.add(bet.getBetAmount()));
	}
}
