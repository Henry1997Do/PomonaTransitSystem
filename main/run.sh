#!/bin/bash

# Set paths
JAVAFX_PATH="javafx-sdk-17.0.2/lib"
MYSQL_JAR="mysql-connector-j-8.3.0.jar"
BIN_DIR="bin"

# Run the application
echo "Running Pomona Transit System..."
java --module-path $JAVAFX_PATH \
     --add-modules javafx.controls,javafx.fxml \
     -Dprism.order=sw \
     -cp $BIN_DIR:$MYSQL_JAR \
     pts.Main
