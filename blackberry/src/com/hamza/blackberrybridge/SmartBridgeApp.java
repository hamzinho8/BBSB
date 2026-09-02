package com.hamza.blackberrybridge;

import net.rim.device.api.ui.UiApplication;

public class SmartBridgeApp extends UiApplication {
    private UIManager uiManager;
    private ConnectionManager connectionManager;
    private SettingsManager settingsManager;
    private NotificationManager notificationManager;
    private CallManager callManager;
    private ContactManager contactManager;
    private MediaManager mediaManager;
    
    public static void main(String[] args) {
        SmartBridgeApp app = new SmartBridgeApp();
        app.enterEventDispatcher();
    }
    
    public SmartBridgeApp() {
        LogManager.log("App", "Starting BlackBerrySmartBridge...");
        
        settingsManager = new SettingsManager();
        uiManager = new UIManager(this);
        
        notificationManager = new NotificationManager(uiManager, this);
        callManager = new CallManager(uiManager, this);
        contactManager = new ContactManager(this);
        mediaManager = new MediaManager(this);
        
        pushScreen(uiManager.getMainScreen());
        
        connectionManager = new ConnectionManager(uiManager, this);
        connectionManager.startServer();
    }
    
    public ConnectionManager getConnectionManager() { return connectionManager; }
    public SettingsManager getSettingsManager() { return settingsManager; }
    public NotificationManager getNotificationManager() { return notificationManager; }
    public CallManager getCallManager() { return callManager; }
    public ContactManager getContactManager() { return contactManager; }
    public MediaManager getMediaManager() { return mediaManager; }
    public UIManager getUIManager() { return uiManager; }
    
    protected void onExit() {
        LogManager.log("App", "Shutting down...");
        if (connectionManager != null) {
            connectionManager.stopServer();
        }
        HardwareManager.stopAlerts();
    }
}
