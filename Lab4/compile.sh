#!/bin/bash
# Compilation script for Pomona Transit Management System

echo "Compiling Pomona Transit Management System..."

# Create bin directory if it doesn't exist
mkdir -p bin

# Compile all Java files
javac -d bin -cp "lib/*:." src/db/*.java src/dao/*.java src/gui/*.java src/Main.java

if [ $? -eq 0 ]; then
    echo "✓ Compilation successful!"
    echo "Run './run.sh' to start the application"
else
    echo "✗ Compilation failed!"
    exit 1
fi
