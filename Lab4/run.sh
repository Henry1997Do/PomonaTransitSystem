#!/bin/bash
# Run script for Pomona Transit Management System

echo "Starting Pomona Transit Management System..."

# Run the application
java -cp "bin:lib/*" Main

if [ $? -ne 0 ]; then
    echo "✗ Application failed to start!"
    exit 1
fi
