package com.hamza.blackberrybridge;

import java.util.Vector;

public class ContactManager {
    private SmartBridgeApp app;
    private Vector contacts;
    
    public ContactManager(SmartBridgeApp app) {
        this.app = app;
        this.contacts = new Vector();
    }
    
    public void requestContacts() {
        contacts.removeAllElements();
        app.getConnectionManager().sendData("CONTACTS_REQUEST\n");
        app.getUIManager().getMainScreen().addLog("[CONTACTS] Sync requested...");
    }
    
    public void handleContact(String id, String name, String number) {
        // Prevent duplicates in current session
        for (int i = 0; i < contacts.size(); i++) {
            Contact c = (Contact) contacts.elementAt(i);
            if (c.id.equals(id)) {
                return;
            }
        }
        contacts.addElement(new Contact(id, name, number));
    }
    
    public Vector getContacts() {
        return contacts;
    }
    
    public void callContact(String number) {
        app.getCallManager().initiateCall(number);
    }
}
