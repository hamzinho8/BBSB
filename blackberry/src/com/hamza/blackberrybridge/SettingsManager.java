package com.hamza.blackberrybridge;

import net.rim.device.api.system.PersistentObject;
import net.rim.device.api.system.PersistentStore;
import java.util.Hashtable;
import java.util.Vector;

public class SettingsManager {
    private static final long STORE_ID = 0x5a2b1f8c4e7d9034L;
    private PersistentObject persistentObject;
    private Hashtable settings;

    public SettingsManager() {
        persistentObject = PersistentStore.getPersistentObject(STORE_ID);
        Object contents = persistentObject.getContents();
        if (contents instanceof Hashtable) {
            settings = (Hashtable) contents;
        } else {
            settings = new Hashtable();
            settings.put("auto_reconnect", new Boolean(true));
            settings.put("debug_mode", new Boolean(false));
            settings.put("night_mode", new Boolean(false));
            
            // Audio & Profile Settings
            settings.put("sound_calls", new Boolean(true));
            settings.put("vol_calls", new Integer(80));
            settings.put("sound_notifs", new Boolean(true));
            settings.put("vol_notifs", new Integer(70));
            settings.put("vibration_enabled", new Boolean(true));
            settings.put("profile", "Ring+Vibrate"); // "Silent", "Vibrate", "Ring", "Ring+Vibrate"
            
            Vector defaultQr = new Vector();
            defaultQr.addElement("I'm busy right now.");
            defaultQr.addElement("OK.");
            defaultQr.addElement("Call me later.");
            settings.put("quick_replies", defaultQr);
            
            persistentObject.setContents(settings);
            persistentObject.commit();
        }
    }

    // Existing getters/setters
    public boolean isAutoReconnect() {
        Boolean b = (Boolean) settings.get("auto_reconnect");
        return b != null ? b.booleanValue() : true;
    }
    public void setAutoReconnect(boolean val) {
        settings.put("auto_reconnect", new Boolean(val));
        persistentObject.commit();
    }
    
    public boolean isDebugMode() {
        Boolean b = (Boolean) settings.get("debug_mode");
        return b != null ? b.booleanValue() : false;
    }
    public void setDebugMode(boolean val) {
        settings.put("debug_mode", new Boolean(val));
        persistentObject.commit();
        LogManager.setDebugEnabled(val); 
    }
    
    public boolean isNightMode() {
        Boolean b = (Boolean) settings.get("night_mode");
        return b != null ? b.booleanValue() : false;
    }
    public void setNightMode(boolean val) {
        settings.put("night_mode", new Boolean(val));
        persistentObject.commit();
    }
    
    public Vector getQuickReplies() {
        Vector qr = (Vector) settings.get("quick_replies");
        if (qr == null) {
            qr = new Vector();
            qr.addElement("OK.");
        }
        return qr;
    }
    public void setQuickReplies(Vector newQr) {
        settings.put("quick_replies", newQr);
        persistentObject.commit();
    }

    // New Audio getters/setters
    public boolean isSoundCalls() {
        Boolean b = (Boolean) settings.get("sound_calls");
        return b != null ? b.booleanValue() : true;
    }
    public void setSoundCalls(boolean val) {
        settings.put("sound_calls", new Boolean(val));
        persistentObject.commit();
    }

    public int getVolCalls() {
        Integer i = (Integer) settings.get("vol_calls");
        return i != null ? i.intValue() : 80;
    }
    public void setVolCalls(int val) {
        settings.put("vol_calls", new Integer(val));
        persistentObject.commit();
    }

    public boolean isSoundNotifs() {
        Boolean b = (Boolean) settings.get("sound_notifs");
        return b != null ? b.booleanValue() : true;
    }
    public void setSoundNotifs(boolean val) {
        settings.put("sound_notifs", new Boolean(val));
        persistentObject.commit();
    }

    public int getVolNotifs() {
        Integer i = (Integer) settings.get("vol_notifs");
        return i != null ? i.intValue() : 70;
    }
    public void setVolNotifs(int val) {
        settings.put("vol_notifs", new Integer(val));
        persistentObject.commit();
    }

    public boolean isVibrationEnabled() {
        Boolean b = (Boolean) settings.get("vibration_enabled");
        return b != null ? b.booleanValue() : true;
    }
    public void setVibrationEnabled(boolean val) {
        settings.put("vibration_enabled", new Boolean(val));
        persistentObject.commit();
    }

    public String getProfile() {
        String p = (String) settings.get("profile");
        return p != null ? p : "Ring+Vibrate";
    }
    public void setProfile(String val) {
        settings.put("profile", val);
        persistentObject.commit();
    }
}
