package com.github.al.roulette.game;


import com.github.al.roulette.bet.PlayerBet;
import com.github.al.roulette.bet.PlayerBetsStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.PrimitiveIterator;
import java.util.Random;
import java.util.Set;

@Component
class GameProcessor {
	private static final PrimitiveIterator.OfInt WINNING_NUMBERS = new Random().ints(0, 36).iterator();
	private static final String BALL_LANDING_TIME_PLACEHOLDER = "${ball.landing.time}";

	@Autowired
	private PlayerBetsStorage playerBetsStorage;

	@Autowired
	private BetsResultsProcessor betsResultsProcessor;

	@Autowired
	private GameResultsPublisher gameResultsPublisher;

	@Scheduled(fixedRateString = BALL_LANDING_TIME_PLACEHOLDER, initialDelayString = BALL_LANDING_TIME_PLACEHOLDER)
	void landBall() {
		int winningNumber = nextWinningNumber();
		Set<PlayerBet> playerBets = playerBetsStorage.grabCurrentGameBets();

		betsResultsProcessor.processResults(winningNumber, playerBets);
		gameResultsPublisher.publishCurrentGameResults(winningNumber, playerBets);
		gameResultsPublisher.publishTotalResults();
	}

	private int nextWinningNumber() {
		return WINNING_NUMBERS.nextInt();
	}

}
