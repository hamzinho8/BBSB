package com.hamza.blackberrybridge;

import java.util.Vector;

public class ProtocolManager {
    private ConnectionManager connectionManager;
    private UIManager uiManager;
    
    public ProtocolManager(ConnectionManager connectionManager, UIManager uiManager) {
        this.connectionManager = connectionManager;
        this.uiManager = uiManager;
    }
    
    public void processMessage(String message) {
        if (message == null || message.length() == 0) return;
        
        String[] parts = split(message, '|');
        if (parts.length == 0) return;
        
        String command = parts[0];
        
        if (command.equals("PING")) {
            connectionManager.sendData("PONG\n");
        } 
        else if (command.equals("HELLO")) {
            connectionManager.sendData("HELLO|BSB/1|BLACKBERRY_9790\n");
            connectionManager.sendData("READY\n");
        }
        else if (command.equals("BATTERY")) {
            if (parts.length > 1) {
                uiManager.updateBattery(parts[1]);
            }
        }
        else if (command.equals("NOTIFICATION")) {
            // NOTIFICATION|id|application|sender|message
            if (parts.length >= 5) {
                uiManager.showNotification(parts[1], parts[2], parts[3], parts[4]);
            } else {
                connectionManager.sendData("ERROR|INVALID_PACKET\n");
            }
        }
        else if (command.equals("CALL_INCOMING")) {
            // CALL_INCOMING|id|name|number
            if (parts.length >= 4) {
                uiManager.showIncomingCall(parts[1], parts[2], parts[3]);
            } else {
                connectionManager.sendData("ERROR|INVALID_PACKET\n");
            }
        }
        else if (command.equals("CONTACT")) {
            // Future extension for syncing contacts
        }
        else {
            connectionManager.sendData("ERROR|UNKNOWN_COMMAND\n");
        }
    }
    
    // Custom split function for strict Java ME compatibility
    private String[] split(String str, char separator) {
        Vector nodes = new Vector();
        int index = str.indexOf(separator);
        while (index >= 0) {
            nodes.addElement(str.substring(0, index));
            str = str.substring(index + 1);
            index = str.indexOf(separator);
        }
        nodes.addElement(str);
        
        String[] result = new String[nodes.size()];
        if (nodes.size() > 0) {
            for (int loop = 0; loop < nodes.size(); loop++) {
                result[loop] = (String) nodes.elementAt(loop);
            }
        }
        return result;
    }
}
