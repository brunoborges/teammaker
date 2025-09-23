package io.github.brunoborges.teammaker;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Player Tests")
class PlayerTest {

    @Test
    @DisplayName("Should create player with name and score")
    void shouldCreatePlayerWithNameAndScore() {
        // Given
        String name = "John Doe";
        double score = 4.5;
        
        // When
        Player player = new Player(name, score);
        
        // Then
        assertEquals(name, player.name());
        assertEquals(score, player.score());
    }

    @Test
    @DisplayName("Should handle integer score values")
    void shouldHandleIntegerScoreValues() {
        // Given
        String name = "Jane Smith";
        double score = 3.0;
        
        // When
        Player player = new Player(name, score);
        
        // Then
        assertEquals(name, player.name());
        assertEquals(score, player.score());
    }

    @Test
    @DisplayName("Should handle decimal score values")
    void shouldHandleDecimalScoreValues() {
        // Given
        String name = "Mike Johnson";
        double score = 2.75;
        
        // When
        Player player = new Player(name, score);
        
        // Then
        assertEquals(name, player.name());
        assertEquals(score, player.score(), 0.001);
    }

    @Test
    @DisplayName("Should handle zero score")
    void shouldHandleZeroScore() {
        // Given
        String name = "Zero Player";
        double score = 0.0;
        
        // When
        Player player = new Player(name, score);
        
        // Then
        assertEquals(name, player.name());
        assertEquals(score, player.score());
    }

    @Test
    @DisplayName("Should handle negative score")
    void shouldHandleNegativeScore() {
        // Given
        String name = "Negative Player";
        double score = -1.5;
        
        // When
        Player player = new Player(name, score);
        
        // Then
        assertEquals(name, player.name());
        assertEquals(score, player.score());
    }

    @Test
    @DisplayName("Should handle empty name")
    void shouldHandleEmptyName() {
        // Given
        String name = "";
        double score = 3.0;
        
        // When
        Player player = new Player(name, score);
        
        // Then
        assertEquals(name, player.name());
        assertEquals(score, player.score());
    }

    @Test
    @DisplayName("Should handle null name")
    void shouldHandleNullName() {
        // Given
        String name = null;
        double score = 3.0;
        
        // When
        Player player = new Player(name, score);
        
        // Then
        assertNull(player.name());
        assertEquals(score, player.score());
    }

}