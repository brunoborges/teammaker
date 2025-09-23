package io.github.brunoborges.teammaker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Service class responsible for creating balanced teams from a list of players.
 * This class contains the core team-making logic without any UI concerns.
 */
public class TeamMaker {

	private static final int PLAYERS_PER_TEAM = 2;
	private static final char[] ALPHABET = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O',
			'P', 'Q',
			'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

	private List<Player> players;
	private List<Team> teams;
	private double averageStrength;

	public TeamMaker() {
		this.players = new ArrayList<>();
		this.teams = new ArrayList<>();
	}

	/**
	 * Creates balanced teams using the default player list.
	 * 
	 * @return TeamMakerResult containing the teams and balance information
	 */
	public TeamMakerResult createBalancedTeams() {
		return createBalancedTeams(DefaultPlayers.get());
	}

	/**
	 * Creates balanced teams from the provided list of players.
	 * 
	 * @param playerList the list of players to organize into teams
	 * @return TeamMakerResult containing the teams and balance information
	 */
	public TeamMakerResult createBalancedTeams(List<Player> playerList) {
		initializePlayers(playerList);
		prepareTeams();
		assembleTeams();

		boolean balanced = calculateBalance();
		double minStrength = teams.stream().mapToDouble(Team::getScore).min().orElse(0);
		double maxStrength = teams.stream().mapToDouble(Team::getScore).max().orElse(0);

		return new TeamMakerResult(new ArrayList<>(teams), balanced, minStrength, maxStrength);
	}

	private void initializePlayers(List<Player> playerList) {
		players.clear();
		players.addAll(playerList);
		calculateAverage();
	}

	private void prepareTeams() {
		teams.clear();
		int totalTeams = players.size() / PLAYERS_PER_TEAM;
		for (int i = 0; i < totalTeams; i++) {
			teams.add(new Team("Team " + ALPHABET[i], PLAYERS_PER_TEAM));
		}
	}

	private void calculateAverage() {
		double totalStrength = 0;

		for (Player p : players) {
			totalStrength += p.score();
		}

		averageStrength = totalStrength / players.size();
	}

	private boolean calculateBalance() {
		double minimumStrength = Double.MAX_VALUE;
		double maximumStrength = Double.MIN_VALUE;

		for (Team team : teams) {
			minimumStrength = Math.min(team.getScore(), minimumStrength);
			maximumStrength = Math.max(team.getScore(), maximumStrength);
		}

		return !(minimumStrength < 0.7 * maximumStrength);
	}

	private void assembleTeams() {
		// Randomize the list of players
		Collections.shuffle(players, new Random(System.currentTimeMillis()));
		Collections.shuffle(teams, new Random(System.currentTimeMillis()));

		Iterator<Team> itTeam = teams.iterator();
		while (players.size() > 0) {
			if (!itTeam.hasNext()) {
				// Check if any teams can still accept players
				boolean anyIncompleteTeam = teams.stream().anyMatch(team -> !team.isComplete());
				if (!anyIncompleteTeam) {
					// All teams are complete, but we have remaining players
					break;
				}
				itTeam = teams.iterator();
			}

			Team currentTeam = itTeam.next();
			if (currentTeam.isComplete()) {
				continue;
			}

			Player p = getPlayer(currentTeam);
			currentTeam.add(p);
		}
	}

	private Player getPlayer(double strength) {
		Player player = null;
		if (players.size() == 0)
			return null;
		else if (players.size() == 1)
			return players.remove(0);

		Iterator<Player> it = players.iterator();
		while (it.hasNext()) {
			Player p = it.next();
			if (p.score() == strength) {
				player = p;
				break;
			}
		}

		if (player != null)
			players.remove(player);

		return player;
	}

	private Player getPlayer(Team currentTeam) {
		double weight = Math.random();

		if (currentTeam.getScore() < averageStrength) {
			weight += Math.random();
		} else {
			weight -= Math.random();
		}

		double strength = 0;
		if (weight >= 0 && weight < 0.10) {
			// 10%
			strength = 1;
		} else if (weight >= 0.10 && weight < 0.25) {
			// 15%
			strength = 2;
		} else if (weight >= 0.25 && weight < 0.65) {
			// 40%
			strength = 3;
		} else if (weight >= 0.65 && weight < 0.95) {
			// 30%
			strength = 4;
		} else if (weight >= 0.95) {
			// 5%
			strength = 5;
		}

		Player player = getPlayer(strength);
		while (player == null && players.size() > 0)
			player = getPlayer(currentTeam);

		return player;
	}
}