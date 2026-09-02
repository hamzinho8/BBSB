package com.hamza.blackberrybridge;

import net.rim.device.api.system.Alert;
import net.rim.device.api.system.LED;

public class HardwareManager {
    public static void triggerNotificationAlert(String appName) {
        if (Alert.isVibrateSupported()) {
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
    
    public static void triggerCallAlert() {
        if (Alert.isVibrateSupported()) {
            Alert.startVibrate(2000);
        }
        LED.setConfiguration(250, 250, LED.BRIGHTNESS_MAX);
        LED.setState(LED.STATE_BLINKING);
    }
    
    public static void stopAlerts() {
        LED.setState(LED.STATE_OFF);
    }
}
