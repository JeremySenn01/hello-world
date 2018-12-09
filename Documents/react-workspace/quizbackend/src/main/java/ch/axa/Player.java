package ch.axa;

public class Player {

	private String name;
	private int id;
	private int score;
	private int gamePin;
	
	public Player() {
	}
	
	public Player(String name, int gamePin, int id) {
		this.name = name;
		this.gamePin = gamePin;
		this.id = id;
		this.score = 0;
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getGamePin() {
		return gamePin;
	}

	public void setGamePin(int gamePin) {
		this.gamePin = gamePin;
	}
	
}
