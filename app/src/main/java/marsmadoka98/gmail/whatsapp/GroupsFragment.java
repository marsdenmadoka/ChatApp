package marsmadoka98.gmail.whatsapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 */
public class GroupsFragment extends Fragment {
    private View groupFragmentView;
    private ListView listView;
    private ArrayAdapter<String>arrayAdapter;
    private ArrayList<String>list_of_groups = new ArrayList<>();
    private DatabaseReference GroupRef;

    public GroupsFragment() {
        // Required empty public constructor
    }

//displaying our groups in listView fetching it from our database
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment working with ListView in Fragments
        groupFragmentView=inflater.inflate(R.layout.fragment_groups, container, false);
       GroupRef= FirebaseDatabase.getInstance().getReference().child("Groups"); //note the name must be the same with that in the database else it will crash and never work

        listView=(ListView)groupFragmentView.findViewById(R.id.list_view);
       arrayAdapter=new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,list_of_groups);
       listView.setAdapter(arrayAdapter);

       RetriveAndDisplayGroups();

//open new group for caht
       listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) { //int i is the position of the item in listview and long l is the id of the item in listview
          String curentGroupName=adapterView.getItemAtPosition(position).toString();
               Intent groupChatIntent = new Intent(getContext(),GroupChatActivity.class); //getContext() since we are using fragments
            groupChatIntent.putExtra("groupName",curentGroupName);
               startActivity(groupChatIntent);
           }
       });

        return groupFragmentView;
    }

    public void RetriveAndDisplayGroups(){

        GroupRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Set<String> set= new HashSet<>();//to prevent dupliction of values when database is update eg when new group is added the dataase is update and database will have old values of it
                //that is when db is updated the list of groups become old and new list is created and therefore if we dint use hashsets our groups list will be duplicated and repeated in the listview
                Iterator iterator = dataSnapshot.getChildren().iterator();//we using iterator to loop through the names in the db and display them
                while (iterator.hasNext()){
               set.add(((DataSnapshot)iterator.next()).getKey()); //set to remove duplication,iterator to display and datasnashot to fetch from db...read collection and generics topic to understand iterator and Hashset
//get key get all the names
                }
                list_of_groups.clear();
                list_of_groups.addAll(set);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
 