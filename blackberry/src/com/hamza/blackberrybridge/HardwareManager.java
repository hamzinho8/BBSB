package com.hamza.blackberrybridge;

import net.rim.device.api.system.Alert;
import java.util.Timer;
import java.util.TimerTask;

public class HardwareManager {
    private static Timer vibrateTimer;

    public static void triggerMessageAlert() {
        if (Alert.isVibrateSupported()) {
            Alert.startVibrate(500); // 500ms
        }
        try {
            net.rim.device.api.system.LED.setState(net.rim.device.api.system.LED.STATE_BLINKING);
        } catch (Throwable t) {}
    }
    
    public static void stopMessageAlert() {
        try {
            net.rim.device.api.system.LED.setState(net.rim.device.api.system.LED.STATE_OFF);
        } catch (Throwable t) {}
    }
    
    public static void triggerNotificationAlert(SmartBridgeApp app, String appName) {
        if (Alert.isVibrateSupported()) {
            Alert.startVibrate(500); // 500ms
        }
    }
    
    public static void triggerCallAlert(SmartBridgeApp app) {
        if (Alert.isVibrateSupported()) {
            stopAlerts();
            vibrateTimer = new Timer();
            vibrateTimer.scheduleAtFixedRate(new TimerTask() {
                public void run() {
                    Alert.startVibrate(255);
                }
            }, 0, 1000); // Vibrate every second
        }
    }
    
    public static void stopAlerts() {
        if (vibrateTimer != null) {
            vibrateTimer.cancel();
            vibrateTimer = null;
        }
    }
}
