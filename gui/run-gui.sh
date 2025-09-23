#!/bin/bash

# TeamMaker GUI Launcher Script
# Builds and runs the JavaFX GUI application

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to print colored output
print_status() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Get the directory of this script
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(dirname "$SCRIPT_DIR")"

# Change to project root
cd "$PROJECT_ROOT"

print_status "🚀 Starting TeamMaker GUI..."

# Check if Maven is available
if ! command -v mvn &> /dev/null; then
    print_error "Maven is not installed or not in PATH"
    exit 1
fi

# Check if Java is available
if ! command -v java &> /dev/null; then
    print_error "Java is not installed or not in PATH"
    exit 1
fi

# Get Java version
JAVA_VERSION=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}' | awk -F'.' '{print $1}')
if [ "$JAVA_VERSION" -lt 11 ]; then
    print_error "Java 11+ is required. Current version: $JAVA_VERSION"
    exit 1
fi

print_success "✅ Java version: $JAVA_VERSION"

# Build the project
print_status "🔨 Building project..."
if mvn clean compile -q; then
    print_success "✅ Project built successfully"
else
    print_error "❌ Build failed"
    exit 1
fi

# Run the GUI application
print_status "🎨 Launching JavaFX GUI..."

# Check if we're on macOS and need special handling
if [[ "$OSTYPE" == "darwin"* ]]; then
    print_status "🍎 Detected macOS - applying JavaFX settings"
    JAVAFX_ARGS="-Djava.awt.headless=false -Xdock:name=TeamMaker"
fi

# Run the JavaFX application using Maven
if mvn -pl gui javafx:run -q $JAVAFX_ARGS 2>/dev/null; then
    print_success "👋 GUI application closed successfully"
else
    print_warning "⚠️  GUI application exited with warnings or was closed by user"
fi

print_status "🏁 TeamMaker GUI session ended"