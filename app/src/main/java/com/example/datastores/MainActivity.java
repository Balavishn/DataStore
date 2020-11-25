package com.example.datastores;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import de.hdodenhof.circleimageview.CircleImageView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {
    TextView name;
    FirebaseAuth auth;
    DatabaseReference ref;
    StorageReference mStorageRef;
    CircleImageView tool;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar tb=(Toolbar) findViewById(R.id.tool);
        setSupportActionBar(tb);
        MainActivity.super.setTitle("");
        tool=(CircleImageView)findViewById(R.id.profile_toolbar);
        name=(TextView)findViewById(R.id.textView4);
        auth=FirebaseAuth.getInstance();
        final String userid=auth.getCurrentUser().getUid();
        if(auth.getCurrentUser()==null){
            startActivity(new Intent(getApplicationContext(),Login.class));
            finish();
        }
        ref= FirebaseDatabase.getInstance().getReference().child("Member").child(userid);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("hibab",dataSnapshot.child("name").getValue().toString());
                name.setText("Welcome "+dataSnapshot.child("name").getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mStorageRef = FirebaseStorage.getInstance().getReference();
        StorageReference pro=mStorageRef.child("user/"+userid+"/profile.jpg");
        pro.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(tool);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.logout_tool){
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(),Login.class));
        }
        return true;
    }

    public void show(View v){
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
            Toast.makeText(getApplicationContext(),"Permission Already Granted",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(),Show_detail.class));
        }
        else{
            Toast.makeText(getApplicationContext(),"Permission Needed",Toast.LENGTH_SHORT).show();
            requestpermiss();
        }

    }

    private void requestpermiss() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)){
           Toast.makeText(getApplicationContext(),"Permission Needed",Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }
        else{
            Log.d("permision","req");
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==1){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(getApplicationContext(),"Permission Granted",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),Show_detail.class));
            }
            else{
                Toast.makeText(getApplicationContext(),"Permission Denied",Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void add_data(View v){
        startActivity(new Intent(getApplicationContext(),Add_data.class));
    }

}