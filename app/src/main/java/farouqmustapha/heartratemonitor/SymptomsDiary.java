package farouqmustapha.heartratemonitor;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;

public class SymptomsDiary extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth auth;

    EditText txtDate, txtTime;
    private int mYear, mMonth, mDay, mHour, mMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_symptoms_diary);

        txtDate = (EditText) findViewById(R.id.txtDate);
        txtTime = (EditText) findViewById(R.id.txtTime);

        txtDate.setOnClickListener(this);
        txtTime.setOnClickListener(this);
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
            startActivity(new Intent(SymptomsDiary.this, SymptomsDiary.class));
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

                            txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

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

                            txtTime.setText(hourOfDay + ":" + minute);
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
    }

}
