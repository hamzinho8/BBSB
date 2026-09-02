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
            
            Vector defaultQr = new Vector();
            defaultQr.addElement("I'm busy right now.");
            defaultQr.addElement("OK.");
            defaultQr.addElement("Call me later.");
            settings.put("quick_replies", defaultQr);
            
            persistentObject.setContents(settings);
            persistentObject.commit();
        }
    }

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
        LogManager.setDebugEnabled(val); // Toggle logs
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
}
