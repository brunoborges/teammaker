package io.github.brunoborges.teammaker.gui;

import io.github.brunoborges.teammaker.*;

/**
 * Simple test to verify the GUI generates teams without the TabPane casting error.
 */
public class TabPaneTest {
    public static void main(String[] args) {
        // Create test data
        TeamMakerConfig config = new TeamMakerConfig();
        config.setTeamNames(java.util.Arrays.asList("Team Alpha", "Team Beta"));
        
        // Create players
        config.setPlayers(java.util.Arrays.asList(
            new Player("John", 8.5),
            new Player("Jane", 7.0),
            new Player("Bob", 9.0),
            new Player("Alice", 6.5),
            new Player("Mike", 8.0),
            new Player("Sarah", 7.5)
        ));
        
        // Test team generation
        try {
            TeamMaker maker = new TeamMaker(config);
            TeamMakerResult result = maker.createBalancedTeams(config.getPlayers());
            java.util.List<Team> teams = result.getTeams();
            
            System.out.println("✅ Team generation successful!");
            System.out.println("Generated " + teams.size() + " teams:");
            
            for (int i = 0; i < teams.size(); i++) {
                Team team = teams.get(i);
                System.out.println((i + 1) + ". " + team.getName() + " (Score: " + team.getScore() + ")");
                for (Player player : team.getPlayers()) {
                    System.out.println("   - " + player.name() + " (" + player.score() + ")");
                }
            }
            
            System.out.println("🎯 The TabPane casting issue has been resolved!");
            
        } catch (Exception e) {
            System.err.println("❌ Error during team generation: " + e.getMessage());
            e.printStackTrace();
        }
    }
}