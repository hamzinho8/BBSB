package com.hamza.blackberrybridge;

import net.rim.device.api.ui.*;
import net.rim.device.api.ui.component.*;
import net.rim.device.api.ui.container.*;
import net.rim.device.api.ui.decor.*;
import java.util.Vector;

public class ContactListScreen extends MainScreen {
    private ContactManager contactManager;
    private VerticalFieldManager listContainer;
    
    public ContactListScreen(ContactManager manager) {
        this.contactManager = manager;
        getMainManager().setBackground(BackgroundFactory.createSolidBackground(Color.BLACK));
        
        DarkLabelField title = new DarkLabelField("CONTACTS", Field.FIELD_HCENTER, 0x0078D7);
        try { title.setFont(Font.getDefault().derive(Font.BOLD, 24)); } catch(Exception e){}
        add(title);
        add(new SeparatorField());
        
        DarkButtonField syncBtn = new DarkButtonField("Sync from Android", 200, 50);
        syncBtn.setChangeListener(new FieldChangeListener() {
            public void fieldChanged(Field field, int context) {
                contactManager.requestContacts();
                close();
            }
        });
        
        HorizontalFieldManager hfm = new HorizontalFieldManager(Field.FIELD_HCENTER);
        hfm.add(syncBtn);
        add(hfm);
        
        add(new SeparatorField());
        
        listContainer = new VerticalFieldManager(Manager.VERTICAL_SCROLL);
        add(listContainer);
        
        Vector contacts = contactManager.getContacts();
        if (contacts.isEmpty()) {
            listContainer.add(new DarkLabelField("No contacts. Click Sync.", Field.FIELD_HCENTER, 0xAAAAAA));
            return;
        }
        
        for (int i = 0; i < contacts.size(); i++) {
            final Contact c = (Contact) contacts.elementAt(i);
            DarkButtonField btn = new DarkButtonField(c.name, 300, 40);
            btn.setChangeListener(new FieldChangeListener() {
                public void fieldChanged(Field field, int context) { confirmCall(c); }
            });
            listContainer.add(btn);
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
    
    protected boolean keyDown(int keycode, int time) {
        int key = Keypad.key(keycode);
        if (key == Keypad.KEY_END || key == Keypad.KEY_ESCAPE) { 
            close();
            return true;
        }
        return super.keyDown(keycode, time);
    }
}
