package marsmadoka98.gmail.whatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogInActivity extends AppCompatActivity {
private FirebaseUser currentUser;
private Button LoginButton, PhoneLoginButton;
private EditText UserEmail,UserPasssowd;
 private TextView NeedNewAcount,ForgotPassword;
 private ProgressDialog mprogress;
 private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        LoginButton = findViewById(R.id.login_button);
        PhoneLoginButton=findViewById(R.id.phone_login_button);
        UserEmail=findViewById(R.id.login_email);
        UserPasssowd=findViewById(R.id.login_password);
        NeedNewAcount=findViewById(R.id.need_newAccount);
        ForgotPassword=findViewById(R.id.forget_password);

       mAuth=FirebaseAuth.getInstance();
       currentUser=mAuth.getCurrentUser();
        mprogress=new ProgressDialog(this);

        NeedNewAcount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signupIntent= new Intent(LogInActivity.this,RegisterActivity.class);
                startActivity(signupIntent);
            }
        });


        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Login();
            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();
        if(currentUser !=null){  //if user is logged in
            SendUserToMainActivity();

        }
    }

    public void Login(){
        String email=UserEmail.getText().toString().trim();
        String password= UserPasssowd.getText().toString().trim();
        if(TextUtils.isEmpty(email)){
            Toast.makeText(LogInActivity.this,"email field cant be empty", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(LogInActivity.this,"password field required",Toast.LENGTH_SHORT).show();
        }else{
            mprogress.setMessage("sign in account....");
            mprogress.setTitle("please wait as we sign you in");
            mprogress.setCanceledOnTouchOutside(true);
            mprogress.show();
            mAuth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                mprogress.dismiss();
                                SendUserToMainActivity();
                                Toast.makeText(LogInActivity.this,"logged in succesful",Toast.LENGTH_SHORT).show();

                            }else{
                                mprogress.dismiss();
                                String message = task.getException().toString();
                                Toast.makeText(LogInActivity.this,"Error: "+ message,Toast.LENGTH_LONG).show();
                            }

                        }
                    });
        }

    }
public void SendUserToMainActivity(){
    Intent mainIntent = new Intent(LogInActivity.this,MainActivity.class);
    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    startActivity(mainIntent);
    finish();

}


}
