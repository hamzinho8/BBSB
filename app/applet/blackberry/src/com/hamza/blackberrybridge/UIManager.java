package com.hamza.blackberrybridge;

import net.rim.device.api.system.Application;
import net.rim.device.api.ui.Screen;

public class UIManager {
    private SmartBridgeScreen mainScreen;
    private SmartBridgeApp app;
    private NotificationListScreen notifListScreen;
    
    public UIManager(SmartBridgeApp app) {
        this.app = app;
        mainScreen = new SmartBridgeScreen(app);
    }
    
    public SmartBridgeScreen getMainScreen() {
        return mainScreen;
    }
    
    public void pushScreen(final Screen screen) {
        Application.getApplication().invokeLater(new Runnable() {
            public void run() {
                Application.getApplication().pushScreen(screen);
            }
        });
    }
    
    public void updateConnectionStatus(final String status) {
        Application.getApplication().invokeLater(new Runnable() {
            public void run() {
                mainScreen.updateConnectionStatus(status);
            }
        });
    }

    public void updateBattery(final String level) {
        Application.getApplication().invokeLater(new Runnable() {
            public void run() {
                mainScreen.updateBattery(level);
            }
        });
    }
    
    public void notifyNewNotification(final Notification n) {
        Application.getApplication().invokeLater(new Runnable() {
            public void run() {
                mainScreen.addLog("[NOTIF] " + n.app + ": " + n.sender);
                mainScreen.updateNotificationCount(app.getNotificationManager().getNotifications().size());
                if (notifListScreen != null && notifListScreen.isDisplayed()) {
                    notifListScreen.refreshList();
                }
            }
        });
    }
    
    public void openNotificationList() {
        if (notifListScreen == null) {
            notifListScreen = new NotificationListScreen(app.getNotificationManager(), app);
        } else {
            notifListScreen.refreshList();
        }
        pushScreen(notifListScreen);
    }
    
    public void openSettings() {
        pushScreen(new SettingsScreen(app.getSettingsManager()));
    }
    
    public void openContacts() {
        pushScreen(new ContactListScreen(app.getContactManager()));
    }
    
    public void openMedia() {
        pushScreen(new MediaScreen(app.getMediaManager()));
    }
}
