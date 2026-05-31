@echo off
dir /s /B src\*.java > sources.txt
javac -encoding UTF-8 --module-path "lib/javafx-sdk-17.0.12/lib" --add-modules javafx.controls -cp "lib/postgresql-42.7.5.jar;lib/atlantafx-base-1.0.0.jar" -d bin @sources.txt
xcopy /E /Y /I src\com\coffeeshop\resources bin\com\coffeeshop\resources
jar cfm CoffeeShop.jar Manifest.txt -C bin .
echo Build completed!

