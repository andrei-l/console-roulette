package com.github.al.roulette.player;


import com.google.common.base.Objects;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class Player {
	private final String name;
	private final AtomicReference<BigDecimal> totalWin;
	private final AtomicReference<BigDecimal> totalBet;

	public Player(String name) {
		this(name, Optional.empty(), Optional.empty());
	}

	public Player(String name, Optional<BigDecimal> totalWin, Optional<BigDecimal> totalBet) {
		this.name = name;
		this.totalWin = new AtomicReference<>(totalWin.orElse(BigDecimal.ZERO));
		this.totalBet = new AtomicReference<>(totalBet.orElse(BigDecimal.ZERO));
	}

	public String getName() {
		return name;
	}

	public AtomicReference<BigDecimal> getTotalWin() {
		return totalWin;
	}

	public AtomicReference<BigDecimal> getTotalBet() {
		return totalBet;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Player)) return false;
		Player player = (Player) o;
		return Objects.equal(name, player.name);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(name);
	}
}
