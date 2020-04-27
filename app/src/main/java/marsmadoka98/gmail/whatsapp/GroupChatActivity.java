package marsmadoka98.gmail.whatsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


public class GroupChatActivity extends AppCompatActivity {
private Toolbar mToolbar;
private ImageButton sendMessageutton;
private ScrollView mScrollview;
private TextView displayTextMessages;
private EditText userMessageInput;


private String curentGroupName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);

        curentGroupName=getIntent().getExtras().get("groupName").toString(); //make sure you use the same name in "groupName" as the FragmentExtra
                                                                             //here we are getting our groupName from the Fragment class HANDLE THIS CAREFUL
        Toast.makeText(GroupChatActivity.this,curentGroupName,Toast.LENGTH_SHORT).show();



        mToolbar=findViewById(R.id.group_chat_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("groupName");


        sendMessageutton=findViewById(R.id.send_message_button);
        mScrollview=findViewById(R.id.my_scrollview_Group);
        displayTextMessages=findViewById(R.id.group_chat_text_display);
        userMessageInput=findViewById(R.id.input_group_messages);
    }
}
