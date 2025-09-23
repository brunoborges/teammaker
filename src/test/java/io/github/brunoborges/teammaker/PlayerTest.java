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
        assertEquals(name, player.getName());
        assertEquals(score, player.getStrength());
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
        assertEquals(name, player.getName());
        assertEquals(score, player.getStrength());
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
        assertEquals(name, player.getName());
        assertEquals(score, player.getStrength(), 0.001);
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
        assertEquals(name, player.getName());
        assertEquals(score, player.getStrength());
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
        assertEquals(name, player.getName());
        assertEquals(score, player.getStrength());
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
        assertEquals(name, player.getName());
        assertEquals(score, player.getStrength());
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
        assertNull(player.getName());
        assertEquals(score, player.getStrength());
    }

    @Test
    @DisplayName("Should return correct toString format")
    void shouldReturnCorrectToStringFormat() {
        // Given
        String name = "Test Player";
        double score = 4.0;
        Player player = new Player(name, score);
        
        // When
        String result = player.toString();
        
        // Then
        assertEquals("Test Player (4.0)", result);
    }

    @Test
    @DisplayName("Should return correct toString format with decimal score")
    void shouldReturnCorrectToStringFormatWithDecimalScore() {
        // Given
        String name = "Decimal Player";
        double score = 3.75;
        Player player = new Player(name, score);
        
        // When
        String result = player.toString();
        
        // Then
        assertEquals("Decimal Player (3.75)", result);
    }

    @Test
    @DisplayName("Should handle special characters in name")
    void shouldHandleSpecialCharactersInName() {
        // Given
        String name = "José María (Pedrinho)";
        double score = 4.0;
        
        // When
        Player player = new Player(name, score);
        
        // Then
        assertEquals(name, player.getName());
        assertEquals("José María (Pedrinho) (4.0)", player.toString());
    }
}