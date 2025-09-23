#!/bin/bash

# TeamMaker Distribution Creator
# This script creates a distribution package with the fat JAR and documentation

DIST_DIR="teammaker-dist"
VERSION="1.0-SNAPSHOT"

echo "=== Creating TeamMaker Distribution ==="

# Clean and build
echo "Building fat JAR..."
mvn clean package -DskipTests

# Create distribution directory
echo "Creating distribution directory..."
rm -rf $DIST_DIR
mkdir -p $DIST_DIR

# Copy the fat JAR
echo "Copying fat JAR..."
cp target/futebol-${VERSION}.jar $DIST_DIR/teammaker.jar

# Copy config files
echo "Copying configuration files..."
cp team-config.json $DIST_DIR/
cp custom-config.json $DIST_DIR/
cp test-6-players.json $DIST_DIR/

# Create a simple runner script
echo "Creating runner script..."
cat > $DIST_DIR/teammaker << 'EOF'
#!/bin/bash
# TeamMaker Runner Script
# Usage: ./teammaker [options]

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
exec java -jar "$SCRIPT_DIR/teammaker.jar" "$@"
EOF

chmod +x $DIST_DIR/teammaker

# Create README
echo "Creating README..."
cat > $DIST_DIR/README.md << 'EOF'
# TeamMaker Distribution

A Java application for generating balanced soccer teams.

## Quick Start

```bash
# Show help
./teammaker --help

# Run with default configuration
./teammaker

# Use hard-coded players
./teammaker --default

# Use custom config file
./teammaker my-config.json
```

## Requirements

- Java 21 or later

## Configuration Files

- `team-config.json` - Default configuration with 20 players and 10 teams
- `custom-config.json` - Alternative configuration with different players
- `test-6-players.json` - Simple configuration with 6 players and 2 teams

## Usage

The application supports various command-line options:

- `--help` or `-h`: Show help message
- `--version` or `-V`: Show version information  
- `--default` or `-d`: Use built-in hard-coded players
- `--resource <name>` or `-r <name>`: Use a specific resource configuration
- `--verbose` or `-v`: Enable verbose output
- `[config-file]`: Path to a JSON configuration file

## Configuration Format

JSON configuration files should follow this structure:

```json
{
  "players": [
    {"name": "Player1", "score": 4.0},
    {"name": "Player2", "score": 3.5}
  ],
  "teams": [
    {"name": "Team A"},
    {"name": "Team B"}
  ],
  "scoreScale": {
    "min": 1.0,
    "max": 5.0
  }
}
```

The number of players must be evenly divisible by the number of teams.
EOF

echo "=== Distribution created in $DIST_DIR/ ==="
echo "Contents:"
ls -la $DIST_DIR/
echo
echo "You can now zip this directory and distribute it:"
echo "zip -r teammaker-${VERSION}.zip $DIST_DIR/"