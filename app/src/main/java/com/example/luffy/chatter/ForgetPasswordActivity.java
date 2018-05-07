package com.example.luffy.chatter;

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

public class ForgetPasswordActivity extends AppCompatActivity {

    private EditText passwordEmail;
    private Button resetPassword;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);


        passwordEmail = (EditText) findViewById(R.id.email_reset_text);
        resetPassword = (Button) findViewById(R.id.reset_btn);
        firebaseAuth = FirebaseAuth.getInstance();

        resetPassword.setOnClickListener( new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String userEmail = passwordEmail.getText().toString().trim();

                if(userEmail.equals(""))
                {
                    Toast.makeText(ForgetPasswordActivity.this, "Vui lòng nhập email", Toast.LENGTH_SHORT).show();
                }
                else{
                    firebaseAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(ForgetPasswordActivity.this, "Đã gửi mail reset password", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(ForgetPasswordActivity.this, MainActivity.class));
                            }
                            else{
                                Toast.makeText(ForgetPasswordActivity.this, "Email của bạn không tồn tại", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}
