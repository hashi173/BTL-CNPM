#!/bin/bash
# Run the application with JavaFX SDK module path and classpaths
java --module-path "lib/javafx-sdk-17.0.12/lib" --add-modules javafx.controls -cp "bin:lib/postgresql-42.7.5.jar:lib/atlantafx-base-1.0.0.jar" com.coffeeshop.Main
