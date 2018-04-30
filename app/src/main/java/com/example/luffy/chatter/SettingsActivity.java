package com.example.luffy.chatter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    private DatabaseReference mUserDatabase;
    private FirebaseUser mCurrentUser;


    // android layout

    private CircleImageView mDisplayImage;
    private TextView mName;
    private TextView mStatus;

    Button btn_change_image;
    Button btn_change_status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mDisplayImage = (CircleImageView) findViewById(R.id.setting_image);
        mName = (TextView) findViewById(R.id.setting_display_name);
        mStatus = (TextView) findViewById(R.id.setting_status);
        btn_change_image = (Button) findViewById(R.id.btn_setting_avatar);
        btn_change_status =(Button) findViewById(R.id.btn_setting_status);

        btn_change_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String status_value = mStatus.getText().toString();

                Intent statusIntent = new Intent(SettingsActivity.this,StatusActivity.class);
                statusIntent.putExtra("status_value", status_value);
                startActivity(statusIntent);
            }
        });

        btn_change_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = current_user.getUid();

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("User").child(uid);

        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("name").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();
                String status = dataSnapshot.child("status").getValue().toString();
                String thumb_image = dataSnapshot.child("thumb_image").getValue().toString();


                mName.setText(name);
                mStatus.setText(status);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





    }
}
