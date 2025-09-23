package io.github.brunoborges.teammaker;

import java.io.IOException;

/**
 * Command-line application for team generation.
 * This class handles user interaction and output formatting,
 * delegating the core logic to TeamMaker.
 */
public class TeamMakerApp {

    public static void main(String[] args) {
        TeamMakerApp app = new TeamMakerApp();
        
        if (args.length > 0) {
            // Use JSON configuration file if provided
            try {
                app.runWithConfig(args[0]);
            } catch (IOException e) {
                System.err.println("Error loading configuration file: " + e.getMessage());
                System.err.println("Falling back to default configuration...");
                app.run();
            }
        } else {
            // Use default configuration or try to load from resources
            try {
                app.runWithResourceConfig("team-config.json");
            } catch (IOException e) {
                System.out.println("No configuration file found, using default players...");
                app.run();
            }
        }
    }

    /**
     * Run the application with default hard-coded players.
     */
    public void run() {
        TeamMaker teamMaker = new TeamMaker();

        // Keep trying until we get balanced teams
        TeamMakerResult result;
        do {
            result = teamMaker.createBalancedTeams();
        } while (!result.isBalanced());

        // Print the results
        printResults(result);
        printEndMessage();
    }

    /**
     * Run the application with JSON configuration from a file.
     * 
     * @param configPath path to the JSON configuration file
     * @throws IOException if the configuration cannot be loaded
     */
    public void runWithConfig(String configPath) throws IOException {
        System.out.println("Loading configuration from: " + configPath);
        
        TeamMakerResult result;
        do {
            result = TeamMaker.createBalancedTeamsFromConfig(configPath);
        } while (!result.isBalanced());

        // Print the results
        printResults(result);
        printEndMessage();
    }

    /**
     * Run the application with JSON configuration from resources.
     * 
     * @param resourceName name of the JSON configuration resource
     * @throws IOException if the configuration cannot be loaded
     */
    public void runWithResourceConfig(String resourceName) throws IOException {
        System.out.println("Loading configuration from resource: " + resourceName);
        
        TeamMakerResult result;
        do {
            result = TeamMaker.createBalancedTeamsFromResource(resourceName);
        } while (!result.isBalanced());

        // Print the results
        printResults(result);
        printEndMessage();
    }
    
    private void printResults(TeamMakerResult result) {
        for (Team team : result.getTeams()) {
            System.out.println(team);
            System.out.println("-----");
        }
    }
    
    private void printEndMessage() {
        System.out.println("##################");
        System.out.println("  END OF DRAW");
        System.out.println("##################");
    }
}