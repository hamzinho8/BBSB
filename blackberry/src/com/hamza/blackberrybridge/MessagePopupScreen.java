package com.hamza.blackberrybridge;

import net.rim.device.api.ui.*;
import net.rim.device.api.ui.component.*;
import net.rim.device.api.ui.container.*;
import net.rim.device.api.system.Display;

public class MessagePopupScreen extends PopupScreen {
    
    public MessagePopupScreen(String sender, String body) {
        super(new VerticalFieldManager(Manager.VERTICAL_SCROLL | Manager.VERTICAL_SCROLLBAR));
        
        DarkLabelField title = new DarkLabelField("\uD83D\uDCAC Nouveau Message de " + sender, Field.FIELD_HCENTER, 0x00A2E8); // Blue Title
        try { title.setFont(Font.getDefault().derive(Font.BOLD, 18)); } catch(Exception e){}
        
        add(title);
        add(new SeparatorField());
        
        ActiveRichTextField bodyField = new ActiveRichTextField(body, Field.FIELD_HCENTER | Field.FOCUSABLE);
        try { bodyField.setFont(Font.getDefault().derive(Font.PLAIN, 16)); } catch(Exception e){}
        
        VerticalFieldManager bodyContainer = new VerticalFieldManager(Field.FIELD_HCENTER);
        bodyContainer.setPadding(10, 10, 10, 10);
        bodyContainer.add(bodyField);
        
        add(bodyContainer);
        add(new SeparatorField());
        
        ButtonField btnClose = new ButtonField("Fermer", ButtonField.CONSUME_CLICK | Field.FIELD_HCENTER);
        btnClose.setChangeListener(new FieldChangeListener() {
            public void fieldChanged(Field field, int context) {
                HardwareManager.stopMessageAlert();
                close();
            }
        });
        
        HorizontalFieldManager btnContainer = new HorizontalFieldManager(Field.FIELD_HCENTER);
        btnContainer.setPadding(10, 0, 5, 0);
        btnContainer.add(btnClose);
        
        add(btnContainer);
    }
    
    protected void sublayout(int width, int height) {
        int displayWidth = Display.getWidth();
        int displayHeight = Display.getHeight();
        int popupWidth = (int)(displayWidth * 0.90);
        int popupHeight = Math.min(super.getPreferredHeight(), (int)(displayHeight * 0.80));
        
        super.sublayout(popupWidth, popupHeight);
        setExtent(popupWidth, popupHeight);
    }
}
