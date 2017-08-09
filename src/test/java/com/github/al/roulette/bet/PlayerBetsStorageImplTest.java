package com.github.al.roulette.bet;

import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PlayerBetsStorageImplTest {
	private PlayerBetsStorage playerBetsStorage = new PlayerBetsStorageImpl();

	@Test
	public void testAddBetAndGrabAllBets() throws Exception {
		List<PlayerBet> bets = createBets(10);
		bets.forEach(playerBetsStorage::addBet);
		assertContainBets(bets);
	}

	@Test
	public void testAddBetAndGrabAllBetsConcurrently() throws Exception {
		List<Future<?>> processedFutures = Lists.newArrayList();
		int totalThreads = 5000;
		ExecutorService executorService = Executors.newFixedThreadPool(totalThreads);
		List<PlayerBet> bets = createBets(totalThreads);
		bets.forEach(bet -> processedFutures.add(executorService.submit(() -> playerBetsStorage.addBet(bet))));

		for (Future f : processedFutures) {
			f.get();
		}

		assertContainBets(bets);
	}

	private List<PlayerBet> createBets(int numberOfBets) {
		return IntStream.range(0, numberOfBets).mapToObj(i -> Mockito.mock(PlayerBet.class)).collect(Collectors.toList());
	}

	private void assertContainBets(List<PlayerBet> bets) {
		Set<PlayerBet> playersBets = playerBetsStorage.grabCurrentGameBets();
		bets.forEach(b -> Assert.assertTrue("Missing bet: " + b, playersBets.contains(b)));
		Assert.assertTrue(playerBetsStorage.grabCurrentGameBets().isEmpty());
	}
}