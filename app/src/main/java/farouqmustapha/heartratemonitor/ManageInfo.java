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

public class ManageInfo extends AppCompatActivity {

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

        user_info.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                inputName.setText((String) dataSnapshot.child("Name").getValue());
                inputPhone.setText((String) dataSnapshot.child("Phone").getValue());
                inputAddress.setText((String) dataSnapshot.child("Address").getValue());
                inputAge.setText((String) dataSnapshot.child("Age").getValue());
                inputWeight.setText((String) dataSnapshot.child("Weight").getValue());
                inputHeight.setText((String) dataSnapshot.child("Height").getValue());
                inputEmergencyPerson.setText((String) dataSnapshot.child("EmergencyPerson").getValue());
                inputEmergencyNumber.setText((String) dataSnapshot.child("EmergencyNumber").getValue());
//                spinnerBloodType.setse
                String bloodType = (String) dataSnapshot.child("BloodType").getValue();
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

//        mDatabase.child("Users").child(uid).child("personalInfo").child("Name").getValue();

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
//                mDatabase = FirebaseDatabase.getInstance().getReference();

                mDatabase.child("Users").child(uid).child("personalInfo").child("Name").setValue(name);
                mDatabase.child("Users").child(uid).child("personalInfo").child("Phone").setValue(phone);
                mDatabase.child("Users").child(uid).child("personalInfo").child("Address").setValue(address);
                mDatabase.child("Users").child(uid).child("personalInfo").child("Age").setValue(age);
                mDatabase.child("Users").child(uid).child("personalInfo").child("Weight").setValue(weight);
                mDatabase.child("Users").child(uid).child("personalInfo").child("Height").setValue(height);
                mDatabase.child("Users").child(uid).child("personalInfo").child("EmergencyPerson").setValue(emergencyPerson);
                mDatabase.child("Users").child(uid).child("personalInfo").child("EmergencyNumber").setValue(emergencyNumber);
                mDatabase.child("Users").child(uid).child("personalInfo").child("BloodType").setValue(bloodType);

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
            startActivity(new Intent(ManageInfo.this, HeartRate.class));
            finish();
        }
        else if(id == R.id.action_symptoms_diary){
            startActivity(new Intent(ManageInfo.this, SymptomsDiary.class));
            finish();
        }
        else if(id == R.id.action_manage_info){
            startActivity(new Intent(ManageInfo.this, ManageInfo.class));
            finish();
        }
        else if (id == R.id.action_logout) {
            //return true;
            auth.signOut();
            //UserInfo user;
            if(FirebaseAuth.getInstance().getCurrentUser()==null){
                startActivity(new Intent(ManageInfo.this, Login.class));
            }
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
