package farouqmustapha.heartratemonitor;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class ViewSymptomsDiary extends AppCompatActivity {
    private FloatingActionButton addFab;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_symptoms_diary);
        auth = FirebaseAuth.getInstance();
        addFab = (FloatingActionButton) findViewById(R.id.addFab);

        addFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                startActivity(new Intent(ViewSymptomsDiary.this, SymptomsDiary.class));
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
}
