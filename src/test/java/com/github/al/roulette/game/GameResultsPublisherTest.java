package com.github.al.roulette.game;

import com.github.al.roulette.bet.Bet;
import com.github.al.roulette.bet.PlayerBet;
import com.github.al.roulette.player.Player;
import com.github.al.roulette.player.PlayersStorage;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Optional;


@RunWith(MockitoJUnitRunner.class)
public class GameResultsPublisherTest {
	@InjectMocks
	private GameResultsPublisher gameResultsPublisher;

	@Mock
	private PlayersStorage playersStorage;

	private Player player1;
	private Player player2;
	private Player player3;
	private PlayerBet playerBet1;
	private PlayerBet playerBet2;

	@Before
	public void setUp() throws Exception {
		player1 = new Player("player1");
		player2 = new Player("player2", Optional.of(new BigDecimal("10")), Optional.of(new BigDecimal("10")));
		player3 = new Player("player3");

		Bet bet1 = new Bet(Optional.empty(), new BigDecimal("10"), Bet.Type.EVEN);
		Bet bet2 = new Bet(Optional.empty(), new BigDecimal("5"), Bet.Type.ODD);
		bet1.getOutcome().set(Optional.of(Bet.Outcome.WIN));
		bet2.getWinnings().set(Optional.of(new BigDecimal(0)));

		playerBet1 = new PlayerBet(player1, bet1);
		playerBet2 = new PlayerBet(player2, bet2);
	}

	@Test
	public void testPublishCurrentGameResults() throws Exception {
		gameResultsPublisher.publishCurrentGameResults(4, ImmutableSet.of(playerBet1, playerBet2));
	}

	@Test
	public void testPublishTotalResults() throws Exception {
		Mockito.when(playersStorage.getRegisteredPlayers()).thenReturn(ImmutableSet.of(player1, player2, player3));
		gameResultsPublisher.publishTotalResults();
	}

	@Test
	public void testPlayerBetToResultString() throws Exception {
		Assert.assertEquals("player1            10        WIN           ", gameResultsPublisher.playerBetToResultString(playerBet1));
		Assert.assertEquals("player2             5                     0", gameResultsPublisher.playerBetToResultString(playerBet2));
	}

	@Test
	public void testBuildFinalBetsPublishingResult() throws Exception {
		ImmutableList<String> results = gameResultsPublisher.buildFinalBetsPublishingResult(4, Lists.newArrayList("line1", "line2"));
		Assert.assertEquals("Number: 4", results.get(0));
		Assert.assertEquals("Player            Bet    Outcome   Winnings", results.get(1));
		Assert.assertEquals("---", results.get(2));
		Assert.assertEquals("line1", results.get(3));
		Assert.assertEquals("line2", results.get(4));
	}

	@Test
	public void testPlayerToTotalString() throws Exception {
		Assert.assertEquals("player1                  0          0", gameResultsPublisher.playerToTotalString(player1));
		Assert.assertEquals("player2                 10         10", gameResultsPublisher.playerToTotalString(player2));
		Assert.assertEquals("player3                  0          0", gameResultsPublisher.playerToTotalString(player3));
	}

	@Test
	public void testBuildFinalPlayersTotalResults() throws Exception {
		ImmutableList<String> results = gameResultsPublisher.buildFinalPlayersTotalResults(Lists.newArrayList("line1", "line2"));
		Assert.assertEquals("Player           Total Win  Total Bet", results.get(0));
		Assert.assertEquals("---", results.get(1));
		Assert.assertEquals("line1", results.get(2));
		Assert.assertEquals("line2", results.get(3));
	}
}