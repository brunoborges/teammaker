#!/bin/bash

echo "=== TeamMaker CLI Examples (Fat JAR) ==="
echo

echo "First, build the fat JAR:"
echo "mvn clean package -DskipTests"
echo

echo "Now you can run the application with just 'java -jar':"
echo

echo "1. Show help:"
echo "java -jar target/futebol-1.0-SNAPSHOT.jar --help"
echo

echo "2. Show version:"
echo "java -jar target/futebol-1.0-SNAPSHOT.jar --version"
echo

echo "3. Run with default resource configuration (team-config.json):"
echo "java -jar target/futebol-1.0-SNAPSHOT.jar"
echo

echo "4. Run with default hard-coded players:"
echo "java -jar target/futebol-1.0-SNAPSHOT.jar --default"
echo

echo "5. Run with a specific config file:"
echo "java -jar target/futebol-1.0-SNAPSHOT.jar test-6-players.json"
echo

echo "6. Run with a different resource file:"
echo "java -jar target/futebol-1.0-SNAPSHOT.jar --resource custom-config.json"
echo

echo "7. Run with verbose output:"
echo "java -jar target/futebol-1.0-SNAPSHOT.jar --verbose"
echo

echo "=== JAR File Information ==="
echo "Original JAR (without dependencies): $(ls -lh target/original-futebol-1.0-SNAPSHOT.jar 2>/dev/null | awk '{print $5}')"
echo "Fat JAR (with all dependencies): $(ls -lh target/futebol-1.0-SNAPSHOT.jar | awk '{print $5}')"
echo

echo "=== Running example #3 (default configuration) ==="
java -jar target/futebol-1.0-SNAPSHOT.jar