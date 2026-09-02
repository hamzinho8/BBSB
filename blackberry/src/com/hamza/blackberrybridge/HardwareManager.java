package com.hamza.blackberrybridge;

import net.rim.device.api.system.Alert;
import net.rim.device.api.system.LED;

public class HardwareManager {
    public static void triggerNotificationAlert() {
        if (Alert.isVibrateSupported()) {
            Alert.startVibrate(500); // 500ms vibration
        }
        LED.setConfiguration(500, 1000, LED.BRIGHTNESS_MAX);
        LED.setState(LED.STATE_BLINKING);
    }
    
    public static void triggerCallAlert() {
        if (Alert.isVibrateSupported()) {
            Alert.startVibrate(2000); // 2 sec vibration for call
        }
        LED.setConfiguration(250, 250, LED.BRIGHTNESS_MAX);
        LED.setState(LED.STATE_BLINKING);
    }
    
    public static void stopAlerts() {
        LED.setState(LED.STATE_OFF);
    }
}
