package com.example.takpict.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.takpict.R;
import com.example.takpict.modle.Rater;
import com.example.takpict.modle.User;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class RatesAdapter extends RecyclerView.Adapter<RatesAdapter.myViewHolder> {

    ArrayList<Rater> data;
    Context context;

    public RatesAdapter(ArrayList<Rater> data){
        this.data=data;
    }



    @NonNull
    @NotNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        View view= LayoutInflater.from(context).inflate(R.layout.show_rates,null,false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull myViewHolder holder, int position) {
        holder.id.setText(data.get(position).getId());
        holder.rate.setText(data.get(position).getRate());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder{
        TextView id,rate;
        public myViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            id=itemView.findViewById(R.id.idrater);
            rate=itemView.findViewById(R.id.therate);
        }
    }
}
