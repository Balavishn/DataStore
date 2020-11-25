package com.example.datastores;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Add_data extends AppCompatActivity {
    EditText title,data;
    FirebaseAuth auth;
    String userid;
    DatabaseReference ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);
        title=(EditText)findViewById(R.id.editTextTextPersonName3);
        data=(EditText)findViewById(R.id.dataofuser);
        auth=FirebaseAuth.getInstance();
        userid = auth.getCurrentUser().getUid();
    }

    public void add(View v){
        String t=title.getText().toString();
        String d=data.getText().toString();
        ref= FirebaseDatabase.getInstance().getReference().child("Member").child(userid).child("data");
        Dataof da= new Dataof(t,d);
        ref.child(t).setValue(da);
        Toast.makeText(getApplicationContext(),"pass",Toast.LENGTH_SHORT).show();
        Intent home=new Intent(getApplicationContext(),MainActivity.class);
        startActivity(home);
    }
}