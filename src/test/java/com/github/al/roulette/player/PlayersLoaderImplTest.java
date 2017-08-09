package com.github.al.roulette.player;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class PlayersLoaderImplTest {

	@InjectMocks
	private PlayersLoaderImpl playersLoader;

	@Mock
	private PlayerFactory playerFactory;

	@Mock
	private PlayersStorage playersStorage;

	@Mock
	private Player player;

	@Test
	public void testLoadPlayers() throws Exception {
		String[] inputStrings = {"line1", "line2"};
		String input = Joiner.on(System.lineSeparator()).join(inputStrings);
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(input.getBytes(Charsets.UTF_8));

		Mockito.when(playerFactory.createPlayer("line1")).thenReturn(Optional.empty());
		Mockito.when(playerFactory.createPlayer("line2")).thenReturn(Optional.of(player));

		playersLoader.loadPlayers(byteArrayInputStream);

		Mockito.verify(playersStorage).addPlayer(player);
		Mockito.verifyNoMoreInteractions(playersStorage);
	}
}