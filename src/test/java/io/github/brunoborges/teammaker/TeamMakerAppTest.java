package io.github.brunoborges.teammaker;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

@DisplayName("TeamMakerApp Tests")
class TeamMakerAppTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private TeamMakerApp app;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outContent));
        app = new TeamMakerApp();
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    @DisplayName("Should create app instance")
    void shouldCreateAppInstance() {
        // Given/When
        TeamMakerApp newApp = new TeamMakerApp();
        
        // Then
        assertNotNull(newApp);
    }

    @Test
    @DisplayName("Should run without exceptions")
    void shouldRunWithoutExceptions() {
        // When/Then
        assertDoesNotThrow(() -> app.run());
    }

    @Test
    @DisplayName("Should produce output when running")
    void shouldProduceOutputWhenRunning() {
        // When
        app.run();
        
        // Then
        String output = outContent.toString();
        assertFalse(output.isEmpty(), "App should produce output");
        assertTrue(output.contains("Team"), "Output should contain team information");
        assertTrue(output.contains("strength"), "Output should contain strength information");
        assertTrue(output.contains("-----"), "Output should contain separator lines");
    }

    @Test
    @DisplayName("Should display team names in output")
    void shouldDisplayTeamNamesInOutput() {
        // When
        app.run();
        
        // Then
        String output = outContent.toString();
        assertTrue(output.contains("Team A"), "Output should contain Team A");
        assertTrue(output.contains("Team B"), "Output should contain Team B");
    }

    @Test
    @DisplayName("Should display balance information")
    void shouldDisplayStrengthInformation() {
        // When
        app.run();
        
        // Then
        String output = outContent.toString();
        assertTrue(output.contains("strength = "), "Output should show strength values");
        assertTrue(output.contains("players = "), "Output should show players information");
    }

    @Test
    @DisplayName("Should display player information")
    void shouldDisplayPlayerInformation() {
        // When
        app.run();
        
        // Then
        String output = outContent.toString();
        // Check for some known default players
        assertTrue(output.contains("Bruno") || output.contains("Alex") || output.contains("Leo"),
                   "Output should contain player names");
    }

    @Test
    @DisplayName("Main method should run without exceptions")
    void mainMethodShouldRunWithoutExceptions() {
        // When/Then
        assertDoesNotThrow(() -> TeamMakerApp.main(new String[]{}));
        
        // Check output was produced
        String output = outContent.toString();
        assertFalse(output.isEmpty(), "Main method should produce output");
    }

    @Test
    @DisplayName("Main method should handle command line arguments")
    void mainMethodShouldHandleCommandLineArguments() {
        // When/Then - should not throw even with arguments
        assertDoesNotThrow(() -> TeamMakerApp.main(new String[]{"arg1", "arg2"}));
    }

    @Test
    @DisplayName("Should create multiple teams")
    void shouldCreateMultipleTeams() {
        // When
        app.run();
        
        // Then
        String output = outContent.toString();
        
        // Count occurrences of "Team " followed by a letter to see multiple teams
        long teamCount = output.lines()
                .filter(line -> line.matches("^Team [A-Z] \\[strength = .*"))
                .count();
        
        assertTrue(teamCount >= 2, "Should create multiple teams, found: " + teamCount);
    }
}