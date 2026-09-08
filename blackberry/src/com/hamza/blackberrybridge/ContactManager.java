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
        app.getConnectionManager().sendData("CONTACTS_REQUEST\n");
    }
    
    public void handleContact(String id, String name, String number) {
        contacts.addElement(new Contact(id, name, number));
    }
    
    public void callContact(String number) {
        if (app.getConnectionManager() != null) {
            app.getConnectionManager().sendData("CALL:" + number + "\n");
        }
    }
    
    public Vector getContacts() {
        return contacts;
    }
}
