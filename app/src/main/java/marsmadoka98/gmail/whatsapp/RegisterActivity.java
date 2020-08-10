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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import org.w3c.dom.Text;

public class RegisterActivity extends AppCompatActivity {
private Button CreateAcountbtn;
private EditText SignUpEmail,SignUpPassword;
private TextView AlreadyHaveAccount;
private FirebaseAuth mAuth; //add this in the main activity also
private ProgressDialog mprogress;
    private DatabaseReference RootRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
 mAuth=FirebaseAuth.getInstance();
        RootRef= FirebaseDatabase.getInstance().getReference();

mprogress=new ProgressDialog(this);

        CreateAcountbtn=findViewById(R.id.signup_button);
        SignUpEmail=findViewById(R.id.signup_email);
        SignUpPassword=findViewById(R.id.signup_password);
        AlreadyHaveAccount=findViewById(R.id.Already_have_account);

        AlreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendUserToLoginActivity();
            }
        });
CreateAcountbtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Register();
    }
});
    }

    public void Register(){

        String email=SignUpEmail.getText().toString().trim();
        String password= SignUpPassword.getText().toString().trim();
        if(TextUtils.isEmpty(email)){
            Toast.makeText(getApplicationContext(),"email field cant be empty", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(RegisterActivity.this,"password field required",Toast.LENGTH_SHORT).show();
        }
           else{
            mprogress.setMessage("creating account....");
            mprogress.setTitle("account creation");
            mprogress.setCanceledOnTouchOutside(true);
               mprogress.show();
  mAuth.createUserWithEmailAndPassword(email,password)
        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    String devicetoken= FirebaseInstanceId.getInstance().getToken();
                    String currentUserID=mAuth.getCurrentUser().getUid();
                    RootRef.child("Users").child(currentUserID).setValue("");

                    RootRef.child("Users").child(currentUserID).child("device_token")
                            .setValue(devicetoken);

                    mprogress.dismiss();
                    Toast.makeText(RegisterActivity.this,"Account created successful! ",Toast.LENGTH_SHORT).show();
                    SendUserToMainActivity();
                }else{
                            mprogress.dismiss();
                    String message = task.getException().toString();
                    Toast.makeText(RegisterActivity.this,"Error :"+message,Toast.LENGTH_SHORT).show();
                }

            }
        });


        }
    }
    public void SendUserToLoginActivity(){
        Intent loginIntent = new Intent(RegisterActivity.this,LogInActivity.class);
        startActivity(loginIntent);


    }
    public void SendUserToMainActivity(){
        Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();

    }

}
