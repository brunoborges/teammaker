#!/bin/bash

# TeamMaker Demo Script
# Demonstrates both CLI and GUI functionality

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
PURPLE='\033[0;35m'
CYAN='\033[0;36m'
NC='\033[0m' # No Color

# Function to print colored output
print_header() {
    echo -e "${PURPLE}═══════════════════════════════════════════════════════════════════${NC}"
    echo -e "${PURPLE}                    ⚽ $1 ⚽                    ${NC}"
    echo -e "${PURPLE}═══════════════════════════════════════════════════════════════════${NC}"
    echo
}

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
cd "$SCRIPT_DIR"

print_header "TEAMMAKER COMPREHENSIVE DEMO"

print_status "🏗️  Building project..."
if mvn clean compile -q; then
    print_success "✅ Project built successfully"
else
    print_error "❌ Build failed"
    exit 1
fi

# Demonstrate CLI functionality
print_header "COMMAND LINE INTERFACE DEMO"

print_status "🎲 Running CLI with default configuration..."
echo
mvn -pl cli exec:java -q -Dexec.args=""

echo
print_status "📋 Creating custom configuration..."
cat > demo-config.json << 'EOF'
{
  "players": [
    {"name": "Cristiano", "score": 5.0},
    {"name": "Messi", "score": 5.0},
    {"name": "Neymar", "score": 4.5},
    {"name": "Mbappé", "score": 4.5},
    {"name": "Haaland", "score": 4.0},
    {"name": "Benzema", "score": 4.0},
    {"name": "Salah", "score": 3.5},
    {"name": "Mané", "score": 3.5}
  ],
  "teamNames": ["Real Madrid", "Barcelona", "PSG", "Man City"],
  "scoreScale": {"min": 1.0, "max": 5.0}
}
EOF

print_success "✅ Custom configuration created: demo-config.json"

echo
print_status "🌟 Running CLI with custom star players..."
echo
mvn -pl cli exec:java -q -Dexec.args="-c demo-config.json"

# Demonstrate GUI functionality
print_header "JAVAFX GUI DEMO"

print_status "🎨 Launching JavaFX GUI application..."
print_warning "ℹ️  The GUI will open in a new window"
print_status "⚡ Features to try in the GUI:"
echo "   • Add/edit players and teams"
echo "   • Adjust score sliders"
echo "   • Generate teams with 🎲 Generate Teams button"
echo "   • Save/load configurations"
echo "   • View formatted results in Results tab"

echo
read -p "Press Enter to launch GUI (or Ctrl+C to skip)..."

# Try to launch GUI
if command -v osascript &> /dev/null && [[ "$OSTYPE" == "darwin"* ]]; then
    # macOS - bring Terminal to front after GUI
    (mvn -pl gui javafx:run -q; osascript -e 'tell application "Terminal" to activate') &
    print_status "🍎 GUI launched on macOS"
else
    mvn -pl gui javafx:run -q &
    print_status "🐧 GUI launched"
fi

# Wait a moment for GUI to potentially start
sleep 2

print_header "NATIVE IMAGE DEMO"

print_status "🔥 Building GraalVM Native Image..."
print_warning "ℹ️  This process may take 1-2 minutes..."

if ./build-native.sh; then
    print_success "✅ Native image built successfully!"
    
    print_status "⚡ Testing native binary startup speed..."
    echo
    time ./target/teammaker 
    
    print_success "🚀 Native image demo completed!"
    print_status "📊 Binary info:"
    if [[ -f "./target/teammaker" ]]; then
        ls -lh ./target/teammaker
    fi
else
    print_error "❌ Native image build failed (GraalVM may not be available)"
    print_warning "ℹ️  You can install GraalVM using: sdk install java 21.0.4-graal"
fi

# Project structure demo
print_header "PROJECT STRUCTURE OVERVIEW"

print_status "📁 Multi-module Maven project structure:"
tree -I 'target|.git|*.class' -L 3 || find . -name "target" -prune -o -name ".git" -prune -o -type d -print | head -20

print_header "DEMO SUMMARY"

print_success "✅ CLI Demo: Command-line interface with beautiful formatted output"
print_success "✅ GUI Demo: Modern JavaFX interface with interactive team management"
print_success "✅ Native Image: Ultra-fast startup with GraalVM compilation"

print_status "🎯 Key highlights:"
echo "   • 🏗️  Modular architecture (core, cli, gui)"
echo "   • 🧪 Comprehensive testing with 80%+ coverage"
echo "   • ⚡ GraalVM native image support"
echo "   • 🎨 Beautiful UI with JavaFX and CSS styling"
echo "   • 📋 JSON configuration management"
echo "   • 🔧 Professional CLI with Picocli"

echo
print_status "🔍 Next steps:"
echo "   • Explore the code in your favorite IDE"
echo "   • Run ./run-gui.sh for quick GUI access"
echo "   • Try mvn test for full test suite"
echo "   • Check README.md for comprehensive documentation"

# Cleanup
rm -f demo-config.json

print_header "THANK YOU FOR TRYING TEAMMAKER"
print_status "⚽ Made with passion for beautiful, functional software! ⚽"