package marsmadoka98.gmail.whatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    private String receiverUserID;

    private CircleImageView userProfileimage;
    private TextView userProfileName,userProfilestatus;
    private Button sendMessageRequestButton;
    private DatabaseReference UserRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        UserRef= FirebaseDatabase.getInstance().getReference().child("Users");
        receiverUserID=getIntent().getExtras().get("visit_user_id").toString();
        //Toast.makeText(this, "userid"+receiverUserID, Toast.LENGTH_SHORT).show();


        userProfileimage=findViewById(R.id.visit_profile_image);
        userProfileName=findViewById(R.id.visit_profile_name);
        userProfilestatus=findViewById(R.id.visit_profile_status);
        sendMessageRequestButton=findViewById(R.id.send_message_request_button);

        RetriveData();

        sendMessageRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void RetriveData() { //fetching data from database
          UserRef.child(receiverUserID).addValueEventListener(new ValueEventListener() {
              @Override
              public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                  if((dataSnapshot.exists()) && (dataSnapshot.hasChild("image"))){ //the profile picture is optional

                     String userImage=dataSnapshot.child("image").getValue().toString();//the  name "image" should be the same as the one in db
                      String userName=dataSnapshot.child("name").getValue().toString();//be careful with the names make sure there are exactly the same with those of your bd
                      String userStatus=dataSnapshot.child("status").getValue().toString();

                      /**DISPLAYING THEM IN TE ACTIVITY***/
                      Picasso.get().load(userImage).placeholder(R.drawable.profile_image).into(userProfileimage);
                      userProfileName.setText(userName);
                      userProfilestatus.setText(userStatus);

                  }else{
                        //if the user has no profilePIc
                      String userName=dataSnapshot.child("name").getValue().toString();
                      String userStatus=dataSnapshot.child("status").getValue().toString();

                      userProfileName.setText(userName);
                      userProfilestatus.setText(userStatus);
                  }

              }

              @Override
              public void onCancelled(@NonNull DatabaseError databaseError) {

              }
          });

    }
}
