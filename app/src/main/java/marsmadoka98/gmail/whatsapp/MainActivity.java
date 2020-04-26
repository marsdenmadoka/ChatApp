package marsmadoka98.gmail.whatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

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
}
