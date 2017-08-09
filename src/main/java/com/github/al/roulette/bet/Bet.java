package com.github.al.roulette.bet;


import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class Bet {
	private static final BigDecimal X36_MULTIPLIER = new BigDecimal("36");
	private static final BigDecimal X2_MULTIPLIER = new BigDecimal("2");
	private final Optional<Integer> betNumber;
	private final BigDecimal betAmount;
	private final Type type;
	private final AtomicReference<Optional<BigDecimal>> winnings = new AtomicReference<>(Optional.empty());
	private final AtomicReference<Optional<Outcome>> outcome = new AtomicReference<>(Optional.empty());

	public Bet(Optional<Integer> betNumber, BigDecimal betAmount, Type type) {
		this.betNumber = betNumber;
		this.betAmount = betAmount;
		this.type = type;
	}

	public Optional<BigDecimal> getWinnings(int number) {
		switch (type) {
			case NUMBER:
				return multiplyWinnings(betNumber.filter(n -> n == number).map(n -> betAmount), X36_MULTIPLIER);
			case ODD:
				return multiplyWinnings(Optional.of(betAmount).filter(amount -> number % 2 != 0), X2_MULTIPLIER);
			case EVEN:
				return multiplyWinnings(Optional.of(betAmount).filter(amount -> number % 2 == 0), X2_MULTIPLIER);
			default:
				return Optional.empty();
		}
	}

	private Optional<BigDecimal> multiplyWinnings(Optional<BigDecimal> winnings, BigDecimal multiplier) {
		return winnings.map(amount -> amount.multiply(multiplier));
	}

	public BigDecimal getBetAmount() {
		return betAmount;
	}

	public Optional<Integer> getBetNumber() {
		return betNumber;
	}

	public Type getType() {
		return type;
	}

	public AtomicReference<Optional<BigDecimal>> getWinnings() {
		return winnings;
	}

	public AtomicReference<Optional<Outcome>> getOutcome() {
		return outcome;
	}

	public enum Type {
		NUMBER, ODD, EVEN
	}

	public enum Outcome {
		LOSE, WIN
	}
}

