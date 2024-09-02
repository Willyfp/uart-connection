package com.uartconnection;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;

import android.widget.Toast;
import java.util.List;
import com.facebook.react.bridge.ReadableMap;

import com.facebook.react.modules.core.DeviceEventManagerModule; // Import the correct class
import tp.xmaihh.serialport.SerialHelper;
import android_serialport_api.SerialPortFinder;
import tp.xmaihh.serialport.bean.ComBean;

public class UartConnectionModule extends ReactContextBaseJavaModule {

    private final SerialPortFinder serialPortFinder;

    private SerialHelper serialHelper;

    public UartConnectionModule(ReactApplicationContext reactContext) {
        super(reactContext);
        serialPortFinder = new SerialPortFinder();
    }

    @Override
    public String getName() {
        return NAME;
    }

    @com.facebook.react.bridge.ReactMethod
    public void init(ReadableMap config, Promise promise) {
      try{
        serialHelper = new SerialHelper("dev/" + config.getString("sPort"), Integer.parseInt(config.getString("baudRate"))) {
          @Override
          protected void onDataReceived(final ComBean comBean) {
            new Thread(new Runnable() {
              @Override
              public void run() {
                try {
                  String data = new String(comBean.bRec, "UTF-8");
                  getReactApplicationContext()
                    .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                    .emit("onDataReceived", data);
                } catch (Exception e) {
                  e.printStackTrace();
                }
              }
            }).start();
          }
        };

        serialHelper.open();

        promise.resolve("success");
      } catch(Exception e){
        promise.reject(e.getMessage());
      }
    }

    @com.facebook.react.bridge.ReactMethod
    public void send(String data, Promise promise) {
      try{
        serialHelper.sendTxt(data);
        promise.resolve("success");
      } catch(Exception e){
        promise.reject(e.getMessage());
      }
    }

    @com.facebook.react.bridge.ReactMethod
    public void listDevices(Promise promise) {
        String[] devices = serialPortFinder.getAllDevices();
        StringBuilder text = new StringBuilder();

        for (String device : devices) {
            text.append(device).append("\n");
        }

        promise.resolve(text.toString());
    }

    // Example method
    // See https://reactnative.dev/docs/native-modules-android
    @com.facebook.react.bridge.ReactMethod
    public void multiply(double a, double b, Promise promise) {
        promise.resolve(a * b);
    }

    public static final String NAME = "UartConnection";
}
