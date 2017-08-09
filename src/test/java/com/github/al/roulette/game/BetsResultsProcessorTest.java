package com.github.al.roulette.game;

import com.github.al.roulette.bet.Bet;
import com.github.al.roulette.bet.PlayerBet;
import com.github.al.roulette.player.Player;
import com.google.common.collect.Sets;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Optional;

public class BetsResultsProcessorTest {

	private BetsResultsProcessor betsResultsProcessor = new BetsResultsProcessor();

	@Test
	public void testProcessResults() throws Exception {
		Player player1 = new Player("player1");
		Player player2 = new Player("player2", Optional.of(new BigDecimal("10")), Optional.of(new BigDecimal("10")));

		Bet bet1 = new Bet(Optional.empty(), new BigDecimal("10"), Bet.Type.EVEN);
		Bet bet2 = new Bet(Optional.empty(), new BigDecimal("6"), Bet.Type.ODD);

		PlayerBet playerBet1 = new PlayerBet(player1, bet1);
		PlayerBet playerBet2 = new PlayerBet(player2, bet2);

		betsResultsProcessor.processResults(3, Sets.newHashSet(playerBet1, playerBet2));

		assertBet(playerBet1, Bet.Outcome.LOSE, BigDecimal.ZERO);
		assertBet(playerBet2, Bet.Outcome.WIN, new BigDecimal("12"));

		assertPlayer(player1, BigDecimal.ZERO, new BigDecimal("10"));
		assertPlayer(player2, new BigDecimal("22"), new BigDecimal("16"));
	}

	private void assertPlayer(Player player1, BigDecimal expectedTotalWin, BigDecimal expectedTotalBet) {
		Assert.assertEquals(expectedTotalWin, player1.getTotalWin().get());
		Assert.assertEquals(expectedTotalBet, player1.getTotalBet().get());
	}

	private void assertBet(PlayerBet playerBet, Bet.Outcome expectedOutcome, BigDecimal expectedWinnings) {
		Assert.assertEquals(expectedOutcome, playerBet.getBet().getOutcome().get().get());
		Assert.assertEquals(expectedWinnings, playerBet.getBet().getWinnings().get().get());
	}
}