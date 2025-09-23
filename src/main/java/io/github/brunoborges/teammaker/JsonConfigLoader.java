package io.github.brunoborges.teammaker;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Utility class for loading TeamMaker configuration from JSON files.
 */
public class JsonConfigLoader {
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * Load configuration from a JSON file.
     * 
     * @param filePath path to the JSON configuration file
     * @return TeamMakerConfig loaded from the file
     * @throws IOException if the file cannot be read or parsed
     */
    public static TeamMakerConfig loadFromFile(String filePath) throws IOException {
        return loadFromFile(Path.of(filePath));
    }
    
    /**
     * Load configuration from a JSON file.
     * 
     * @param filePath path to the JSON configuration file
     * @return TeamMakerConfig loaded from the file
     * @throws IOException if the file cannot be read or parsed
     */
    public static TeamMakerConfig loadFromFile(Path filePath) throws IOException {
        try (InputStream inputStream = Files.newInputStream(filePath)) {
            return loadFromInputStream(inputStream);
        }
    }
    
    /**
     * Load configuration from a classpath resource.
     * 
     * @param resourceName name of the resource file
     * @return TeamMakerConfig loaded from the resource
     * @throws IOException if the resource cannot be read or parsed
     */
    public static TeamMakerConfig loadFromResource(String resourceName) throws IOException {
        try (InputStream inputStream = JsonConfigLoader.class.getClassLoader().getResourceAsStream(resourceName)) {
            if (inputStream == null) {
                throw new IOException("Resource not found: " + resourceName);
            }
            return loadFromInputStream(inputStream);
        }
    }
    
    /**
     * Load configuration from an InputStream.
     * 
     * @param inputStream the input stream containing JSON data
     * @return TeamMakerConfig loaded from the stream
     * @throws IOException if the stream cannot be read or parsed
     */
    public static TeamMakerConfig loadFromInputStream(InputStream inputStream) throws IOException {
        TeamMakerConfig config = objectMapper.readValue(inputStream, TeamMakerConfig.class);
        validateConfig(config);
        return config;
    }
    
    /**
     * Save configuration to a JSON file.
     * 
     * @param config the configuration to save
     * @param filePath path where to save the JSON file
     * @throws IOException if the file cannot be written
     */
    public static void saveToFile(TeamMakerConfig config, String filePath) throws IOException {
        saveToFile(config, Path.of(filePath));
    }
    
    /**
     * Save configuration to a JSON file.
     * 
     * @param config the configuration to save
     * @param filePath path where to save the JSON file
     * @throws IOException if the file cannot be written
     */
    public static void saveToFile(TeamMakerConfig config, Path filePath) throws IOException {
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(filePath.toFile(), config);
    }
    
    /**
     * Validate the loaded configuration.
     * 
     * @param config the configuration to validate
     * @throws IllegalArgumentException if the configuration is invalid
     */
    private static void validateConfig(TeamMakerConfig config) throws IllegalArgumentException {
        if (config.getPlayers() == null || config.getPlayers().isEmpty()) {
            throw new IllegalArgumentException("Configuration must contain at least one player");
        }
        
        if (config.getTeamNames() == null || config.getTeamNames().isEmpty()) {
            throw new IllegalArgumentException("Configuration must contain at least one team name");
        }
        
        if (config.getScoreScale() == null) {
            throw new IllegalArgumentException("Configuration must contain score scale information");
        }
        
        // Validate that all player scores are within the defined scale
        List<Player> invalidPlayers = config.getPlayers().stream()
                .filter(player -> !config.getScoreScale().isValidScore(player.score()))
                .toList();
        
        if (!invalidPlayers.isEmpty()) {
            throw new IllegalArgumentException("The following players have scores outside the valid range: " + invalidPlayers);
        }
        
        // Validate that we can form complete teams
        int playersPerTeam = config.calculatePlayersPerTeam();
        if (playersPerTeam == 0) {
            throw new IllegalArgumentException("Not enough players to form teams. Players: " + 
                    config.getPlayers().size() + ", Teams: " + config.getTeamNames().size());
        }
        
        int totalPlayersInCompleteTeams = config.getTeamNames().size() * playersPerTeam;
        if (totalPlayersInCompleteTeams > config.getPlayers().size()) {
            throw new IllegalArgumentException("Not enough players to fill all teams completely. " +
                    "Players: " + config.getPlayers().size() + ", Required: " + totalPlayersInCompleteTeams);
        }
    }
}