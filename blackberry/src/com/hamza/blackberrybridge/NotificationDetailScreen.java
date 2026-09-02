package com.hamza.blackberrybridge;

import net.rim.device.api.ui.*;
import net.rim.device.api.ui.component.*;
import net.rim.device.api.ui.container.*;
import net.rim.device.api.ui.decor.*;
import java.util.Vector;

public class NotificationDetailScreen extends MainScreen {
    private NotificationManager notifManager;
    private Notification notification;
    private BasicEditField replyField;
    private SmartBridgeApp app;
    
    public NotificationDetailScreen(NotificationManager nm, Notification notif, SmartBridgeApp app) {
        this.notifManager = nm;
        this.notification = notif;
        this.app = app;
        
        getMainManager().setBackground(BackgroundFactory.createSolidBackground(Color.BLACK));
        
        DarkLabelField appLabel = new DarkLabelField(notif.app, Field.FIELD_HCENTER, 0x00FF00);
        try { appLabel.setFont(Font.getDefault().derive(Font.BOLD, 22)); } catch(Exception e){}
        add(appLabel);
        
        DarkLabelField senderLabel = new DarkLabelField("From: " + notif.sender, Color.WHITE);
        add(senderLabel);
        add(new SeparatorField());
        
        RichTextField msgField = new RichTextField(notif.message, Field.NON_FOCUSABLE);
        msgField.setFont(Font.getDefault());
        add(msgField);
        
        add(new SeparatorField());
        
        replyField = new BasicEditField("Reply: ", "", 256, BasicEditField.DEFAULT_KEYBOARD_LAYOUT);
        add(replyField);
        
        HorizontalFieldManager actionsRow = new HorizontalFieldManager(Field.FIELD_HCENTER);
        DarkButtonField btnSend = new DarkButtonField("Send", 100, 40);
        btnSend.setChangeListener(new FieldChangeListener() {
            public void fieldChanged(Field field, int context) { sendReply(); }
        });
        DarkButtonField btnDismiss = new DarkButtonField("Dismiss", 100, 40);
        btnDismiss.setChangeListener(new FieldChangeListener() {
            public void fieldChanged(Field field, int context) {
                app.getConnectionManager().sendData("NOTIFICATION_ACTION|" + notification.id + "|DISMISS\n");
                close();
            }
        });
        DarkButtonField btnOpen = new DarkButtonField("Open", 100, 40);
        btnOpen.setChangeListener(new FieldChangeListener() {
            public void fieldChanged(Field field, int context) {
                app.getConnectionManager().sendData("NOTIFICATION_ACTION|" + notification.id + "|OPEN\n");
                close();
            }
        });
        actionsRow.add(btnSend); actionsRow.add(btnDismiss); actionsRow.add(btnOpen);
        add(actionsRow);
        
        add(new SeparatorField());
        DarkLabelField qrTitle = new DarkLabelField("Quick Replies:", 0x0078D7);
        add(qrTitle);
        
        Vector qr = app.getSettingsManager().getQuickReplies();
        VerticalFieldManager vfmQr = new VerticalFieldManager(Field.FIELD_HCENTER);
        for (int i = 0; i < qr.size(); i++) {
            final String replyText = (String) qr.elementAt(i);
            if (replyText != null && replyText.trim().length() > 0) {
                DarkButtonField btn = new DarkButtonField(replyText, 250, 40);
                btn.setChangeListener(new FieldChangeListener() {
                    public void fieldChanged(Field field, int context) {
                        notifManager.replyToNotification(notification.id, replyText);
                        close();
                    }
                });
                vfmQr.add(btn);
            }
        }
        add(vfmQr);
        
        // Auto Mark Read
        app.getConnectionManager().sendData("NOTIFICATION_ACTION|" + notification.id + "|MARK_READ\n");
    }
    
    private void sendReply() {
        String text = replyField.getText();
        if (text != null && text.length() > 0) {
            notifManager.replyToNotification(notification.id, text);
            close();
        }
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
