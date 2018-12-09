package ch.axa;

import java.util.ArrayList;
import java.util.List;

public class Game {

	// TODO : Make Enums out of Constants
	public static final int WAITINGFORPLAYERS = 0;
	public static final int PLAYING = 1;
	public static final int FINISHED = 2;

	private int pin;
	private int status;
	private Quiz quiz;
	private List<Player> players;

	public Game(int pin, Quiz quiz) {
		super();
		this.pin = pin;
		this.status = WAITINGFORPLAYERS;
		this.players = new ArrayList<>();
		this.quiz = quiz;
	}
	
	public Quiz getQuiz() {
		return this.quiz;
	}

	public List<Player> getPlayers() {
		return this.players;
	}

	public void addPlayer(Player p) {
		players.add(p);
	}

	public int getPin() {
		return pin;
	}

	public void setPin(int pin) {
		this.pin = pin;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}