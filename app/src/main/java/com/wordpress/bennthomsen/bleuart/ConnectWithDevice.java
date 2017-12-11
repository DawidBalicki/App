package com.wordpress.bennthomsen.bleuart;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Parcelable;
import android.provider.SyncStateContract;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.util.Date;

import com.wordpress.bennthomsen.bleuart.UartService;

import static com.wordpress.bennthomsen.bleuart.MainActivity.TAG;

public class ConnectWithDevice extends Activity {
    public static final int REQUEST_SELECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    private static final int UART_PROFILE_READY = 10;
    public static final String TAG = "nRFUART";
    private static final int UART_PROFILE_CONNECTED = 20;
    private static final int UART_PROFILE_DISCONNECTED = 21;
    private static final int STATE_OFF = 10;
    //ADDED
    private static String flag_2 = "2c";
    private static String flag_3 = "3c";


    private int mState = UART_PROFILE_DISCONNECTED;
    //public UartService mService = null;
    //public UartService mService = G.getmService();
    public G mService;
    public G dane;
    private BluetoothDevice mDevice = null;
    private BluetoothAdapter mBtAdapter = null;


    private Button btnConnectDisconnect;
    private  TextView messageAboutNetwork;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_with_device);

       messageAboutNetwork = (TextView) findViewById(R.id.messageAboutNetwork);


        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBtAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }



        btnConnectDisconnect=(Button) findViewById(R.id.connectWithNetworkButton);



        service_init();



        // Handler Disconnect & Connect button
        btnConnectDisconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mBtAdapter.isEnabled()) {
                    Log.i(TAG, "onClick - BT not enabled yet");
                    Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
                }
                else {
                    if (btnConnectDisconnect.getText().equals("Connect with network")){

                        //Connect button pressed, open DeviceListActivity class, with popup windows that scan for devices

                       // Intent newIntent = new Intent(ConnectWithDevice.this, DeviceListActivity.class);
                       // startActivityForResult(newIntent, REQUEST_SELECT_DEVICE);


                        try {
                            Log.d(TAG, "wszedlem do try");
                            String deviceAddress = "C9:7B:CF:C5:A7:42";
                            Log.d(TAG, "jestem przed mDevice");

                            mDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(deviceAddress);


                            mService.getmService().connect("C9:7B:CF:C5:A7:42");







                                messageAboutNetwork.setText("dziala connect");
                                Intent newIntent1 = new Intent(ConnectWithDevice.this, LoginActivity.class);

                                startActivity(newIntent1);




                        } catch ( Exception ee) {
                            Log.d(TAG, "wszedlem do catch1");

                        }


                        //-------------------------------------------------------------------------------------------CHANGED
                        // mService.connect(deviceAddress);



                    } else {
                        //Disconnect button pressed
                        if (mDevice!=null)
                        {
                            mService.getmService().disconnect();

                        }
                    }
                }
            }
        });
        // Handler Send button


    }

    //UART service connected/disconnected
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder rawBinder) {
            //mService = ((UartService.LocalBinder) rawBinder).getService();
            mService.setmService(((UartService.LocalBinder) rawBinder).getService());
            Log.d(TAG, "onServiceConnected mService= " + mService);
            if (!mService.getmService().initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                finish();
            }

        }

        public void onServiceDisconnected(ComponentName classname) {
            ////     mService.disconnect(mDevice);
            mService = null;
        }
    };

    private Handler mHandler = new Handler() {
        @Override

        //Handler events that received from UART service
        public void handleMessage(Message msg) {

        }
    };

    private final BroadcastReceiver UARTStatusChangeReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            final Intent mIntent = intent;
            //*********************//
            if (action.equals(UartService.ACTION_GATT_CONNECTED)) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
                        Log.d(TAG, "UART_CONNECT_MSG");
                        btnConnectDisconnect.setText("Disconnect");



                        mState = UART_PROFILE_CONNECTED;
                    }
                });
            }

            //*********************//
            if (action.equals(UartService.ACTION_GATT_DISCONNECTED)) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
                        Log.d(TAG, "UART_DISCONNECT_MSG");
                        btnConnectDisconnect.setText("Connect");

                       // ((TextView) findViewById(R.id.deviceName)).setText("Not Connected");

                        mState = UART_PROFILE_DISCONNECTED;
                        mService.getmService().close();
                        //setUiState();

                    }
                });
            }


            //*********************//
            if (action.equals(UartService.ACTION_GATT_SERVICES_DISCOVERED)) {
                mService.getmService().enableTXNotification();
            }
            //*********************//
            if (action.equals(UartService.ACTION_DATA_AVAILABLE)) {

                final byte[] txValue = intent.getByteArrayExtra(UartService.EXTRA_DATA);

                runOnUiThread(new Runnable() {
                    public void run() {
                        try {
                            String text = new String(txValue, "UTF-8");
                            mService.setDane(text);
                            Log.d(TAG,"DANEEEEE: " +text);
                            String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());

                        } catch (Exception e) {
                            Log.e(TAG, e.toString());
                        }
                    }
                });
            }
            //*********************//
            if (action.equals(UartService.DEVICE_DOES_NOT_SUPPORT_UART)){
                showMessage("Device doesn't support UART. Disconnecting");
                mService.getmService().disconnect();
            }


        }
    };

    private void service_init() {
        Intent bindIntent = new Intent(this, UartService.class);
        bindService(bindIntent, mServiceConnection, Context.BIND_AUTO_CREATE);

        LocalBroadcastManager.getInstance(this).registerReceiver(UARTStatusChangeReceiver, makeGattUpdateIntentFilter());
    }
    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(UartService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(UartService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(UartService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(UartService.ACTION_DATA_AVAILABLE);
        intentFilter.addAction(UartService.DEVICE_DOES_NOT_SUPPORT_UART);
        return intentFilter;
    }
    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");

        try {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(UARTStatusChangeReceiver);
        } catch (Exception ignore) {
            Log.e(TAG, ignore.toString());
        }
        unbindService(mServiceConnection);
        mService.getmService().stopSelf();
        mService.setmService(null);

    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop");
        super.onStop();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        if (!mBtAdapter.isEnabled()) {
            Log.i(TAG, "onResume - BT not enabled yet");
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {

            case REQUEST_SELECT_DEVICE:
                Log.d(TAG,"WSZEDLEM.............................");
                //When the DeviceListActivity return, with the selected device address
                if (resultCode == Activity.RESULT_OK && data != null) {
                    String deviceAddress = data.getStringExtra(BluetoothDevice.EXTRA_DEVICE);
                    mDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(deviceAddress);

                    Log.d(TAG, "... onActivityResultdevice.address==" + mDevice + "mserviceValue" + mService);


                    //-------------------------------------------------------------------------------------------CHANGED
                    // mService.connect(deviceAddress);
                    mService.getmService().connect("C9:7B:CF:C5:A7:42");

                    Log.d(TAG, "MOJ ADRESS!!!" + deviceAddress);


                }
                break;
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    Toast.makeText(this, "Bluetooth has turned on ", Toast.LENGTH_SHORT).show();

                } else {
                    // User did not enable Bluetooth or an error occurred
                    Log.d(TAG, "BT not enabled");
                    Toast.makeText(this, "Problem in BT Turning ON ", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
                Log.e(TAG, "wrong request code");
                break;
        }
    }


    public void onCheckedChanged(RadioGroup group, int checkedId) {

    }


    private void showMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onBackPressed() {
        if (mState == UART_PROFILE_CONNECTED) {
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
            showMessage("nRFUART's running in background.\n             Disconnect to exit");
        }
        else {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle(R.string.popup_title)
                    .setMessage(R.string.popup_message)
                    .setPositiveButton(R.string.popup_yes, new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setNegativeButton(R.string.popup_no, null)
                    .show();
        }
    }
}