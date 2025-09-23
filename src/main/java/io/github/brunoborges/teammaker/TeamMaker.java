package io.github.brunoborges.teammaker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class TeamMaker {

	static final int PLAYERS_PER_TEAM = 2;
	static List<Player> players = new ArrayList<Player>();
	static List<Team> teams = new ArrayList<Team>();
	static char[] alphabet = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q',
			'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

	private static double averageStrength;

	private static void initializePlayers() {
		players.clear();

		players.add(new Player("Alex Souza", 3));
		players.add(new Player("Andre Guedes", 2));
		players.add(new Player("Augusto Nesser (Caixetinha)", 3));
		players.add(new Player("Bruno Borges", 4));
		players.add(new Player("Diego Berardino", 3));
		players.add(new Player("Diogo Goncalves", 4));
		players.add(new Player("Diogo Maximo", 4));
		players.add(new Player("Felipe Magela", 4));
		players.add(new Player("Guilhermo Reid", 3));
		players.add(new Player("Jean Pereira", 3));
		players.add(new Player("Juan Garay", 2));
		players.add(new Player("Leo Soares", 4));
		players.add(new Player("Leonardo Pinto", 3));
		players.add(new Player("LLucio Simoes", 3));
		players.add(new Player("Marcelo Behera", 3));
		players.add(new Player("Pedro Barros", 3));
		players.add(new Player("Rafael Coutinho", 3));
		players.add(new Player("Rodrigo Caixeta", 4));
		players.add(new Player("Tiago Barros (pedrinho)", 3));
		players.add(new Player("Tiago Carpanese", 2));

		calculateAverage();
	}

	private static void prepareTeams() {
		teams.clear();
		int totalTeams = players.size() / PLAYERS_PER_TEAM;
		for (int i = 0; i < totalTeams; i++) {
			teams.add(new Team("Team " + alphabet[i], PLAYERS_PER_TEAM));
		}
	}

	private static void calculateAverage() {
		double totalStrength = 0;

		for (Player p : players) {
			totalStrength += p.getStrength();
		}

		averageStrength = totalStrength / players.size();
	}

	public static void main(String[] args) {
		do {
			initializePlayers();
			prepareTeams();
			assembleTeams();
		} while (!calculateBalance());

		printTeams();

		System.out.println("##################");
		System.out.println("  END OF DRAW");
		System.out.println("##################");
	}

	private static void printTeams() {
		for (Team team : teams) {
			System.out.println(team);
			System.out.println("-----");

			try {
				Thread.sleep(new Random().nextInt(3000));
			} catch (InterruptedException e) {
			}
		}
	}

	private static boolean calculateBalance() {
		int minimumStrength = Integer.MAX_VALUE;
		int maximumStrength = Integer.MIN_VALUE;

		for (Team team : teams) {
			minimumStrength = Math.min(team.getStrength(), minimumStrength);
			maximumStrength = Math.max(team.getStrength(), maximumStrength);
		}

		return !(minimumStrength < 0.7 * maximumStrength);
	}

	private static void assembleTeams() {
		// Randomize the list of players
		Collections.shuffle(players, new Random(System.currentTimeMillis()));
		Collections.shuffle(teams, new Random(System.currentTimeMillis()));

		Iterator<Team> itTeam = teams.iterator();
		while (players.size() > 0) {
			Team currentTeam = itTeam.next();
			if (currentTeam.isComplete()) {
				continue;
			}

			Player p = getPlayer(currentTeam);
			currentTeam.addPlayer(p);

			if (itTeam.hasNext() == false) {
				itTeam = teams.iterator();
			}
		}
	}

	private static Player getPlayer(int strength) {
		Player player = null;
		if (players.size() == 0)
			return null;
		else if (players.size() == 1)
			return players.remove(0);

		Iterator<Player> it = players.iterator();
		while (it.hasNext()) {
			Player p = it.next();
			if (p.getStrength() == strength) {
				player = p;
				break;
			}
		}

		if (player != null)
			players.remove(player);

		return player;
	}

	private static Player getPlayer(Team currentTeam) {
		double weight = Math.random();

		if (currentTeam.getStrength() < averageStrength) {
			weight += Math.random();
		} else {
			weight -= Math.random();
		}

		int strength = 0;
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