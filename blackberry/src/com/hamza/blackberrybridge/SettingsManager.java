package com.hamza.blackberrybridge;

import net.rim.device.api.system.PersistentObject;
import net.rim.device.api.system.PersistentStore;
import java.util.Hashtable;

public class SettingsManager {
    // Unique ID for the PersistentStore. MUST be a unique long value.
    private static final long STORE_ID = 0x4253427269646765L; // "BSBbridge"
    private PersistentObject store;
    private Hashtable settings;

    public SettingsManager() {
        store = PersistentStore.getPersistentObject(STORE_ID);
        if (store.getContents() == null) {
            settings = new Hashtable();
            store.setContents(settings);
            store.commit();
        } else {
            settings = (Hashtable) store.getContents();
        }
    }

    public void saveSetting(String key, String value) {
        settings.put(key, value);
        store.commit();
    }

    public String getSetting(String key, String def) {
        if (settings.containsKey(key)) {
            return (String) settings.get(key);
        }
        return def;
    }
}
