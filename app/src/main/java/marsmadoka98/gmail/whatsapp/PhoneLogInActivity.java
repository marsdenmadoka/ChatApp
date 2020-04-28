package marsmadoka98.gmail.whatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneLogInActivity extends AppCompatActivity {
    private Button sendVerificationcodebtn,verifyButton;
    private EditText InputphoneNumber,Inputverificationcode;
    private ProgressDialog progressDialog;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
   private String mVerificationId;
   private PhoneAuthProvider.ForceResendingToken  mResendToken;

   private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_log_in);


        mAuth=FirebaseAuth.getInstance();
        progressDialog= new ProgressDialog(this);

        sendVerificationcodebtn=findViewById(R.id.Send_ver_code_button);
        verifyButton=findViewById(R.id.ver_account_button);
        InputphoneNumber=findViewById(R.id.phone_number_input);
        Inputverificationcode=findViewById(R.id.verification_code_input);

        sendVerificationcodebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumer=InputphoneNumber.getText().toString();
                if(TextUtils.isEmpty(phoneNumer)){

                    Toast.makeText(PhoneLogInActivity.this, "please input your phone number", Toast.LENGTH_SHORT).show();
                }else{
                    progressDialog.setMessage("please wait we are authenticating your phone...");
                    progressDialog.show();
                    progressDialog.setCanceledOnTouchOutside(false);
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        phoneNumer,
                        60,
                        TimeUnit.SECONDS,
                        PhoneLogInActivity.this,
                        callbacks);
                }

            }
        });
         callbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
             @Override
             public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                 signInWithPhoneAuthCredential(phoneAuthCredential);

             }

             @Override
             public void onVerificationFailed(@NonNull FirebaseException e) {
                 Toast.makeText(PhoneLogInActivity.this, "invalid Phone number!! please enter a correct phone number using your country code", Toast.LENGTH_LONG).show();

                 sendVerificationcodebtn.setVisibility(View.VISIBLE);
                 InputphoneNumber.setVisibility(View.VISIBLE);
                 Inputverificationcode.setVisibility(View.INVISIBLE);
                 verifyButton.setVisibility(View.INVISIBLE);
             }
             @Override
             public void onCodeSent(@NonNull String verificationId,
                                    @NonNull PhoneAuthProvider.ForceResendingToken token) {
                 // The SMS verification code has been sent to the provided phone number, we now need to ask the user to enter the code and then construct a credential
                 // Save verification ID and resending token so we can use them later
                 mVerificationId = verificationId;
                 mResendToken = token;
                 Toast.makeText(PhoneLogInActivity.this, "verification code sent please check!", Toast.LENGTH_SHORT).show();
                 sendVerificationcodebtn.setVisibility(View.INVISIBLE);
                 InputphoneNumber.setVisibility(View.INVISIBLE);
                 Inputverificationcode.setVisibility(View.VISIBLE);
                 verifyButton.setVisibility(View.VISIBLE);
             }
         };


 verifyButton.setOnClickListener(new View.OnClickListener() {
     @Override
     public void onClick(View view) {
         sendVerificationcodebtn.setVisibility(view.INVISIBLE);
         InputphoneNumber.setVisibility(view.INVISIBLE);

         String verificationCode = Inputverificationcode.getText().toString();

         if(TextUtils.isEmpty(verificationCode)){
             Toast.makeText(PhoneLogInActivity.this, "verification code cannot be empty", Toast.LENGTH_SHORT).show();
         }else{
             progressDialog.setMessage("please wait as we verify your code...");
             progressDialog.show();
             progressDialog.setCanceledOnTouchOutside(false);
             PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, verificationCode);
             signInWithPhoneAuthCredential(credential);
         }
     }
 });

    }//end of oncreate method


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            progressDialog.dismiss();
                            Toast.makeText(PhoneLogInActivity.this, "signed in successful", Toast.LENGTH_SHORT).show();
                            // Sign in success, update UI with the signed-in user's information
                            SendUserToMainActivity();

                        } else {
                            String message= task.getException().toString();
                            Toast.makeText(PhoneLogInActivity.this, "Error :"+message, Toast.LENGTH_SHORT).show();
                            // Sign in failed, display a message and update the UI
                        }
                    }
                });
    }

    public void SendUserToMainActivity(){
        Intent intent = new Intent(PhoneLogInActivity.this,MainActivity.class);
        startActivity(intent);
        finish();


    }

}
