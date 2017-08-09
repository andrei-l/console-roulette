package com.github.al.roulette;


import com.github.al.roulette.player.PlayersLoader;
import com.google.common.collect.Iterables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@SpringBootApplication
@EnableScheduling
public class RouletteApplication implements ApplicationRunner {
	private static final String PLAYERS_DEFAULT_INPUT_FILE = "input.txt";

	private static final String PLAYERS_CUSTOM_INPUT_FILE_PARAM = "input";

	@Autowired
	private PlayersLoader playersLoader;

	public static void main(String[] args) {
		SpringApplication.run(RouletteApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		String fileName = null;
		if (args.containsOption(PLAYERS_CUSTOM_INPUT_FILE_PARAM)) {
			fileName = Iterables.getFirst(args.getOptionValues(PLAYERS_CUSTOM_INPUT_FILE_PARAM), null);
		}
		InputStream inputStream = fileToInputStream(fileName);
		playersLoader.loadPlayers(inputStream);
	}

	private InputStream fileToInputStream(String fileName) throws IOException {
		try {
			return new ClassPathResource(Optional.ofNullable(fileName).orElse(PLAYERS_DEFAULT_INPUT_FILE)).getInputStream();
		} catch (Exception e) {
			return new FileInputStream(fileName);
		}
	}

}
