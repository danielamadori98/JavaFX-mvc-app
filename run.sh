#!/bin/bash

# Variables
JAVA_HOME="JavaEnvironment/zulu23.32.11-ca-fx-jdk23.0.2-macosx_aarch64/zulu-23.jdk/Contents/Home"
JAVAFX_PATH="$JAVA_HOME/lib"
SRC_PATH="src"
OUT_PATH="bin"
LIB_PATH="lib"

# Create output directory if it doesn't exist
mkdir -p $OUT_PATH

# Compile the application
echo "Compiling the application..."
"$JAVA_HOME/bin/javac" -cp "$LIB_PATH/*:$SRC_PATH" -d $OUT_PATH \
    $SRC_PATH/com/dashapp/Main.java \
    $SRC_PATH/com/dashapp/config/AppConfig.java \
    $SRC_PATH/com/dashapp/model/User.java \
    $SRC_PATH/com/dashapp/model/UserRepository.java \
    $SRC_PATH/com/dashapp/model/DatabaseManager.java \
    $SRC_PATH/com/dashapp/view/ViewNavigator.java \
    $SRC_PATH/com/dashapp/view/components/NavBar.java \
    $SRC_PATH/com/dashapp/controller/MainController.java \
    $SRC_PATH/com/dashapp/controller/HomeController.java \
    $SRC_PATH/com/dashapp/controller/LoginController.java \
    $SRC_PATH/com/dashapp/controller/RegisterController.java \
    $SRC_PATH/com/dashapp/controller/DashboardController.java \
    $SRC_PATH/com/dashapp/controller/ProfileController.java \
    $SRC_PATH/com/dashapp/controller/StatsController.java

# Copy resources to output directory
echo "Copying resources..."
cp -r $SRC_PATH/resources $OUT_PATH/

# Run the application
echo "Running the application..."
"$JAVA_HOME/bin/java" --add-modules javafx.controls,javafx.fxml -cp "$OUT_PATH:$LIB_PATH/*" com.dashapp.Main