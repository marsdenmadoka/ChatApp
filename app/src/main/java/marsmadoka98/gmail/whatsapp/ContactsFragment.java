package marsmadoka98.gmail.whatsapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ContactsFragment extends Fragment {
    private View ContactsView;
    private RecyclerView myContactsRecycler;
    private DatabaseReference ContactsRef,UserRef;
    private FirebaseAuth mAuth;
    private String currentUserID;

    public ContactsFragment() {
        // Required empty public constructor
    }
//using the firebase recylerView Adapter

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ContactsView=inflater.inflate(R.layout.fragment_contacts, container, false);
        myContactsRecycler= ContactsView.findViewById(R.id.Contacts_recycler_viewer);

        myContactsRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mAuth=FirebaseAuth.getInstance();
        currentUserID=mAuth.getCurrentUser().getUid();
        ContactsRef= FirebaseDatabase.getInstance().getReference().child("Contacts").child(currentUserID); //Contacts here is our database Rootchild having contacts in our database
       UserRef=FirebaseDatabase.getInstance().getReference().child("Users");



        return  ContactsView;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions options=
                new FirebaseRecyclerOptions.Builder<Contacts>() //Contacts here is our model class that we created Contacts.java
                .setQuery(ContactsRef,Contacts.class)
                .build();
        FirebaseRecyclerAdapter<Contacts, contactViewHolder> adapter=
                new FirebaseRecyclerAdapter<Contacts, contactViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final contactViewHolder holder, int position, @NonNull Contacts model) {

                        String userIDs=getRef(position).getKey();
                        UserRef.child(userIDs).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.hasChild("image")){
                                    //if the username profileimae
                                    String Image=dataSnapshot.child("image").getValue().toString();
                                    String profilename=dataSnapshot.child("name").getValue().toString();
                                    String profilestatus=dataSnapshot.child("status").getValue().toString();

                                holder.username.setText(profilename);
                                holder.userstatus.setText(profilestatus);


                                    Picasso.get().load(Image).into(holder.profileimage);
                                }else{
                                    //else if the user has no profileImage
                                    String profilename=dataSnapshot.child("name").getValue().toString();
                                    String profilestatus=dataSnapshot.child("status").getValue().toString();

                                    holder.username.setText(profilename);
                                    holder.userstatus.setText(profilestatus);

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                    @NonNull
                    @Override
                    public contactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.find_friends_users_display,parent,false);
                    contactViewHolder viewHolder = new contactViewHolder(view);
                    return  viewHolder;
                    }
                };
        myContactsRecycler.setAdapter(adapter);
        adapter.startListening();
    }


    public class contactViewHolder extends RecyclerView.ViewHolder{
        TextView username,userstatus;
        CircleImageView profileimage;

        public contactViewHolder(@NonNull View itemView) {
            super(itemView);

            username=itemView.findViewById(R.id.user_profile_name);
            userstatus=itemView.findViewById(R.id.user_profile_status);
            profileimage=itemView.findViewById(R.id.users_profile_image);
        }


    }
}
