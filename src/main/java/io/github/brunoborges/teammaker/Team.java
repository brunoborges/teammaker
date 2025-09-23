package io.github.brunoborges.teammaker;

import java.util.ArrayList;
import java.util.List;

public class Team {

	private List<Player> players = new ArrayList<Player>();
	private double score = 0;
	private String name;
	private int playerLimit;

	public Team(String name, int playersPerTeam) {
		this.name = name;
		this.playerLimit = playersPerTeam;
	}

	public boolean isComplete() {
		return players.size() == playerLimit;
	}

	public void add(Player p) {
		if (isComplete()) {
			throw new IllegalStateException("Team '" + getName() + "' is already complete");
		}
		score += p.score();
		players.add(p);
	}

	public double getScore() {
		return score;
	}

	public String getName() {
		return name;
	}

	public String toString() {
		StringBuilder string = new StringBuilder();
		string.append(getName());
		string.append(" [strength = ").append(getScore()).append(", players = {");
		for (Player p : players) {
			string.append("\n\t");
			string.append(p.name());
			string.append(" (").append(p.score()).append(')');
			string.append(',');
		}
		if (!players.isEmpty()) {
			string.deleteCharAt(string.length() - 1);
		}
		string.append("}]");
		return string.toString();
	}

	public void reset() {
		this.score = 0;
		players.clear();
	}
}