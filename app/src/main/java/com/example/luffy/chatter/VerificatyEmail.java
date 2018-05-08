package com.example.luffy.chatter;

import android.app.ProgressDialog;
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

public class VerificatyEmail extends AppCompatActivity {
    private EditText email;
    private FirebaseAuth auth;
    private ProgressDialog dialog;
    private Button btn_verifyemail;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verificaty_email);

        dialog=new ProgressDialog(this);

        auth=FirebaseAuth.getInstance();
        btn_verifyemail= findViewById(R.id.btn_verify_email);
        user = FirebaseAuth.getInstance().getCurrentUser();

        btn_verifyemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setMessage("Đang gửi email xác nhận");
                dialog.show();
                btn_verifyemail.setEnabled(false);
                user.sendEmailVerification()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                btn_verifyemail.setEnabled(true);
                                if (task.isSuccessful()) {
                                    dialog.dismiss();
                                    Toast.makeText(VerificatyEmail.this,"Email đã được gửi",Toast.LENGTH_LONG).show();

                                }
                            }
                        });
            }
        });

    }
}
