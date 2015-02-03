package com.atomic.ArduinoApp.Managers;

import android.app.PendingIntent;

import android.content.Context;
import android.content.Intent;

import android.hardware.usb.UsbAccessory;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;

import android.util.Log;

import android.widget.Toast;

import com.atomic.ArduinoApp.BaseActivity;
import com.atomic.ArduinoApp.R;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.HashMap;
import java.util.Iterator;

/**
 * ArduinoApp for SRS Copyright (C) 2015 - Atomic Development
 */
public class DeviceManager {

    public boolean foundDevice = false;
    private static final int ARDUINO_USB_VENDOR_ID = 0x2341;
    private static final int ARDUINO_UNO_USB_PRODUCT_ID = 0x01;
    private static final int ARDUINO_MEGA_2560_USB_PRODUCT_ID = 0x10;
    private static final int ARDUINO_MEGA_2560_R3_USB_PRODUCT_ID = 0x42;
    private static final int ARDUINO_UNO_R3_USB_PRODUCT_ID = 0x43;
    private static final int ARDUINO_MEGA_2560_ADK_R3_USB_PRODUCT_ID = 0x44;
    private static final int ARDUINO_MEGA_2560_ADK_USB_PRODUCT_ID = 0x3F;
    String TAG = "DeviceManager";
    private UsbDevice device = null;
    private boolean DEBUG = true;
    BaseActivity activity;
    public DeviceManager(BaseActivity activity) {
        this.activity = activity;
    }

    public void findDevice() {
        UsbManager usbManager = (UsbManager) activity.getSystemService(Context.USB_SERVICE);
        UsbDevice usbDevice = null;
        HashMap<String, UsbDevice> usbDeviceList = usbManager.getDeviceList();
        if (DEBUG) Log.d(TAG, "length: " + usbDeviceList.size());
        Iterator<UsbDevice> deviceIterator = usbDeviceList.values().iterator();
        if (deviceIterator.hasNext()) {
            UsbDevice tempUsbDevice = deviceIterator.next();

            // Print device information. If you think your device should be able
            // to communicate with this app, add it to accepted products below.
            if (DEBUG) Log.d(TAG, "VendorId: " + tempUsbDevice.getVendorId());
            if (DEBUG) Log.d(TAG, "ProductId: " + tempUsbDevice.getProductId());
            if (DEBUG) Log.d(TAG, "DeviceName: " + tempUsbDevice.getDeviceName());
            if (DEBUG) Log.d(TAG, "DeviceId: " + tempUsbDevice.getDeviceId());
            if (DEBUG) Log.d(TAG, "DeviceClass: " + tempUsbDevice.getDeviceClass());
            if (DEBUG) Log.d(TAG, "DeviceSubclass: " + tempUsbDevice.getDeviceSubclass());
            if (DEBUG) Log.d(TAG, "InterfaceCount: " + tempUsbDevice.getInterfaceCount());
            if (DEBUG) Log.d(TAG, "DeviceProtocol: " + tempUsbDevice.getDeviceProtocol());

            if (tempUsbDevice.getVendorId() == ARDUINO_USB_VENDOR_ID) {
                if (DEBUG) Log.i(TAG, "Arduino device found!");

                switch (tempUsbDevice.getProductId()) {
                    case ARDUINO_UNO_USB_PRODUCT_ID:
                        Toast.makeText(activity.getBaseContext(), "Arduino Uno " + activity.getString(R.string.found), Toast.LENGTH_SHORT).show();
                        usbDevice = tempUsbDevice;
                        break;
                    case ARDUINO_MEGA_2560_USB_PRODUCT_ID:
                        Toast.makeText(activity.getBaseContext(), "Arduino Mega 2560 " + activity.getString(R.string.found), Toast.LENGTH_SHORT).show();
                        usbDevice = tempUsbDevice;
                        break;
                    case ARDUINO_MEGA_2560_R3_USB_PRODUCT_ID:
                        Toast.makeText(activity.getBaseContext(), "Arduino Mega 2560 R3 " + activity.getString(R.string.found), Toast.LENGTH_SHORT).show();
                        usbDevice = tempUsbDevice;
                        break;
                    case ARDUINO_UNO_R3_USB_PRODUCT_ID:
                        Toast.makeText(activity.getBaseContext(), "Arduino Uno R3 " + activity.getString(R.string.found), Toast.LENGTH_SHORT).show();
                        usbDevice = tempUsbDevice;
                        break;
                    case ARDUINO_MEGA_2560_ADK_R3_USB_PRODUCT_ID:
                        Toast.makeText(activity.getBaseContext(), "Arduino Mega 2560 ADK R3 " + activity.getString(R.string.found), Toast.LENGTH_SHORT).show();
                        usbDevice = tempUsbDevice;
                        break;
                    case ARDUINO_MEGA_2560_ADK_USB_PRODUCT_ID:
                        Toast.makeText(activity.getBaseContext(), "Arduino Mega 2560 ADK " + activity.getString(R.string.found), Toast.LENGTH_SHORT).show();
                        usbDevice = tempUsbDevice;
                        break;
                }
            }
        }

        if (usbDevice == null) {
            if (DEBUG) Log.i(TAG, "No device found!");
            Toast.makeText(activity.getBaseContext(), activity.getString(R.string.no_device_found), Toast.LENGTH_LONG).show();
        } else {
            if (DEBUG) Log.i(TAG, "Device found!");
             Intent startIntent = new Intent(activity.getApplicationContext(), DeviceCommunicatorService.class);
             PendingIntent pendingIntent = PendingIntent.getService(activity.getApplicationContext(), 0, startIntent, 0);
             usbManager.requestPermission(usbDevice, pendingIntent);
        }
    }

    public UsbDevice getDevice() {
        return device;
    }

    public void sendData(UsbAccessory accessory, byte[] data) {
        UsbManager usbManager = (UsbManager) activity.getSystemService(Context.USB_SERVICE);
        FileDescriptor fd = usbManager.openAccessory(accessory).getFileDescriptor();
        FileOutputStream f = new FileOutputStream(fd);
        try {
            f.write(data);
        } catch (IOException e) { e.printStackTrace(); }

    }
}
