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
            settings.put("debug_mode", new Boolean(true));
            
            Vector qr = new Vector();
            qr.addElement("OK");
            qr.addElement("Yes");
            qr.addElement("No");
            qr.addElement("I'm coming");
            qr.addElement("Call you later");
            settings.put("quick_replies", qr);
            
            persistentObject.setContents(settings);
            persistentObject.commit();
        }
        LogManager.setDebugEnabled(isDebugMode());
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
        return b != null ? b.booleanValue() : true;
    }

    public void setDebugMode(boolean val) {
        settings.put("debug_mode", new Boolean(val));
        LogManager.setDebugEnabled(val);
        persistentObject.commit();
    }
    
    public Vector getQuickReplies() {
        Object qr = settings.get("quick_replies");
        if (qr instanceof Vector) {
            return (Vector) qr;
        }
        return new Vector();
    }
    
    public void setQuickReplies(Vector replies) {
        settings.put("quick_replies", replies);
        persistentObject.commit();
    }
}
