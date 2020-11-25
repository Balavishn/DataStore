package com.example.datastores;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Reset_Password extends AppCompatActivity {
    EditText email;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset__password);
        email=(EditText)findViewById(R.id.reset_email);
        auth=FirebaseAuth.getInstance();
    }

    public  void reset(View v){
        auth.sendPasswordResetEmail(email.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Password link to Your email",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),Login.class));
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Enter valid email or Check internet connection",Toast.LENGTH_SHORT).show();}
            }
        });

    }
}