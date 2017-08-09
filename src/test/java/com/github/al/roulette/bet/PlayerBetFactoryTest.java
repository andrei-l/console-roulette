package com.github.al.roulette.bet;

import com.github.al.roulette.bet.Bet.Type;
import com.github.al.roulette.player.Player;
import com.github.al.roulette.player.PlayersStorage;
import com.google.common.collect.ImmutableSet;
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
public class PlayerBetFactoryTest {
	@InjectMocks
	private PlayerBetFactory playerBetFactory;

	@Mock
	private PlayersStorage playersStorage;

	private Player somePlayer = new Player("Some_Player");
	private Player barbaraPlayer = new Player("Barbara");

	@Before
	public void setUp() throws Exception {
		Mockito.when(playersStorage.getRegisteredPlayers()).thenReturn(ImmutableSet.of(
				somePlayer, barbaraPlayer
		));
	}

	@Test
	public void testCreatePlayerBetNumber() throws Exception {
		Optional<PlayerBet> playerBet = playerBetFactory.createPlayerBet("Some_Player 2 1.0");

		assertPlayerBet(playerBet, true, somePlayer, Type.NUMBER, Optional.of(2), new BigDecimal("1.0"));
	}

	@Test
	public void testCreatePlayerBetNullInput() throws Exception {
		Optional<PlayerBet> playerBet = playerBetFactory.createPlayerBet(null);

		assertPlayerBet(playerBet, false, null, null, null, null);
	}

	@Test
	public void testCreatePlayerBetWithAmountWithoutFloatingPoint() throws Exception {
		Optional<PlayerBet> playerBet = playerBetFactory.createPlayerBet("Some_Player 2 1");

		assertPlayerBet(playerBet, true, somePlayer, Type.NUMBER, Optional.of(2), new BigDecimal("1"));
	}

	@Test
	public void testCreatePlayerBetEven() throws Exception {
		Optional<PlayerBet> playerBet = playerBetFactory.createPlayerBet("Barbara EVEN 3.0");

		assertPlayerBet(playerBet, true, barbaraPlayer, Type.EVEN, Optional.empty(), new BigDecimal("3.0"));
	}

	@Test
	public void testCreatePlayerBetOdd() throws Exception {
		Optional<PlayerBet> playerBet = playerBetFactory.createPlayerBet("Barbara ODD 3.0");

		assertPlayerBet(playerBet, true, barbaraPlayer, Type.ODD, Optional.empty(), new BigDecimal("3.0"));
	}

	@Test
	public void testCreatePlayerBetInvalidBet() throws Exception {
		Optional<PlayerBet> playerBet = playerBetFactory.createPlayerBet("Barbara wrong wrong");

		assertPlayerBet(playerBet, false, null, null, null, null);
	}

	@Test
	public void testCreatePlayerBetInvalidPlayer() throws Exception {
		Optional<PlayerBet> playerBet = playerBetFactory.createPlayerBet("wrong ODD 3.0");

		assertPlayerBet(playerBet, false, null, null, null, null);
	}


	private void assertPlayerBet(Optional<PlayerBet> playerBet, boolean exists, Player player, Type betType, Optional<Integer> betNumber, BigDecimal betAmount) {
		Assert.assertTrue(playerBet.isPresent() == exists);
		playerBet.ifPresent(pb -> {
			Assert.assertEquals(pb.getPlayer(), player);
			Assert.assertEquals(pb.getBet().getType(), betType);
			Assert.assertEquals(pb.getBet().getBetNumber(), betNumber);
			Assert.assertEquals(pb.getBet().getBetAmount(), betAmount);
		});
	}
}