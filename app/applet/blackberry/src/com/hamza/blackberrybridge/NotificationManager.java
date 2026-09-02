package com.hamza.blackberrybridge;

import java.util.Vector;

public class NotificationManager {
    private UIManager uiManager;
    private SmartBridgeApp app;
    private Vector notifications;
    
    public NotificationManager(UIManager uiManager, SmartBridgeApp app) {
        this.uiManager = uiManager;
        this.app = app;
        this.notifications = new Vector();
    }
    
    public void handleNotification(String id, String appName, String sender, String text) {
        Notification n = new Notification(id, appName, sender, text);
        
        // Keep max 20 notifications to save memory
        if (notifications.size() >= 20) {
            notifications.removeElementAt(0);
        }
        notifications.addElement(n);
        
        HardwareManager.triggerNotificationAlert();
        uiManager.notifyNewNotification(n);
    }
    
    public Vector getNotifications() {
        return notifications;
    }
    
    public void openNotification(Notification n) {
        HardwareManager.stopAlerts();
        uiManager.pushScreen(new NotificationDetailScreen(this, n, app));
    }
    
    public void replyToNotification(String id, String text) {
        app.getConnectionManager().sendData("REPLY|" + id + "|" + text + "\n");
    }
}
