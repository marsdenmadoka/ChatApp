package marsmadoka98.gmail.whatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
private Toolbar mtoolbar;
private ViewPager myviewpager;
private TabLayout myTablayout;
private TabsFragmentAdapter tabsFragmentAdapter;
private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListerner;
    private DatabaseReference RootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    mAuth=FirebaseAuth.getInstance();
    currentUser=mAuth.getCurrentUser();
    RootRef= FirebaseDatabase.getInstance().getReference();

        mtoolbar=findViewById(R.id.main_toolbar);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setTitle("WhatsappClone");

        // Acessing the Fragment and displaying them from the TabsFragmentAdapter
        myviewpager=findViewById(R.id.viewpager);
        tabsFragmentAdapter=new TabsFragmentAdapter(getSupportFragmentManager());
        myviewpager.setAdapter(tabsFragmentAdapter);

        myTablayout=findViewById(R.id.tabslayout);
        myTablayout.setupWithViewPager(myviewpager);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(currentUser==null){ //if user is not login
            SendUserToLogin();


        }else{
            VerifyUserExistence();
        }
    }

    public void VerifyUserExistence(){ //check if user exstis
  String currentUserID=mAuth.getCurrentUser().getUid();
        RootRef.child("Users").child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
       if((dataSnapshot.child("name").exists())){
           Toast.makeText(MainActivity.this,"welcome",Toast.LENGTH_SHORT).show();

       }else{

           Toast.makeText(MainActivity.this,"you need to setup your account",Toast.LENGTH_SHORT).show();
           SendUserToSettings();
       }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.options_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public  boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId()==R.id.main_find_friends_option){

        }
        if(item.getItemId()==R.id.settings){
            SendUserToSettings();
        }

        if(item.getItemId()==R.id.logout_option){
            mAuth.signOut();
            SendUserToLogin();
        }
        if(item.getItemId()==R.id.create_group){
           RequestNewGroup();
        }
        return super.onOptionsItemSelected(item);
    }

   public void SendUserToSettings(){
    Intent intent = new Intent(MainActivity.this,SettingsActivity.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    startActivity(intent);
    finish();
    }
    public void SendUserToLogin(){
        Intent loginIntent = new Intent(MainActivity.this,LogInActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }
    public void RequestNewGroup(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this,R.style.AlertDialog);
        builder.setTitle("Enter Group Name");
        final EditText groupNameField=new EditText(MainActivity.this);
        groupNameField.setHint("eg Wanachini Group");
        builder.setView(groupNameField);

        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String groupName=groupNameField.getText().toString();
                if(TextUtils.isEmpty(groupName)){
                    Toast.makeText(MainActivity.this,"Enter Valid Group Name",Toast.LENGTH_SHORT).show();
                }else{
                 CreateNewGroup(groupName);
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
 builder.show();

    }

    public void  CreateNewGroup(final String groupName){
 RootRef.child("Groups").child(groupName).setValue("")   //create a RootChild Called Groups with child of groupname
.addOnCompleteListener(new OnCompleteListener<Void>() {
    @Override
    public void onComplete(@NonNull Task<Void> task) {
        if(task.isSuccessful()){
            Toast.makeText(MainActivity.this,groupName+"Group Created Successful",Toast.LENGTH_SHORT).show();
        }else{

            
        }
    }
});
    }

}
