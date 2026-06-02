#!/bin/bash
# Create bin directory
mkdir -p bin

# Find all Java source files
find src -name "*.java" > sources.txt

# Compile using JavaFX SDK and other dependencies
javac -encoding UTF-8 --module-path "lib/javafx-sdk-17.0.12/lib" --add-modules javafx.controls -cp "lib/postgresql-42.7.5.jar:lib/atlantafx-base-1.0.0.jar:lib/junit-4.13.2.jar:lib/hamcrest-core-1.3.jar" -d bin @sources.txt

# Copy resources to bin directory
mkdir -p bin/com/coffeeshop/resources
cp -R src/com/coffeeshop/resources/* bin/com/coffeeshop/resources/ 2>/dev/null || cp -R src/com/coffeeshop/resources bin/com/coffeeshop/

# Package CoffeeShop.jar
jar cfm CoffeeShop.jar Manifest.txt -C bin .

echo "Build completed!"

