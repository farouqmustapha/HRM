package farouqmustapha.heartratemonitor;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ManageInfoActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private DatabaseReference mDatabase;

    private EditText inputName, inputPhone,inputAddress, inputAge,inputWeight,
            inputHeight,inputEmergencyPerson,inputEmergencyNumber;
    private Spinner spinnerBloodType;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_manage_info);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String uid = user.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference user_info = mDatabase.child("Users").child(uid).child("personalInfo");


        inputName = (EditText) findViewById(R.id.editName);
        inputPhone = (EditText) findViewById(R.id.editPhone);
        inputAddress = (EditText) findViewById(R.id.editAddress);
        inputAge = (EditText) findViewById(R.id.editAge);
        inputWeight = (EditText) findViewById(R.id.editWeight);
        inputHeight = (EditText) findViewById(R.id.editHeight);
        inputEmergencyPerson = (EditText) findViewById(R.id.editEmergencyPerson);
        inputEmergencyNumber = (EditText) findViewById(R.id.editEmergencyNumber);
        spinnerBloodType = (Spinner) findViewById(R.id.spinner);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        user_info.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                inputName.setText((String) dataSnapshot.child("name").getValue());
                inputPhone.setText((String) dataSnapshot.child("phone").getValue());
                inputAddress.setText((String) dataSnapshot.child("address").getValue());
                inputAge.setText((String) dataSnapshot.child("age").getValue());
                inputWeight.setText((String) dataSnapshot.child("weight").getValue());
                inputHeight.setText((String) dataSnapshot.child("height").getValue());
                inputEmergencyPerson.setText((String) dataSnapshot.child("emergencyPerson").getValue());
                inputEmergencyNumber.setText((String) dataSnapshot.child("emergencyNumber").getValue());
                String bloodType = (String) dataSnapshot.child("bloodType").getValue();
                for (int i=0;i<spinnerBloodType.getCount();i++){
                    if (spinnerBloodType.getItemAtPosition(i).equals(bloodType)){
                        spinnerBloodType.setSelection(i);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String name = inputName.getText().toString();
                String phone = inputPhone.getText().toString();
                String address = inputAddress.getText().toString();
                String age = inputAge.getText().toString();
                String weight = inputWeight.getText().toString();
                String height = inputHeight.getText().toString();
                String emergencyPerson = inputEmergencyPerson.getText().toString();
                String emergencyNumber = inputEmergencyNumber.getText().toString();
                String bloodType = spinnerBloodType.getSelectedItem().toString();

                Toast.makeText(getApplicationContext(), "Your personal information has been updated.", Toast.LENGTH_SHORT).show();

                mDatabase.child("Users").child(uid).child("personalInfo").child("name").setValue(name);
                mDatabase.child("Users").child(uid).child("personalInfo").child("phone").setValue(phone);
                mDatabase.child("Users").child(uid).child("personalInfo").child("address").setValue(address);
                mDatabase.child("Users").child(uid).child("personalInfo").child("age").setValue(age);
                mDatabase.child("Users").child(uid).child("personalInfo").child("weight").setValue(weight);
                mDatabase.child("Users").child(uid).child("personalInfo").child("height").setValue(height);
                mDatabase.child("Users").child(uid).child("personalInfo").child("emergencyPerson").setValue(emergencyPerson);
                mDatabase.child("Users").child(uid).child("personalInfo").child("emergencyNumber").setValue(emergencyNumber);
                mDatabase.child("Users").child(uid).child("personalInfo").child("bloodType").setValue(bloodType);
                mDatabase.child("Users").child(uid).child("appData").child("profileEdited").setValue("true");

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

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
            auth.signOut();
            if(FirebaseAuth.getInstance().getCurrentUser()==null){
                startActivity(new Intent(this, LoginActivity.class));
            }
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
