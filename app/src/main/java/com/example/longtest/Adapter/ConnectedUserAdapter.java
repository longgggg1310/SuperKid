package com.example.longtest.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.longtest.LiveMapActivity;
import com.example.longtest.R;
import com.example.longtest.model.UserRegister;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ConnectedUserAdapter extends RecyclerView.Adapter<ConnectedUserAdapter.ConnectedAdapterViewHolder> {
    ArrayList<UserRegister> nameList;
    Context c;
    public ConnectedUserAdapter(ArrayList<UserRegister> nameList,Context c)
    {
        this.nameList = nameList;
        this.c=c;
    }


    @Override
    public int getItemCount()
    {
        return nameList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public ConnectedAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.connected_card_layout,parent,false);
        ConnectedUserAdapter.ConnectedAdapterViewHolder viewHolder1 = new ConnectedUserAdapter.ConnectedAdapterViewHolder(view,c,nameList);
        return viewHolder1;
    }



    @Override
    public void onBindViewHolder(ConnectedAdapterViewHolder holder, int position) {

        UserRegister userRegister = nameList.get(position);
        //   String name = nameList.get(position);
        holder.name_txt.setText(userRegister.name);

    }

    public static class ConnectedAdapterViewHolder extends RecyclerView.ViewHolder
            implements View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener,View.OnClickListener
    {
        TextView name_txt;
        View v;
        Context ctx;
        DatabaseReference reference,currentReference;
        FirebaseAuth auth;
        FirebaseUser user;
        ArrayList<UserRegister> nameArrayList;
        //  CircleImageView i1;
        public ConnectedAdapterViewHolder(View itemView,Context ctx,ArrayList<UserRegister> nameArrayList) {
            super(itemView);
            itemView.setOnCreateContextMenuListener(this);
            itemView.setOnClickListener(this);
            this.v = itemView;
            this.ctx=ctx;
            this.nameArrayList = nameArrayList;
            auth = FirebaseAuth.getInstance();
            user = auth.getCurrentUser();
            reference = FirebaseDatabase.getInstance().getReference().child("Parent").child(user.getUid()).child("ConnectedUsers");
            currentReference = FirebaseDatabase.getInstance().getReference().child("Children");

            name_txt = (TextView)itemView.findViewById(R.id.item_title);

        }

        @Override
        public void onClick(View v) {

            int position = getAdapterPosition();
            UserRegister userRegister = this.nameArrayList.get(position);
            String latitude_user = userRegister.latitude;
            String longitude_user = userRegister.longitude;


            if(latitude_user.equals("na") && longitude_user.equals("na"))
            {

                Toast.makeText(ctx,"This circle member is not sharing location.",Toast.LENGTH_SHORT).show();


            }
            else
            {
                Intent mYIntent = new Intent(ctx, LiveMapActivity.class);
                // mYIntent.putExtra("createuserobject",addCircle);
                mYIntent.putExtra("latitude",latitude_user);
                mYIntent.putExtra("longitude",longitude_user);
                mYIntent.putExtra("name",userRegister.name);
                mYIntent.putExtra("userid",userRegister.userid);
                mYIntent.putExtra("date",userRegister.lasttime);
                mYIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ctx.startActivity(mYIntent);
            }

        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {

            int position = getAdapterPosition();
            final UserRegister addCircle = this.nameArrayList.get(position);

            reference.child(addCircle.userid).removeValue()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                currentReference.child(addCircle.userid).child("CircleMembers").child(user.getUid()).removeValue()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful())
                                                {
                                                    Toast.makeText(ctx,"Removed successfully",Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });



                            }
                        }
                    });

            return true;
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            MenuItem myActionItem = menu.add("REMOVE");
            myActionItem.setOnMenuItemClickListener(this);
        }


    }









}



