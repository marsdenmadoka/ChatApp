package marsmadoka98.gmail.whatsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class PhoneLogInActivity extends AppCompatActivity {
    private Button sendVerificationcodebtn,verifyButton;
    private EditText InputphoneNumber,Inputverificationcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_log_in);

        sendVerificationcodebtn=findViewById(R.id.Send_ver_code_button);
        verifyButton=findViewById(R.id.ver_account_button);
        InputphoneNumber=findViewById(R.id.phone_number_input);
        Inputverificationcode=findViewById(R.id.verification_code_input);

        sendVerificationcodebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendVerificationcodebtn.setVisibility(View.INVISIBLE);
                InputphoneNumber.setVisibility(View.INVISIBLE);

                Inputverificationcode.setVisibility(View.VISIBLE);
                verifyButton.setVisibility(View.VISIBLE);

            }
        });

 verifyButton.setOnClickListener(new View.OnClickListener() {
     @Override
     public void onClick(View view) {

     }
 });

    }
}
