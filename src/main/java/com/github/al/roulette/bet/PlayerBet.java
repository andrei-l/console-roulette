package com.github.al.roulette.bet;


import com.github.al.roulette.player.Player;

public class PlayerBet {
	private final Player player;
	private final Bet bet;

	public PlayerBet(Player player, Bet bet) {
		this.player = player;
		this.bet = bet;
	}

	public Player getPlayer() {
		return player;
	}

	public Bet getBet() {
		return bet;
	}
}
