package com.hamza.blackberrybridge;

import java.util.Timer;
import java.util.TimerTask;

public class ConnectionManager {
    private BluetoothServer btServer;
    private ProtocolManager protocolManager;
    private UIManager uiManager;
    private SmartBridgeApp app;
    private long lastDataTime = 0;
    private Timer watchdogTimer;
    
    public ConnectionManager(UIManager uiManager, SmartBridgeApp app) {
        this.uiManager = uiManager;
        this.app = app;
        this.protocolManager = new ProtocolManager(this, app);
    }
    
    public void startServer() {
        uiManager.updateConnectionStatus("CONNECTING");
        if (btServer != null) {
            btServer.stopServer();
        }
        btServer = new BluetoothServer(this);
        btServer.start();
        
        startWatchdog();
    }
    
    public void stopServer() {
        stopWatchdog();
        if (btServer != null) {
            btServer.stopServer();
            btServer = null;
        }
    }
    
    private void startWatchdog() {
        stopWatchdog();
        watchdogTimer = new Timer();
        watchdogTimer.schedule(new TimerTask() {
            public void run() {
                if (btServer != null && btServer.isConnected()) {
                    long now = System.currentTimeMillis();
                    // If no data for 20 seconds, connection might be dead
                    if (now - lastDataTime > 20000) {
                        LogManager.error("ConnMgr", "Watchdog timeout. Restarting connection.");
                        btServer.forceDisconnect();
                    } else {
                        // Send PING
                        sendData("PING\n");
                    }
                }
            }
        }, 10000, 10000); // Check every 10 seconds
    }
    
    private void stopWatchdog() {
        if (watchdogTimer != null) {
            watchdogTimer.cancel();
            watchdogTimer = null;
        }
    }
    
    public void onConnected() {
        lastDataTime = System.currentTimeMillis();
        LogManager.log("ConnMgr", "Android connected");
        uiManager.updateConnectionStatus("CONNECTED");
    }
    
    public void onDisconnected() {
        LogManager.log("ConnMgr", "Connection lost");
        uiManager.updateConnectionStatus("DISCONNECTED");
        // BluetoothServer has its own retry loop now. We do NOT call startServer() here.
    }
    
    public void onDataReceived(String data) {
        lastDataTime = System.currentTimeMillis();
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
