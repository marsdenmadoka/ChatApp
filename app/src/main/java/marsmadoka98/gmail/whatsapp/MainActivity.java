package marsmadoka98.gmail.whatsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
private Toolbar mtoolbar;
private ViewPager myviewpager;
private TabLayout myTablayout;
private TabsFragmentAdapter tabsFragmentAdapter;
//private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
   // private FirebaseAuth.AuthStateListener mAuthListerner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    mAuth=FirebaseAuth.getInstance();
   // currentUser=mAuth.getCurrentUser();

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

    /*@Override
    protected void onStart() {
        super.onStart();
        if(currentUser==null){ //if user is not login
            SendUserToLogin();


        }
    }

     */

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

        }

        if(item.getItemId()==R.id.logout_option){
            mAuth.signOut();
            SendUserToLogin();
        }
        return super.onOptionsItemSelected(item);
    }

    public void SendUserToLogin(){
        Intent loginIntent = new Intent(MainActivity.this,LogInActivity.class);
        startActivity(loginIntent);

    }
}
