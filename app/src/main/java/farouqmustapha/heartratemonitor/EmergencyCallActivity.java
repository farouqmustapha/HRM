package farouqmustapha.heartratemonitor;

import android.content.Intent;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ActionMenuItemView;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class EmergencyCallActivity extends AppCompatActivity {

    private ImageButton buttonSend;
    private EditText textPhoneNo, textSMS;
    private int mLastEvent = MotionEvent.ACTION_UP;

    private FirebaseAuth auth;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_call);
        auth = FirebaseAuth.getInstance();

        buttonSend = (ImageButton) findViewById(R.id.buttonSend);
        textPhoneNo = (EditText) findViewById(R.id.editTextPhoneNo);
        textSMS = (EditText) findViewById(R.id.editTextSMS);
        final Vibrator v = (Vibrator)getApplicationContext().getSystemService(getApplicationContext().VIBRATOR_SERVICE);


        final Runnable runr = new Runnable() {

            @Override
            public void run() {
                if(mLastEvent == MotionEvent.ACTION_MOVE) {
                    Toast.makeText(getApplicationContext(), "3 seconds reached!", Toast.LENGTH_SHORT).show();
                    // Vibrate for 0.25 seconds
                    v.vibrate(250);
                    String phoneNo = textPhoneNo.getText().toString();
                    String sms = textSMS.getText().toString();

                    try {
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(phoneNo, null, sms, null, null);
                        Toast.makeText(getApplicationContext(), "SMS Sent!",
                                Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(),
                                "SMS faild, please try again later!",
                                Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
            }
        };

        final Handler handel = new Handler();
        buttonSend.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                mLastEvent = arg1.getAction();
                switch (arg1.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        v.vibrate(250);
                        handel.postDelayed(runr,3000);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                    default:
                        handel.removeCallbacks(runr);
                        break;

                }
                return true;
            }
        });

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
