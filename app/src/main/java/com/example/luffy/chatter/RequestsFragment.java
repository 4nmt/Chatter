package com.example.luffy.chatter;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;



public class RequestsFragment extends Fragment {


    RecyclerView mRequestsList;

    View mMainView;

    DatabaseReference mRequestsDatabase;

    DatabaseReference mUsersDatabase;

    DatabaseReference mRootRef;
    DatabaseReference mFriendsDatabaseRef;
    DatabaseReference mFriendReqDatabaseRef;

    FirebaseUser mCurrent_user;

    FirebaseAuth mAuth;

    String online_user_id;

    public RequestsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mMainView = inflater.inflate(R.layout.fragment_requests, container, false);

        mRequestsList = mMainView.findViewById(R.id.requests_list);

        mRequestsList.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        mRequestsList.setLayoutManager(linearLayoutManager);

        mAuth = FirebaseAuth.getInstance();
        online_user_id = mAuth.getCurrentUser().getUid();
        mRequestsDatabase = FirebaseDatabase.getInstance().getReference().child("Friend_req").child(online_user_id);
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("User");

        mCurrent_user = FirebaseAuth.getInstance().getCurrentUser();
        mFriendsDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Friends");
        mFriendReqDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Friend_req");

        mRootRef = FirebaseDatabase.getInstance().getReference();
        return mMainView;
    }


    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Requests> options =
                new FirebaseRecyclerOptions.Builder<Requests>()
                        .setQuery(mRequestsDatabase, Requests.class)
                        .build();
        FirebaseRecyclerAdapter firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Requests,RequestsViewHolder>(options){

            @NonNull
            @Override
            public RequestsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.friends_request_all_users_layout, parent, false);

                return new RequestsViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final RequestsViewHolder holder, int position, @NonNull Requests model) {


                final String user_id = getRef(position).getKey();

                DatabaseReference get_type_ref = getRef(position).child("request_type").getRef();

                get_type_ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){

                            String request_type = dataSnapshot.getValue().toString();

                            if("received".equals(request_type)){
                                mUsersDatabase.child(user_id).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        final String userName = dataSnapshot.child("name").getValue().toString();
                                        final String userStatus = dataSnapshot.child("status").getValue().toString();
                                        final String userImage = dataSnapshot.child("image").getValue().toString();

                                        holder.setUserName(userName);
                                        holder.setUserImage(userImage);
                                        holder.setStatus(userStatus);

                                        holder.mView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                CharSequence options[] = new CharSequence[]{"Chấp nhận lời mòi kết bạn", "Từ chối lời mòi kết bạn"};

                                                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                                                builder.setTitle("Lựa chọn yêu cầu kết bạn");
                                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {

                                                        //Click Event for each item.
                                                        if(i == 0){

                                                                final String currentDate = DateFormat.getDateTimeInstance().format(new Date());

                                                                Map friendsMap = new HashMap();
                                                                friendsMap.put("Friends/" + mCurrent_user.getUid() + "/" + user_id + "/date", currentDate);
                                                                friendsMap.put("Friends/" + user_id + "/" + mCurrent_user.getUid() + "/date", currentDate);


                                                                friendsMap.put("Friend_req/" + mCurrent_user.getUid() + "/" + user_id, null);
                                                                friendsMap.put("Friend_req/" + user_id + "/" + mCurrent_user.getUid(), null);

                                                            mRootRef.updateChildren(friendsMap, new DatabaseReference.CompletionListener() {
                                                                @Override
                                                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {


                                                                    if (databaseError == null) {

                                                                        Toast.makeText(getContext(), "Chấp nhận yêu cầu thành công ..", Toast.LENGTH_SHORT).show();

                                                                    } else {

                                                                        String error = databaseError.getMessage();

                                                                        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                                                                    }

                                                                }
                                                            });

                                                        }

                                                        if(i == 1){
                                                            mFriendReqDatabaseRef.child(mCurrent_user.getUid()).child(user_id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {

                                                                    mFriendReqDatabaseRef.child(user_id).child(mCurrent_user.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void aVoid) {

                                                                            Toast.makeText(getContext(), "Hủy yêu cầu kết bạn thành công ..", Toast.LENGTH_SHORT).show();



                                                                        }
                                                                    });

                                                                }
                                                            });

                                                        }

                                                    }
                                                });

                                                builder.show();
                                            }
                                        });
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                            }
                            else if ("sent".equals(request_type)){

                                Button req_sent_btn = holder.mView.findViewById(R.id.request_profile_accept_btn);
                                req_sent_btn.setText("Hủy bỏ lời mời kết bạn");

                                holder.mView.findViewById(R.id.request_profile_decline_btn).setVisibility(View.INVISIBLE);

                                mUsersDatabase.child(user_id).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        final String userName = dataSnapshot.child("name").getValue().toString();
                                        final String userStatus = dataSnapshot.child("status").getValue().toString();
                                        final String userImage = dataSnapshot.child("image").getValue().toString();

                                        holder.setUserName(userName);
                                        holder.setUserImage(userImage);
                                        holder.setStatus(userStatus);

                                        holder.mView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                CharSequence options[] = new CharSequence[]{ "Hủy bỏ lời mòi kết bạn"};

                                                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                                                builder.setTitle("Lựa chọn yêu cầu kết bạn");
                                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {

                                                        if(i == 0){
                                                            mFriendReqDatabaseRef.child(mCurrent_user.getUid()).child(user_id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {

                                                                    mFriendReqDatabaseRef.child(user_id).child(mCurrent_user.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void aVoid) {

                                                                            Toast.makeText(getContext(), "Hủy yêu cầu kết bạn thành công ..", Toast.LENGTH_SHORT).show();

                                                                        }
                                                                    });

                                                                }
                                                            });

                                                        }

                                                    }
                                                });

                                                builder.show();
                                            }
                                        });
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });



            }
        };
        firebaseRecyclerAdapter.startListening();
        mRequestsList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class RequestsViewHolder extends RecyclerView.ViewHolder{

        View mView;
        public RequestsViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setUserName(String userName){
            TextView mDisplayName = mView.findViewById(R.id.request_profile_name);
            mDisplayName.setText(userName);
        }

        public void setUserImage( String thumbImage  ) {

            final CircleImageView thumb_image = (CircleImageView) mView.findViewById(R.id.request_profile_image);

            Picasso.get().load(thumbImage).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.avatar_default)
                    .into(thumb_image, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });

        }
        public void setStatus(String userStatus) {
            TextView user_status = mView.findViewById(R.id.request_profile_status);
            user_status.setText(userStatus);
        }
    }

}
