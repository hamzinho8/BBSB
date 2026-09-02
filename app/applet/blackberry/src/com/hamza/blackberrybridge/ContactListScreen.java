package com.hamza.blackberrybridge;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.container.MainScreen;
import java.util.Vector;

public class ContactListScreen extends MainScreen {
    private ContactManager contactManager;
    
    public ContactListScreen(ContactManager manager) {
        this.contactManager = manager;
        setTitle("Contacts");
        
        ButtonField syncBtn = new ButtonField("Refresh Contacts (Sync)", ButtonField.CONSUME_CLICK | ButtonField.FIELD_HCENTER);
        syncBtn.setChangeListener(new FieldChangeListener() {
            public void fieldChanged(Field field, int context) {
                contactManager.requestContacts();
                close();
            }
        });
        add(syncBtn);
        add(new net.rim.device.api.ui.component.SeparatorField());
        
        Vector contacts = contactManager.getContacts();
        if (contacts.isEmpty()) {
            add(new net.rim.device.api.ui.component.LabelField("No contacts. Click Sync."));
            return;
        }
        
        for (int i = 0; i < contacts.size(); i++) {
            final Contact c = (Contact) contacts.elementAt(i);
            ButtonField btn = new ButtonField(c.name + " - " + c.number, ButtonField.CONSUME_CLICK);
            btn.setChangeListener(new FieldChangeListener() {
                public void fieldChanged(Field field, int context) {
                    confirmCall(c);
                }
            });
            add(btn);
        }
    }
    
    private void confirmCall(final Contact c) {
        net.rim.device.api.system.Application.getApplication().invokeLater(new Runnable() {
            public void run() {
                int response = Dialog.ask(Dialog.D_YES_NO, "Call " + c.name + "?");
                if (response == Dialog.YES) {
                    contactManager.callContact(c.number);
                    close();
                }
            }
        });
    }
}
