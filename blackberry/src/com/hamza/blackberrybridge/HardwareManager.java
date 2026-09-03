package com.hamza.blackberrybridge;

import net.rim.device.api.system.Alert;
import net.rim.device.api.system.LED;

public class HardwareManager {
    public static void triggerNotificationAlert(SmartBridgeApp app, String appName) {
        SettingsManager sm = app.getSettingsManager();
        String p = sm.getProfile();
        if (p == null || p.equals("Silent")) return; // No alerts in silent
        
        boolean doVibrate = sm.isVibrationEnabled() && (p.equals("Vibrate") || p.equals("Ring+Vibrate"));
        
        if (doVibrate && Alert.isVibrateSupported()) {
            if (appName != null && appName.toLowerCase().indexOf("whatsapp") != -1) {
                // Two short vibes for WhatsApp
                Alert.startVibrate(255);
                try { Thread.sleep(200); } catch(Exception e){}
                Alert.startVibrate(255);
            } else {
                // Standard one vibe for others
                Alert.startVibrate(500);
            }
        }
        
        LED.setConfiguration(500, 1000, LED.BRIGHTNESS_MAX);
        LED.setState(LED.STATE_BLINKING);
    }
    
    public static void triggerCallAlert(SmartBridgeApp app) {
        SettingsManager sm = app.getSettingsManager();
        String p = sm.getProfile();
        if (p == null || p.equals("Silent")) return; 
        
        boolean doVibrate = sm.isVibrationEnabled() && (p.equals("Vibrate") || p.equals("Ring+Vibrate"));
        
        if (doVibrate && Alert.isVibrateSupported()) {
            Alert.startVibrate(2000); // 2 second vibrate for incoming call
        }
        
        LED.setConfiguration(250, 250, LED.BRIGHTNESS_MAX);
        LED.setState(LED.STATE_BLINKING);
    }
    
    public static void stopAlerts() {
        LED.setState(LED.STATE_OFF);
        // Alert.stopVibrate() is not natively accessible to interrupt a specific length easily without just letting it finish 
        // in BBOS unless it's looping, but for 2000ms it will just end naturally.
    }
}
