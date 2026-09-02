package com.hamza.blackberrybridge;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.component.BasicEditField;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.RichTextField;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.MainScreen;
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
        
        setTitle(notif.app);
        
        add(new LabelField("From: " + notif.sender));
        add(new SeparatorField());
        add(new RichTextField(notif.message));
        add(new SeparatorField());
        
        replyField = new BasicEditField("Reply: ", "", 256, BasicEditField.DEFAULT_KEYBOARD_LAYOUT);
        add(replyField);
        
        ButtonField btnSend = new ButtonField("Send Reply", ButtonField.CONSUME_CLICK | ButtonField.FIELD_HCENTER);
        btnSend.setChangeListener(new FieldChangeListener() {
            public void fieldChanged(Field field, int context) {
                sendReply();
            }
        });
        add(btnSend);
        
        add(new SeparatorField());
        add(new LabelField("Quick Replies:"));
        
        // Add Dynamic Quick Replies
        Vector qr = app.getSettingsManager().getQuickReplies();
        for (int i = 0; i < qr.size(); i++) {
            final String replyText = (String) qr.elementAt(i);
            if (replyText != null && replyText.trim().length() > 0) {
                ButtonField btn = new ButtonField(replyText, ButtonField.CONSUME_CLICK);
                btn.setChangeListener(new FieldChangeListener() {
                    public void fieldChanged(Field field, int context) {
                        notifManager.replyToNotification(notification.id, replyText);
                        close();
                    }
                });
                add(btn);
            }
        }
    }
    
    private void sendReply() {
        String text = replyField.getText();
        if (text != null && text.length() > 0) {
            notifManager.replyToNotification(notification.id, text);
            close();
        }
    }
}
