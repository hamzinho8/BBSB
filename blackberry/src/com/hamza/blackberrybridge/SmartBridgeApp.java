package com.hamza.blackberrybridge;

import net.rim.device.api.ui.UiApplication;

public class SmartBridgeApp extends UiApplication {
    private UIManager uiManager;
    private ConnectionManager connectionManager;
    private SettingsManager settingsManager;
    
    public static void main(String[] args) {
        SmartBridgeApp app = new SmartBridgeApp();
        app.enterEventDispatcher();
    }
    
    public SmartBridgeApp() {
        LogManager.log("App", "Starting BlackBerrySmartBridge...");
        
        settingsManager = new SettingsManager();
        uiManager = new UIManager(this);
        
        // Push the main screen to the display stack
        pushScreen(uiManager.getMainScreen());
        
        connectionManager = new ConnectionManager(uiManager, this);
        connectionManager.startServer();
    }
    
    public ConnectionManager getConnectionManager() {
        return connectionManager;
    }
    
    public SettingsManager getSettingsManager() {
        return settingsManager;
    }
    
    protected void onExit() {
        LogManager.log("App", "Shutting down...");
        if (connectionManager != null) {
            connectionManager.stopServer();
        }
    }
}
