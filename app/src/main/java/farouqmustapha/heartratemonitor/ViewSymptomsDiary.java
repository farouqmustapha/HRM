package farouqmustapha.heartratemonitor;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewSymptomsDiary extends AppCompatActivity {
    private FloatingActionButton addFab;
    private FirebaseAuth auth;
    private DatabaseReference mDatabase;

    private List<Symptom> symptomList = new ArrayList<>();
    private RecyclerView recyclerView;
    private SymptomAdapter mAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_symptoms_diary);

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String uid = user.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference symptomsDiary = mDatabase.child("Users").child(uid).child("symptomsDiary");

        addFab = (FloatingActionButton) findViewById(R.id.addFab);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mAdapter = new SymptomAdapter(symptomList);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        symptomsDiary.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                symptomList.clear();
                for (DataSnapshot symptomSnapshot: dataSnapshot.getChildren()) {
                    Symptom symptom = symptomSnapshot.getValue(Symptom.class);
                    symptom.setKey(symptomSnapshot.getKey());
                    symptomList.add(symptom);
                    Log.d("Symptom KEY :",symptomSnapshot.getKey());
                }
                Collections.reverse(symptomList);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        addFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                startActivity(new Intent(ViewSymptomsDiary.this, SymptomsDiary.class));
            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Symptom symptom = symptomList.get(position);
                Toast.makeText(getApplicationContext(), symptom.getActivity() + " is selected!\nKey :" +symptom.getKey(), Toast.LENGTH_SHORT).show();
                Intent mIntent = new Intent(ViewSymptomsDiary.this, ViewSymptom.class);
                mIntent.putExtra("KEY", symptom.getKey());
                startActivity(mIntent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

//        prepareSymptomData();




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
            startActivity(new Intent(ViewSymptomsDiary.this, HeartRate.class));
            finish();
        }
        else if(id == R.id.action_symptoms_diary){
            startActivity(new Intent(ViewSymptomsDiary.this, ViewSymptomsDiary.class));
            finish();
        }
        else if(id == R.id.action_manage_info){
            startActivity(new Intent(ViewSymptomsDiary.this, ManageInfo.class));
            finish();
        }
        else if (id == R.id.action_logout) {
            //return true;
            auth.signOut();
            //UserInfo user;
            if(FirebaseAuth.getInstance().getCurrentUser()==null){
                startActivity(new Intent(ViewSymptomsDiary.this, Login.class));
            }
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void prepareSymptomData() {
        Symptom symptom = new Symptom("Running","Date","Time","Remarks","radio8");
        symptomList.add(symptom);

        symptom = new Symptom("Running","Date","Time","Remarks","radio8");
        symptomList.add(symptom);

        symptom = new Symptom("Running","Date","Time","Remarks","radio8");
        symptomList.add(symptom);

        symptom = new Symptom("Running","Date","Time","Remarks","radio8");
        symptomList.add(symptom);

        symptom = new Symptom("Running","Date","Time","Remarks","radio8");
        symptomList.add(symptom);

        symptom = new Symptom("Running","Date","Time","Remarks","radio8");
        symptomList.add(symptom);

        symptom = new Symptom("Running","Date","Time","Remarks","radio8");
        symptomList.add(symptom);

        symptom = new Symptom("Running","Date","Time","Remarks","radio8");
        symptomList.add(symptom);

        symptom = new Symptom("Running","Date","Time","Remarks","radio8");
        symptomList.add(symptom);

        symptom = new Symptom("Running","Date","Time","Remarks","radio8");
        symptomList.add(symptom);

        symptom = new Symptom("Running","Date","Time","Remarks","radio8");
        symptomList.add(symptom);

        symptom = new Symptom("Running","Date","Time","Remarks","radio8");
        symptomList.add(symptom);

        symptom = new Symptom("Running","Date","Time","Remarks","radio8");
        symptomList.add(symptom);

        symptom = new Symptom("Running","Date","Time","Remarks","radio8");
        symptomList.add(symptom);

        mAdapter.notifyDataSetChanged();
    }
}
