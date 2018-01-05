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
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class Controller extends Application  implements Application.ActivityLifecycleCallbacks{

    private static Activity mCurrentActivity;

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
        mCurrentActivity = activity;
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

    private enum MessageFromDevice
    {
        ADMIN("ad"),
        USER("us"),
        SAVEDUSER("su"),
        ERROR("er"),
        WRONGPASSWORD("wp"),
        DISPLAY("d:"),
        LOGINEXISTS("le"),
        STATUS("st:"),
        ACCESS1("a:1"),
        ACCESS2("a:2"),
        ACCESS12("a:12");

        private String message;

        MessageFromDevice(String messageFromDevice) {
            this.message = messageFromDevice;
        }

        public String getMessage() {
            return message;
        }

    }

    public static final int REQUEST_SELECT_DEVICE = 1;
    public static final int REQUEST_ENABLE_BT = 2;
    private static final int UART_PROFILE_READY = 10;
    public static final String TAG = "BLE MESH CONTROLLER";
    private static final int UART_PROFILE_CONNECTED = 20;
    private static final int UART_PROFILE_DISCONNECTED = 21;
    private static final int STATE_OFF = 10;
    private static int mState = UART_PROFILE_DISCONNECTED;
    private static BluetoothDevice mDevice = null;
    private static BluetoothAdapter mBtAdapter = null;
    private static BluetoothService mService ;
    private static String dane;
    private static Boolean accessDoor1 = true;
    private static Boolean accessDoor2 = true;
    private static Boolean accessToNetwork = false;
    private static  Boolean statusOfDoor1 = false;
    private static  Boolean statusOfDoor2 = false;
    private static  Boolean adminIsLogged = false;
    private static String name;
    public static ArrayList<String> listOfUsers = new ArrayList<String>();

    protected static BluetoothService getmService() {
        return mService;
    }
    protected static void setmService(BluetoothService mService) {
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
    protected static void setAccessDoor1(Boolean accessDoor1) {
        Controller.accessDoor1 = accessDoor1;
    }
    protected static Boolean getAccessDoor1() {
        return accessDoor1;
    }
    protected static void setAccessDoor2(Boolean accessDoor2) {
        Controller.accessDoor2 = accessDoor2;
    }
    public static Boolean getAccessDoor2() {
        return accessDoor2;
    }
    public static void setAccessToNetwork(Boolean accessToNetwork) {
        Controller.accessToNetwork = accessToNetwork;
    }
    public static Boolean getAccessToNetwork() {
        return accessToNetwork;
    }
    public static String getName() {
        return name;
    }
    public static void setName(String name) {
        Controller.name = name;
    }
    public static Boolean getStatusOfDoor1() {
        return statusOfDoor1;
    }
    public static Boolean getStatusOfDoor2() {
        return statusOfDoor2;
    }
    public static void setAdminIsLogged(Boolean adminIsLogged) {
        Controller.adminIsLogged = adminIsLogged;
    }
    public static Boolean getAdminIsLogged() {
        return adminIsLogged;
    }
    public static void setStatusOfDoor1(Boolean statusOfDoor1) {
        Controller.statusOfDoor1 = statusOfDoor1;
    }
    public static void setStatusOfDoor2(Boolean statusOfDoor2) {
        Controller.statusOfDoor2 = statusOfDoor2;
    }

    public static ServiceConnection mServiceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder rawBinder) {
            mService = (((BluetoothService.LocalBinder) rawBinder).getService());
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
    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(this);
    }

    public static final BroadcastReceiver UARTStatusChangeReceiver = new BroadcastReceiver() {

        public void onReceive(final Context context, Intent intent) {

            String action = intent.getAction();

            final Intent mIntent = intent;

            if (action.equals(BluetoothService.ACTION_GATT_CONNECTED)) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Log.d(TAG, "UART_CONNECT_MSG");
                        mState = UART_PROFILE_CONNECTED;
                    }
                });
            }

            if (action.equals(BluetoothService.ACTION_GATT_DISCONNECTED)) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Log.d(TAG, "UART_DISCONNECT_MSG");
                        mState = UART_PROFILE_DISCONNECTED;
                        mService.close();
                    }
                });
            }

            if (action.equals(BluetoothService.ACTION_GATT_SERVICES_DISCOVERED)) {
                mService.enableTXNotification();
            }

            if (action.equals(BluetoothService.ACTION_DATA_AVAILABLE)) {

                final byte[] txValue = intent.getByteArrayExtra(BluetoothService.EXTRA_DATA);
                try {
                    String text = new String(txValue, "UTF-8");
                    dane = text;
                    Log.d(TAG,"Data from Client: " + text);

                     for (MessageFromDevice za : MessageFromDevice.values()) {
                        if(text.contains(za.getMessage())){
                            messageHandler(context, za, text);
                        }
                }

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    public void run() {
                        try {
                            String text = new String(txValue, "UTF-8");
                            setDane(text);
                        } catch (Exception e) {
                            Log.e(TAG, e.toString());
                        }
                    }
                });
            }

            if (action.equals(BluetoothService.DEVICE_DOES_NOT_SUPPORT_UART)){
                Log.d(TAG,"Device doesn't support UART. Disconnecting");
                getmService().disconnect();
            }
        }
    };

    public static void messageHandler(final Context context, MessageFromDevice message, String text){
        TableLayout table = mCurrentActivity.findViewById(R.id.tab);
        EditText editTextLogin = mCurrentActivity.findViewById(R.id.editTextLogin);

        switch(message){

            case ADMIN:
                Intent newIntent = new Intent(context,AdminActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(newIntent);
                setAdminIsLogged(true);
                Toast.makeText(context,"Welcome", Toast.LENGTH_SHORT).show();
                break;

            case ERROR:
                Toast.makeText(context.getApplicationContext(),"The account does not exist", Toast.LENGTH_SHORT).show();
                Intent newIntent123 = new Intent(context, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(newIntent123);
                break;

            case WRONGPASSWORD:
                Toast.makeText(context.getApplicationContext(),"Wrong password. Try again...", Toast.LENGTH_SHORT).show();
                Intent newIntentWP = new Intent(context, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(newIntentWP);
                break;

            case LOGINEXISTS:
                editTextLogin.setError("Login exists");
                editTextLogin.requestFocus();
                break;

            case SAVEDUSER:
                Toast.makeText(context.getApplicationContext(),"Account has been created", Toast.LENGTH_SHORT).show();
                break;

            case USER:
                Controller.setAccessToNetwork(true);
                break;

            case DISPLAY:
                if (!text.substring(2).equals("end") && !listOfUsers.contains(text.substring(2))){
                    listOfUsers.add(text.substring(2));
                }
                if(text.substring(2).equals("end")) {
                    fillTableOfUsers(table, listOfUsers, context);
                    listOfUsers.clear();
                }
                break;

            case STATUS:
                char numberOfDoor = text.charAt(3);
                char statusOfDoor = text.charAt(5);
                updateStatusOfDoor(context, numberOfDoor, statusOfDoor);
                disableButton();
                break;

            case ACCESS1:
                if(Controller.getAccessToNetwork()) {
                    Controller.setAccessDoor1(true);
                    Controller.setAccessDoor2(false);
                    Intent newIntent1 = new Intent(context, UserActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(newIntent1);
                    setAdminIsLogged(false);
                    Toast.makeText(context, "Welcome", Toast.LENGTH_SHORT).show();
                }
                break;

            case ACCESS2:
                if(Controller.getAccessToNetwork()) {
                    Controller.setAccessDoor1(false);
                    Controller.setAccessDoor2(true);
                    Intent newIntent2 = new Intent(context, UserActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(newIntent2);
                    setAdminIsLogged(false);
                    Toast.makeText(context, "Welcome", Toast.LENGTH_SHORT).show();
                }
                break;

            case ACCESS12:
                if(Controller.getAccessToNetwork()) {
                    Controller.setAccessDoor1(true);
                    Controller.setAccessDoor2(true);
                    Intent newIntent12 = new Intent(context, UserActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(newIntent12);
                    setAdminIsLogged(false);
                    Toast.makeText(context, "Welcome", Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }

    private  static void fillTableOfUsers(TableLayout table, ArrayList<String> listOfUsers, Context context){

        for(int i=0; listOfUsers.size()>i; i++){
            String nameOfUser = listOfUsers.get(i).substring(0,listOfUsers.get(i).indexOf(":"));
            String access = listOfUsers.get(i).substring(listOfUsers.get(i).indexOf(":") + 1, listOfUsers.get(i).length());

            TableRow newRow = new TableRow(context);

            TextView nameLabel = new TextView(context);
            nameLabel.setText(nameOfUser);
            nameLabel.setTextSize(30);
            nameLabel.setTypeface(Typeface.SERIF, Typeface.BOLD);
            nameLabel.setGravity(Gravity.CENTER);

            if(i % 2 == 0){
                newRow.setBackgroundColor(Color.argb(255,230,231,232));
                nameLabel.setTextColor(Color.DKGRAY);
            }
            else {
                newRow.setBackgroundColor(Color.argb(255, 230, 255, 255));
                nameLabel.setTextColor(Color.argb(255,13,97,133));
            }

            ImageView iconNo = new ImageView(context);
            iconNo.setImageResource(R.drawable.no_icon);
            iconNo.setScaleType(ImageView.ScaleType.CENTER);
            iconNo.setY(15);

            ImageView iconYes = new ImageView(context);
            iconYes.setImageResource(R.drawable.yes_icon);
            iconYes.setY(15);

            ImageView iconYesNew = new ImageView(context);
            iconYesNew.setImageResource(R.drawable.yes_icon);
            iconYesNew.setY(15);

            newRow.addView(nameLabel);

            if(access.contains("1")) {newRow.addView(iconYes);}
            else {newRow.addView(iconNo);}

            if(access.contains("2")) {newRow.addView(iconYesNew);}
            else {newRow.addView(iconNo);}

            table.addView(newRow);

        }

    }

    private static void updateStatusOfDoor( Context context, Character numberOfDoor, Character statusOfDoor ){
        TextView textViewStateOfDoor1;
        TextView textViewStateOfDoor2;

        if(getAdminIsLogged()) {
            textViewStateOfDoor1 = mCurrentActivity.findViewById(R.id.stateOfDoor);
            textViewStateOfDoor2 = mCurrentActivity.findViewById(R.id.stateOfDoor1);
        }else{
            textViewStateOfDoor1 = mCurrentActivity.findViewById(R.id.stateOfDoorUser);
            textViewStateOfDoor2 = mCurrentActivity.findViewById(R.id.stateOfDoor1User);
        }

        if(numberOfDoor == '1'){
            if(statusOfDoor == '0') {
                setStatusOfDoor1(false);
                textViewStateOfDoor1.setText("CLOSE");
                textViewStateOfDoor1.setTextColor(Color.RED);
            }
            else{
                setStatusOfDoor1(true);
                textViewStateOfDoor1.setText("OPEN");
                textViewStateOfDoor1.setTextColor(context.getResources().getColor(R.color.DARKGREEN));
            }
        }
        else if(numberOfDoor == '2') {
            if(statusOfDoor == '0') {
                setStatusOfDoor2(false);
                textViewStateOfDoor2.setText("CLOSE");
                textViewStateOfDoor2.setTextColor(Color.RED);
            }
            else {
                setStatusOfDoor2(true);
                textViewStateOfDoor2.setText("OPEN");
                textViewStateOfDoor2.setTextColor(context.getResources().getColor(R.color.DARKGREEN));
            }
        }

    }

    private static void disableButton(){
        Button buttonOpenDoor1;
        Button buttonOpenDoor2;
        Button buttonCloseDoor1;
        Button buttonCloseDoor2;

        if(getAdminIsLogged()){
            buttonOpenDoor1 = mCurrentActivity.findViewById(R.id.buttonOpen);
            buttonOpenDoor2 = mCurrentActivity.findViewById(R.id.buttonOpen1);
            buttonCloseDoor1 = mCurrentActivity.findViewById(R.id.buttonClose);
            buttonCloseDoor2 = mCurrentActivity.findViewById(R.id.buttonClose1);
        }else
            {
            buttonOpenDoor1 = mCurrentActivity.findViewById(R.id.buttonOpenUser);
            buttonOpenDoor2 = mCurrentActivity.findViewById(R.id.buttonOpen1User);
            buttonCloseDoor1 = mCurrentActivity.findViewById(R.id.buttonCloseUser);
            buttonCloseDoor2 = mCurrentActivity.findViewById(R.id.buttonClose1User);
        }


        if(Controller.getStatusOfDoor1()){
            buttonCloseDoor1.setEnabled(true);
            buttonCloseDoor1.setAlpha(1);
            buttonOpenDoor1.setEnabled(false);
            buttonOpenDoor1.setAlpha(.3f);
        }else{
            buttonCloseDoor1.setEnabled(false);
            buttonCloseDoor1.setAlpha(.3f);
            buttonOpenDoor1.setEnabled(true);
            buttonOpenDoor1.setAlpha(1);
        }

        if(Controller.getStatusOfDoor2()){
            buttonCloseDoor2.setEnabled(true);
            buttonCloseDoor2.setAlpha(1);
            buttonOpenDoor2.setEnabled(false);
            buttonOpenDoor2.setAlpha(.3f);
        }else{
            buttonCloseDoor2.setEnabled(false);
            buttonCloseDoor2.setAlpha(.3f);
            buttonOpenDoor2.setEnabled(true);
            buttonOpenDoor2.setAlpha(1);
        }

    }

    private static void runOnUiThread(Runnable action) {

    }

    public static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothService.ACTION_DATA_AVAILABLE);
        intentFilter.addAction(BluetoothService.DEVICE_DOES_NOT_SUPPORT_UART);
        return intentFilter;
    }

}
