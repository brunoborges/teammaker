#!/bin/bash

echo "🚀 TeamMaker Native Image Builder"
echo "=================================="

# Function to check and setup SDKMAN environment
setup_sdkman() {
    # Check if SDKMAN is installed
    if [ -s "$HOME/.sdkman/bin/sdkman-init.sh" ]; then
        echo "📦 SDKMAN found! Initializing..."
        source "$HOME/.sdkman/bin/sdkman-init.sh"
        
        # Check if GraalVM 21.0.4-graal is installed
        if sdk list java | grep -q "21.0.4-graal"; then
            echo "✅ GraalVM 21.0.4-graal found in SDKMAN!"
            echo "🔄 Switching to GraalVM..."
            sdk use java 21.0.4-graal
            return 0
        else
            echo "⚠️  GraalVM 21.0.4-graal not found in SDKMAN."
            echo "📥 Installing GraalVM 21.0.4-graal..."
            sdk install java 21.0.4-graal
            if [ $? -eq 0 ]; then
                echo "✅ GraalVM installed successfully!"
                sdk use java 21.0.4-graal
                return 0
            else
                echo "❌ Failed to install GraalVM via SDKMAN."
                return 1
            fi
        fi
    else
        echo "📦 SDKMAN not found at $HOME/.sdkman/"
        return 1
    fi
}

# Try to setup SDKMAN and GraalVM automatically
echo "🔍 Checking for SDKMAN and GraalVM..."
if setup_sdkman; then
    echo "🎯 Using GraalVM via SDKMAN!"
    java -version
else
    echo "⚠️  SDKMAN setup failed or not available."
fi

# Check if native-image is available
if ! command -v native-image &> /dev/null; then
    echo ""
    echo "❌ GraalVM native-image not found!"
    echo ""
    echo "To install GraalVM and native-image:"
    echo ""
    echo "1. Install GraalVM using SDKMAN (recommended):"
    echo "   curl -s \"https://get.sdkman.io\" | bash"
    echo "   source ~/.sdkman/bin/sdkman-init.sh"
    echo "   sdk install java 21.0.4-graal"
    echo "   sdk use java 21.0.4-graal"
    echo ""
    echo "2. Or download from: https://www.graalvm.org/downloads/"
    echo ""
    echo "3. Then run this script again!"
    exit 1
fi

echo "✅ GraalVM native-image found!"
native-image --version

echo ""
echo "🔧 Building native image..."

# Clean and compile first
mvn clean compile -q

# Build native image using the profile
mvn -Pnative package -DskipTests

if [ $? -eq 0 ]; then
    echo ""
    echo "🎉 Native image built successfully!"
    echo ""
    echo "📊 Binary size:"
    ls -lh target/teammaker
    echo ""
    echo "🚀 Testing native binary:"
    echo ""
    ./target/teammaker --default
else
    echo ""
    echo "❌ Native build failed. Check the output above for errors."
    exit 1
fi