package marsmadoka98.gmail.whatsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;



public class GroupChatActivity extends AppCompatActivity {
private Toolbar mToolbar;
private ImageButton sendMessageutton;
private ScrollView mScrollview;
private TextView displayTextMessages;
private EditText userMessageInput;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);

        mToolbar=findViewById(R.id.group_chat_bar);
        getSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("gropu name");


        sendMessageutton=findViewById(R.id.send_message_button);
        mScrollview=findViewById(R.id.my_scrollview_Group);
        displayTextMessages=findViewById(R.id.group_chat_text_display);
        userMessageInput=findViewById(R.id.input_group_messages);
    }
}
