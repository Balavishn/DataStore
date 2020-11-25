package com.example.datastores;

import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class Add_Detail extends AppCompatActivity {
    EditText name,nic;
    String n,nn,userid;
    FirebaseAuth auth;
    DatabaseReference ref;
    private static final String TAG="hi";
    CircleImageView profile;
    StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__detail);
        name=(EditText)findViewById(R.id.Name);
        nic=(EditText)findViewById(R.id.Nick);
        profile=(CircleImageView)findViewById(R.id.profile);

        auth=FirebaseAuth.getInstance();
        userid=auth.getCurrentUser().getUid();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        StorageReference pro=mStorageRef.child("user/"+userid+"/profile.jpg");
        pro.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profile);
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galary=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galary,1000);
            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1000){
            if(resultCode== Activity.RESULT_OK){
                Uri imageuri=data.getData();
                profile.setImageURI(imageuri);
                uploadimage(imageuri);
            }
        }
    }

    private void uploadimage(final Uri image) {
        StorageReference file=mStorageRef.child("user/"+userid+"/profile.jpg");
        file.putFile(image).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Picasso.get().load(image).into(profile);
            }
        });

    }

    public void update(View v){
        n=name.getText().toString().trim();
        nn=nic.getText().toString().trim();
        Log.d(TAG,userid);
        ref= FirebaseDatabase.getInstance().getReference().child("Member");
        Store s= new Store(n,nn);
        ref.child(userid).setValue(s);
        Toast.makeText(getApplicationContext(),"pass",Toast.LENGTH_SHORT).show();
        Intent home=new Intent(getApplicationContext(),MainActivity.class);
        startActivity(home);
    }
}