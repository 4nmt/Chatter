package com.example.luffy.chatter;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserSearchActivity extends AppCompatActivity {

    private RecyclerView mUserList;
    private Button mSearchBtn;
    private EditText mSearchEditText;

    private DatabaseReference mUserDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_search);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSearchBtn = (Button) findViewById(R.id.search_btn);

        mSearchEditText = (EditText) findViewById(R.id.editText);


        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("User");
        mUserDatabase.keepSynced(true);
        mUserList = (RecyclerView) findViewById(R.id.user_list);
        mUserList.setHasFixedSize(true);
        mUserList.setLayoutManager(new LinearLayoutManager(this));


        mSearchBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String searchUserName = mSearchEditText.getText().toString();

                if(TextUtils.isEmpty(searchUserName))
                {
                    Toast.makeText(UserSearchActivity.this, "Vui lòng nhập user name", Toast.LENGTH_SHORT).show();
                }
                SearchForPeopleAndFriend(searchUserName);
            }
        });
    }



    protected void SearchForPeopleAndFriend(String searchUserName) {

        Toast.makeText(this, "Đang tìm kiếm...", Toast.LENGTH_SHORT).show();
        Query searchPeopleAndFriends = mUserDatabase.orderByChild("name")
                .startAt(searchUserName).endAt(searchUserName + "\uf8ff");

        FirebaseRecyclerOptions<Users> options =
                new FirebaseRecyclerOptions.Builder<Users>()
                        .setQuery(searchPeopleAndFriends, Users.class)
                        .build();
        FirebaseRecyclerAdapter firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Users,UsersViewHolder>(options
        )
        {

            @NonNull
            @Override
            public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.user_single_layout, parent, false);

                return new UsersViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull UsersViewHolder holder, int position, @NonNull Users model) {

                holder.setName(model.name);
                holder.setUserStatus(model.getStatus());
                holder.setUserImage(model.getImage(), getApplicationContext());

                final String user_id = getRef(position).getKey();

                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent profileItent = new Intent( UserSearchActivity.this,ProfileActivity.class);

                        profileItent.putExtra("user_id",user_id);

                        startActivity(profileItent);

                    }
                });
            }
        };
        firebaseRecyclerAdapter.startListening();
        mUserList.setAdapter(firebaseRecyclerAdapter);

    }



    public class UsersViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public UsersViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
        }

        public void setName(String name){

            TextView usernameView = mView.findViewById(R.id.user_single_name);
            usernameView.setText(name);
        }
        public void setUserStatus(String status){

            TextView userStatusView = (TextView) mView.findViewById(R.id.user_single_status);
            userStatusView.setText(status);


        }

        public void setUserImage(String thumb_image, Context ctx){

            CircleImageView userImageView = (CircleImageView) mView.findViewById(R.id.user_single_image);

            Picasso.get().load(thumb_image).placeholder(R.drawable.avatar_default).into(userImageView);

        }

    }
}
