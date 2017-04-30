package farouqmustapha.heartratemonitor;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;

public class ViewSymptom extends AppCompatActivity {
    String KEY = new String();
    private FirebaseAuth auth;
    private DatabaseReference mDatabase;
    private TextView activity, date, time, painAreaLabel, descriptionLabel, description, remarks;
    private ImageView painArea;
    private FloatingActionButton deleteFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_symptom);
        KEY = getIntent().getExtras().getString("KEY"); //getting intent bundle value of firebase KEY

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String uid = user.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference symptomsDiaryRef = mDatabase.child("Users").child(uid).child("symptomsDiary").child(KEY);

        activity = (TextView) findViewById(R.id.sdActivity);
        date = (TextView) findViewById(R.id.sdDate);
        time = (TextView) findViewById(R.id.sdTime);
        painAreaLabel = (TextView) findViewById(R.id.sdPainLabel);
        descriptionLabel = (TextView) findViewById(R.id.sdDescriptionLabel);
        remarks = (TextView) findViewById(R.id.sdRemarks);
        painArea = (ImageView) findViewById(R.id.painImage);
        description = (TextView) findViewById(R.id.sdDescription);
        deleteFab = (FloatingActionButton) findViewById(R.id.deleteFab);

        final ValueEventListener listener = symptomsDiaryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Symptom symptom = dataSnapshot.getValue(Symptom.class);

                activity.setText(symptom.getActivity());
                date.setText(symptom.getDate());
                time.setText(symptom.getTime());
                painAreaLabel.setText("Pain Area :");
                descriptionLabel.setText("Description :");
                remarks.setText(symptom.getRemarks());

                switch (symptom.getPainArea()){
                    case "radio1":
                        description.setText(R.string.radio1);
                        painArea.setImageResource(R.drawable.upperbody1);
                        break;

                    case "radio2":
                        description.setText(R.string.radio2);
                        painArea.setImageResource(R.drawable.upperbody2);
                        break;

                    case "radio3":
                        description.setText(R.string.radio3);
                        painArea.setImageResource(R.drawable.upperbody3);
                        break;

                    case "radio4":
                        description.setText(R.string.radio4);
                        painArea.setImageResource(R.drawable.upperbody4);
                        break;

                    case "radio5":
                        description.setText(R.string.radio5);
                        painArea.setImageResource(R.drawable.upperbody5);
                        break;

                    case "radio6":
                        description.setText(R.string.radio6);
                        painArea.setImageResource(R.drawable.upperbody6);
                        break;

                    case "radio7":
                        description.setText(R.string.radio7);
                        painArea.setImageResource(R.drawable.upperbody7);
                        break;

                    case "radio8":
                        description.setText(R.string.radio8);
                        painArea.setImageResource(R.drawable.upperbody8);
                        break;

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        deleteFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                symptomsDiaryRef.removeEventListener(listener);
                symptomsDiaryRef.removeValue();
                Toast.makeText(getApplicationContext(), "The symptom entry is successfully deleted!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }


}
