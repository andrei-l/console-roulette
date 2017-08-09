package com.github.al.roulette.bet;

import com.github.al.roulette.roulette.mockito.IterativeAnswer;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.stubbing.answers.Returns;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.BufferedReader;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class PlayerBetsFromConsoleIteratorImplTest {
	@Mock
	private PlayerBetFactory playerBetFactory;

	@Mock
	private BufferedReader bufferedReader;

	@Mock
	private PlayerBet playerBet;

	@InjectMocks
	private PlayerBetsFromConsoleIteratorImpl playerBetsConsoleSourceIterator;


	@Test
	public void testHasNext() throws Exception {
		Assert.assertTrue(playerBetsConsoleSourceIterator.hasNext());
	}

	@Test
	public void testNext() throws Exception {
		Mockito.when(bufferedReader.readLine()).thenReturn("line");
		Mockito.when(playerBetFactory.createPlayerBet("line")).thenReturn(Optional.of(playerBet));
		Assert.assertEquals(playerBet, playerBetsConsoleSourceIterator.next());

		Mockito.verify(bufferedReader).readLine();
	}

	@Test
	public void testNextWhenInvalidInput() throws Exception {
		Mockito.when(bufferedReader.readLine()).thenAnswer(new IterativeAnswer(new Returns("line"), new Returns("line2")));
		Mockito.when(playerBetFactory.createPlayerBet("line")).thenReturn(Optional.empty());
		Mockito.when(playerBetFactory.createPlayerBet("line2")).thenReturn(Optional.of(playerBet));
		Assert.assertEquals(playerBet, playerBetsConsoleSourceIterator.next());

		Mockito.verify(bufferedReader, Mockito.times(2)).readLine();
	}
	@Test
	public void testNextWhenException() throws Exception {
		Mockito.when(bufferedReader.readLine()).thenAnswer(new IterativeAnswer(new Returns("line"), new Returns("line2")));
		Mockito.when(playerBetFactory.createPlayerBet("line")).thenThrow(new NumberFormatException("ex"));
		Mockito.when(playerBetFactory.createPlayerBet("line2")).thenReturn(Optional.of(playerBet));
		Assert.assertEquals(playerBet, playerBetsConsoleSourceIterator.next());

		Mockito.verify(bufferedReader, Mockito.times(2)).readLine();
	}


	@Test
	public void testDestroy() throws Exception {
		playerBetsConsoleSourceIterator.destroy();
		Mockito.verify(bufferedReader).close();
	}
}