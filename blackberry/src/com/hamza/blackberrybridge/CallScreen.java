package com.hamza.blackberrybridge;

import net.rim.device.api.ui.*;
import net.rim.device.api.ui.component.*;
import net.rim.device.api.ui.container.*;
import net.rim.device.api.ui.decor.*;
import net.rim.device.api.system.Application;

public class CallScreen extends MainScreen {
    private CallManager callManager;
    private String callId;
    private DarkLabelField statusLabel;
    
    public CallScreen(CallManager cm, String id, String name, String number) {
        super(MainScreen.NO_VERTICAL_SCROLL | MainScreen.NO_HORIZONTAL_SCROLL);
        this.callManager = cm;
        this.callId = id;
        
        getMainManager().setBackground(BackgroundFactory.createSolidBackground(Color.BLACK));
        
        VerticalFieldManager vfm = new VerticalFieldManager(Field.FIELD_HCENTER | Field.FIELD_VCENTER);
        vfm.setPadding(20, 0, 0, 0);
        
        DarkLabelField title = new DarkLabelField("Incoming Call", Field.FIELD_HCENTER, 0x00FF00);
        try { title.setFont(Font.getDefault().derive(Font.BOLD, 30)); } catch(Exception e){}
        
        DarkLabelField nameLabel = new DarkLabelField(name, Field.FIELD_HCENTER, Color.WHITE);
        try { nameLabel.setFont(Font.getDefault().derive(Font.BOLD, 40)); } catch(Exception e){}
        
        DarkLabelField numberLabel = new DarkLabelField(number, Field.FIELD_HCENTER, 0xAAAAAA);
        
        statusLabel = new DarkLabelField("Status: Ringing...", Field.FIELD_HCENTER, 0xFFCC00);
        
        vfm.add(title);
        vfm.add(new SeparatorField());
        vfm.add(nameLabel);
        vfm.add(numberLabel);
        vfm.add(new SeparatorField());
        vfm.add(statusLabel);
        add(vfm);
        
        HorizontalFieldManager hfm = new HorizontalFieldManager(Field.FIELD_HCENTER);
        hfm.setPadding(40, 0, 0, 0);
        
        DarkButtonField btnAnswer = new DarkButtonField("Answer", 150, 60);
        btnAnswer.setChangeListener(new FieldChangeListener() {
            public void fieldChanged(Field field, int context) { answer(); }
        });
        
        DarkButtonField btnReject = new DarkButtonField("Reject", 150, 60);
        btnReject.setChangeListener(new FieldChangeListener() {
            public void fieldChanged(Field field, int context) { reject(); }
        });
        
        hfm.add(btnAnswer);
        hfm.add(btnReject);
        add(hfm);
    }
    
    public void setStatus(final String status) {
        Application.getApplication().invokeLater(new Runnable() {
            public void run() {
                statusLabel.setText("Status: " + status);
                if (status.equals("Active")) {
                    statusLabel.setColor(0x00FF00); // Green
                }
            }
        });
    }
    
    private void answer() {
        setStatus("Active");
        callManager.answerCall(callId);
        HardwareManager.stopAlerts();
    }
    
    private void reject() {
        callManager.rejectCall(callId);
        HardwareManager.stopAlerts();
        close();
    }
    
    protected boolean keyDown(int keycode, int time) {
        int key = Keypad.key(keycode);
        if (key == Keypad.KEY_SEND) { // Green key
            answer();
            return true;
        } else if (key == Keypad.KEY_END) { // Red key
            reject();
            return true;
        }
        return super.keyDown(keycode, time);
    }
}
