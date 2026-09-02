package com.hamza.blackberrybridge;

public class NotificationManager {
    private UIManager uiManager;
    
    public NotificationManager(UIManager uiManager) {
        this.uiManager = uiManager;
    }
    
    public void handleNotification(String id, String app, String sender, String text) {
        uiManager.showNotification(id, app, sender, text);
    }
}
