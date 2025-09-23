package io.github.brunoborges.teammaker;

import java.util.List;

/**
 * Result object containing the generated teams and balance information.
 */
public class TeamMakerResult {
    private final List<Team> teams;
    private final boolean balanced;
    private final double minimumStrength;
    private final double maximumStrength;

    public TeamMakerResult(List<Team> teams, boolean balanced, double minimumStrength, double maximumStrength) {
        this.teams = teams;
        this.balanced = balanced;
        this.minimumStrength = minimumStrength;
        this.maximumStrength = maximumStrength;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public boolean isBalanced() {
        return balanced;
    }

    public double getMinimumStrength() {
        return minimumStrength;
    }

    public double getMaximumStrength() {
        return maximumStrength;
    }
}