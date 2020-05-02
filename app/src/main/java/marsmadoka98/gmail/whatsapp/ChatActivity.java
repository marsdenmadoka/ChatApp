package marsmadoka98.gmail.whatsapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

//refer to chatfragments
public class ChatActivity extends AppCompatActivity {
    private  String messageReceiverID,messageReceiverName,MessageReceiverImage;
  private Toolbar ChatToolbar;
    private TextView userName,userLastSeen;
    private CircleImageView userImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
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

    }
}
