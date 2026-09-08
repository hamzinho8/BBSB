package com.hamza.blackberrybridge;

import net.rim.device.api.system.DeviceInfo;

public class BatteryManager {
    public static int getBatteryLevel() {
        try {
            return DeviceInfo.getBatteryLevel();
        } catch (Throwable t) {
            return -1;
        }
    }
}
