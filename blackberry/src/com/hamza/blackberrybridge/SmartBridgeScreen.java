package com.hamza.blackberrybridge;

import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.RichTextField;
import net.rim.device.api.ui.component.SeparatorField;

public class SmartBridgeScreen extends MainScreen {
    private LabelField btStatusLabel;
    private LabelField androidStatusLabel;
    private LabelField batteryLabel;
    private RichTextField logField;
    
    public SmartBridgeScreen() {
        setTitle("BlackBerrySmartBridge");
        
        btStatusLabel = new LabelField("Bluetooth: DISCONNECTED");
        androidStatusLabel = new LabelField("Android: Not connected");
        batteryLabel = new LabelField("Battery: --");
        
        add(btStatusLabel);
        add(androidStatusLabel);
        add(batteryLabel);
        add(new SeparatorField());
        
        logField = new RichTextField("Status: Waiting for Android...\n");
        add(logField);
    }
    
    public void updateConnectionStatus(String status) {
        if (status.equals("CONNECTED")) {
            btStatusLabel.setText("Bluetooth: CONNECTED");
            androidStatusLabel.setText("Android: Connected");
            logField.setText("Status: Ready\n" + logField.getText());
        } else if (status.equals("DISCONNECTED")) {
            btStatusLabel.setText("Bluetooth: DISCONNECTED");
            androidStatusLabel.setText("Android: Not connected");
            logField.setText("Status: Connection lost, waiting...\n" + logField.getText());
        } else {
            btStatusLabel.setText("Bluetooth: " + status);
        }
    }

    public void updateBattery(String level) {
        batteryLabel.setText("Battery: " + level + "%");
    }
    
    public void addLog(String log) {
        // Keep logs relatively short to avoid memory issues
        String current = logField.getText();
        if (current.length() > 1024) {
            current = current.substring(0, 1024);
        }
        logField.setText(log + "\n" + current);
    }
}
