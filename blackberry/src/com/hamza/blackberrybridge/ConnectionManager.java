package com.hamza.blackberrybridge;

public class ConnectionManager {
    private BluetoothServer btServer;
    private ProtocolManager protocolManager;
    private UIManager uiManager;
    private SmartBridgeApp app;
    
    public ConnectionManager(UIManager uiManager, SmartBridgeApp app) {
        this.uiManager = uiManager;
        this.app = app;
        this.protocolManager = new ProtocolManager(this, uiManager);
    }
    
    public void startServer() {
        uiManager.updateConnectionStatus("CONNECTING");
        if (btServer != null) {
            btServer.stopServer();
        }
        btServer = new BluetoothServer(this);
        btServer.start(); // Starts the dedicated Bluetooth Thread
    }
    
    public void stopServer() {
        if (btServer != null) {
            btServer.stopServer();
            btServer = null;
        }
    }
    
    public void onConnected() {
        LogManager.log("ConnMgr", "Android connected");
        uiManager.updateConnectionStatus("CONNECTED");
    }
    
    public void onDisconnected() {
        LogManager.log("ConnMgr", "Connection lost");
        uiManager.updateConnectionStatus("DISCONNECTED");
        
        // Wait a moment before restarting the server to prevent CPU spin loops
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
        }
        
        startServer();
    }
    
    public void onDataReceived(String data) {
        protocolManager.processMessage(data);
    }
    
    public void sendData(String data) {
        if (btServer != null && btServer.isConnected()) {
            btServer.send(data);
        } else {
            LogManager.error("ConnMgr", "Cannot send, disconnected");
        }
    }
}
