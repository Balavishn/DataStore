package com.example.datastores;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Show_detail extends AppCompatActivity implements View.OnLongClickListener {
    String title;
    FirebaseAuth auth;
    DatabaseReference ref;
    List<Dataof> fetchdata;
    RecyclerView recyclerView;
    HelpAdapter helpAdapter;
    boolean notallow=true;
    Toolbar t;
    public boolean ischeckbox=false;
    public boolean isselectall=false;
    public boolean isnotselectall=false;
    ArrayList<String> select_list=new ArrayList<String>();
    ArrayList<String> postion_list=new ArrayList<String>();
    List<Dataof> pos_list=new ArrayList<>();
    Workbook wb=new HSSFWorkbook();
    Cell cell=null;
    String userid;
    EditText filename;
    Button export_file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_detail);
        t=(Toolbar)findViewById(R.id.recycletool);
        setSupportActionBar(t);
        setTitle(null);
        recyclerView=findViewById(R.id.viewdatacycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        fetchdata=new ArrayList<>();
        auth=FirebaseAuth.getInstance();
        userid=auth.getCurrentUser().getUid();
        ref= FirebaseDatabase.getInstance().getReference().child("Member").child(userid).child("data");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()) {
                    String dataa =ds.child("data").getValue().toString();
                    String titlee=ds.child("title").getValue().toString();
                    Dataof d=new Dataof(titlee,dataa);
                    Log.d("fetchdata",d.getData()+" "+d.getTitle());
                    fetchdata.add(d);
                }
                helpAdapter=new HelpAdapter(fetchdata,Show_detail.this);
                recyclerView.setAdapter(helpAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onLongClick(View v) {
        ischeckbox=true;
        t.getMenu().clear();
        t.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearmode();
            }
        });
        t.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        t.inflateMenu(R.menu.toolmenu);
        Log.d("longpress","checktrue");
        helpAdapter.notifyDataSetChanged();
        return true;
    }
    public void copy(String dataa){
        ClipboardManager c=(ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
        ClipData clipData=ClipData.newPlainText("text",dataa);
        c.setPrimaryClip(clipData);
    }

    public void copyall(){
        String copied="";
        for(int i=0;i<select_list.size();i++){
            copied+=select_list.get(i).toString()+":"+postion_list.get(i).toString();
            if(i!=select_list.size()-1){
                copied+="\n";
           }
        }
        ClipboardManager c=(ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
        ClipData clipData=ClipData.newPlainText("text",copied);
        c.setPrimaryClip(clipData);
        Toast.makeText(getApplicationContext(),"Copied",Toast.LENGTH_SHORT).show();
        clearmode();
    }
    public void selection(View v,String title,String data,int pos){
        if(((CheckBox)v).isChecked()){
            select_list.add(title);
            postion_list.add(data);
            pos_list.add(fetchdata.get(pos));
            Log.d("trueselection",select_list.toString());
        }
        else{
            select_list.remove(title);
            postion_list.remove(data);
            pos_list.remove(fetchdata.get(pos));
            Log.d("falseselection",select_list.toString());
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.delete_tool){
         //   HelpAdapter help=(HelpAdapter)helpAdapter;
         //   help.updateadap(pos_list);
         //   clearmode();
            AlertDialog.Builder alert=new AlertDialog.Builder(Show_detail.this);
            View v=getLayoutInflater().inflate(R.layout.delete_permision,null);
            Button cancel=(Button) v.findViewById(R.id.cancel);
            Button ok=(Button) v.findViewById(R.id.ok);
            alert.setView(v);
            final AlertDialog alertDialog=alert.create();
            alertDialog.setCanceledOnTouchOutside(true);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View ve) {
                    alertDialog.dismiss();
                }
            });
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View ve) {
                    delete();
                }
            });

            alertDialog.show();
            Log.d("deleted","deleted");
        }
        if(item.getItemId()==R.id.logout_tool){
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(),Login.class));
        }
        if(item.getItemId()==R.id.copy_tool){
            copyall();
        }
        if(item.getItemId()==R.id.Select_All){
            isselectall=true;
            notallow=false;
            Log.d("isselect",String.valueOf(isselectall));
            Log.d("isnotselect",String.valueOf(isnotselectall));
            helpAdapter.notifyDataSetChanged();
            isnotselectall=false;

        }
        if(item.getItemId()==R.id.Not_Select_All){
            isnotselectall=true;
            select_list.clear();
            Log.d("isselect",String.valueOf(isselectall));
            Log.d("isnotselect",String.valueOf(isnotselectall));
            Log.d("falseselect",select_list.toString());
            helpAdapter.notifyDataSetChanged();
            isselectall=false;

        }
        if(item.getItemId()==R.id.export){
            export_button();
        }
        return super.onOptionsItemSelected(item);
    }

    public void clearmode(){
        ischeckbox=false;
        t.getMenu().clear();
        t.inflateMenu(R.menu.menu);
        t.setNavigationIcon(null);
        select_list.clear();
        postion_list.clear();
        pos_list.clear();
        helpAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if(ischeckbox){
            clearmode();
            helpAdapter.notifyDataSetChanged();
        }
        else {
            Intent main=new Intent(getApplicationContext(), MainActivity.class);
            main.putStringArrayListExtra("delete",select_list);
            startActivity(main);
        }
    }
    //Delete All
    public void delete(){
        Log.d("call","called");
        for(int j=0;j<select_list.size();j++){
                isselectall=false;
                isnotselectall=false;
                DatabaseReference del=FirebaseDatabase.getInstance().getReference().child("Member").child(userid).child("data").child(select_list.get(j));
                del.removeValue();
                Log.d("removed","remove");
            }
        startActivity(new Intent(getApplicationContext(),Show_detail.class));

        }
        public void deletesingle(String title){
            DatabaseReference del=FirebaseDatabase.getInstance().getReference().child("Member").child(userid).child("data").child(title);
            del.removeValue();
            Log.d("removedsingle","remove");
            startActivity(new Intent(getApplicationContext(),Show_detail.class));
        }
        public void export_button(){
            if(select_list.size()>0) {
                AlertDialog.Builder alert = new AlertDialog.Builder(Show_detail.this);
                View v = getLayoutInflater().inflate(R.layout.filename, null);
                filename = (EditText) v.findViewById(R.id.filename);
                export_file = v.findViewById(R.id.button_export);
                alert.setView(v);
                AlertDialog alertDialog = alert.create();
                alertDialog.setCanceledOnTouchOutside(true);
                export_file.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View ve) {
                        CellStyle cellStyle = wb.createCellStyle();
                        cellStyle.setFillForegroundColor(HSSFColor.LIGHT_BLUE.index);
                        Sheet sheet = wb.createSheet("Details");
                        Row row = sheet.createRow(0);
                        Row row2 = sheet.createRow(1);
                        for (int i = 0; i < select_list.size(); i++) {
                            cell = row.createCell(i);
                            cell.setCellValue(select_list.get(i));
                            cell.setCellStyle(cellStyle);
                        }
                        for (int i = 0; i < postion_list.size(); i++) {
                            cell = row2.createCell(i);
                            cell.setCellValue(postion_list.get(i));
                            cell.setCellStyle(cellStyle);
                        }
                        File f = new File(Environment.getExternalStorageDirectory() + "/BabuStore");
                        Log.d("filestore", f.getAbsolutePath().toString());
                        File file = new File(getExternalFilesDir(null),filename.getText().toString() + ".xls");
                        Log.d("filestore", file.getAbsolutePath().toString());
                        if (!file.isDirectory()) {
                            Log.d("not exist", "notexits");
                        }
                        if (true) {
                            Log.d("crete exist", "creat exits");
                        }
                        FileOutputStream outputStream = null;
                        try {
                            outputStream = new FileOutputStream(file);
                            wb.write(outputStream);
                            Toast.makeText(getApplicationContext(), "File Stored in" + file.getAbsolutePath().toString(), Toast.LENGTH_LONG).show();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        startActivity(new Intent(getApplicationContext(), Show_detail.class));
                    }
                });
                alertDialog.show();
            }
            else{
                Toast.makeText(getApplicationContext(),"Muttapayale field click panra",Toast.LENGTH_SHORT).show();
            }
        }
        public void delete_permission(final String titllle){
            AlertDialog.Builder alert=new AlertDialog.Builder(Show_detail.this);
            View v=getLayoutInflater().inflate(R.layout.delete_permision,null);
            Button cancel=(Button) v.findViewById(R.id.cancel);
            Button ok=(Button) v.findViewById(R.id.ok);
            alert.setView(v);
            final AlertDialog alertDialog=alert.create();
            alertDialog.setCanceledOnTouchOutside(true);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View ve) {
                  alertDialog.dismiss();
                }
            });
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View ve) {
                    deletesingle(titllle);
                }
            });

            alertDialog.show();
        }
    }

