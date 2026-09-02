package com.hamza.blackberrybridge;

import net.rim.device.api.system.Application;
import net.rim.device.api.ui.component.Dialog;

public class UIManager {
    private SmartBridgeScreen mainScreen;
    private SmartBridgeApp app;
    
    public UIManager(SmartBridgeApp app) {
        this.app = app;
        mainScreen = new SmartBridgeScreen();
    }
    
    public SmartBridgeScreen getMainScreen() {
        return mainScreen;
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
    
    public void showNotification(final String id, final String appName, final String sender, final String text) {
        Application.getApplication().invokeLater(new Runnable() {
            public void run() {
                mainScreen.addLog("[NOTIF] " + appName + " - " + sender + ": " + text);
                
                int result = Dialog.ask(Dialog.D_OK_CANCEL, "New from " + appName + "\n" + sender + "\n" + text + "\nReply?");
                if (result == Dialog.OK) {
                    // Simple reply implementation stub
                    app.getConnectionManager().sendData("REPLY|" + id + "|OK\n");
                }
            }
        });
    }
    
    public void showIncomingCall(final String id, final String name, final String number) {
        Application.getApplication().invokeLater(new Runnable() {
            public void run() {
                mainScreen.addLog("[CALL] Incoming from " + name + " (" + number + ")");
                int result = Dialog.ask(Dialog.D_YES_NO, "INCOMING CALL\n" + name + "\n" + number + "\nAnswer?");
                
                if (result == Dialog.YES) {
                    app.getConnectionManager().sendData("CALL_ANSWER|" + id + "\n");
                } else {
                    app.getConnectionManager().sendData("CALL_REJECT|" + id + "\n");
                }
            }
        });
    }
}
