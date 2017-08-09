package com.github.al.roulette.game;

import com.github.al.roulette.roulette.PlayerBetTestIterator;
import com.github.al.roulette.bet.PlayerBet;
import com.github.al.roulette.bet.PlayerBetsStorage;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Iterator;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@RunWith(MockitoJUnitRunner.class)
public class PlayerBetsListenerTest {
	@InjectMocks
	private PlayerBetsListener playerBetsListener;

	@Mock
	private PlayerBetsStorage playerBetsStorage;

	@Mock
	private PlayerBet bet1;

	@Mock
	private PlayerBet bet2;


	@Before
	public void setUp() throws Exception {
		Iterator<PlayerBet> playerBetIterator = new PlayerBetTestIterator(new boolean[]{true, true, false}, new PlayerBet[]{bet1, bet2});
		Whitebox.setInternalState(playerBetsListener, "playerBetIterator", playerBetIterator);
	}

	@Test
	public void shouldIterativelyAddAllBets() throws Exception {
		playerBetsListener.acceptBets();


		ScheduledFuture<?> scheduledFuture = Executors.newSingleThreadScheduledExecutor().schedule(() -> {
			Mockito.verify(playerBetsStorage).addBet(bet1);
			Mockito.verify(playerBetsStorage).addBet(bet2);
		}, 20, TimeUnit.MILLISECONDS);

		scheduledFuture.get();
	}

}
