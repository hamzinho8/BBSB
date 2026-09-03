package com.hamza.blackberrybridge;

import javax.microedition.media.Manager;
import javax.microedition.media.Player;
import javax.microedition.media.control.RecordControl;
import java.io.ByteArrayOutputStream;

public class VoiceRecorder {
    private Player player;
    private RecordControl recordControl;
    private ByteArrayOutputStream outputStream;
    private boolean isRecording = false;

    public void startRecording() throws Exception {
        // BBOS natively supports AMR encoding for voice notes (highly compressed, perfect for Bluetooth)
        player = Manager.createPlayer("capture://audio?encoding=amr");
        player.realize();
        recordControl = (RecordControl) player.getControl("RecordControl");
        outputStream = new ByteArrayOutputStream();
        recordControl.setRecordStream(outputStream);
        recordControl.startRecord();
        player.start();
        isRecording = true;
    }

    public byte[] stopRecording() {
        byte[] data = null;
        if (isRecording) {
            try {
                recordControl.commit();
                data = outputStream.toByteArray();
            } catch (Exception e) {
                LogManager.error("VOICE", "Stop error: " + e.getMessage());
            } finally {
                try {
                    player.stop();
                    player.close();
                } catch (Exception e) {}
                isRecording = false;
                player = null;
                recordControl = null;
                outputStream = null;
            }
        }
        return data;
    }
    
    public boolean isRecording() {
        return isRecording;
    }
}
