package com.hamza.blackberrybridge;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.component.BasicEditField;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.CheckboxField;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.MainScreen;
import java.util.Vector;

public class SettingsScreen extends MainScreen {
    private SettingsManager settingsManager;
    private CheckboxField autoReconnectField;
    private CheckboxField debugModeField;
    private BasicEditField[] quickReplyFields = new BasicEditField[3];
    
    public SettingsScreen(SettingsManager manager) {
        this.settingsManager = manager;
        setTitle("Settings");
        
        autoReconnectField = new CheckboxField("Auto-Reconnect Bluetooth", settingsManager.isAutoReconnect());
        add(autoReconnectField);
        
        debugModeField = new CheckboxField("Enable Debug Logging", settingsManager.isDebugMode());
        add(debugModeField);
        
        add(new SeparatorField());
        add(new net.rim.device.api.ui.component.LabelField("Quick Replies (Top 3)"));
        
        Vector qr = settingsManager.getQuickReplies();
        for (int i = 0; i < 3; i++) {
            String val = "";
            if (i < qr.size()) {
                val = (String) qr.elementAt(i);
            }
            quickReplyFields[i] = new BasicEditField((i+1) + ": ", val, 50, BasicEditField.DEFAULT_KEYBOARD_LAYOUT);
            add(quickReplyFields[i]);
        }
        
        add(new SeparatorField());
        
        ButtonField saveBtn = new ButtonField("Save Settings", ButtonField.CONSUME_CLICK | ButtonField.FIELD_HCENTER);
        saveBtn.setChangeListener(new FieldChangeListener() {
            public void fieldChanged(Field field, int context) {
                saveSettings();
            }
        });
        add(saveBtn);
    }
    
    private void saveSettings() {
        settingsManager.setAutoReconnect(autoReconnectField.getChecked());
        settingsManager.setDebugMode(debugModeField.getChecked());
        
        Vector newQr = new Vector();
        for (int i = 0; i < 3; i++) {
            String txt = quickReplyFields[i].getText();
            if (txt != null && txt.trim().length() > 0) {
                newQr.addElement(txt.trim());
            }
        }
        // Preserve old ones if they had more than 3
        Vector oldQr = settingsManager.getQuickReplies();
        for (int i = 3; i < oldQr.size(); i++) {
             newQr.addElement(oldQr.elementAt(i));
        }
        settingsManager.setQuickReplies(newQr);
        
        close();
    }
}
