package com.hamza.blackberrybridge;

public class CallManager {
    private UIManager uiManager;
    private SmartBridgeApp app;
    private CallScreen currentCallScreen;
    
    public CallManager(UIManager uiManager, SmartBridgeApp app) {
        this.uiManager = uiManager;
        this.app = app;
    }
    
    public void handleIncomingCall(String id, String name, String number) {
        HardwareManager.triggerCallAlert();
        currentCallScreen = new CallScreen(this, id, name, number);
        uiManager.pushScreen(currentCallScreen);
    }
    
    public void handleCallActive(String id) {
        if (currentCallScreen != null) {
            currentCallScreen.setStatus("Active");
            HardwareManager.stopAlerts();
        }
    }
    
    public void handleCallEnd(String id) {
        HardwareManager.stopAlerts();
        if (currentCallScreen != null) {
            currentCallScreen.close();
            currentCallScreen = null;
        }
        uiManager.getMainScreen().addLog("[CALL] Call ended");
    }
    
    public void handleCallMissed(String id, String name, String number) {
        HardwareManager.stopAlerts();
        if (currentCallScreen != null) {
            currentCallScreen.close();
            currentCallScreen = null;
        }
        LogManager.log("CALL", "Missed call from " + name);
        uiManager.getMainScreen().addLog("[CALL] Missed: " + name);
    }
    
    public void answerCall(String id) {
        app.getConnectionManager().sendData("CALL_ANSWER|" + id + "\n");
    }
    
    public void rejectCall(String id) {
        app.getConnectionManager().sendData("CALL_REJECT|" + id + "\n");
    }
    
    public void initiateCall(String number) {
        app.getConnectionManager().sendData("CALL|" + number + "\n");
        HardwareManager.triggerNotificationAlert(); // small vibrate to confirm
        uiManager.getMainScreen().addLog("[CALL] Outgoing: " + number);
    }
}
