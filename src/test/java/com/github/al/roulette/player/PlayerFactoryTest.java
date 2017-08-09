package com.github.al.roulette.player;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Optional;


public class PlayerFactoryTest {
	private PlayerFactory playerFactory = new PlayerFactory();

	@Test
	public void createPlayerSimple() throws Exception {
		Optional<Player> player = playerFactory.createPlayer("Some_Player");
		assertPlayer(player, true, "Some_Player", BigDecimal.ZERO, BigDecimal.ZERO);
	}

	@Test
	public void createPlayerNullInput() throws Exception {
		Optional<Player> player = playerFactory.createPlayer(null);
		assertPlayer(player, false, null, null, null);
	}

	@Test
	public void createPlayerComplex() throws Exception {
		Optional<Player> player = playerFactory.createPlayer("Barbara,2.0,1.0");
		assertPlayer(player, true, "Barbara", new BigDecimal("2.0"), new BigDecimal("1.0"));
	}

	@Test
	public void createPlayerComplexWithAmountsWithoutFloatingPoint() throws Exception {
		Optional<Player> player = playerFactory.createPlayer("Barbara,2,1");
		assertPlayer(player, true, "Barbara", new BigDecimal("2"), new BigDecimal("1"));
	}

	@Test
	public void createPlayerInvalid() throws Exception {
		Optional<Player> player = playerFactory.createPlayer("wrong player");
		assertPlayer(player, false, null, null, null);
	}

	private void assertPlayer(Optional<Player> player, boolean exists, String name, BigDecimal totalWin, BigDecimal totalBet) {
		Assert.assertTrue(player.isPresent() == exists);
		player.ifPresent(p -> {
			Assert.assertEquals(name, p.getName());
			Assert.assertEquals(p.getTotalBet().get(), totalBet);
			Assert.assertEquals(p.getTotalWin().get(), totalWin);
		});
	}

}