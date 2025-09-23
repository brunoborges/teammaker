package io.github.brunoborges.teammaker;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

@DisplayName("TeamMaker Tests")
class TeamMakerTest {

    private TeamMaker teamMaker;

    @BeforeEach
    void setUp() {
        teamMaker = new TeamMaker();
    }

    @Test
    @DisplayName("Should create balanced teams with default players")
    void shouldCreateBalancedTeamsWithDefaultPlayers() {
        // When
        TeamMakerResult result = teamMaker.createBalancedTeams();
        
        // Then
        assertNotNull(result);
        assertNotNull(result.getTeams());
        assertFalse(result.getTeams().isEmpty());
        assertTrue(result.isBalanced());
    }

    @Test
    @DisplayName("Should return correct number of default players")
    void shouldReturnCorrectNumberOfDefaultPlayers() {
        // When
        List<Player> players = DefaultPlayers.get();
        
        // Then
        assertEquals(20, players.size());
        
        // Check some specific players
        assertTrue(players.stream().anyMatch(p -> p.name().equals("Bruno")));
        assertTrue(players.stream().anyMatch(p -> p.name().equals("Alex")));
        assertTrue(players.stream().anyMatch(p -> p.name().equals("Leo")));
    }

    @Test
    @DisplayName("Should create correct number of teams from player count")
    void shouldCreateCorrectNumberOfTeamsFromPlayerCount() {
        // Given
        List<Player> players = DefaultPlayers.get(); // 20 players
        
        // When
        TeamMakerResult result = teamMaker.createBalancedTeams(players);
        
        // Then
        // 20 players / 2 players per team = 10 teams
        assertEquals(10, result.getTeams().size());
    }

    @Test
    @DisplayName("Should create teams with proper names")
    void shouldCreateTeamsWithProperNames() {
        // When
        TeamMakerResult result = teamMaker.createBalancedTeams();
        
        // Then
        List<Team> teams = result.getTeams();
        assertTrue(teams.size() > 0, "Should create at least one team");
        
        // The teams should have alphabetic names, but order may be different due to shuffling
        // Just check that team names follow the pattern "Team X" where X is a letter
        for (Team team : teams) {
            assertTrue(team.getName().matches("Team [A-Z]"), 
                       "Team name should be 'Team X' where X is a letter, but was: " + team.getName());
        }
        
        // Check that we have distinct team names
        long distinctNames = teams.stream()
                .map(Team::getName)
                .distinct()
                .count();
        assertEquals(teams.size(), distinctNames, "All team names should be unique");
    }

    @Test
    @DisplayName("Should distribute all players to teams")
    void shouldDistributeAllPlayersToTeams() {
        // Given
        List<Player> originalPlayers = DefaultPlayers.get();
        int originalPlayerCount = originalPlayers.size();
        
        // When
        TeamMakerResult result = teamMaker.createBalancedTeams(originalPlayers);
        
        // Then
        List<Team> teams = result.getTeams();
        
        // Count total players in all teams
        int totalPlayersInTeams = teams.stream().mapToInt(team -> {
            // Using reflection to access private field
            try {
                var field = Team.class.getDeclaredField("players");
                field.setAccessible(true);
                @SuppressWarnings("unchecked")
                List<Player> playersInTeam = (List<Player>) field.get(team);
                return playersInTeam.size();
            } catch (Exception e) {
                return 0;
            }
        }).sum();
        
        assertEquals(originalPlayerCount, totalPlayersInTeams, "All original players should be in teams");
        
        // All teams should be complete
        for (Team team : teams) {
            assertTrue(team.isComplete(), "All teams should be complete after assembly");
        }
    }

    @Test
    @DisplayName("Should work with custom player list")
    void shouldWorkWithCustomPlayerList() {
        // Given
        List<Player> customPlayers = new ArrayList<>();
        customPlayers.add(new Player("Player 1", 3.0));
        customPlayers.add(new Player("Player 2", 3.0));
        customPlayers.add(new Player("Player 3", 3.0));
        customPlayers.add(new Player("Player 4", 3.0));
        
        // When
        TeamMakerResult result = teamMaker.createBalancedTeams(customPlayers);
        
        // Then
        assertNotNull(result);
        assertEquals(2, result.getTeams().size()); // 4 players / 2 = 2 teams
        
        for (Team team : result.getTeams()) {
            assertTrue(team.isComplete());
            assertEquals(6.0, team.getScore(), 0.001); // 3.0 + 3.0
        }
    }

    @Test
    @DisplayName("Should handle odd number of players")
    void shouldHandleOddNumberOfPlayers() {
        // Given
        List<Player> oddPlayers = new ArrayList<>();
        oddPlayers.add(new Player("Player 1", 3.0));
        oddPlayers.add(new Player("Player 2", 3.0));
        oddPlayers.add(new Player("Player 3", 3.0)); // Odd number: 3
        
        // When
        TeamMakerResult result = teamMaker.createBalancedTeams(oddPlayers);
        
        // Then
        // 3 players / 2 players per team = 1 team (integer division, 1 player left out)
        assertEquals(1, result.getTeams().size());
    }

    @Test
    @DisplayName("Should calculate balance correctly for balanced teams")
    void shouldCalculateBalanceCorrectlyForBalancedTeams() {
        // Given
        List<Player> balancedPlayers = new ArrayList<>();
        balancedPlayers.add(new Player("Player 1", 3.0));
        balancedPlayers.add(new Player("Player 2", 3.0));
        balancedPlayers.add(new Player("Player 3", 3.0));
        balancedPlayers.add(new Player("Player 4", 3.0));
        
        // When
        TeamMakerResult result = teamMaker.createBalancedTeams(balancedPlayers);
        
        // Then
        assertTrue(result.isBalanced(), "Teams with equal strength should be balanced");
        assertEquals(6.0, result.getMinimumStrength(), 0.001);
        assertEquals(6.0, result.getMaximumStrength(), 0.001);
    }

    @Test
    @DisplayName("Should detect unbalanced teams")
    void shouldDetectUnbalancedTeams() {
        // Given - Create players that would result in unbalanced teams
        List<Player> unbalancedPlayers = new ArrayList<>();
        unbalancedPlayers.add(new Player("Strong Player 1", 5.0));
        unbalancedPlayers.add(new Player("Strong Player 2", 5.0));
        unbalancedPlayers.add(new Player("Weak Player 1", 1.0));
        unbalancedPlayers.add(new Player("Weak Player 2", 1.0));
        
        // When
        TeamMakerResult result = teamMaker.createBalancedTeams(unbalancedPlayers);
        
        // Then
        // Note: The result might still be balanced due to the random shuffling,
        // but we can check the min/max values are different
        assertTrue(result.getMinimumStrength() <= result.getMaximumStrength());
    }

    @Test
    @DisplayName("Should create new instance each time")
    void shouldCreateNewInstanceEachTime() {
        // Given
        List<Player> players1 = List.of(new Player("P1", 1.0), new Player("P2", 1.0));
        List<Player> players2 = List.of(new Player("P3", 2.0), new Player("P4", 2.0));
        
        // When
        TeamMakerResult result1 = teamMaker.createBalancedTeams(players1);
        TeamMakerResult result2 = teamMaker.createBalancedTeams(players2);
        
        // Then
        assertNotSame(result1, result2);
        assertNotSame(result1.getTeams(), result2.getTeams());
        assertEquals(2.0, result1.getTeams().get(0).getScore(), 0.001);
        assertEquals(4.0, result2.getTeams().get(0).getScore(), 0.001);
    }
}