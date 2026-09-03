package com.hamza.blackberrybridge;

import net.rim.device.api.ui.UiApplication;

public class SmartBridgeApp extends UiApplication {
    private UIManager uiManager;
    private ConnectionManager connectionManager;
    private CallManager callManager;
    private NotificationManager notificationManager;
    private ContactManager contactManager;
    private MediaManager mediaManager;
    private SettingsManager settingsManager;
    private AudioManager audioManager;

    public static void main(String[] args) {
        SmartBridgeApp app = new SmartBridgeApp();
        app.enterEventDispatcher();
    }

    public SmartBridgeApp() {
        LogManager.init();
        settingsManager = new SettingsManager();
        
        LogManager.setDebugEnabled(settingsManager.isDebugMode());
        LogManager.info("APP", "Starting SmartBridge");
        
        uiManager = new UIManager(this);
        audioManager = new AudioManager(this);
        connectionManager = new ConnectionManager(this);
        callManager = new CallManager(uiManager, this);
        notificationManager = new NotificationManager(uiManager, this);
        contactManager = new ContactManager(uiManager);
        mediaManager = new MediaManager(this);
        
        pushScreen(uiManager.getMainScreen());
        
        // Auto-connect if enabled
        if (settingsManager.isAutoReconnect()) {
            connectionManager.startServer();
        }
    }

    public UIManager getUIManager() { return uiManager; }
    public ConnectionManager getConnectionManager() { return connectionManager; }
    public CallManager getCallManager() { return callManager; }
    public NotificationManager getNotificationManager() { return notificationManager; }
    public ContactManager getContactManager() { return contactManager; }
    public MediaManager getMediaManager() { return mediaManager; }
    public SettingsManager getSettingsManager() { return settingsManager; }
    public AudioManager getAudioManager() { return audioManager; }
    
    protected void onExit() {
        LogManager.info("APP", "Exiting");
        if (audioManager != null) audioManager.stopCallRingtone();
        connectionManager.stopServer();
        super.onExit();
    }
}
