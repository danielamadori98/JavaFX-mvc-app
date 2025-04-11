package com.dashapp.config;

import java.io.File;

public class AppConfig {
    // Application settings
    public static final String APP_TITLE = "Dash App - JavaFX Version";
    
    // Data storage settings
    public static final String DATA_DIR = "src/resources/data";
    public static final String DATABASE_PATH = DATA_DIR + "/dashapp.db";
    
    // Create data directory if it doesn't exist
    static {
        File dataDir = new File(DATA_DIR);
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }
    }
}