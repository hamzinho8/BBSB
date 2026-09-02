package com.hamza.blackberrybridge;

import net.rim.device.api.system.Application;

public class MediaManager {
    private SmartBridgeApp app;
    private String currentTitle = "Not Playing";
    private String currentArtist = "Unknown Artist";
    private MediaScreen activeScreen;
    
    public MediaManager(SmartBridgeApp app) {
        this.app = app;
    }
    
    public void play() { app.getConnectionManager().sendData("MEDIA_PLAY\n"); }
    public void pause() { app.getConnectionManager().sendData("MEDIA_PAUSE\n"); }
    public void next() { app.getConnectionManager().sendData("MEDIA_NEXT\n"); }
    public void previous() { app.getConnectionManager().sendData("MEDIA_PREVIOUS\n"); }
    
    public void updateMeta(String title, String artist) {
        this.currentTitle = title;
        this.currentArtist = artist;
        if (activeScreen != null) {
            Application.getApplication().invokeLater(new Runnable() {
                public void run() {
                    activeScreen.refreshMeta();
                }
            });
        }
    }
    
    public String getTitle() { return currentTitle; }
    public String getArtist() { return currentArtist; }
    
    public void setActiveScreen(MediaScreen screen) {
        this.activeScreen = screen;
    }
}
