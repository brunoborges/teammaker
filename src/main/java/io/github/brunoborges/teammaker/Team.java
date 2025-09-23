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
		var playersText = new StringBuilder();
		players.forEach(p -> playersText.append("\n\t\t%s (%.1f),".formatted(p.name(), p.score())));

		if (!players.isEmpty()) {
			playersText.deleteCharAt(playersText.length() - 1); // Remove last comma
		}

		return """
				%s [strength = %.1f, players = {%s}]""".formatted(getName(), getScore(), playersText.toString());
	}

	public void reset() {
		this.score = 0;
		players.clear();
	}
}