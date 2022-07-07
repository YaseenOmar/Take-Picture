package com.example.takpict.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.takpict.R;
import com.example.takpict.modle.User;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.myViewHolder> {

    ArrayList<User> data;
    Context context;


    public UsersAdapter(ArrayList<User> data,Context context){
        this.data=data;
        this.context=context;
    }

    @NonNull
    @NotNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.show_users,null,false);

        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull myViewHolder holder, int position) {

        User user=data.get(position);

        holder.id.setText(user.getId());
        holder.name.setText(user.getName());
        holder.email.setText(user.getEmail());
        holder.pass.setText(user.getPassword());
        Glide.with(context).load(user.getPhoto()).into(holder.image);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView name,id,pass,email;

        public myViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

           image= itemView.findViewById(R.id.usersimg);
           name=itemView.findViewById(R.id.username);
           id=itemView.findViewById(R.id.userid);
           email=itemView.findViewById(R.id.useremail);
           pass=itemView.findViewById(R.id.userpass);


        }
    }
}
