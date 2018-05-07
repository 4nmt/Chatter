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

public class ChangePassword extends AppCompatActivity {
    private EditText newpassword;
    private FirebaseAuth auth;
    private ProgressDialog dialog;
    private Button btn_changepass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        dialog=new ProgressDialog(this);
        newpassword=findViewById(R.id.c_pass);
        auth=FirebaseAuth.getInstance();
        btn_changepass= findViewById(R.id.btn_changepassword);

        btn_changepass.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String newPassword = newpassword.getText().toString();

                dialog.setMessage("Đang đổi mật khẩu");
                dialog.show();

                user.updatePassword(newPassword)
                        .addOnCompleteListener(new OnCompleteListener<Void>()
                        {
                            @Override
                            public void onComplete(@NonNull Task<Void> task)
                            {
                                if (task.isSuccessful()) {
                                    dialog.dismiss();
                                    Toast.makeText(ChangePassword.this,"Mật khẩu đã được đổi",Toast.LENGTH_LONG).show();
                                    auth.signOut();
                                    finish();
                                    Intent i= new Intent(ChangePassword.this,StartActivity.class);
                                    startActivity(i);


                                }
                                else
                                {
                                    dialog.dismiss();
                                    Toast.makeText(ChangePassword.this,"Mật khẩu không đổi được",Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });

    }
}
