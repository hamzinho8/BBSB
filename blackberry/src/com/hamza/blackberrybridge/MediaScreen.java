package com.hamza.blackberrybridge;

import net.rim.device.api.ui.*;
import net.rim.device.api.ui.component.*;
import net.rim.device.api.ui.container.*;
import net.rim.device.api.ui.decor.*;

public class MediaScreen extends MainScreen {
    private MediaManager mediaManager;
    
    public MediaScreen(MediaManager manager) {
        this.mediaManager = manager;
        getMainManager().setBackground(BackgroundFactory.createSolidBackground(Color.BLACK));
        
        DarkLabelField title = new DarkLabelField("MUSIC PLAYER", Field.FIELD_HCENTER, 0x0078D7);
        try { title.setFont(Font.getDefault().derive(Font.BOLD, 24)); } catch(Exception e){}
        add(title);
        add(new SeparatorField());
        
        VerticalFieldManager vfm = new VerticalFieldManager(Field.FIELD_HCENTER | Field.FIELD_VCENTER);
        vfm.setPadding(50, 0, 0, 0);
        
        DarkButtonField playPauseBtn = new DarkButtonField("Play / Pause", 200, 80);
        playPauseBtn.setChangeListener(new FieldChangeListener() {
            public void fieldChanged(Field field, int context) { mediaManager.play(); }
        });
        vfm.add(playPauseBtn);
        
        HorizontalFieldManager hfm = new HorizontalFieldManager(Field.FIELD_HCENTER);
        hfm.setPadding(20, 0, 0, 0);
        
        DarkButtonField prevBtn = new DarkButtonField("<< Prev", 120, 60);
        prevBtn.setChangeListener(new FieldChangeListener() {
            public void fieldChanged(Field field, int context) { mediaManager.previous(); }
        });
        hfm.add(prevBtn);
        
        DarkButtonField nextBtn = new DarkButtonField("Next >>", 120, 60);
        nextBtn.setChangeListener(new FieldChangeListener() {
            public void fieldChanged(Field field, int context) { mediaManager.next(); }
        });
        hfm.add(nextBtn);
        
        vfm.add(hfm);
        add(vfm);
    }
    
    protected boolean keyDown(int keycode, int time) {
        int key = Keypad.key(keycode);
        if (key == Keypad.KEY_END || key == Keypad.KEY_ESCAPE) { 
            close();
            return true;
        }
        return super.keyDown(keycode, time);
    }
}
