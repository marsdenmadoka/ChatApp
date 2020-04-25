package marsmadoka98.gmail.whatsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
private Toolbar mtoolbar;
private ViewPager myviewpager;
private TabLayout myTablayout;
private TabsFragmentAdapter tabsFragmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
}
