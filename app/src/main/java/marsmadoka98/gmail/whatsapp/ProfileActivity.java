package marsmadoka98.gmail.whatsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    private String receiverUserID;

    private CircleImageView userProfile;
    private TextView userProfileName,userProfilestatus;
    private Button sendMessageRequestButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        receiverUserID=getIntent().getExtras().get("visit_user_id").toString();
        //Toast.makeText(this, "userid"+receiverUserID, Toast.LENGTH_SHORT).show();


        userProfile=findViewById(R.id.users_profile_image);
        userProfileName=findViewById(R.id.user_profile_name);
        userProfilestatus=findViewById(R.id.user_profile_status);
        sendMessageRequestButton=findViewById(R.id.send_message_request_button);


        sendMessageRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
