package com.example.luffy.chatter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    Button btnRegis;

    EditText edtName;
    EditText edtEmail;
    EditText edtPass;

    ProgressDialog loadingBar;


    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btnRegis = (Button) findViewById(R.id.btnRegister);

        edtName = (EditText) findViewById(R.id.reg_name);
        edtEmail = (EditText) findViewById(R.id.reg_email);
        edtPass = (EditText) findViewById(R.id.reg_pass);

        loadingBar = new ProgressDialog(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled (true);


        mAuth = FirebaseAuth.getInstance();

        btnRegis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String display_name = edtName.getText().toString();
                String email = edtEmail.getText().toString();
                String password = edtPass.getText().toString();

                loadingBar.setTitle("Tạo tài khoản mới");
                loadingBar.setMessage("Làm ơn đợi một lát, để chúng tôi tạo tài khoản cho bạn");
                loadingBar.show();
                if(TextUtils.isEmpty(display_name))
                {
                    Toast.makeText(RegisterActivity.this, "Tên hiển thị không được rỗng",
                            Toast.LENGTH_SHORT).show();

                }
                if(TextUtils.isEmpty(email))
                {
                    Toast.makeText(RegisterActivity.this, "Email không được rỗng",
                            Toast.LENGTH_SHORT).show();

                }
                if(TextUtils.isEmpty(password))
                {
                    Toast.makeText(RegisterActivity.this, "Password không được rỗng và phải trên 6 kí tự",
                            Toast.LENGTH_SHORT).show();

                }
                else
                    register_user(display_name,email,password);
            }
        });
    }

    private void register_user(final String display_name, String email , String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                            String uid = current_user.getUid();

                            mDatabase = FirebaseDatabase.getInstance().getReference().child("User").child(uid);

                            HashMap<String,String> userMap = new HashMap<>();

                            userMap.put("name", display_name);
                            userMap.put("image", "default");
                            userMap.put("status", "Đang sử dụng app");
                            userMap.put("thumb_image", "default");

                            mDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Intent mainIntent = new Intent(RegisterActivity.this,MainActivity.class);
                                    mainIntent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(mainIntent);
                                    finish();
                                }
                            });



                        } else {

                            Toast.makeText(RegisterActivity.this, "Đăng kí thất bại",
                                    Toast.LENGTH_SHORT).show();
                        }
                        loadingBar.dismiss();
                    }
                });
    }


}
