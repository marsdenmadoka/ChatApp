package marsmadoka98.gmail.whatsapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
public class FriendsRequestFragment extends Fragment {
    private View RequestFragmentsView;
    private RecyclerView MyRequestRecyclerView;

    private DatabaseReference ChatRequestRef,UsersRef;
    FirebaseAuth mAuth;
    String currentUserID;

    public FriendsRequestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mAuth= FirebaseAuth.getInstance();
        currentUserID=mAuth.getCurrentUser().getUid();
        UsersRef=FirebaseDatabase.getInstance().getReference().child("Users");
        ChatRequestRef= FirebaseDatabase.getInstance().getReference().child("Chat Requests").child(currentUserID);//we want to retrieve the data in chat request of our db

        RequestFragmentsView=inflater.inflate(R.layout.fragment_friends_request, container, false);
        MyRequestRecyclerView=RequestFragmentsView.findViewById(R.id.friend_request_recycler);
        MyRequestRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


    return  RequestFragmentsView;
    }


    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions options =
                new FirebaseRecyclerOptions.Builder<Contacts>() //Contacts here is our model class that we created Contacts.java
                        .setQuery(ChatRequestRef, Contacts.class)
                        .build();
        FirebaseRecyclerAdapter<Contacts,RequestViewHolder> adapter =
                new FirebaseRecyclerAdapter<Contacts, RequestViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final RequestViewHolder holder, int position, @NonNull Contacts model) {

                        holder.itemView.findViewById(R.id.request_accept_button).setVisibility(View.VISIBLE);
                        holder.itemView.findViewById(R.id.request_Cancel_button).setVisibility(View.VISIBLE);

                        final String list_user_id=getRef(position).getKey();//this gets the keys/ID of each users
                        DatabaseReference getTypeRef= getRef(position).child("request_type").getRef();
                        getTypeRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()){
                                    String type=dataSnapshot.getValue().toString();
                                    if(type.equals("received")){ //getting the sent friend requests
                                    UsersRef.child(list_user_id).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if(dataSnapshot.hasChild("image")){ //if there is a profile image
                                                final String requestUsername=dataSnapshot.child("name").getValue().toString();
                                                final String requestUserstatus=dataSnapshot.child("status").getValue().toString();
                                                final String requestUserimage=dataSnapshot.child("image").getValue().toString();

                                                 holder.username.setText(requestUsername);
                                                 holder.userstatus.setText(requestUserstatus);
                                                Picasso.get().load(requestUserimage).into(holder.profileimage);


                                            }else{ //if no profile pic
                                                final String requestUsername=dataSnapshot.child("name").getValue().toString();
                                                final String requestUserstatus=dataSnapshot.child("status").getValue().toString();
                                                holder.username.setText(requestUsername);
                                                holder.userstatus.setText(requestUserstatus);

                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

                                    }
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                    @NonNull
                    @Override
                    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.find_friends_users_display,parent,false);
                    RequestViewHolder holder = new RequestViewHolder(view);
                    return holder;
                    }
                };
        MyRequestRecyclerView.setAdapter(adapter);
        adapter.startListening();
    }

public static class RequestViewHolder extends RecyclerView.ViewHolder{
    TextView username,userstatus;
        CircleImageView profileimage;
        Button AcceptButton,CancelButton;
    public RequestViewHolder(@NonNull View itemView) {
        super(itemView);
        username=itemView.findViewById(R.id.user_profile_name);
        userstatus=itemView.findViewById(R.id.user_profile_status);
        profileimage=itemView.findViewById(R.id.users_profile_image);

        AcceptButton=itemView.findViewById(R.id.request_accept_button);
        CancelButton=itemView.findViewById(R.id.request_Cancel_button);

    }
}


}
