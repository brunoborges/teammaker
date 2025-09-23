package io.github.brunoborges.teammaker;

import java.util.Random;

/**
 * Command-line application for team generation.
 * This class handles user interaction and output formatting,
 * delegating the core logic to TeamMaker.
 */
public class TeamMakerApp {

    public static void main(String[] args) {
        TeamMakerApp app = new TeamMakerApp();
        app.run();
    }

    public void run() {
        TeamMaker teamMaker = new TeamMaker();

        // Keep trying until we get balanced teams
        TeamMakerResult result;
        do {
            result = teamMaker.createBalancedTeams();
        } while (!result.isBalanced());

        // Print the results
        printResults(result);

        System.out.println("##################");
        System.out.println("  END OF DRAW");
        System.out.println("##################");
    }

    private void printResults(TeamMakerResult result) {
        for (Team team : result.getTeams()) {
            System.out.println(team);
            System.out.println("-----");
        }
    }
}