package com.example.datastores;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class HelpAdapter extends RecyclerView.Adapter {
    List<Dataof> fetchdata;
    Show_detail s;
    Context context;
    ViewHold viewall;
    int lastPosition = -1;

    public HelpAdapter(List<Dataof> fetchdata, Context cx) {
        this.fetchdata = fetchdata;
        this.context=cx;
        this.s=(Show_detail)cx;
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_data,parent,false);
        ViewHold viewHold=new ViewHold(v);
        return viewHold;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final ViewHold viewHold=(ViewHold)holder;
        viewall=viewHold;
        Animation animation = AnimationUtils.loadAnimation(context,
                (position > lastPosition) ? R.anim.up_from_bottom
                        : R.anim.down_from_top);
        holder.itemView.startAnimation(animation);
        lastPosition = position;
        Log.d("position",String.valueOf(position)+" "+String.valueOf(lastPosition));
        Dataof data=fetchdata.get(position);
        final String dataaaa=data.getData().toString();
        viewHold.title.setText(data.getTitle());
        viewHold.data.setText(data.getData());
        if(!s.ischeckbox){
            viewHold.checkBox.setVisibility(View.GONE);
        }

        else{
            viewHold.checkBox.setVisibility(View.VISIBLE);
        }
        if(s.isselectall==true && viewHold.checkBox.isChecked()==false){
           Log.d("checkselect", String.valueOf(viewHold.checkBox.isChecked()));
            viewHold.checkBox.setVisibility(View.VISIBLE);
            viewHold.checkBox.setChecked(true);

            if(viewHold.checkBox.isChecked())
            {
                s.pos_list.add(fetchdata.get(viewHold.getAdapterPosition()));
                s.select_list.add(data.getTitle());
                Log.d("trueselect",s.select_list.toString());
            }
        }
        if(s.isnotselectall==true && viewHold.checkBox.isChecked()==true){
            viewHold.checkBox.setChecked(false);
        }


//        lastPos = position;
    }

    @Override
    public int getItemCount() {
        return fetchdata.size();
    }
    public class ViewHold extends RecyclerView.ViewHolder{
        TextView title,data;
        ImageView delete,copy;
        CheckBox checkBox;
        LinearLayout single;
        View v;
        public ViewHold(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.textView7);
            data=itemView.findViewById(R.id.textView8);
            delete=itemView.findViewById(R.id.delete);
            copy=itemView.findViewById(R.id.copy);
            checkBox=itemView.findViewById(R.id.checkBox);
            single=itemView.findViewById(R.id.singlelinear);
            v=itemView;
            v.setOnLongClickListener(s);
            copy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    s.copy(data.getText().toString());
                    Log.d("copy","copied");
                    Toast.makeText(s.getApplicationContext(),"Copied",Toast.LENGTH_SHORT).show();
                }
            });
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    s.selection(v,title.getText().toString(),data.getText().toString(),getAdapterPosition());
                }
            });
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    s.delete_permission(title.getText().toString());
                }
            });
        }

    }

    public void updateadap(List<Dataof> list){
        for (Dataof data:list){
            Log.d("updatedata",data.toString());
            fetchdata.remove(data);
        }
    }
}
