package com.hamza.blackberrybridge;

import javax.microedition.media.Manager;
import javax.microedition.media.Player;
import javax.microedition.media.control.VolumeControl;
import java.io.InputStream;
import net.rim.device.api.system.Alert;

public class AudioManager {
    private SmartBridgeApp app;
    private Player callPlayer;
    private Player notifPlayer;
    private long lastNotifTime = 0;

    public AudioManager(SmartBridgeApp app) {
        this.app = app;
    }

    private boolean canPlayAudio() {
        SettingsManager sm = app.getSettingsManager();
        String p = sm.getProfile();
        return p != null && (p.equals("Ring") || p.equals("Ring+Vibrate"));
    }

    public void playCallRingtone() {
        if (!app.getSettingsManager().isSoundCalls() || !canPlayAudio()) return;
        try {
            stopCallRingtone();
            InputStream is = getClass().getResourceAsStream("/ringtone.mp3");
            if (is != null) {
                callPlayer = Manager.createPlayer(is, "audio/mpeg");
                callPlayer.realize();
                VolumeControl vc = (VolumeControl) callPlayer.getControl("VolumeControl");
                if (vc != null) vc.setLevel(app.getSettingsManager().getVolCalls());
                callPlayer.prefetch();
                callPlayer.setLoopCount(-1); // Infinite loop
                callPlayer.start();
            } else {
                LogManager.error("AUDIO", "Missing /ringtone.mp3, using fallback tone");
                // Fallback to system tone if file missing
                Alert.startBuzzer(new byte[] { 50, 50, 50, 50 }, 50);
            }
        } catch (Exception e) {
            LogManager.error("AUDIO", "Call ring error: " + e.getMessage());
        }
    }

    public void stopCallRingtone() {
        try {
            if (callPlayer != null) {
                if (callPlayer.getState() == Player.STARTED) {
                    callPlayer.stop();
                }
                callPlayer.deallocate();
                callPlayer.close();
                callPlayer = null;
            }
        } catch (Exception e) {}
    }

    public void playNotificationSound(String appName) {
        if (!app.getSettingsManager().isSoundNotifs() || !canPlayAudio()) return;

        long now = System.currentTimeMillis();
        if (now - lastNotifTime < 1500) return; // Anti-spam limit
        lastNotifTime = now;

        try {
            if (notifPlayer != null) {
                if (notifPlayer.getState() == Player.STARTED) {
                    notifPlayer.stop();
                }
                notifPlayer.deallocate();
                notifPlayer.close();
                notifPlayer = null;
            }

            String file = "/notif.mp3";
            String lApp = appName != null ? appName.toLowerCase() : "";
            if (lApp.indexOf("whatsapp") != -1) file = "/whatsapp.mp3";
            else if (lApp.indexOf("messenger") != -1 || lApp.indexOf("facebook") != -1) file = "/messenger.mp3";
            else if (lApp.indexOf("telegram") != -1) file = "/telegram.mp3";
            else if (lApp.indexOf("sms") != -1 || lApp.indexOf("message") != -1) file = "/sms.mp3";

            InputStream is = getClass().getResourceAsStream(file);
            if (is != null) {
                notifPlayer = Manager.createPlayer(is, "audio/mpeg");
                notifPlayer.realize();
                VolumeControl vc = (VolumeControl) notifPlayer.getControl("VolumeControl");
                if (vc != null) vc.setLevel(app.getSettingsManager().getVolNotifs());
                notifPlayer.prefetch();
                notifPlayer.start();
            } else {
                LogManager.error("AUDIO", "Missing " + file + ", using fallback tone");
            }
        } catch (Exception e) {
            LogManager.error("AUDIO", "Notif error: " + e.getMessage());
        }
    }
    
    public void testSound() {
        playNotificationSound("sms");
    }
}
