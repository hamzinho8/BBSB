package com.hamza.blackberrybridge;

import net.rim.device.api.system.EventLogger;

public class LogManager {
    private static final long APP_GUID = 0x1a2b3c4d5e6f7a8bL;
    private static final String APP_NAME = "SmartBridge";
    private static boolean debugEnabled = true;

    static {
        EventLogger.register(APP_GUID, APP_NAME, EventLogger.VIEWER_STRING);
    }

    public static void setDebugEnabled(boolean enabled) {
        debugEnabled = enabled;
    }

    public static void log(String tag, String message) {
        String fullMsg = "[" + tag + "] " + message;
        System.out.println(fullMsg);
        
        if (debugEnabled) {
            EventLogger.logEvent(APP_GUID, fullMsg.getBytes(), EventLogger.INFORMATION);
        }
    }

    public static void error(String tag, String message) {
        String fullMsg = "[ERROR][" + tag + "] " + message;
        System.out.println(fullMsg);
        EventLogger.logEvent(APP_GUID, fullMsg.getBytes(), EventLogger.SEVERE_ERROR);
    }
}
