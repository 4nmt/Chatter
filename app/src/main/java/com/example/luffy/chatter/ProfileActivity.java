package com.example.luffy.chatter;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {


    ImageView mProfileImage;

    TextView mProfileName,mProfileStatus,mProfileFriendsCount;

    Button mProfileSendReqBtn;
    ProgressDialog loadingBar;
    DatabaseReference mUserDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        String user_id = getIntent().getStringExtra("user_id");

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("User").child(user_id);


        mProfileImage = (ImageView) findViewById(R.id.profile_image);
        mProfileSendReqBtn = (Button) findViewById(R.id.profile_send_req_btn);
        mProfileName = (TextView) findViewById(R.id.profile_name);
        mProfileStatus = (TextView) findViewById(R.id.profile_name);
        mProfileFriendsCount = (TextView) findViewById(R.id.profile_name);


        loadingBar = new ProgressDialog(ProfileActivity.this);
        loadingBar.setTitle("Đang tải dữ liệu người dùng");
        loadingBar.setMessage("Làm ơn đợi một lát ...");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("name").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();
                String status = dataSnapshot.child("status").getValue().toString();


                mProfileName.setText(name);
                mProfileStatus.setText(status);

                Picasso.get().load(image).placeholder(R.drawable.avatar_default).into(mProfileImage);

                loadingBar.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
