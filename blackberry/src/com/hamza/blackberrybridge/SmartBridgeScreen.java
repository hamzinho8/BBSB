package com.hamza.blackberrybridge;

import net.rim.device.api.ui.*;
import net.rim.device.api.ui.component.*;
import net.rim.device.api.ui.container.*;
import net.rim.device.api.ui.decor.*;
import net.rim.device.api.system.*;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class SmartBridgeScreen extends MainScreen {
    private DarkLabelField clockLabel;
    private DarkLabelField dateLabel;
    private DarkLabelField btStatusLabel;
    private DarkLabelField batteryLabel;
    private DarkLabelField weatherLabel;
    private DarkButtonField btnNotifs;
    private SmartBridgeApp app;
    private Timer uiTimer;
    
    public SmartBridgeScreen(SmartBridgeApp application) {
        super(MainScreen.NO_VERTICAL_SCROLL | MainScreen.NO_HORIZONTAL_SCROLL);
        this.app = application;
        
        getMainManager().setBackground(BackgroundFactory.createSolidBackground(Color.BLACK));
        
        VerticalFieldManager header = new VerticalFieldManager(Field.FIELD_HCENTER);
        header.setPadding(10, 0, 5, 0); 
        
        boolean isNight = app.getSettingsManager().isNightMode();
        int clockColor = isNight ? 0x555555 : Color.WHITE;
        
        clockLabel = new DarkLabelField("--:--", Field.FIELD_HCENTER, clockColor);
        try {
            clockLabel.setFont(Font.getDefault().derive(Font.BOLD, 70));
        } catch (Exception e) {}
        
        dateLabel = new DarkLabelField("---", Field.FIELD_HCENTER, 0xAAAAAA);
        try { dateLabel.setFont(Font.getDefault().derive(Font.PLAIN, 20)); } catch(Exception e){}
        
        weatherLabel = new DarkLabelField("", Field.FIELD_HCENTER, 0x0078D7); // Blueish for weather
        
        header.add(clockLabel);
        header.add(dateLabel);
        header.add(weatherLabel);
        
        HorizontalFieldManager statusContainer = new HorizontalFieldManager(Field.FIELD_HCENTER);
        statusContainer.setPadding(5, 0, 10, 0);
        
        btStatusLabel = new DarkLabelField("[BT: WAIT] ", 0xFF0000); 
        batteryLabel = new DarkLabelField("[AND: --%] [BB: --%]", 0xAAAAAA);
        
        try {
            Font smallFont = Font.getDefault().derive(Font.PLAIN, 14);
            btStatusLabel.setFont(smallFont);
            batteryLabel.setFont(smallFont);
        } catch (Exception e) {}
        
        statusContainer.add(btStatusLabel);
        statusContainer.add(batteryLabel);
        header.add(statusContainer);
        
        add(header);
        
        // --- 3x3 LAUNCHER GRID ---
        VerticalFieldManager grid = new VerticalFieldManager(Field.FIELD_HCENTER);
        
        int btnW = 145;
        int btnH = 40; // Smaller height to fit everything with weather label
        
        HorizontalFieldManager row1 = new HorizontalFieldManager(Field.FIELD_HCENTER);
        DarkButtonField btnCalls = new DarkButtonField("Calls", btnW, btnH);
        btnCalls.setChangeListener(new FieldChangeListener() {
            public void fieldChanged(Field field, int context) { app.getUIManager().openContacts(); }
        });
        
        DarkButtonField btnMessages = new DarkButtonField("Messages", btnW, btnH);
        btnMessages.setChangeListener(new FieldChangeListener() {
            public void fieldChanged(Field field, int context) { app.getConnectionManager().sendData("OPEN_APP|Messages\n"); }
        });
        
        btnNotifs = new DarkButtonField("Notifs (0)", btnW, btnH);
        btnNotifs.setChangeListener(new FieldChangeListener() {
            public void fieldChanged(Field field, int context) { app.getUIManager().openNotificationList(); }
        });
        
        row1.add(btnCalls);
        row1.add(btnMessages);
        row1.add(btnNotifs);
        grid.add(row1);
        
        HorizontalFieldManager row2 = new HorizontalFieldManager(Field.FIELD_HCENTER);
        DarkButtonField btnWA = new DarkButtonField("WhatsApp", btnW, btnH);
        btnWA.setChangeListener(new FieldChangeListener() {
            public void fieldChanged(Field field, int context) { app.getConnectionManager().sendData("OPEN_APP|WhatsApp\n"); }
        });

        DarkButtonField btnFB = new DarkButtonField("Messenger", btnW, btnH);
        btnFB.setChangeListener(new FieldChangeListener() {
            public void fieldChanged(Field field, int context) { app.getConnectionManager().sendData("OPEN_APP|Messenger\n"); }
        });
        
        DarkButtonField btnTG = new DarkButtonField("Telegram", btnW, btnH);
        btnTG.setChangeListener(new FieldChangeListener() {
            public void fieldChanged(Field field, int context) { app.getConnectionManager().sendData("OPEN_APP|Telegram\n"); }
        });
        
        row2.add(btnWA);
        row2.add(btnFB);
        row2.add(btnTG);
        grid.add(row2);
        
        HorizontalFieldManager row3 = new HorizontalFieldManager(Field.FIELD_HCENTER);
        DarkButtonField btnContacts = new DarkButtonField("Contacts", btnW, btnH);
        btnContacts.setChangeListener(new FieldChangeListener() {
            public void fieldChanged(Field field, int context) { app.getUIManager().openContacts(); }
        });

        DarkButtonField btnMusic = new DarkButtonField("Music", btnW, btnH);
        btnMusic.setChangeListener(new FieldChangeListener() {
            public void fieldChanged(Field field, int context) { app.getUIManager().openMedia(); }
        });
        
        DarkButtonField btnSettings = new DarkButtonField("Settings", btnW, btnH);
        btnSettings.setChangeListener(new FieldChangeListener() {
            public void fieldChanged(Field field, int context) { app.getUIManager().openSettings(); }
        });
        
        row3.add(btnContacts);
        row3.add(btnMusic);
        row3.add(btnSettings);
        grid.add(row3);
        
        add(grid);
        
        updateTimeAndBBBattery();
        
        uiTimer = new Timer();
        uiTimer.schedule(new TimerTask() {
            public void run() {
                Application.getApplication().invokeLater(new Runnable() {
                    public void run() { updateTimeAndBBBattery(); }
                });
            }
        }, 0, 10000); 
    }
    
    private void updateTimeAndBBBattery() {
        Calendar cal = Calendar.getInstance();
        int h = cal.get(Calendar.HOUR_OF_DAY);
        int m = cal.get(Calendar.MINUTE);
        String time = (h < 10 ? "0" + h : h) + ":" + (m < 10 ? "0" + m : m);
        clockLabel.setText(time);
        
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);
        dateLabel.setText((day < 10 ? "0"+day : day) + "/" + (month < 10 ? "0"+month : month) + "/" + year);
        
        String currentBat = batteryLabel.getText();
        int andIndex = currentBat.indexOf("[AND: ");
        String andBat = "--%";
        if (andIndex != -1) {
            int end = currentBat.indexOf("]", andIndex);
            if (end != -1) andBat = currentBat.substring(andIndex + 6, end);
        }
        int bbBat = DeviceInfo.getBatteryLevel();
        batteryLabel.setText("[AND: " + andBat + "] [BB: " + bbBat + "%]");
        
        // Refresh Night Mode color if changed
        boolean isNight = app.getSettingsManager().isNightMode();
        clockLabel.setColor(isNight ? 0x555555 : Color.WHITE);
    }
    
    public void updateConnectionStatus(String status) {
        if (status.equals("CONNECTED")) {
            btStatusLabel.setText("[BT: ON] ");
            btStatusLabel.setColor(0x00FF00); 
        } else {
            btStatusLabel.setText("[BT: WAIT] ");
            btStatusLabel.setColor(0xFF0000); 
        }
    }
    
    public void updateWeather(String temp, String cond) {
        weatherLabel.setText(temp + " - " + cond);
    }

    public void updateBattery(String level) {
        String currentBat = batteryLabel.getText();
        int bbIndex = currentBat.indexOf("[BB: ");
        String bbBat = "--%";
        if (bbIndex != -1) {
            int end = currentBat.indexOf("]", bbIndex);
            if (end != -1) bbBat = currentBat.substring(bbIndex + 5, end);
        }
        batteryLabel.setText("[AND: " + level + "%] [BB: " + bbBat + "]");
    }
    
    public void updateNotificationCount(int count) {
        btnNotifs.setText("Notifs (" + count + ")");
    }
    
    public void addLog(String log) {
    }
    
    protected boolean keyDown(int keycode, int time) {
        int key = Keypad.key(keycode);
        if (key == Keypad.KEY_END) { 
            // Instead of System.exit(0), request background mode to keep receiving BT events!
            Application.getApplication().requestBackground();
            return true;
        }
        return super.keyDown(keycode, time);
    }
}
