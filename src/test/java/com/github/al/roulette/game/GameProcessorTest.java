package com.github.al.roulette.game;

import com.github.al.roulette.bet.PlayerBet;
import com.github.al.roulette.bet.PlayerBetsStorage;
import com.google.common.collect.Sets;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashSet;

@RunWith(MockitoJUnitRunner.class)
public class GameProcessorTest {
	@InjectMocks
	private GameProcessor gameProcessor;

	@Mock
	private PlayerBetsStorage playerBetsStorage;

	@Mock
	private BetsResultsProcessor betsResultsProcessor;

	@Mock
	private GameResultsPublisher gameResultsPublisher;

	@Mock
	private PlayerBet playerBet1;

	@Mock
	private PlayerBet playerBet2;

	@Captor
	private ArgumentCaptor<Integer> winningNumberCapture1;

	@Captor
	private ArgumentCaptor<Integer> winningNumberCapture2;

	@Test
	public void testLandBall() throws Exception {
		HashSet<PlayerBet> playerBets = Sets.newHashSet(playerBet1, playerBet2);
		Mockito.when(playerBetsStorage.grabCurrentGameBets()).thenReturn(playerBets);

		gameProcessor.landBall();

		Mockito.verify(betsResultsProcessor).processResults(winningNumberCapture1.capture(), Matchers.eq(playerBets));
		Mockito.verify(gameResultsPublisher).publishCurrentGameResults(winningNumberCapture2.capture(), Matchers.eq(playerBets));
		Mockito.verify(gameResultsPublisher).publishTotalResults();

		Integer value1 = winningNumberCapture1.getValue();
		Integer value2 = winningNumberCapture2.getValue();

		Assert.assertEquals(value1, value2);
		Assert.assertTrue(value1 >= 0 && value1 <= 36);
	}
}