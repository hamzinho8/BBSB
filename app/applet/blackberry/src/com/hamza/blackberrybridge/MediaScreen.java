package com.hamza.blackberrybridge;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.HorizontalFieldManager;

public class MediaScreen extends MainScreen {
    private MediaManager mediaManager;
    
    public MediaScreen(MediaManager manager) {
        this.mediaManager = manager;
        setTitle("Media Controls");
        
        add(new LabelField("Android Media:", LabelField.FIELD_HCENTER));
        add(new SeparatorField());
        
        HorizontalFieldManager row1 = new HorizontalFieldManager(Field.FIELD_HCENTER);
        ButtonField prevBtn = new ButtonField("<< Prev", ButtonField.CONSUME_CLICK);
        prevBtn.setChangeListener(new FieldChangeListener() {
            public void fieldChanged(Field field, int context) {
                mediaManager.previous();
            }
        });
        row1.add(prevBtn);
        
        ButtonField nextBtn = new ButtonField("Next >>", ButtonField.CONSUME_CLICK);
        nextBtn.setChangeListener(new FieldChangeListener() {
            public void fieldChanged(Field field, int context) {
                mediaManager.next();
            }
        });
        row1.add(nextBtn);
        add(row1);
        
        ButtonField playPauseBtn = new ButtonField("Play / Pause", ButtonField.CONSUME_CLICK | ButtonField.FIELD_HCENTER);
        playPauseBtn.setChangeListener(new FieldChangeListener() {
            public void fieldChanged(Field field, int context) {
                mediaManager.play(); 
            }
        });
        add(playPauseBtn);
    }
}
