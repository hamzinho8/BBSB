package com.hamza.blackberrybridge;

public class MediaManager {
    private ConnectionManager connectionManager;
    
    public MediaManager(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }
    
    public void play() { connectionManager.sendData("MEDIA_PLAY\n"); }
    public void pause() { connectionManager.sendData("MEDIA_PAUSE\n"); }
    public void next() { connectionManager.sendData("MEDIA_NEXT\n"); }
    public void previous() { connectionManager.sendData("MEDIA_PREVIOUS\n"); }
}
