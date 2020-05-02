package marsmadoka98.gmail.whatsapp;

import android.content.Intent;
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
import com.google.android.material.circularreveal.cardview.CircularRevealCardView;
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
public class ChatsFragment extends Fragment {
private View PrivateChatView;
private RecyclerView chatlist;
private DatabaseReference ChatsRefs,UsersRef;
private FirebaseAuth mAuth;
private String currentUserID;
    public ChatsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         PrivateChatView=inflater.inflate(R.layout.fragment_chats, container, false);
         mAuth=FirebaseAuth.getInstance();
         currentUserID=mAuth.getCurrentUser().getUid();
         ChatsRefs= FirebaseDatabase.getInstance().getReference().child("Contacts").child(currentUserID);
         UsersRef=FirebaseDatabase.getInstance().getReference().child("Users");
         chatlist=PrivateChatView.findViewById(R.id.chat_list);
         chatlist.setLayoutManager(new LinearLayoutManager(getContext()) );

         return PrivateChatView;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Contacts> options=
                new FirebaseRecyclerOptions.Builder< Contacts>()
                .setQuery(ChatsRefs,Contacts.class)
                .build();

        FirebaseRecyclerAdapter<Contacts,ChatsVieHolder> adapter =
                new FirebaseRecyclerAdapter<Contacts, ChatsVieHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final ChatsVieHolder holder, int position, @NonNull Contacts model) {

                        final String usersIds=getRef(position).getKey();
                        UsersRef.child(usersIds).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                  if(dataSnapshot.exists()){
                      if(dataSnapshot.hasChild("image")){
                          final String retimage=dataSnapshot.child("image").getValue().toString();
                          Picasso.get().load(retimage).into(holder.profileimage);
                      }
                      final  String retname=dataSnapshot.child("name").getValue().toString();
                      final  String retstatus=dataSnapshot.child("status").getValue().toString();
                      holder.username.setText(retname);
                      holder.userstatus.setText("Last Seen: "+"\n"+"Date " + " Time" );

                 holder.itemView.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View view) {
                         Intent chatintent = new Intent(getContext(),ChatActivity.class);
                         chatintent.putExtra("visit_user_id",usersIds);
                         chatintent.putExtra("visit_user_name",retname);
                         startActivity(chatintent);
                     }
                 });


                  }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }

                    @NonNull
                    @Override
                    public ChatsVieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.find_friends_users_display,parent,false);
                    return new ChatsVieHolder(view);
                    }
                };
        chatlist.setAdapter(adapter);
        adapter.startListening();
    }

    public static class ChatsVieHolder extends RecyclerView.ViewHolder{
    CircleImageView profileimage;
    TextView userstatus,username;
        public ChatsVieHolder(@NonNull View itemView) {
            super(itemView);
            profileimage=itemView.findViewById(R.id.users_profile_image);
            userstatus=itemView.findViewById(R.id.user_profile_status);
            username=itemView.findViewById(R.id.user_profile_name);
        }
    }
}

