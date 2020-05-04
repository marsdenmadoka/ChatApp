package marsmadoka98.gmail.whatsapp;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
//you can also choose to use the new Firebase RecyclerView class adapter instead of this
public class messagesAdapter extends RecyclerView.Adapter <messagesAdapter.MessageViewHolder> {

    private List<messages> userMessagesList;
    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;
    public messagesAdapter(List<messages> userMessagesList){

        this.userMessagesList=userMessagesList;
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder{

        public TextView sendermessagetext,receivermessagetext;
        public CircleImageView reciverimage;
        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);

            sendermessagetext=itemView.findViewById(R.id.sender_messages);
            receivermessagetext=itemView.findViewById(R.id.receiver_messages);
            reciverimage=itemView.findViewById(R.id.message_profile_image);
        }
    }
    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_message_layout,parent,false);

       mAuth=FirebaseAuth.getInstance();
        return  new MessageViewHolder(view);

    }


    @Override
    public void onBindViewHolder(@NonNull final MessageViewHolder holder, int position) {

String messageSenderID=mAuth.getCurrentUser().getUid();
messages message=userMessagesList.get(position);
String fromUserID=message.getFrom();
String fromMessageType=message.getType();
usersRef= FirebaseDatabase.getInstance().getReference().child("Users").child(fromUserID);

usersRef.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        if(dataSnapshot.hasChild("image")){
            String recImage=dataSnapshot.child("image").getValue().toString();
            Picasso.get().load(recImage).placeholder(R.drawable.profile_image).into(holder.reciverimage);
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
});
if(fromMessageType.equals("text")){

    holder.receivermessagetext.setVisibility(View.INVISIBLE);
   holder.reciverimage.setVisibility(View.INVISIBLE);
    holder.sendermessagetext.setVisibility(View.INVISIBLE);

    if (fromUserID.equals(messageSenderID)) {
        holder.sendermessagetext.setVisibility(View.VISIBLE);
        holder.sendermessagetext.setBackgroundResource(R.drawable.sender_message_layout);
        holder.sendermessagetext.setText(message.getMessage());
        holder.sendermessagetext.setTextColor(Color.BLACK);



    }else {


        holder.reciverimage.setVisibility(View.VISIBLE);
        holder.receivermessagetext.setVisibility(View.VISIBLE);

        holder.receivermessagetext.setBackgroundResource(R.drawable.receiver_message_layout);
        holder.receivermessagetext.setText(message.getMessage());
        holder.receivermessagetext.setTextColor(Color.BLACK);
    }
}

    }



    @Override
    public int getItemCount() {
        return userMessagesList.size();
    }



}
