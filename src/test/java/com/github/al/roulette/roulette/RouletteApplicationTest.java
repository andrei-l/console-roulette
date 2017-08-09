package com.github.al.roulette.roulette;

import com.github.al.roulette.player.Player;
import com.github.al.roulette.player.PlayersStorage;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RouletteApplicationTest {

	@Autowired
	private PlayersStorage playersStorage;

	@Test
	public void testRoulette() throws Exception {
		Player somePlayer = playersStorage
				.getRegisteredPlayers()
				.stream()
				.filter(player -> Objects.equals("some player", player.getName())).findAny().get();

		ScheduledFuture<?> scheduledFuture = Executors
				.newSingleThreadScheduledExecutor()
				.schedule(() -> Assert.assertEquals(somePlayer.getTotalBet().get(), new BigDecimal("4")), 500, TimeUnit.MILLISECONDS);
		scheduledFuture.get();

	}
}