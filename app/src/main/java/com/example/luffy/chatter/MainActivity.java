package com.example.luffy.chatter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private FirebaseUser user;

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private TabsPagerAdapter tabsPagerAdapter;

    private DatabaseReference mUserRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        viewPager = (ViewPager) findViewById(R.id.main_tabs_pager);

        tabsPagerAdapter = new TabsPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tabsPagerAdapter);
        tabLayout = (TabLayout) findViewById(R.id.main_tabs);
        tabLayout.setupWithViewPager(viewPager);
        user = FirebaseAuth.getInstance().getCurrentUser();


        if (mAuth.getCurrentUser() != null) {


            mUserRef = FirebaseDatabase.getInstance().getReference().child("User").child(mAuth.getCurrentUser().getUid());

        }


    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser == null)
        {
            LogOutUser();


        }else{
            mUserRef.child("online").setValue("true");
        }

    }
    @Override
    protected void onStop() {
        super.onStop();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null) {

            mUserRef.child("online").setValue(false);

        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.main_menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if(item.getItemId() == R.id.btn_logout){
            mAuth.signOut();
            LogOutUser();
        }
        if(item.getItemId() == R.id.btn_setting){
            Intent settingsIntent = new Intent(MainActivity.this,SettingsActivity.class);
            startActivity(settingsIntent);
           // finish();
        }
        if(item.getItemId() == R.id.btn_user_list){
            Intent settingsIntent = new Intent(MainActivity.this,UsersActivity.class);
            startActivity(settingsIntent);
        }
        if(item.getItemId() == R.id.btn_find_username)
        {
            Intent findUserIntent = new Intent(MainActivity.this, UserSearchActivity.class);
            startActivity(findUserIntent);
        }
        if(item.getItemId() == R.id.btn_changepassword){
            Intent settingsIntent = new Intent(MainActivity.this,ChangePassword.class);
            startActivity(settingsIntent);
        }
        if(item.getItemId() == R.id.btn_changeemail){
            Intent settingsIntent = new Intent(MainActivity.this,ChangeEmail.class);
            startActivity(settingsIntent);
        }
        if(item.getItemId() == R.id.btn_verifyemail){
            Intent settingsIntent = new Intent(MainActivity.this,VerificatyEmail.class);
            startActivity(settingsIntent);
        }
        if(item.getItemId() == R.id.btn_del){
            user.delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(MainActivity.this,"Tài khoản đã xóa", Toast.LENGTH_LONG).show();
                                mAuth.signOut();
                                Intent settingsIntent = new Intent(MainActivity.this,StartActivity.class);
                                startActivity(settingsIntent);
                            }
                            else
                            {

                                Toast.makeText(MainActivity.this,"Tài khoản không xóa được",Toast.LENGTH_LONG).show();
                                mAuth.signOut();
                                Intent settingsIntent = new Intent(MainActivity.this,StartActivity.class);
                                startActivity(settingsIntent);
                            }
                        }

                    });

        }

        return  true;
    }



    private void LogOutUser(){
        Intent startIntent = new Intent(MainActivity.this,StartActivity.class);
        startActivity(startIntent);
        finish();
    }

}
