package marsmadoka98.gmail.whatsapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;


public class GroupChatActivity extends AppCompatActivity {
private Toolbar mToolbar;
private ImageButton sendMessageutton;
private ScrollView mScrollview;
private TextView displayTextMessages;
private EditText userMessageInput;


private String curentGroupName;
private String currentUserId;
private  String currentUsername;
private String currentDate;
private String currentTime;
private FirebaseAuth mAuth;
private DatabaseReference UsersRef;
private DatabaseReference GroupNameRef;
    private DatabaseReference GroupMessageKeyRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);


        curentGroupName=getIntent().getExtras().get("groupName").toString(); //make sure you use the same name in "groupName" as the FragmentExtra
        //here we are getting our groupName from the Fragment class HANDLE THIS CAREFUL
        Toast.makeText(GroupChatActivity.this,curentGroupName,Toast.LENGTH_SHORT).show();


        mAuth=FirebaseAuth.getInstance();
        currentUserId=mAuth.getCurrentUser().getUid();
        UsersRef= FirebaseDatabase.getInstance().getReference().child("Users");
       GroupNameRef=FirebaseDatabase.getInstance().getReference().child("Groups").child(curentGroupName);//creating a reference pn any group that we will click




        mToolbar=findViewById(R.id.group_chat_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(curentGroupName);


        sendMessageutton=findViewById(R.id.send_message_button);
        mScrollview=findViewById(R.id.my_scrollview_Group);
        displayTextMessages=findViewById(R.id.group_chat_text_display);
        userMessageInput=findViewById(R.id.input_group_messages);

        GetUserInfo();//getting the username



        sendMessageutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveMessageToFirebase();
                userMessageInput.setText("");//clear the text view after you send the message
                mScrollview.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    @Override
    protected void onStart() { //d dispalying the messages we created
        super.onStart();
        GroupNameRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            if(dataSnapshot.exists()){ //if the group exists
                DisplayMessages(dataSnapshot);
              }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists()){ //if the group exists
                    DisplayMessages(dataSnapshot);
                }

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void GetUserInfo(){

    UsersRef.child(currentUserId).addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        //checking if the user exsists
        if(dataSnapshot.exists()){
            currentUsername=dataSnapshot.child("name").getValue().toString(); //if the user exists get the name of the user i.e the username as we set it in settings
        }

    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
});

    }

    public void SaveMessageToFirebase(){
String message= userMessageInput.getText().toString();
String messageKEY=GroupNameRef.push().getKey();//creat a unique key for each message
if(TextUtils.isEmpty(message)){
Toast.makeText(GroupChatActivity.this,"Message cannot be empty",Toast.LENGTH_SHORT).show();
}else{
      //we want to get the time the message was sent
    Calendar calForDate = Calendar.getInstance();//getting Date
    SimpleDateFormat currentDateFormat = new SimpleDateFormat("MMM dd, YYYY");
    currentDate=currentDateFormat.format(calForDate.getTime());

    Calendar calForTime= Calendar.getInstance();//getting Time
    SimpleDateFormat curentTimeformat = new SimpleDateFormat("hh:mm a"); //hh:mm:ss-seconds
    currentTime=curentTimeformat.format(calForTime.getTime());
    //saving the time and date in firebase
    HashMap<String,Object>groupMessageKey=new HashMap<>();
    GroupNameRef.updateChildren(groupMessageKey);
    GroupMessageKeyRef=GroupNameRef.child(messageKEY);
    HashMap<String,Object> messageInfoMap = new HashMap<>();
    messageInfoMap.put("name",currentUsername);
    messageInfoMap.put("message",message);
    messageInfoMap.put("date",currentDate);
    messageInfoMap.put("time",currentTime);
    GroupMessageKeyRef.updateChildren(messageInfoMap);
}
    }
    public void  DisplayMessages(DataSnapshot dataSnapshot){
        Iterator iterator = dataSnapshot.getChildren().iterator();//move to each children(message) of each child(Group)reading the message line by line
      while(iterator.hasNext()){
          String chatDate=(String) ((DataSnapshot)iterator.next()).getValue();
          String chatMessage=(String) ((DataSnapshot)iterator.next()).getValue();
          String chatName=(String) ((DataSnapshot)iterator.next()).getValue();
          String chatTime=(String) ((DataSnapshot)iterator.next()).getValue();
          displayTextMessages.append(chatName +":\n" + chatMessage +":\n" + chatTime + "    " + chatDate + "\n\n\n");

          mScrollview.fullScroll(ScrollView.FOCUS_DOWN);//scroll to the new message
      }

    }
}
