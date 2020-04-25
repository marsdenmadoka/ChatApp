package marsmadoka98.gmail.whatsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseUser;

public class LogInActivity extends AppCompatActivity {
private FirebaseUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);




    }

    @Override
    protected void onStart() {
        super.onStart();
        if(currentUser !=null){  //if user is logged in
            Intent loginIntent = new Intent(LogInActivity.this,MainActivity.class);
            startActivity(loginIntent);

        }
    }
}
