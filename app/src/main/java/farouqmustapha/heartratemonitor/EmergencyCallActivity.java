package farouqmustapha.heartratemonitor;

import android.content.Intent;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;
import android.app.Activity;
import android.content.IntentSender;
import android.content.IntentSender.SendIntentException;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.LocationListener;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.google.android.gms.common.api.GoogleApiClient.*;

public class EmergencyCallActivity extends AppCompatActivity implements
        ConnectionCallbacks, OnConnectionFailedListener, LocationListener {

    private ImageButton buttonSend;
    private Button buttonTemplate;
    private EditText textPhoneNo, textSMS;
    private Switch ambulanceSwitch;
    String _latitude, _longitude;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    LocationRequest mLocationRequest;
    private int mLastEvent = MotionEvent.ACTION_UP;

    private FirebaseAuth auth;
    private DatabaseReference mDatabase;
    String profileEdited = "false";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_call);
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String uid = user.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference appDataRef = mDatabase.child("Users").child(uid).child("appData");
        final DatabaseReference emergencyPhoneRef = mDatabase.child("Users").child(uid).child("personalInfo").child("emergencyNumber");
        final DatabaseReference ambulanceRequestRef = mDatabase.child("AmbulanceRequest").push();

        buttonSend = (ImageButton) findViewById(R.id.buttonSend);
        buttonTemplate = (Button) findViewById(R.id.buttonTemplate);
        textPhoneNo = (EditText) findViewById(R.id.editTextPhoneNo);
        textSMS = (EditText) findViewById(R.id.editTextSMS);
        ambulanceSwitch = (Switch) findViewById(R.id.ambulanceSwitch);

        appDataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                textSMS.setText((String) dataSnapshot.child("messageTemplate").getValue());
                profileEdited = (String) dataSnapshot.child("profileEdited").getValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        emergencyPhoneRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                textPhoneNo.setText((String) dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        buttonTemplate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                appDataRef.child("messageTemplate").setValue(textSMS.getText().toString());
                Toast.makeText(getApplicationContext(),"Template has been saved!",Toast.LENGTH_LONG);
            }
        });

        final Vibrator v = (Vibrator) getApplicationContext().getSystemService(getApplicationContext().VIBRATOR_SERVICE);


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        } else
            Toast.makeText(this, "Not Connected!", Toast.LENGTH_SHORT).show();
        
        
        final Runnable runr = new Runnable() {

            @Override
            public void run() {
                if(mLastEvent == MotionEvent.ACTION_MOVE) {
                    // Vibrate for 0.25 seconds
                    v.vibrate(250);
                    if(!_latitude.isEmpty()&&!_longitude.isEmpty()) {
                        if(ambulanceSwitch.isChecked()){
                            DateFormat df = new SimpleDateFormat("dd/MM/yyyy, HH:mm");
                            String date = df.format(Calendar.getInstance().getTime());
                            String patientKey = uid;
                            String requestStatus = "Requesting Aid";
                            ambulanceRequestRef.setValue(new AmbulanceRequest(date, patientKey, requestStatus, _latitude, _longitude));
                        }
                        String phoneNo = textPhoneNo.getText().toString();
                        String sms = textSMS.getText().toString();
                        //creating hyperlink for google maps :google.com/maps/?q=<lat>,<long>
                        sms = sms + "\nMy Current Location : " + "google.com/maps/?q=" + _latitude + "," + _longitude;
                        sms = sms + "\n\nThis message is sent using Pulse Keeper application.";

                        try {
                            SmsManager smsManager = SmsManager.getDefault();
                            smsManager.sendTextMessage(phoneNo, null, sms, null, null);
                            Toast.makeText(getApplicationContext(), "Emergency message sent!",
                                    Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(),
                                    "SMS failed, please try again later!",
                                    Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                    else
                        Toast.makeText(getApplicationContext(), "Failed to get current location!", Toast.LENGTH_LONG).show();
                }
            }
        };

        final Handler handel = new Handler();
        buttonSend.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                if(!profileEdited.isEmpty() && profileEdited.equals("true")) {
                    mLastEvent = arg1.getAction();

                    switch (arg1.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            v.vibrate(250);
                            handel.postDelayed(runr, 3000);
                            break;
                        case MotionEvent.ACTION_MOVE:
                            break;
                        case MotionEvent.ACTION_UP:
                        default:
                            handel.removeCallbacks(runr);
                            break;
                    }
                }
                else
                    Toast.makeText(getApplicationContext(), "Please update your personal information first!", Toast.LENGTH_LONG).show();

                return true;
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        settingRequest();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, "Connection Suspended!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Connection Failed!", Toast.LENGTH_SHORT).show();
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, 90000);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i("Current Location", "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    /*Method to get the enable location settings dialog*/
    public void settingRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);    // 10 seconds, in milliseconds
        mLocationRequest.setFastestInterval(1000);   // 1 second, in milliseconds
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient,
                        builder.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {

            @Override
            public void onResult(@NonNull LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can
                        // initialize location requests here.
                        getLocation();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied, but this can be fixed
                        // by showing the user a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(EmergencyCallActivity.this, 1000);
                        } catch (SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way
                        // to fix the settings so we won't show the dialog.
                        break;
                }
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
        switch (requestCode) {
            case 1000:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        // All required changes were successfully made
                        getLocation();
                        break;
                    case Activity.RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to
                        Toast.makeText(this, "Location Service not Enabled", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
                break;
        }
    }

    public void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        } else {
            /*Getting the location after aquiring location service*/
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);

            if (!mGoogleApiClient.isConnected())
                mGoogleApiClient.connect();

            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
//            }
        }
    }

    /*When Location changes, this method get called. */
    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        _latitude = Double.toString(mLastLocation.getLatitude());
        _longitude = Double.toString(mLastLocation.getLongitude());
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_heart_rate) {
            startActivity(new Intent(this, HeartRateActivity.class));
            finish();
        }
        else if(id == R.id.action_symptoms_diary){
            startActivity(new Intent(this, ViewSymptomsDiaryActivity.class));
            finish();
        }
        else if(id == R.id.action_manage_info){
            startActivity(new Intent(this, ManageInfoActivity.class));
            finish();
        }
        else if(id == R.id.action_emergency_call){
            startActivity(new Intent(this, EmergencyCallActivity.class));
            finish();
        }
        else if (id == R.id.action_logout) {
            //return true;
            auth.signOut();
            //UserInfo user;
            if(FirebaseAuth.getInstance().getCurrentUser()==null){
                startActivity(new Intent(this, LoginActivity.class));
            }
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
