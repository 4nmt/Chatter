package com.example.luffy.chatter;

import android.content.Context;
import android.graphics.Color;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

<<<<<<< HEAD
=======
import com.example.luffy.chatter.Messages;
import com.example.luffy.chatter.R;
>>>>>>> master
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by AkshayeJH on 24/07/17.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder>{


    private List<Messages> mMessageList;
    private DatabaseReference mUserDatabase;

    public MessageAdapter(List<Messages> mMessageList) {

        this.mMessageList = mMessageList;

    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_single_layout ,parent, false);

        return new MessageViewHolder(v);

    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        public TextView messageText;
        public CircleImageView profileImage;
        public TextView displayName;
        public ImageView messageImage;

        public MessageViewHolder(View view) {
            super(view);

            messageText = (TextView) view.findViewById(R.id.message_text_layout);
            profileImage = (CircleImageView) view.findViewById(R.id.message_profile_layout);
            displayName = (TextView) view.findViewById(R.id.name_text_layout);
            messageImage = (ImageView) view.findViewById(R.id.message_image_layout);

        }
    }

    @Override
    public void onBindViewHolder(final MessageViewHolder viewHolder, int i) {

        Messages c = mMessageList.get(i);

        String from_user = c.getFrom();
        String message_type = c.getType();


<<<<<<< HEAD
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("User").child(from_user);
=======
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(from_user);
>>>>>>> master

        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("name").getValue().toString();
<<<<<<< HEAD
                String image = dataSnapshot.child("image").getValue().toString();

                viewHolder.displayName.setText(name);

                Picasso.get().load(image)
                        .placeholder(R.drawable.avatar_default).into(viewHolder.profileImage);
=======
                String image = dataSnapshot.child("thumb_image").getValue().toString();

                viewHolder.displayName.setText(name);



                //Picasso.with(viewHolder.profileImage.getContext()).load(image).placeholder(R.drawable.avatar_default).into(viewHolder.profileImage);
                Picasso.get().load(image).placeholder(R.drawable.avatar_default).into(viewHolder.profileImage);
>>>>>>> master

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if(message_type.equals("text")) {

            viewHolder.messageText.setText(c.getMessage());
            viewHolder.messageImage.setVisibility(View.INVISIBLE);


        } else {

            viewHolder.messageText.setVisibility(View.INVISIBLE);
<<<<<<< HEAD
            Picasso.get().load(c.getMessage())
                    .placeholder(R.drawable.avatar_default).into(viewHolder.messageImage);
=======
            //Picasso.with(viewHolder.profileImage.getContext()).load(c.getMessage()).placeholder(R.drawable.avatar_default).into(viewHolder.messageImage);
            Picasso.get().load(c.getMessage()).placeholder(R.drawable.avatar_default).into(viewHolder.profileImage);


>>>>>>> master

        }

    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }






}
