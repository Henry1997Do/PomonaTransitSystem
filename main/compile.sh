#!/bin/bash

# Set paths
JAVAFX_PATH="javafx-sdk-17.0.2/lib"
MYSQL_JAR="mysql-connector-j-8.3.0.jar"
SRC_DIR="src"
BIN_DIR="bin"

# Create bin directory if it doesn't exist
mkdir -p $BIN_DIR

# Compile the project
echo "Compiling Java files..."
javac --module-path $JAVAFX_PATH \
      --add-modules javafx.controls,javafx.fxml \
      -cp $MYSQL_JAR \
      -d $BIN_DIR \
      $(find $SRC_DIR -name "*.java")

if [ $? -eq 0 ]; then
    echo "Compilation successful!"
    
    # Copy resources (FXML files, images, etc.)
    echo "Copying resources..."
    cp -r $SRC_DIR/pts/*.fxml $BIN_DIR/pts/ 2>/dev/null || true
    cp -r $SRC_DIR/pts/*.png $BIN_DIR/pts/ 2>/dev/null || true
    cp -r $SRC_DIR/pts/q1/*.fxml $BIN_DIR/pts/q1/ 2>/dev/null || true
    cp -r $SRC_DIR/pts/q2/*.fxml $BIN_DIR/pts/q2/ 2>/dev/null || true
    cp -r $SRC_DIR/pts/q3/*.fxml $BIN_DIR/pts/q3/ 2>/dev/null || true
    cp -r $SRC_DIR/pts/q4/*.fxml $BIN_DIR/pts/q4/ 2>/dev/null || true
    cp -r $SRC_DIR/pts/q5/*.fxml $BIN_DIR/pts/q5/ 2>/dev/null || true
    cp -r $SRC_DIR/pts/q6/*.fxml $BIN_DIR/pts/q6/ 2>/dev/null || true
    cp -r $SRC_DIR/pts/q7/*.fxml $BIN_DIR/pts/q7/ 2>/dev/null || true
    cp -r $SRC_DIR/pts/q8/*.fxml $BIN_DIR/pts/q8/ 2>/dev/null || true
    echo "Resources copied!"
else
    echo "Compilation failed!"
    exit 1
fi
