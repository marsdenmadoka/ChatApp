package marsmadoka98.gmail.whatsapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

//refer to chatfragments
public class ChatActivity extends AppCompatActivity {
    private  String messageReceiverID,messageReceiverName,MessageReceiverImage;
  private  String MessageSenderID;
  private Toolbar ChatToolbar;
    private TextView userName,userLastSeen;
    private CircleImageView userImage;
    private ImageButton SendMessageButton;
    private EditText MessageInputtext;
    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;

    private  final List<messages>messagesList=new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private messagesAdapter messageAdapter;
    private RecyclerView usermessagerecyclerview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mAuth=FirebaseAuth.getInstance();
        MessageSenderID=mAuth.getCurrentUser().getUid();
        RootRef= FirebaseDatabase.getInstance().getReference();


        messageReceiverID=getIntent().getExtras().get("visit_user_id").toString(); //will  these extras in our toolbar to dislay them..instead of fetching them again from db
        messageReceiverName=getIntent().getExtras().get("visit_user_name").toString();
       MessageReceiverImage=getIntent().getExtras().get("visit_user_image").toString();
      //  Toast.makeText(ChatActivity.this,messageReceiverName,Toast.LENGTH_SHORT).show();

        ChatToolbar=findViewById(R.id.chat_toobar);
        setSupportActionBar(ChatToolbar);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

        LayoutInflater layoutInflater=(LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE); //setting our custom_chat_bar layout as Toolbar
        View actionBarView =layoutInflater.inflate(R.layout.custom_chat_bar,null);
        actionBar.setCustomView(actionBarView);
        //displaying them
        userImage=findViewById(R.id.custom_profile_IMAGE);
        userName=findViewById(R.id.custom_profile_NAME);
        userLastSeen=findViewById(R.id.custom_user_lastseen);

        userName.setText(messageReceiverName);
        Picasso.get().load(MessageReceiverImage).placeholder(R.drawable.profile_image).into(userImage);

        SendMessageButton=findViewById(R.id.Send_message_btn);
        MessageInputtext=findViewById(R.id.input_message);
       messageAdapter = new messagesAdapter(messagesList);
    usermessagerecyclerview=findViewById(R.id.chat_recycler_viewer);
    linearLayoutManager=new LinearLayoutManager(this);
    usermessagerecyclerview.setLayoutManager(linearLayoutManager);
    usermessagerecyclerview.setAdapter(messageAdapter);

        SendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendMessage();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        RootRef.child("Message").child(MessageSenderID).child(messageReceiverID)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                   messages message = dataSnapshot.getValue(messages.class);
                   messagesList.add(message);
                   messageAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

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

    public void SendMessage(){

final String MessageText=MessageInputtext.getText().toString();
if(TextUtils.isEmpty(MessageText)){
    Toast.makeText(ChatActivity.this, "message cant be empty", Toast.LENGTH_SHORT).show();
}else{
String messageSenderRef= "Message/" + MessageSenderID + "/" + messageReceiverID;
String messageReceiveRef = "Message/" + messageReceiverID + "/" + MessageSenderID;


DatabaseReference userMessageKeyRef = RootRef.child("Message").child(MessageSenderID).child(messageReceiverID).push();

String messagePushID=userMessageKeyRef.getKey();//having a unique random key for each messaage
//pushing our messages to db usi HashMap
    Map messageTextBody= new HashMap();
    messageTextBody.put("message",MessageText);
    messageTextBody.put("type","text");
    messageTextBody.put("from",MessageSenderID);

    Map messageBodyDetails = new HashMap();
    messageBodyDetails.put(messageSenderRef + "/" + messagePushID,messageTextBody);
    messageBodyDetails.put(messageReceiveRef + "/" + messagePushID,messageTextBody);

    RootRef.updateChildren(messageBodyDetails).addOnCompleteListener(new OnCompleteListener() {
        @Override
        public void onComplete(@NonNull Task task) {
            if(task.isSuccessful()){
                Toast.makeText(ChatActivity.this, "message sent", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(ChatActivity.this, "Error ", Toast.LENGTH_SHORT).show();
            }
        MessageInputtext.setText(" ");//clear the TextView after message is sent
        }
    });
}
    }
}
