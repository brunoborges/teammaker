#!/bin/bash

# TeamMaker GUI Application Runner
# This script runs the JavaFX GUI version of TeamMaker

cd "$(dirname "$0")"

echo "🚀 Starting TeamMaker GUI..."

# Build the project if needed
mvn -q -pl gui compile 2>/dev/null

# Run the GUI application using JavaFX plugin (includes proper module path setup)
mvn -q -pl gui javafx:run

echo "✅ TeamMaker GUI application closed."