package io.github.brunoborges.teammaker;

import java.util.List;

/**
 * Configuration class for team maker that can be loaded from JSON.
 * Contains players, team names, and scoring configuration.
 */
public class TeamMakerConfig {
    
    private List<Player> players;
    private List<String> teamNames;
    private ScoreScale scoreScale;
    
    public TeamMakerConfig() {
        // Default constructor for Jackson
    }
    
    public TeamMakerConfig(List<Player> players, List<String> teamNames, ScoreScale scoreScale) {
        this.players = players;
        this.teamNames = teamNames;
        this.scoreScale = scoreScale;
    }
    
    public List<Player> getPlayers() {
        return players;
    }
    
    public void setPlayers(List<Player> players) {
        this.players = players;
    }
    
    public List<String> getTeamNames() {
        return teamNames;
    }
    
    public void setTeamNames(List<String> teamNames) {
        this.teamNames = teamNames;
    }
    
    public ScoreScale getScoreScale() {
        return scoreScale;
    }
    
    public void setScoreScale(ScoreScale scoreScale) {
        this.scoreScale = scoreScale;
    }
    
    /**
     * Calculate players per team based on total players and number of teams.
     * 
     * @return number of players per team
     */
    public int calculatePlayersPerTeam() {
        if (players == null || teamNames == null || players.isEmpty() || teamNames.isEmpty()) {
            return 2; // default
        }
        return players.size() / teamNames.size();
    }
    
    /**
     * Inner class to represent the score scale configuration.
     */
    public static class ScoreScale {
        private double min;
        private double max;
        
        public ScoreScale() {
            // Default constructor for Jackson
        }
        
        public ScoreScale(double min, double max) {
            this.min = min;
            this.max = max;
        }
        
        public double getMin() {
            return min;
        }
        
        public void setMin(double min) {
            this.min = min;
        }
        
        public double getMax() {
            return max;
        }
        
        public void setMax(double max) {
            this.max = max;
        }
        
        /**
         * Validate if a score is within the allowed range.
         * 
         * @param score the score to validate
         * @return true if the score is within range
         */
        public boolean isValidScore(double score) {
            return score >= min && score <= max;
        }
    }
}