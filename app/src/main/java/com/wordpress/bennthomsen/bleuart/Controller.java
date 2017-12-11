package com.wordpress.bennthomsen.bleuart;


import android.app.Activity;
import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Button;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.util.Date;

public class Controller extends Application {

    public static final int REQUEST_SELECT_DEVICE = 1;
    public static final int REQUEST_ENABLE_BT = 2;
    private static final int UART_PROFILE_READY = 10;
    public static final String TAG = "nRFUART";
    private static final int UART_PROFILE_CONNECTED = 20;
    private static final int UART_PROFILE_DISCONNECTED = 21;
    private static final int STATE_OFF = 10;
    private static int mState = UART_PROFILE_DISCONNECTED;
    private static BluetoothDevice mDevice = null;
    private static BluetoothAdapter mBtAdapter = null;
    private static UartService mService ;
    private static String dane;


    protected static UartService getmService() {
        return mService;
    }
    protected static void setmService(UartService mService) {
        Controller.mService = mService;
    }
    protected static void setDane(String dane) {
        Controller.dane = dane;
    }
    protected static String getDane() {
        return dane;
    }
    protected static BluetoothAdapter getmBtAdapter() {
        return mBtAdapter;
    }
    protected static void setmBtAdapter(BluetoothAdapter mBtAdapter) {
        Controller.mBtAdapter = mBtAdapter;
    }
    protected static void setmDevice(BluetoothDevice mDevice) {
        Controller.mDevice = mDevice;
    }
    protected static BluetoothDevice getmDevice() {
        return mDevice;
    }
    protected static int getmState() {
        return mState;
    }
    protected static void setmState(int mState) {
        Controller.mState = mState;
    }
    protected static int getUartProfileConnected() {
        return UART_PROFILE_CONNECTED;
    }

    public static ServiceConnection mServiceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder rawBinder) {
            mService = (((UartService.LocalBinder) rawBinder).getService());
            Log.d(TAG, "onServiceConnected mService= " + mService);
            if (!mService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                //finish();
            }
        }

        public void onServiceDisconnected(ComponentName classname) {
            mService.disconnect();
            mService = null;
        }
    };

    private void finish() {
    }

    public static final BroadcastReceiver UARTStatusChangeReceiver = new BroadcastReceiver() {

        public void onReceive(final Context context, Intent intent) {
            String action = intent.getAction();

            final Intent mIntent = intent;

            if (action.equals(UartService.ACTION_GATT_CONNECTED)) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
                        Log.d(TAG, "UART_CONNECT_MSG");
                        mState = UART_PROFILE_CONNECTED;
                    }
                });
            }

            if (action.equals(UartService.ACTION_GATT_DISCONNECTED)) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
                        Log.d(TAG, "UART_DISCONNECT_MSG");

                        mState = UART_PROFILE_DISCONNECTED;
                        mService.close();
                        //setUiState();
                    }
                });
            }

            if (action.equals(UartService.ACTION_GATT_SERVICES_DISCOVERED)) {
                mService.enableTXNotification();
            }

            if (action.equals(UartService.ACTION_DATA_AVAILABLE)) {


                final byte[] txValue = intent.getByteArrayExtra(UartService.EXTRA_DATA);
                try {
                    String text = new String(txValue, "UTF-8");
                    dane = text;
                    Log.d(TAG,"DANEEEEE: " +text);

                    if(text.equals("daw")){
                        Intent intent2 = new Intent(context,Main2Activity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent2);
                    }











                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    public void run() {
                        try {
                            String text = new String(txValue, "UTF-8");
                            setDane(text);
                            Log.d(TAG,"DANEEEEE: " +text);
                            String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
                        } catch (Exception e) {
                            Log.e(TAG, e.toString());
                        }
                    }
                });
            }

            if (action.equals(UartService.DEVICE_DOES_NOT_SUPPORT_UART)){
               // showMessage("Device doesn't support UART. Disconnecting");
                Log.d(TAG,"Device doesn't support UART. Disconnecting");
                getmService().disconnect();
            }
        }
    };

    private static void runOnUiThread(Runnable action) {

    }

    public void service_init() {
        Intent bindIntent = new Intent(Controller.this, UartService.class);
        bindService(bindIntent, Controller.mServiceConnection, Context.BIND_AUTO_CREATE);
        LocalBroadcastManager.getInstance(this).registerReceiver(UARTStatusChangeReceiver, makeGattUpdateIntentFilter());
    }

    public static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(UartService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(UartService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(UartService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(UartService.ACTION_DATA_AVAILABLE);
        intentFilter.addAction(UartService.DEVICE_DOES_NOT_SUPPORT_UART);
        return intentFilter;
    }

}
