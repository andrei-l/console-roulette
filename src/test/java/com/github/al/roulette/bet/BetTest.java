package com.github.al.roulette.bet;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Optional;

public class BetTest {

	@Test
	public void testGetWinningsNumber() throws Exception {
		Bet betWithNumber = new Bet(Optional.of(4), new BigDecimal("3"), Bet.Type.NUMBER);

		Assert.assertEquals(new BigDecimal("108"), betWithNumber.getWinnings(4).get());
		Assert.assertEquals(Optional.empty(), betWithNumber.getWinnings(5));
	}

	@Test
	public void testGetWinningsOdd() throws Exception {
		Bet betWithNumber = new Bet(Optional.empty(), new BigDecimal("3"), Bet.Type.ODD);

		Assert.assertEquals(new BigDecimal("6"), betWithNumber.getWinnings(3).get());
		Assert.assertEquals(Optional.empty(), betWithNumber.getWinnings(4));
	}

	@Test
	public void testGetWinningsEven() throws Exception {
		Bet betWithNumber = new Bet(Optional.empty(), new BigDecimal("3"), Bet.Type.EVEN);

		Assert.assertEquals(new BigDecimal("6"), betWithNumber.getWinnings(4).get());
		Assert.assertEquals(Optional.empty(), betWithNumber.getWinnings(5));
	}
}