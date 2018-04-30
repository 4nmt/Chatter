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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StatusActivity extends AppCompatActivity {

    EditText mStatus;
    Button btnSaveStt;

    private DatabaseReference mStatusDatabase;
    private FirebaseUser mStatusUser;

    ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        mStatus = (EditText) findViewById(R.id.status_change);
        btnSaveStt = (Button) findViewById(R.id.btn_status_change);

        String status_value = getIntent().getStringExtra("status_value");

        mStatus.setText(status_value);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true) ;

        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = current_user.getUid();

        mStatusDatabase = FirebaseDatabase.getInstance().getReference().child("User").child(uid);



        btnSaveStt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadingBar = new ProgressDialog(StatusActivity.this);

                loadingBar.setTitle("Lưu trạng thái");
                loadingBar.setMessage("Làm ơn đợi một lát! Để chúng tôi cập nhật trạng thái cho bạn");
                loadingBar.show();
                String status = mStatus.getText().toString();
                mStatusDatabase.child("status").setValue(status).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isComplete()){
                            loadingBar.dismiss();
                        }else{

                            Toast.makeText(getApplicationContext(),"Có một vài lỗi đã xảy ra! Vui lòng kiểm tra lại",Toast.LENGTH_SHORT).show();
                            loadingBar.hide();
                        }
                    }
                });

            }
        });
    }
}
