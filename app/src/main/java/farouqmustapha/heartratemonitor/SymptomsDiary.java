package farouqmustapha.heartratemonitor;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class SymptomsDiary extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth auth;
    private DatabaseReference mDatabase;

    private EditText txtActivity, txtDate, txtTime, txtRemarks;
    private FloatingActionButton saveFab;
    private ImageView img1, img2, img3, img4, img5, img6, img7, img8;
    private RadioButton radio1, radio2,radio3, radio4, radio5, radio6, radio7, radio8;
    private int mYear, mMonth, mDay, mHour, mMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_symptoms_diary);

        txtActivity = (EditText) findViewById(R.id.txtActivity);
        txtDate = (EditText) findViewById(R.id.txtDate);
        txtTime = (EditText) findViewById(R.id.txtTime);
        txtRemarks = (EditText) findViewById(R.id.txtRemarks);
        radio1 = (RadioButton) findViewById(R.id.radio1);
        radio2 = (RadioButton) findViewById(R.id.radio2);
        radio3 = (RadioButton) findViewById(R.id.radio3);
        radio4 = (RadioButton) findViewById(R.id.radio4);
        radio5 = (RadioButton) findViewById(R.id.radio5);
        radio6 = (RadioButton) findViewById(R.id.radio6);
        radio7 = (RadioButton) findViewById(R.id.radio7);
        radio8 = (RadioButton) findViewById(R.id.radio8);
        saveFab = (FloatingActionButton) findViewById(R.id.saveFab);

        img1 = (ImageView)findViewById(R.id.img1);
        img2 = (ImageView)findViewById(R.id.img2);
        img3 = (ImageView)findViewById(R.id.img3);
        img4 = (ImageView)findViewById(R.id.img4);
        img5 = (ImageView)findViewById(R.id.img5);
        img6 = (ImageView)findViewById(R.id.img6);
        img7 = (ImageView)findViewById(R.id.img7);
        img8 = (ImageView)findViewById(R.id.img8);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String uid = user.getUid();

        final AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference symptomsDiaryRef = mDatabase.child("Users").child(uid).child("symptomsDiary").push();

        txtDate.setOnClickListener(this);
        txtTime.setOnClickListener(this);
        img1.setOnClickListener(this);
        img2.setOnClickListener(this);
        img3.setOnClickListener(this);
        img4.setOnClickListener(this);
        img5.setOnClickListener(this);
        img6.setOnClickListener(this);
        img7.setOnClickListener(this);
        img8.setOnClickListener(this);

        saveFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String activity = txtActivity.getText().toString();
                String date = txtDate.getText().toString();
                String time = txtTime.getText().toString();
                String remarks = txtRemarks.getText().toString();
                String painArea = new String();

                if (radio1.isChecked()){
                    painArea = "radio1";
                }
                else if (radio2.isChecked()){
                    painArea = "radio2";
                }
                else if (radio3.isChecked()){
                    painArea = "radio3";
                }
                else if (radio4.isChecked()){
                    painArea = "radio4";
                }
                else if (radio5.isChecked()){
                    painArea = "radio5";
                }
                else if (radio6.isChecked()){
                    painArea = "radio6";
                }
                else if (radio7.isChecked()){
                    painArea = "radio7";
                }
                else if (radio8.isChecked()){
                    painArea = "radio8";
                }

                if (!activity.isEmpty() && !date.isEmpty() && !time.isEmpty() && !painArea.isEmpty()){
                    symptomsDiaryRef.setValue(new Symptom(activity, date, time, remarks, painArea));
                    Toast.makeText(getApplicationContext(), "Your symptom has been logged to the system.", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else{
                    String errorString = new String();
                    if (activity.isEmpty()){
                        errorString = errorString + "\nCurrent Activity";
                    }
                    if (date.isEmpty()){
                        errorString = errorString + "\nDate";
                    }
                    if (time.isEmpty()){
                        errorString = errorString + "\nTime";
                    }
                    if (painArea.isEmpty()){
                        errorString = errorString + "\nPain Area";
                    }



                    dlgAlert.setMessage(errorString);
                    dlgAlert.setTitle("Please Enter :");
                    dlgAlert.setPositiveButton("OK", null);
                    dlgAlert.setCancelable(true);
                    dlgAlert.create().show();

                    dlgAlert.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });

                }

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
            startActivity(new Intent(SymptomsDiary.this, HeartRate.class));
            finish();
        }
        else if(id == R.id.action_symptoms_diary){
            startActivity(new Intent(SymptomsDiary.this, ViewSymptomsDiary.class));
            finish();
        }
        else if(id == R.id.action_manage_info){
            startActivity(new Intent(SymptomsDiary.this, ManageInfo.class));
            finish();
        }
        else if (id == R.id.action_logout) {
            //return true;
            auth.signOut();
            //UserInfo user;
            if(FirebaseAuth.getInstance().getCurrentUser()==null){
                startActivity(new Intent(SymptomsDiary.this, Login.class));
            }
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void onRadioButtonClicked(View v) {
        RadioButton rb1 = (RadioButton) findViewById(R.id.radio1);
        RadioButton rb2 = (RadioButton) findViewById(R.id.radio2);
        RadioButton rb3 = (RadioButton) findViewById(R.id.radio3);
        RadioButton rb4 = (RadioButton) findViewById(R.id.radio4);
        RadioButton rb5 = (RadioButton) findViewById(R.id.radio5);
        RadioButton rb6 = (RadioButton) findViewById(R.id.radio6);
        RadioButton rb7 = (RadioButton) findViewById(R.id.radio7);
        RadioButton rb8 = (RadioButton) findViewById(R.id.radio8);


        //is the current radio button now checked?
        boolean  checked = ((RadioButton) v).isChecked();

        //now check which radio button is selected
        //android switch statement
        switch(v.getId()){

            case R.id.radio1:
                if(checked) {
                    rb2.setChecked(false);
                    rb3.setChecked(false);
                    rb4.setChecked(false);
                    rb5.setChecked(false);
                    rb6.setChecked(false);
                    rb7.setChecked(false);
                    rb8.setChecked(false);
                }
                break;

            case R.id.radio2:
                if(checked){
                    rb1.setChecked(false);
                    rb3.setChecked(false);
                    rb4.setChecked(false);
                    rb5.setChecked(false);
                    rb6.setChecked(false);
                    rb7.setChecked(false);
                    rb8.setChecked(false);
                }

                break;

            case R.id.radio3:
                if(checked){
                    rb1.setChecked(false);
                    rb2.setChecked(false);
                    rb4.setChecked(false);
                    rb5.setChecked(false);
                    rb6.setChecked(false);
                    rb7.setChecked(false);
                    rb8.setChecked(false);
                }

                break;

            case R.id.radio4:
                if(checked) {
                    rb1.setChecked(false);
                    rb2.setChecked(false);
                    rb3.setChecked(false);
                    rb5.setChecked(false);
                    rb6.setChecked(false);
                    rb7.setChecked(false);
                    rb8.setChecked(false);
                }
                break;

            case R.id.radio5:
                if(checked){
                    rb1.setChecked(false);
                    rb2.setChecked(false);
                    rb3.setChecked(false);
                    rb4.setChecked(false);
                    rb6.setChecked(false);
                    rb7.setChecked(false);
                    rb8.setChecked(false);
                }

                break;

            case R.id.radio6:
                if(checked){
                    rb1.setChecked(false);
                    rb2.setChecked(false);
                    rb3.setChecked(false);
                    rb4.setChecked(false);
                    rb5.setChecked(false);
                    rb7.setChecked(false);
                    rb8.setChecked(false);
                }

                break;

            case R.id.radio7:
                if(checked) {
                    rb1.setChecked(false);
                    rb2.setChecked(false);
                    rb3.setChecked(false);
                    rb4.setChecked(false);
                    rb5.setChecked(false);
                    rb6.setChecked(false);
                    rb8.setChecked(false);
                }
                break;

            case R.id.radio8:
                if(checked){
                    rb1.setChecked(false);
                    rb2.setChecked(false);
                    rb3.setChecked(false);
                    rb4.setChecked(false);
                    rb5.setChecked(false);
                    rb6.setChecked(false);
                    rb7.setChecked(false);
                }

                break;
        }
    }

    @Override
    public void onClick(View v){
        if (v == txtDate) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            txtDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (v == txtTime) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            String sHour, sMinute;
                            sHour = Integer.toString(hourOfDay);
                            sMinute = Integer.toString(minute);
                            if(hourOfDay<10){
                                sHour = "0" + sHour;
                            }
                            if(minute<10){
                                sMinute = "0" +sMinute;
                            }
                            txtTime.setText(sHour + ":" + sMinute);
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }

        if(v instanceof ImageView){
            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
            String description = new String();

            switch(v.getId()){
                case R.id.img1:
                    description = getString(R.string.radio1);
                    break;

                case R.id.img2:
                    description = getString(R.string.radio2);
                    break;

                case R.id.img3:
                    description = getString(R.string.radio3);
                    break;

                case R.id.img4:
                    description = getString(R.string.radio4);
                    break;

                case R.id.img5:
                    description = getString(R.string.radio5);
                    break;

                case R.id.img6:
                    description = getString(R.string.radio6);
                    break;

                case R.id.img7:
                    description = getString(R.string.radio7);
                    break;

                case R.id.img8:
                    description = getString(R.string.radio8);
                    break;
            }
            dlgAlert.setMessage(description);
            dlgAlert.setTitle("Description:");
            dlgAlert.setPositiveButton("OK", null);
            dlgAlert.setCancelable(true);
            dlgAlert.create().show();

            dlgAlert.setPositiveButton("Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
        }
    }

}
