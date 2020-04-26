package marsmadoka98.gmail.whatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    private EditText setUsername, setUserstatus;
    private Button UpdateBtn;
    private CircleImageView setImageProfile;
    private String currentUserID;
    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        RootRef = FirebaseDatabase.getInstance().getReference();

        setUsername = findViewById(R.id.set_user_name);
        setUserstatus = findViewById(R.id.set_profile_status);
        UpdateBtn = findViewById(R.id.update_settings);
        setImageProfile = findViewById(R.id.profile_image);

        UpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateSettings();

            }
        });

    }

    public void UpdateSettings() {

        String Username = setUsername.getText().toString();
        String userStatus = setUserstatus.getText().toString();

        if (TextUtils.isEmpty(Username)) {
            Toast.makeText(SettingsActivity.this, "please input username", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(userStatus)) {
            Toast.makeText(SettingsActivity.this, "please insert user status", Toast.LENGTH_SHORT).show();
        } else {  //saving the status username to firebase
            HashMap<String, String> profileMap = new HashMap<>();
            profileMap.put("uid", currentUserID);
            profileMap.put("name", Username);//make sure this name is the same as that one of the dataSnapshot child else the app will crash
            profileMap.put("status", userStatus);
            RootRef.child("Users").child(currentUserID).setValue(profileMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(SettingsActivity.this, "profile update", Toast.LENGTH_SHORT).show();
                                SendUserToMainActivity();

                            } else {

                                String message = task.getException().toString();
                                Toast.makeText(SettingsActivity.this, "Error: " + message, Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }

    public void SendUserToMainActivity() {
        Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();

    }
}
