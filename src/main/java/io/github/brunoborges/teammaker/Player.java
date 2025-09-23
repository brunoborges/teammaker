package io.github.brunoborges.teammaker;

public class Player {

	private String name;
	private double score;

	public Player(String name, double score) {
		this.name = name;
		this.score = score;
	}

	public String getName() {
		return name;
	}

	public double getStrength() {
		return score;
	}

	public String toString() {
		StringBuilder string = new StringBuilder();
		string.append(getName()).append(" (").append(getStrength()).append(")");
		return string.toString();
	}
}