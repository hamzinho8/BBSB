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
    // SPP UUID: 00001101-0000-1000-8000-00805F9B34FB
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
            // Set device to be discoverable
            LocalDevice.getLocalDevice().setDiscoverable(DiscoveryAgent.GIAC);
            LogManager.log("BT", "Starting SPP Server...");
            
            notifier = (StreamConnectionNotifier) Connector.open(URL);
            
            while (running) {
                LogManager.log("BT", "Waiting for Android...");
                
                // This call blocks until a client connects. Thread is necessary!
                connection = notifier.acceptAndOpen();
                LogManager.log("BT", "Connected!");
                connected = true;
                
                connectionManager.onConnected();
                
                inputStream = connection.openInputStream();
                outputStream = connection.openOutputStream();
                
                StringBuffer buffer = new StringBuffer();
                int ch;
                
                // Read loop
                while (running && connected) {
                    try {
                        ch = inputStream.read();
                        if (ch == -1) {
                            // Stream closed by peer
                            break;
                        }
                        
                        if (ch == '\n') {
                            String msg = buffer.toString();
                            LogManager.log("RX", msg);
                            connectionManager.onDataReceived(msg);
                            buffer.setLength(0);
                        } else if (ch != '\r') {
                            buffer.append((char) ch);
                        }
                        
                        if (buffer.length() > 4096) {
                            LogManager.error("BT", "Message too long, clearing buffer");
                            connectionManager.sendData("ERROR|MESSAGE_TOO_LONG\n");
                            buffer.setLength(0);
                        }
                    } catch (IOException e) {
                        LogManager.error("BT_READ", e.getMessage());
                        break; // Exit read loop on error
                    }
                }
                
                cleanupConnection();
            }
        } catch (Exception e) {
            LogManager.error("BT_ERR", "Server Exception: " + e.getMessage());
            cleanupConnection();
        }
    }
    
    private void cleanupConnection() {
        connected = false;
        try { if (inputStream != null) inputStream.close(); } catch (Exception e) {}
        try { if (outputStream != null) outputStream.close(); } catch (Exception e) {}
        try { if (connection != null) connection.close(); } catch (Exception e) {}
        
        if (running) {
            connectionManager.onDisconnected();
        }
    }
    
    public void stopServer() {
        running = false;
        cleanupConnection();
        try { if (notifier != null) notifier.close(); } catch (Exception e) {}
    }
    
    public boolean isConnected() {
        return connected;
    }
    
    public void send(String data) {
        try {
            if (outputStream != null) {
                LogManager.log("TX", data.trim());
                outputStream.write(data.getBytes());
                outputStream.flush();
            }
        } catch (IOException e) {
            LogManager.error("BT_TX_ERR", e.getMessage());
            cleanupConnection();
        }
    }
}
