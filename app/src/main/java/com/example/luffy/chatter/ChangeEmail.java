package com.example.luffy.chatter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangeEmail extends AppCompatActivity {
    private EditText newemail;
    private FirebaseAuth auth;
    private ProgressDialog dialog;
    private Button btn_changeemail1;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_email);
        dialog=new ProgressDialog(this);
        newemail=findViewById(R.id.newemail);
        auth=FirebaseAuth.getInstance();
        btn_changeemail1= findViewById(R.id.btn_change_email);
        user = FirebaseAuth.getInstance().getCurrentUser();
        btn_changeemail1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                String newEmail = newemail.getText().toString();

                dialog.setMessage("Đang đổi email");
                dialog.show();

                user.updateEmail(newEmail)
                        .addOnCompleteListener(new OnCompleteListener<Void>()
                        {
                            @Override
                            public void onComplete(@NonNull Task<Void> task)
                            {
                                if (task.isSuccessful()) {
                                    dialog.dismiss();
                                    Toast.makeText(ChangeEmail.this,"Email đã được đổi",Toast.LENGTH_LONG).show();
                                    auth.signOut();
                                    finish();
                                    Intent i= new Intent(ChangeEmail.this,StartActivity.class);
                                    startActivity(i);


                                }
                                else
                                {
                                    dialog.dismiss();
                                    Toast.makeText(ChangeEmail.this,"Email hiện không đổi được",Toast.LENGTH_LONG).show();
                                    finish();
                                    Intent i= new Intent(ChangeEmail.this,MainActivity.class);
                                    startActivity(i);
                                }
                            }
                        });
            }
        });
    }
}
