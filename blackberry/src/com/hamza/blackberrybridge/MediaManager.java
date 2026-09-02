package com.hamza.blackberrybridge;

public class MediaManager {
    private SmartBridgeApp app;
    
    public MediaManager(SmartBridgeApp app) {
        this.app = app;
    }
    
    public void play() { app.getConnectionManager().sendData("MEDIA_PLAY\n"); }
    public void pause() { app.getConnectionManager().sendData("MEDIA_PAUSE\n"); }
    public void next() { app.getConnectionManager().sendData("MEDIA_NEXT\n"); }
    public void previous() { app.getConnectionManager().sendData("MEDIA_PREVIOUS\n"); }
}
