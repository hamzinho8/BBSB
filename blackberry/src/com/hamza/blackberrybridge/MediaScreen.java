package com.hamza.blackberrybridge;

import net.rim.device.api.ui.*;
import net.rim.device.api.ui.component.*;
import net.rim.device.api.ui.container.*;
import net.rim.device.api.ui.decor.*;

public class MediaScreen extends MainScreen {
    private MediaManager mediaManager;
    private DarkLabelField titleLabel;
    private DarkLabelField artistLabel;
    
    public MediaScreen(MediaManager manager) {
        this.mediaManager = manager;
        manager.setActiveScreen(this);
        
        getMainManager().setBackground(BackgroundFactory.createSolidBackground(Color.BLACK));
        
        DarkLabelField header = new DarkLabelField("MEDIA CONTROL", Field.FIELD_HCENTER, 0x0078D7);
        try { header.setFont(Font.getDefault().derive(Font.BOLD, 24)); } catch(Exception e){}
        add(header);
        add(new SeparatorField());
        
        titleLabel = new DarkLabelField(mediaManager.getTitle(), Field.FIELD_HCENTER, Color.WHITE);
        try { titleLabel.setFont(Font.getDefault().derive(Font.BOLD, 20)); } catch(Exception e){}
        
        artistLabel = new DarkLabelField(mediaManager.getArtist(), Field.FIELD_HCENTER, 0xAAAAAA);
        
        add(titleLabel);
        add(artistLabel);
        add(new SeparatorField());
        
        HorizontalFieldManager btns = new HorizontalFieldManager(Field.FIELD_HCENTER);
        
        DarkButtonField btnPrev = new DarkButtonField("<<", 100, 60);
        btnPrev.setChangeListener(new FieldChangeListener() {
            public void fieldChanged(Field field, int context) { mediaManager.previous(); }
        });
        
        DarkButtonField btnPlay = new DarkButtonField("Play/Pause", 150, 60);
        btnPlay.setChangeListener(new FieldChangeListener() {
            public void fieldChanged(Field field, int context) { mediaManager.play(); } // In real implementation, maybe toggle
        });
        
        DarkButtonField btnNext = new DarkButtonField(">>", 100, 60);
        btnNext.setChangeListener(new FieldChangeListener() {
            public void fieldChanged(Field field, int context) { mediaManager.next(); }
        });
        
        btns.add(btnPrev);
        btns.add(btnPlay);
        btns.add(btnNext);
        add(btns);
    }
    
    public void refreshMeta() {
        titleLabel.setText(mediaManager.getTitle());
        artistLabel.setText(mediaManager.getArtist());
    }
    
    protected boolean keyDown(int keycode, int time) {
        int key = Keypad.key(keycode);
        if (key == Keypad.KEY_END || key == Keypad.KEY_ESCAPE) { 
            mediaManager.setActiveScreen(null);
            close();
            return true;
        }
        return super.keyDown(keycode, time);
    }
}
