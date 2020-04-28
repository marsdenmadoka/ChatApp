package marsmadoka98.gmail.whatsapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    private EditText setUsername, setUserstatus;
    private Button UpdateBtn;
    private CircleImageView setImageProfile;
    private String currentUserID;
    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;
    private StorageReference UserProfileImageRef;
    private Uri imageUri = null;

    private static final int GalleryPick =1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        RootRef = FirebaseDatabase.getInstance().getReference();
        UserProfileImageRef= FirebaseStorage.getInstance().getReference().child("Profile Images");

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

        RetriveUserInfo();//retrive the username and status

    //setting profile image
        setImageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GalleryPick);


            }
        });
    }

//image crop
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GalleryPick && resultCode == RESULT_OK && data!=null){
            imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                setImageProfile.setImageURI(resultUri);
                //sending image to firebase
                // RootRef.child("Users").child(currentUserID).child("image")
                //filepath=UserProfileImageRef.child(currentUserID + ".jpg").child(resultUri.getLastPathSegment());
                final StorageReference filepath=UserProfileImageRef.child(currentUserID + ".jpg");
                filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                final Uri downloadUrl = uri;
                                DatabaseReference newpost=RootRef.push(); //pushing to database
                                newpost.child("Users").child(currentUserID).child("image").setValue(downloadUrl.toString());;
                                Toast.makeText(SettingsActivity.this,"image uploaded to db",Toast.LENGTH_SHORT).show();
                            }}); }}); } }

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

    public  void RetriveUserInfo(){
        RootRef.child("Users").child(currentUserID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                              ////if the user has created his profile
                        if((dataSnapshot.exists()) && (dataSnapshot.hasChild("name") && (dataSnapshot.hasChild("image")))){
                         String retrieveUserName=dataSnapshot.child("name").getValue().toString();
                         String retrieveStatus=dataSnapshot.child("status").getValue().toString();
                         String retrieveImage=dataSnapshot.child("image").getValue().toString();

                         setUsername.setText(retrieveUserName);
                         setUserstatus.setText(retrieveStatus);
                            Picasso.get().load(retrieveImage).into(setImageProfile);

                        }else if((dataSnapshot.exists()) && (dataSnapshot.hasChild("name"))){
                            String retrieveUserName=dataSnapshot.child("name").getValue().toString();
                            String retrieveStatus=dataSnapshot.child("status").getValue().toString();

                            setUsername.setText(retrieveUserName);
                            setUserstatus.setText(retrieveStatus);


                        }else{
                         Toast.makeText(SettingsActivity.this,"please update your profile",Toast.LENGTH_SHORT).show();

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


    }

    public void SendUserToMainActivity() {
        Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();

    }
}
