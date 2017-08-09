package com.github.al.roulette.player;

import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class PlayersStorageImplTest {
	private PlayersStorage playersStorage = new PlayersStorageImpl();


	@Test
	public void testAddPlayerAndGetAllPlayers() throws Exception {
		playersStorage.addPlayer(new Player("test1"));
		playersStorage.addPlayer(new Player("test2"));

		assertContainsPlayers("test1", "test2");
	}


	@Test
	public void testAddPlayerAndGetAllPlayersConcurrentlyEventuallyWorksFine() throws Exception {
		AtomicInteger nextPlayerId = new AtomicInteger(0);
		List<Future<?>> processedFuture = Lists.newArrayList();
		int totalThreads = 5000;
		ExecutorService executorService = Executors.newFixedThreadPool(totalThreads);

		IntStream
				.range(0, totalThreads / 2)
				.forEach(i -> processedFuture.add(executorService.submit(() -> playersStorage.addPlayer(new Player("player" + nextPlayerId.getAndIncrement())))));

		IntStream
				.range(0, totalThreads / 2)
				.forEach(i -> processedFuture.add(executorService.submit(() -> playersStorage.getRegisteredPlayers())));


		for (Future f : processedFuture) {
			f.get();
		}

		Assert.assertEquals(nextPlayerId.get(), totalThreads / 2);
		IntStream.range(0, totalThreads / 2).forEach(i -> assertContainsPlayer("player" + i));
	}

	private void assertContainsPlayers(String... playerNames) {
		for (String playerName : playerNames) {
			assertContainsPlayer(playerName);
		}
	}

	private void assertContainsPlayer(String playerName) {
		Assert.assertTrue(playersStorage.getRegisteredPlayers().contains(new Player(playerName)));
	}
}