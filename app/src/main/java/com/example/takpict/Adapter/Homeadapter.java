package com.example.takpict.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.takpict.Database.Mydatabase;

import com.example.takpict.HomeApp;
import com.example.takpict.R;
import com.example.takpict.modle.Homemodle;
import com.example.takpict.ui.HomeFragment;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

public class Homeadapter extends RecyclerView.Adapter<Homeadapter.myViewHolder> {

    ArrayList<Homemodle> data;
    Context context;
    Mydatabase mydatabase;
    AlertDialog.Builder builder;


    public Homeadapter(ArrayList<Homemodle> data) {
        this.data = data;

    }


    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        mydatabase = new Mydatabase(context);


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.homeadapter, null, false);
        return new myViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        holder.tvText.setText(data.get(position).getText());
        //add img shared pref
        Glide.with(context).load(data.get(position).getImage()).into(holder.ivImage);


        //delete post
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (data.get(position).getFromUser() == 1) {

                    builder = new AlertDialog.Builder(context);
                    builder.setMessage("Do you want to delete post?").setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mydatabase.deletepost(data.get(position).getId());
                                    data.remove(position);
                                    notifyDataSetChanged();

                                }
                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(context, "Cancel", Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.create();
                    builder.setTitle("Delete Post ");
                    builder.show();


                }
                return false;
            }
        });


    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class myViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        TextView tvText;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.iv_Imagee);
            tvText = itemView.findViewById(R.id.tv_text);
        }
    }


}


