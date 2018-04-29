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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {


    Button btnLogin;


    EditText loginEmail;
    EditText loginPassword;

    ProgressDialog loadingBar;
    private FirebaseAuth mAuth;

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
                    login_user(email,password);
            }
        });

    }

    private void login_user(String email, String password) {

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            loadingBar.setTitle("Tạo tài khoản mới");
                            loadingBar.setMessage("Làm ơn đợi một lát, để chúng tôi tạo tài khoản cho bạn");

                            Intent mainIntent = new Intent(LoginActivity.this,MainActivity.class);
                            startActivity(mainIntent);
                            finish();
                        } else {

                            Toast.makeText(LoginActivity.this, "Đăng nhập thất bại.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    loadingBar.dismiss();

                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if(item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, StartActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

        return  true;
    }
}
