package com.hamza.blackberrybridge;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.container.MainScreen;
import java.util.Vector;

public class NotificationListScreen extends MainScreen {
    private NotificationManager notifManager;
    private SmartBridgeApp app;
    
    public NotificationListScreen(NotificationManager nm, SmartBridgeApp app) {
        this.notifManager = nm;
        this.app = app;
        setTitle("Notifications");
        refreshList();
    }
    
    public void refreshList() {
        deleteAll();
        Vector notifs = notifManager.getNotifications();
        if (notifs.isEmpty()) {
            add(new net.rim.device.api.ui.component.LabelField("No notifications."));
            return;
        }
        
        for (int i = 0; i < notifs.size(); i++) {
            final Notification n = (Notification) notifs.elementAt(i);
            ButtonField btn = new ButtonField(n.app + ": " + n.sender, ButtonField.CONSUME_CLICK);
            btn.setChangeListener(new FieldChangeListener() {
                public void fieldChanged(Field field, int context) {
                    notifManager.openNotification(n);
                }
            });
            add(btn);
        }
    }
}
