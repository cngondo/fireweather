package fireweather.example.com;

import android.content.Context;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class Home extends AppCompatActivity {
    private Firebase reference;
    private ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
//        Initialize firebase
        Firebase.setAndroidContext(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Linked to the UI
        Button rainy = (Button)findViewById(R.id.btnRainy);
        Button sunny = (Button)findViewById(R.id.btnSunny);
        final TextView condition = (TextView)findViewById(R.id.tvCondition);
        /*
        * Initialize the firebase App
        * The URL should point to the root node of your date that is stored in
        * the JSON format
        */
        reference = new Firebase("https://weather254.firebaseio.com/Condition");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                /*
                * This method enables you to change data on Firebase and it
                * Automatically updates it on the app.
                * Use typecasting to to "Set" to whatever data type you want
                * */
                String newCondition = (String) dataSnapshot.getValue();
                condition.setText(newCondition);
//              Short Toast for notification
                Toast.makeText(getApplicationContext(), "Weather has changed", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
//              IF it fails to update on Firebase
                Toast.makeText(getApplicationContext(), "Failed to update. Check connection!!", Toast.LENGTH_SHORT).show();
            }
        });
        /*
        * Click listeners that change the values of the text view on click
        * */
        sunny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // test for connection
                if (cm.getActiveNetworkInfo() != null
                        && cm.getActiveNetworkInfo().isAvailable()
                        && cm.getActiveNetworkInfo().isConnected()) {
                    reference.setValue("Sunny");
                }
                else {
                    Toast.makeText(getApplicationContext(), "Failed to update. Check connection!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        rainy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cm.getActiveNetworkInfo()!=null
                        && cm.getActiveNetworkInfo().isAvailable()
                        && cm.getActiveNetworkInfo().isConnected()){
                    reference.setValue("Rainy");
                }
                else{
                    Toast.makeText(getApplicationContext(), "Failed to update. Check connection!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
