package com.hamza.blackberrybridge;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.RichTextField;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.VerticalFieldManager;

public class SmartBridgeScreen extends MainScreen {
    private LabelField btStatusLabel;
    private LabelField androidStatusLabel;
    private LabelField batteryLabel;
    private ButtonField btnNotifs;
    private RichTextField logField;
    private SmartBridgeApp app;
    
    public SmartBridgeScreen(SmartBridgeApp application) {
        this.app = application;
        setTitle("BlackBerrySmartBridge");
        
        btStatusLabel = new LabelField("Bluetooth: DISCONNECTED", Field.NON_FOCUSABLE);
        androidStatusLabel = new LabelField("Android: Not connected", Field.NON_FOCUSABLE);
        batteryLabel = new LabelField("Android Battery: --", Field.NON_FOCUSABLE);
        
        add(btStatusLabel);
        add(androidStatusLabel);
        add(batteryLabel);
        add(new SeparatorField());
        
        // Action Grid
        VerticalFieldManager grid = new VerticalFieldManager();
        
        HorizontalFieldManager row1 = new HorizontalFieldManager();
        btnNotifs = new ButtonField("Notifications (0)", ButtonField.CONSUME_CLICK);
        btnNotifs.setChangeListener(new FieldChangeListener() {
            public void fieldChanged(Field field, int context) {
                app.getUIManager().openNotificationList();
            }
        });
        row1.add(btnNotifs);
        
        ButtonField btnContacts = new ButtonField("Contacts", ButtonField.CONSUME_CLICK);
        btnContacts.setChangeListener(new FieldChangeListener() {
            public void fieldChanged(Field field, int context) {
                app.getUIManager().openContacts();
            }
        });
        row1.add(btnContacts);
        grid.add(row1);
        
        HorizontalFieldManager row2 = new HorizontalFieldManager();
        ButtonField btnMedia = new ButtonField("Media Controls", ButtonField.CONSUME_CLICK);
        btnMedia.setChangeListener(new FieldChangeListener() {
            public void fieldChanged(Field field, int context) {
                app.getUIManager().openMedia();
            }
        });
        row2.add(btnMedia);
        
        ButtonField btnSettings = new ButtonField("Settings", ButtonField.CONSUME_CLICK);
        btnSettings.setChangeListener(new FieldChangeListener() {
            public void fieldChanged(Field field, int context) {
                app.getUIManager().openSettings();
            }
        });
        row2.add(btnSettings);
        grid.add(row2);
        
        add(grid);
        
        add(new SeparatorField());
        
        LabelField logTitle = new LabelField("Activity Log:", Field.NON_FOCUSABLE);
        add(logTitle);
        
        logField = new RichTextField("System Ready\n", Field.NON_FOCUSABLE);
        add(logField);
    }
    
    public void updateConnectionStatus(String status) {
        if (status.equals("CONNECTED")) {
            btStatusLabel.setText("Bluetooth: CONNECTED");
            androidStatusLabel.setText("Android: Connected");
            addLog("[SYS] Connected to Android");
        } else if (status.equals("DISCONNECTED")) {
            btStatusLabel.setText("Bluetooth: DISCONNECTED");
            androidStatusLabel.setText("Android: Not connected");
            addLog("[SYS] Connection lost");
        } else {
            btStatusLabel.setText("Bluetooth: " + status);
        }
    }

    public void updateBattery(String level) {
        batteryLabel.setText("Android Battery: " + level + "%");
    }
    
    public void updateNotificationCount(int count) {
        btnNotifs.setLabel("Notifications (" + count + ")");
    }
    
    public void addLog(String log) {
        String current = logField.getText();
        if (current.length() > 1024) {
            current = current.substring(0, 1024);
        }
        logField.setText(log + "\n" + current);
    }
}
