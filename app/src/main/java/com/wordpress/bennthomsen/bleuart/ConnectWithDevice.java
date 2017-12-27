package com.wordpress.bennthomsen.bleuart;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.content.LocalBroadcastManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import static com.wordpress.bennthomsen.bleuart.Controller.UARTStatusChangeReceiver;

public class ConnectWithDevice extends Activity {

    private Button btnConnectDisconnect;
    private  TextView messageAboutNetwork;
    private final static String TAG = ConnectWithDevice.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_with_device);

        messageAboutNetwork = findViewById(R.id.messageAboutNetwork);
        btnConnectDisconnect = findViewById(R.id.connectWithNetworkButton);

        Controller.setmBtAdapter( BluetoothAdapter.getDefaultAdapter());
        if (Controller.getmBtAdapter() == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        Intent bindIntent = new Intent(this, UartService.class);
        bindService(bindIntent, Controller.mServiceConnection, Context.BIND_AUTO_CREATE);
        LocalBroadcastManager.getInstance(this).registerReceiver(UARTStatusChangeReceiver, Controller.makeGattUpdateIntentFilter());

        btnConnectDisconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                messageAboutNetwork.setText("");
                if (!Controller.getmBtAdapter().isEnabled()) {
                    Log.i(TAG, "onClick - BT not enabled yet");
                    Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableIntent, Controller.REQUEST_ENABLE_BT);
                }
                else {
                    if (btnConnectDisconnect.getText().equals("Connect to the network")){

                        try {
                            String deviceAddress = "D7:10:BD:30:8F:84";
                            //String deviceAddress = "C5:E7:91:66:39:49";
                            Controller.setmDevice(BluetoothAdapter.getDefaultAdapter().getRemoteDevice(deviceAddress)) ;
                            Controller.getmService().connect(deviceAddress);

                            while( Controller.getmService().getmConnectionState()==1){} //empty while

                            if(Controller.getmService().getmConnectionState()==2){  // 2 - STATE_CONNECTED
                                Intent newIntent1 = new Intent(ConnectWithDevice.this, LoginActivity.class);
                                startActivity(newIntent1);
                            }

                            if(Controller.getmService().getmConnectionState()==0){
                                messageAboutNetwork.setText(R.string.disconnecting_message);
                            }

                        }catch ( Exception ee) {  Log.d(TAG, "I am in catch!!");}

                    }else {
                        //Disconnect button pressed
                        if (Controller.getmDevice()!=null){
                            Controller.getmService().disconnect();
                        }
                    }
                }
            }
        });
    }

    private void showMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
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
        unbindService(Controller.mServiceConnection);
        Controller.getmService().stopSelf();
        Controller.setmService(null);
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
        if (!Controller.getmBtAdapter().isEnabled()) {
            Log.i(TAG, "onResume - BT not enabled yet");
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, Controller.REQUEST_ENABLE_BT);
        }

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {

            case Controller.REQUEST_ENABLE_BT:
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

    @Override
    public void onBackPressed() {
        if (Controller.getmState() == Controller.getUartProfileConnected()) {
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
            showMessage("BLE MESH Controller running in background.\n             Disconnect to exit");
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