package marsmadoka98.gmail.whatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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
    private String Current_state;
    private  String senderUserID;

    private CircleImageView userProfileimage;
    private TextView userProfileName,userProfilestatus;
    private Button sendMessageRequestButton;
    private DatabaseReference UserRef;
    private DatabaseReference ChatRequestRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
      mAuth=FirebaseAuth.getInstance();
      senderUserID=mAuth.getCurrentUser().getUid();

        ChatRequestRef=FirebaseDatabase.getInstance().getReference().child("Chat Requests");
        UserRef= FirebaseDatabase.getInstance().getReference().child("Users");
        receiverUserID=getIntent().getExtras().get("visit_user_id").toString();
        //Toast.makeText(this, "userid"+receiverUserID, Toast.LENGTH_SHORT).show();


        userProfileimage=findViewById(R.id.visit_profile_image);
        userProfileName=findViewById(R.id.visit_profile_name);
        userProfilestatus=findViewById(R.id.visit_profile_status);
        sendMessageRequestButton=findViewById(R.id.send_message_request_button);

        Current_state="new";

        RetriveData();

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

                      ManageChatRequest();

                  }else{
                        //if the user has no profilePIc
                      String userName=dataSnapshot.child("name").getValue().toString();
                      String userStatus=dataSnapshot.child("status").getValue().toString();

                      userProfileName.setText(userName);
                      userProfilestatus.setText(userStatus);

                      ManageChatRequest();
                  }

              }

              @Override
              public void onCancelled(@NonNull DatabaseError databaseError) {

              }
          });
    }

    public void  ManageChatRequest(){
        ChatRequestRef.child(senderUserID) //we want to maintain the friend request so that once we move out of the activity it wont be lost
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild(receiverUserID)){

                            String request_type=dataSnapshot.child(receiverUserID).child("request_type").getValue().toString();
                            if(request_type.equals("sent")){
                                Current_state = "request_sent";
                                sendMessageRequestButton.setText("Cancel Chat Request");
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
        //sending the request
        if(!senderUserID.equals(receiverUserID)){ //if sender id is not equal to receiver userID that means you cannot send message for yourself
            // hence this validation meaaing the senderID must not be equal to the ReceiverUserID
            sendMessageRequestButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sendMessageRequestButton.setEnabled(false);//one you click the button it should remain inactive untill the reciever accepts your request
                    if(Current_state.equals("new")){ //if the friends are new to each other
                        SendChatRequest();
                    }
                    if(Current_state.equals("request_sent")){
                        CancelChatRequest();//when on wants to cancel the friends request/messge request he/she sent
                    }

                }
            });

        }else{
            sendMessageRequestButton.setVisibility(View.INVISIBLE); //means that set the set the sendmessage button inivisible in your own account whe you click
            // the profile actvity. since you cannot send your self a message click your name in the itemView under findfriends activty and see the working it.
        } }

        public void SendChatRequest(){ //friend request
        ChatRequestRef.child(senderUserID).child(receiverUserID)
                .child("request_type").setValue("sent") //works like sending a friend request or how room/busbooking apps work if there is space they accept your request
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                   if(task.isSuccessful()){
                       ChatRequestRef.child(receiverUserID).child(senderUserID)
                               .child("request_type").setValue("received")
                               .addOnCompleteListener(new OnCompleteListener<Void>() {
                                   @Override
                                   public void onComplete(@NonNull Task<Void> task) {
                                       if(task.isSuccessful()){

                                           sendMessageRequestButton.setEnabled(true);
                                           Current_state="request_sent";
                                           sendMessageRequestButton.setText("Cancel chat Request");
                                       }

                                   }
                               });
                   }
                    }
                });
    }

    public void CancelChatRequest(){ //if the user cancels the request/friend request they sent

        ChatRequestRef.child(senderUserID).child(receiverUserID)
        .removeValue()
        .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    ChatRequestRef.child(receiverUserID).child(senderUserID)
                            .removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        sendMessageRequestButton.setEnabled(true);
                                        Current_state = "new";
                                        sendMessageRequestButton.setText("Send Message");
                                    }

                                }
                            });
                }
            }
        });

    }
}
