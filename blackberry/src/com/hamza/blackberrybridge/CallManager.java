package com.hamza.blackberrybridge;

public class CallManager {
    private UIManager uiManager;
    
    public CallManager(UIManager uiManager) {
        this.uiManager = uiManager;
    }
    
    public void handleIncomingCall(String id, String name, String number) {
        uiManager.showIncomingCall(id, name, number);
    }
}
