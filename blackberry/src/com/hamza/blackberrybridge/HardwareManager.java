package com.hamza.blackberrybridge;

import net.rim.device.api.system.Alert;
import net.rim.device.api.system.LED;

public class HardwareManager {
    public static void triggerNotificationAlert(SmartBridgeApp app, String appName) {
        SettingsManager sm = app.getSettingsManager();
        String p = sm.getProfile();
        if (p == null || p.equals("Silent")) return; // No alerts in silent
        
        boolean doVibrate = sm.isVibrationEnabled() && (p.equals("Vibrate") || p.equals("Ring+Vibrate"));
        
        try {
            if (doVibrate && Alert.isVibrateSupported()) {
                if (appName != null && appName.toLowerCase().indexOf("whatsapp") != -1) {
                    Alert.startVibrate(255);
                    try { Thread.sleep(200); } catch(Exception e){}
                    Alert.startVibrate(255);
                } else {
                    Alert.startVibrate(500);
                }
            }
        } catch (Throwable t) {
            // Ignore restricted API error
        }
        
        try {
            LED.setState(LED.STATE_BLINKING);
        } catch (Throwable t) {
            // Ignore restricted API error
        }
    }
    
    public static void triggerCallAlert(SmartBridgeApp app) {
        SettingsManager sm = app.getSettingsManager();
        String p = sm.getProfile();
        if (p == null || p.equals("Silent")) return; 
        
        boolean doVibrate = sm.isVibrationEnabled() && (p.equals("Vibrate") || p.equals("Ring+Vibrate"));
        
        try {
            if (doVibrate && Alert.isVibrateSupported()) {
                Alert.startVibrate(2000); 
            }
        } catch (Throwable t) {
            // Ignore
        }
        
        try {
            LED.setState(LED.STATE_BLINKING);
        } catch (Throwable t) {
            // Ignore
        }
    }
    
    public static void stopAlerts() {
        try {
            LED.setState(LED.STATE_OFF);
        } catch (Throwable t) {
            // Ignore
        }
    }
}
