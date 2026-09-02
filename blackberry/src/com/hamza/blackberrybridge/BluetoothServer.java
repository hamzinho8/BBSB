package com.hamza.blackberrybridge;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.DiscoveryAgent;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

public class BluetoothServer extends Thread {
    private static final String UUID = "0000110100001000800000805F9B34FB";
    private static final String URL = "btspp://localhost:" + UUID + ";name=SmartBridge;authorize=false;encrypt=false";
    
    private boolean running = true;
    private boolean connected = false;
    private StreamConnectionNotifier notifier;
    private StreamConnection connection;
    private InputStream inputStream;
    private OutputStream outputStream;
    
    private ConnectionManager connectionManager;
    
    public BluetoothServer(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }
    
    public void run() {
        try {
            LocalDevice.getLocalDevice().setDiscoverable(DiscoveryAgent.GIAC);
            LogManager.log("BT", "Starting SPP Server...");
        } catch (Exception e) {
            LogManager.error("BT_ERR", "Cannot make discoverable");
        }
        
        while (running) {
            try {
                if (notifier == null) {
                    notifier = (StreamConnectionNotifier) Connector.open(URL);
                }
                
                LogManager.log("BT", "Waiting for Android...");
                connection = notifier.acceptAndOpen();
                LogManager.log("BT", "Connected!");
                connected = true;
                
                connectionManager.onConnected();
                
                inputStream = connection.openInputStream();
                outputStream = connection.openOutputStream();
                
                StringBuffer buffer = new StringBuffer();
                int ch;
                
                while (running && connected) {
                    try {
                        ch = inputStream.read();
                        if (ch == -1) {
                            break;
                        }
                        
                        if (ch == '\n') {
                            String msg = buffer.toString();
                            connectionManager.onDataReceived(msg);
                            buffer.setLength(0);
                        } else if (ch != '\r') {
                            buffer.append((char) ch);
                        }
                        
                        if (buffer.length() > 4096) {
                            LogManager.error("BT", "Buffer overflow");
                            connectionManager.sendData("ERROR|MESSAGE_TOO_LONG\n");
                            buffer.setLength(0);
                        }
                    } catch (IOException e) {
                        break;
                    }
                }
            } catch (Exception e) {
                LogManager.error("BT_ERR", "Server Exception: " + e.getMessage());
                try { Thread.sleep(3000); } catch (Exception sleepEx) {}
            } finally {
                cleanupConnection();
            }
        }
    }
    
    private void cleanupConnection() {
        connected = false;
        try { if (inputStream != null) { inputStream.close(); inputStream = null; } } catch (Exception e) {}
        try { if (outputStream != null) { outputStream.close(); outputStream = null; } } catch (Exception e) {}
        try { if (connection != null) { connection.close(); connection = null; } } catch (Exception e) {}
        
        if (running) {
            connectionManager.onDisconnected();
        }
    }
    
    public void forceDisconnect() {
        cleanupConnection();
    }
    
    public void stopServer() {
        running = false;
        cleanupConnection();
        try { if (notifier != null) { notifier.close(); notifier = null; } } catch (Exception e) {}
    }
    
    public boolean isConnected() {
        return connected;
    }
    
    public void send(String data) {
        try {
            if (outputStream != null) {
                outputStream.write(data.getBytes());
                outputStream.flush();
            }
        } catch (IOException e) {
            cleanupConnection();
        }
    }
}
