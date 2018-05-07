package com.example.luffy.chatter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.iid.FirebaseInstanceId;

public class LoginActivity extends AppCompatActivity {


    Button btnLogin;


    EditText loginEmail;
    EditText loginPassword;

    ProgressDialog loadingBar;
    private FirebaseAuth mAuth;
    DatabaseReference mUserDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnLogin = (Button) findViewById(R.id.btnLogin);


        loginEmail = (EditText) findViewById(R.id.login_email);
        loginPassword = (EditText) findViewById(R.id.login_pass);

        loadingBar = new ProgressDialog(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled (true);


        mAuth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String email = loginEmail.getText().toString();
                String password = loginPassword.getText().toString();


                loadingBar.setTitle("Đăng nhâp");
                loadingBar.setMessage("Làm ơn đợi một lát, để chúng tôi cập nhật thông tin cho bạn");
                loadingBar.show();
                if(TextUtils.isEmpty(email))
                {
                    Toast.makeText(LoginActivity.this, "Email không được rỗng",
                            Toast.LENGTH_SHORT).show();

                }
                if(TextUtils.isEmpty(password))
                {
                    Toast.makeText(LoginActivity.this, "Password không được rỗng và phải trên 6 kí tự",
                            Toast.LENGTH_SHORT).show();

                }
                else
                    loginUser(email,password);
            }
        });

    }

    private void loginUser(String email, String password) {


        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    loadingBar.dismiss();

                  //  String current_user_id = mAuth.getCurrentUser().getUid();
                  //  String deviceToken = FirebaseInstanceId.getInstance().getToken();

                  //  mUserDatabase.child(current_user_id).child("device_token").setValue(deviceToken).addOnSuccessListener(new OnSuccessListener<Void>() {
                  //      @Override
                  //      public void onSuccess(Void aVoid) {




                 //       }
                //    });
                    Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(mainIntent);
                    finish();


                } else {

                    loadingBar.hide();

                    String task_result = task.getException().getMessage().toString();

                    Toast.makeText(LoginActivity.this, "Error : " + task_result, Toast.LENGTH_LONG).show();

                }

            }
        });


    }

}
