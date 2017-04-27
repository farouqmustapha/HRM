package farouqmustapha.heartratemonitor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.PointLabelFormatter;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.SimpleXYSeries;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class HeartRate extends AppCompatActivity implements OnItemSelectedListener, Observer {

    private FirebaseAnalytics mFirebaseAnalytics;
    private int MAX_SIZE = 60; //graph max size
    boolean searchBt = true;
    BluetoothAdapter mBluetoothAdapter;
    List<BluetoothDevice> pairedDevices = new ArrayList<>();
    boolean menuBool = false; //display or not the disconnect option
    private XYPlot plot;
    boolean h7 = false; //Was the BTLE tested
    boolean normal = false; //Was the BT tested
    private Spinner spinner1;
    private FirebaseAuth auth;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heart_rate);
        auth = FirebaseAuth.getInstance();
        Log.i("Main Activity", "Starting Polar HR monitor main activity");
        DataHandler.getInstance().addObserver(this);

        //Verify if device is to old for BTLE
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {

            Log.i("Main Activity", "old device H7 disabled");
            h7 = true;
        }

        //verify if bluetooth device are enabled
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (DataHandler.getInstance().newValue) {
            //Verify if bluetooth if activated, if not activate it
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if(mBluetoothAdapter != null){
                if (!mBluetoothAdapter.isEnabled()) {
                    new AlertDialog.Builder(this)
                            .setTitle(R.string.bluetooth)
                            .setMessage(R.string.bluetoothOff)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    mBluetoothAdapter.enable();
                                    try {
                                        Thread.sleep(2000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    listBT();
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    searchBt = false;
                                }
                            })
                            .show();
                } else {
                    listBT();
                }
            }

            // Create Graph
            plot = (XYPlot) findViewById(R.id.dynamicPlot);
            if (plot.getSeriesSet().size() == 0) {
                Number[] series1Numbers = {};
                DataHandler.getInstance().setSeries1(new SimpleXYSeries(Arrays.asList(series1Numbers), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Heart Rate"));
            }
            DataHandler.getInstance().setNewValue(false);

        }
        else {
            listBT();
            plot = (XYPlot) findViewById(R.id.dynamicPlot);

        }
        //LOAD Graph
        LineAndPointFormatter series1Format = new LineAndPointFormatter(Color.rgb(0, 0, 255), Color.rgb(200, 200, 200), null, null);
        series1Format.setPointLabelFormatter(new PointLabelFormatter());
        plot.addSeries(DataHandler.getInstance().getSeries1(), series1Format);
        plot.setTicksPerRangeLabel(3);
        plot.getGraphWidget().setDomainLabelOrientation(-45);

        //ANALYTIC
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.VALUE, "Main");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

    protected void onDestroy() {
        super.onDestroy();
        DataHandler.getInstance().deleteObserver(this);
    }

    /**
     * Run on startup to list bluetooth paired device
     */
    public void listBT() {
        Log.d("Main Activity", "Listing BT elements");
        if (searchBt) {
            //Discover bluetooth devices
            final List<String> list = new ArrayList<>();
            list.add("< None >");
            if (!h7) {
                Log.d("Main Activity", "Listing BTLE elements");
                final BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback() {
                    public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {
                        if (!list.contains(device.getName() + "\n" + device.getAddress())) {
                            Log.d("Main Activity", "Adding " + device.getName());
                            list.add(device.getName() + "\n" + device.getAddress());
                            pairedDevices.add(device);
                        }
                    }
                };


                Thread scannerBTLE = new Thread() {
                    public void run() {
                        Log.d("Main Activity", "Starting scanning for BTLE");
                        mBluetoothAdapter.startLeScan(leScanCallback);
                        try {
                            Thread.sleep(10000);
                            Log.d("Main Activity", "Stoping scanning for BTLE");
                            mBluetoothAdapter.stopLeScan(leScanCallback);
                        } catch (InterruptedException e) {
                            Log.e("Main Activity", "ERROR: on scanning");
                        }
                    }
                };

                scannerBTLE.start();
            }

            //Populate drop down
            spinner1 = (Spinner) findViewById(R.id.spinner1);
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_item, list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner1.setOnItemSelectedListener(this);
            spinner1.setAdapter(dataAdapter);

            if (DataHandler.getInstance().getID() != 0 && DataHandler.getInstance().getID() < spinner1.getCount())
                spinner1.setSelection(DataHandler.getInstance().getID());
        }
    }

    /**
     * When menu button are pressed
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_heart_rate) {
            startActivity(new Intent(HeartRate.this, HeartRate.class));
            finish();
        }
        else if(id == R.id.action_symptoms_diary){
            startActivity(new Intent(HeartRate.this, ViewSymptomsDiary.class));
            finish();
        }
        else if(id == R.id.action_manage_info){
            startActivity(new Intent(HeartRate.this, ManageInfo.class));
            finish();
        }
        else if (id == R.id.action_logout) {
            auth.signOut();
            //UserInfo user;
            if(FirebaseAuth.getInstance().getCurrentUser()==null){
                startActivity(new Intent(HeartRate.this, Login.class));
            }
            finish();
        }

        return super.onOptionsItemSelected(item);
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * When the option is selected in the dropdown we turn on the bluetooth
     */
    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                               long arg3) {


        if (arg2 != 0) {
            //Actual work
            DataHandler.getInstance().setID(arg2);
            if (!h7) {

                Log.i("Main Activity", "Starting h7");
                DataHandler.getInstance().setH7(new H7ConnectThread((BluetoothDevice) pairedDevices.toArray()[DataHandler.getInstance().getID() - 1], this));
                h7 = true;
            }
            menuBool = true;

        }

    }

    public void onNothingSelected(AdapterView<?> arg0) {

    }

    /**
     * Called when bluetooth connection failed
     */
    public void connectionError() {

        Log.w("Main Activity", "Connection error occured");
        if (menuBool) {//did not manually tried to disconnect
            Log.d("Main Activity", "in the app");
            menuBool = false;
            final HeartRate ac = this;
            runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(getBaseContext(), getString(R.string.couldnotconnect), Toast.LENGTH_SHORT).show();
                    Spinner spinner1 = (Spinner) findViewById(R.id.spinner1);
                    if (DataHandler.getInstance().getID() < spinner1.getCount())
                        spinner1.setSelection(DataHandler.getInstance().getID());

                    if (!h7) {

                        Log.w("Main Activity", "starting H7 after error");
                        DataHandler.getInstance().setH7(new H7ConnectThread((BluetoothDevice) pairedDevices.toArray()[DataHandler.getInstance().getID() - 1], ac));
                        h7 = true;
                    }
                }
            });
        }
    }

    public void update(Observable observable, Object data) {
        receiveData();
    }

    /**
     * Update Gui with new value from receiver
     */
    public void receiveData() {
        runOnUiThread(new Runnable() {
            public void run() {
                //menuBool=true;
                TextView rpm = (TextView) findViewById(R.id.rpm);
                rpm.setText(DataHandler.getInstance().getLastValue());

                if (DataHandler.getInstance().getLastIntValue() != 0) {
                    DataHandler.getInstance().getSeries1().addLast(0, DataHandler.getInstance().getLastIntValue());
                    if (DataHandler.getInstance().getSeries1().size() > MAX_SIZE)
                        DataHandler.getInstance().getSeries1().removeFirst();//Prevent graph to overload data.
                    plot.redraw();
                }

                TextView min = (TextView) findViewById(R.id.min);
                min.setText(DataHandler.getInstance().getMin());

                TextView avg = (TextView) findViewById(R.id.avg);
                avg.setText(DataHandler.getInstance().getAvg());

                TextView max = (TextView) findViewById(R.id.max);
                max.setText(DataHandler.getInstance().getMax());
            }
        });
    }

}
