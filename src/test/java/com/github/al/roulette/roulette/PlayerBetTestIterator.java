package com.github.al.roulette.roulette;


import com.github.al.roulette.bet.PlayerBet;

import java.util.Iterator;

public class PlayerBetTestIterator implements Iterator<PlayerBet> {
	private final boolean[] hasNextAnswers;
	private final PlayerBet[] nextAnswers;

	public PlayerBetTestIterator(boolean[] hasNextAnswers, PlayerBet[] nextAnswers) {
		this.hasNextAnswers = hasNextAnswers;
		this.nextAnswers = nextAnswers;
	}

	private int hasNextAnswerNum;
	private int nextAnswerNum;

	@Override
	public boolean hasNext() {
		return hasNextAnswers[hasNextAnswerNum++];
	}

	@Override
	public PlayerBet next() {
		return nextAnswers[nextAnswerNum++];
	}
}
