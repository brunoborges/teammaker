package io.github.brunoborges.teammaker;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.util.List;

@DisplayName("TeamMaker Tests")
class TeamMakerTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    @DisplayName("Should have correct constant values")
    void shouldHaveCorrectConstantValues() {
        assertEquals(2, TeamMaker.PLAYERS_PER_TEAM);
    }

    @Test
    @DisplayName("Should have alphabet array with 26 letters")
    void shouldHaveAlphabetArrayWith26Letters() throws Exception {
        Field alphabetField = TeamMaker.class.getDeclaredField("alphabet");
        alphabetField.setAccessible(true);
        char[] alphabet = (char[]) alphabetField.get(null);
        
        assertEquals(26, alphabet.length);
        assertEquals('A', alphabet[0]);
        assertEquals('Z', alphabet[25]);
    }

    @Test
    @DisplayName("Should initialize players correctly")
    void shouldInitializePlayersCorrectly() throws Exception {
        // Access private method using reflection
        Method initializePlayers = TeamMaker.class.getDeclaredMethod("initializePlayers");
        initializePlayers.setAccessible(true);
        
        // Access static field
        Field playersField = TeamMaker.class.getDeclaredField("players");
        playersField.setAccessible(true);
        
        // When
        initializePlayers.invoke(null);
        
        // Then
        @SuppressWarnings("unchecked")
        List<Player> players = (List<Player>) playersField.get(null);
        assertEquals(20, players.size());
        
        // Check some specific players
        assertTrue(players.stream().anyMatch(p -> p.name().equals("Bruno Borges")));
        assertTrue(players.stream().anyMatch(p -> p.name().equals("Alex Souza")));
        assertTrue(players.stream().anyMatch(p -> p.name().equals("Leo Soares")));
    }

    @Test
    @DisplayName("Should calculate average strength correctly")
    void shouldCalculateAverageStrengthCorrectly() throws Exception {
        // Access private methods using reflection
        Method initializePlayers = TeamMaker.class.getDeclaredMethod("initializePlayers");
        initializePlayers.setAccessible(true);
        
        Field averageStrengthField = TeamMaker.class.getDeclaredField("averageStrength");
        averageStrengthField.setAccessible(true);
        
        // When
        initializePlayers.invoke(null);
        
        // Then
        double averageStrength = (Double) averageStrengthField.get(null);
        
        // Known players have strengths: mostly 3s and 4s, some 2s
        // Total should be around 3.0 (20 players with average ~3)
        assertTrue(averageStrength >= 2.5 && averageStrength <= 3.5, 
                   "Average strength should be between 2.5 and 3.5, but was: " + averageStrength);
    }

    @Test
    @DisplayName("Should prepare correct number of teams")
    void shouldPrepareCorrectNumberOfTeams() throws Exception {
        // Access private methods using reflection
        Method initializePlayers = TeamMaker.class.getDeclaredMethod("initializePlayers");
        Method prepareTeams = TeamMaker.class.getDeclaredMethod("prepareTeams");
        initializePlayers.setAccessible(true);
        prepareTeams.setAccessible(true);
        
        Field teamsField = TeamMaker.class.getDeclaredField("teams");
        teamsField.setAccessible(true);
        
        // When
        initializePlayers.invoke(null);
        prepareTeams.invoke(null);
        
        // Then
        @SuppressWarnings("unchecked")
        List<Team> teams = (List<Team>) teamsField.get(null);
        
        // 20 players / 2 players per team = 10 teams
        assertEquals(10, teams.size());
        
        // Check team names
        assertEquals("Team A", teams.get(0).getName());
        assertEquals("Team B", teams.get(1).getName());
        assertEquals("Team J", teams.get(9).getName());
    }

    @Test
    @DisplayName("Should calculate balance correctly for balanced teams")
    void shouldCalculateBalanceCorrectlyForBalancedTeams() throws Exception {
        Method calculateBalance = TeamMaker.class.getDeclaredMethod("calculateBalance");
        calculateBalance.setAccessible(true);
        
        Field teamsField = TeamMaker.class.getDeclaredField("teams");
        teamsField.setAccessible(true);
        
        // Create balanced teams manually
        @SuppressWarnings("unchecked")
        List<Team> teams = (List<Team>) teamsField.get(null);
        teams.clear();
        
        Team team1 = new Team("Team A", 2);
        team1.add(new Player("Player 1", 3.0));
        team1.add(new Player("Player 2", 3.0));
        
        Team team2 = new Team("Team B", 2);
        team2.add(new Player("Player 3", 3.0));
        team2.add(new Player("Player 4", 3.0));
        
        teams.add(team1);
        teams.add(team2);
        
        // When
        Boolean isBalanced = (Boolean) calculateBalance.invoke(null);
        
        // Then
        assertTrue(isBalanced, "Teams with equal strength should be balanced");
    }

    @Test
    @DisplayName("Should calculate balance correctly for unbalanced teams")
    void shouldCalculateBalanceCorrectlyForUnbalancedTeams() throws Exception {
        Method calculateBalance = TeamMaker.class.getDeclaredMethod("calculateBalance");
        calculateBalance.setAccessible(true);
        
        Field teamsField = TeamMaker.class.getDeclaredField("teams");
        teamsField.setAccessible(true);
        
        // Create unbalanced teams manually
        @SuppressWarnings("unchecked")
        List<Team> teams = (List<Team>) teamsField.get(null);
        teams.clear();
        
        Team strongTeam = new Team("Strong Team", 2);
        strongTeam.add(new Player("Strong Player 1", 5.0));
        strongTeam.add(new Player("Strong Player 2", 5.0));
        // Total: 10.0
        
        Team weakTeam = new Team("Weak Team", 2);
        weakTeam.add(new Player("Weak Player 1", 1.0));
        weakTeam.add(new Player("Weak Player 2", 1.0));
        // Total: 2.0
        
        teams.add(strongTeam);
        teams.add(weakTeam);
        
        // When
        Boolean isBalanced = (Boolean) calculateBalance.invoke(null);
        
        // Then
        assertFalse(isBalanced, "Teams with very different strengths should not be balanced");
        // 2.0 < 0.7 * 10.0 (2.0 < 7.0) should return false for balance
    }

    @Test
    @DisplayName("Should assemble teams without losing players")
    void shouldAssembleTeamsWithoutLosingPlayers() throws Exception {
        // Access private methods using reflection
        Method initializePlayers = TeamMaker.class.getDeclaredMethod("initializePlayers");
        Method prepareTeams = TeamMaker.class.getDeclaredMethod("prepareTeams");
        Method assembleTeams = TeamMaker.class.getDeclaredMethod("assembleTeams");
        initializePlayers.setAccessible(true);
        prepareTeams.setAccessible(true);
        assembleTeams.setAccessible(true);
        
        Field teamsField = TeamMaker.class.getDeclaredField("teams");
        Field playersField = TeamMaker.class.getDeclaredField("players");
        teamsField.setAccessible(true);
        playersField.setAccessible(true);
        
        // When
        initializePlayers.invoke(null);
        int originalPlayerCount = ((List<?>) playersField.get(null)).size();
        prepareTeams.invoke(null);
        assembleTeams.invoke(null);
        
        // Then
        @SuppressWarnings("unchecked")
        List<Team> teams = (List<Team>) teamsField.get(null);
        
        // All players should be distributed to teams
        assertEquals(0, ((List<?>) playersField.get(null)).size(), "All players should be assigned to teams");
        
        // Count total players in all teams
        int totalPlayersInTeams = teams.stream().mapToInt(team -> {
            try {
                Field playersInTeamField = Team.class.getDeclaredField("players");
                playersInTeamField.setAccessible(true);
                @SuppressWarnings("unchecked")
                List<Player> playersInTeam = (List<Player>) playersInTeamField.get(team);
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
    @DisplayName("Should handle edge case with odd number of players")
    void shouldHandleEdgeCaseWithOddNumberOfPlayers() throws Exception {
        Field playersField = TeamMaker.class.getDeclaredField("players");
        playersField.setAccessible(true);
        
        Method prepareTeams = TeamMaker.class.getDeclaredMethod("prepareTeams");
        prepareTeams.setAccessible(true);
        
        Field teamsField = TeamMaker.class.getDeclaredField("teams");
        teamsField.setAccessible(true);
        
        // Create odd number of players
        @SuppressWarnings("unchecked")
        List<Player> players = (List<Player>) playersField.get(null);
        players.clear();
        players.add(new Player("Player 1", 3.0));
        players.add(new Player("Player 2", 3.0));
        players.add(new Player("Player 3", 3.0)); // Odd number: 3
        
        // When
        prepareTeams.invoke(null);
        
        // Then
        @SuppressWarnings("unchecked")
        List<Team> teams = (List<Team>) teamsField.get(null);
        
        // 3 players / 2 players per team = 1 team (integer division)
        assertEquals(1, teams.size());
    }

    @Test
    @DisplayName("Should print teams without throwing exceptions")
    void shouldPrintTeamsWithoutThrowingExceptions() throws Exception {
        // Access private methods using reflection
        Method initializePlayers = TeamMaker.class.getDeclaredMethod("initializePlayers");
        Method prepareTeams = TeamMaker.class.getDeclaredMethod("prepareTeams");
        Method assembleTeams = TeamMaker.class.getDeclaredMethod("assembleTeams");
        Method printTeams = TeamMaker.class.getDeclaredMethod("printTeams");
        
        initializePlayers.setAccessible(true);
        prepareTeams.setAccessible(true);
        assembleTeams.setAccessible(true);
        printTeams.setAccessible(true);
        
        // When
        initializePlayers.invoke(null);
        prepareTeams.invoke(null);
        assembleTeams.invoke(null);
        
        // This should not throw any exceptions
        assertDoesNotThrow(() -> {
            try {
                printTeams.invoke(null);
            } catch (Exception e) {
                if (e.getCause() instanceof RuntimeException) {
                    throw (RuntimeException) e.getCause();
                }
                throw new RuntimeException(e);
            }
        });
        
        // Check that some output was produced
        String output = outContent.toString();
        assertFalse(output.isEmpty(), "Print teams should produce some output");
        assertTrue(output.contains("Team"), "Output should contain team information");
        assertTrue(output.contains("-----"), "Output should contain separator lines");
    }
}