package com.hamza.blackberrybridge;

import net.rim.device.api.ui.*;
import net.rim.device.api.ui.component.*;
import net.rim.device.api.ui.container.*;
import net.rim.device.api.ui.decor.*;
import java.util.Vector;

public class SettingsScreen extends MainScreen {
    private SettingsManager settingsManager;
    private BasicEditField[] quickReplyFields = new BasicEditField[3];
    
    public SettingsScreen(SettingsManager manager) {
        this.settingsManager = manager;
        getMainManager().setBackground(BackgroundFactory.createSolidBackground(Color.BLACK));
        
        DarkLabelField title = new DarkLabelField("SETTINGS", Field.FIELD_HCENTER, 0x0078D7);
        try { title.setFont(Font.getDefault().derive(Font.BOLD, 24)); } catch(Exception e){}
        add(title);
        add(new SeparatorField());
        
        HorizontalFieldManager hfmAuth = new HorizontalFieldManager();
        DarkLabelField lblAuto = new DarkLabelField("Auto-Reconnect: ", Color.WHITE);
        final DarkButtonField btnAuto = new DarkButtonField(settingsManager.isAutoReconnect() ? "ON" : "OFF", 100, 30);
        btnAuto.setChangeListener(new FieldChangeListener() {
            public void fieldChanged(Field field, int context) {
                if (btnAuto.getText().equals("ON")) {
                    btnAuto.setText("OFF");
                    settingsManager.setAutoReconnect(false);
                } else {
                    btnAuto.setText("ON");
                    settingsManager.setAutoReconnect(true);
                }
            }
        });
        hfmAuth.add(lblAuto); hfmAuth.add(btnAuto);
        add(hfmAuth);
        
        HorizontalFieldManager hfmDebug = new HorizontalFieldManager();
        DarkLabelField lblDebug = new DarkLabelField("Debug Mode: ", Color.WHITE);
        final DarkButtonField btnDebug = new DarkButtonField(settingsManager.isDebugMode() ? "ON" : "OFF", 100, 30);
        btnDebug.setChangeListener(new FieldChangeListener() {
            public void fieldChanged(Field field, int context) {
                if (btnDebug.getText().equals("ON")) {
                    btnDebug.setText("OFF");
                    settingsManager.setDebugMode(false);
                } else {
                    btnDebug.setText("ON");
                    settingsManager.setDebugMode(true);
                }
            }
        });
        hfmDebug.add(lblDebug); hfmDebug.add(btnDebug);
        add(hfmDebug);
        
        add(new SeparatorField());
        add(new DarkLabelField("Quick Replies (Top 3):", 0x0078D7));
        
        // BasicEditField displays black text, which is bad on black background.
        // Unfortunately standard fields are hard to style in BBOS. We will just use them anyway, BB handles cursor
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
        
        DarkButtonField saveBtn = new DarkButtonField("Save Settings", 200, 45);
        saveBtn.setChangeListener(new FieldChangeListener() {
            public void fieldChanged(Field field, int context) {
                saveSettings();
            }
        });
        
        HorizontalFieldManager hfmSave = new HorizontalFieldManager(Field.FIELD_HCENTER);
        hfmSave.add(saveBtn);
        add(hfmSave);
    }
    
    private void saveSettings() {
        Vector newQr = new Vector();
        for (int i = 0; i < 3; i++) {
            String txt = quickReplyFields[i].getText();
            if (txt != null && txt.trim().length() > 0) {
                newQr.addElement(txt.trim());
            }
        }
        Vector oldQr = settingsManager.getQuickReplies();
        for (int i = 3; i < oldQr.size(); i++) { 
            newQr.addElement(oldQr.elementAt(i));
        }
        settingsManager.setQuickReplies(newQr);
        
        close();
    }
    
    protected boolean keyDown(int keycode, int time) {
        int key = Keypad.key(keycode);
        if (key == Keypad.KEY_END || key == Keypad.KEY_ESCAPE) { 
            close();
            return true;
        }
        return super.keyDown(keycode, time);
    }
}
