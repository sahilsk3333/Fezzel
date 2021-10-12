package com.sahilpc.fezzle.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.sahilpc.fezzle.Models.Users;
import com.sahilpc.fezzle.R;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
public class AddUserAdapter extends RecyclerView.Adapter<AddUserAdapter.viewHolder>{

    ArrayList<Users> list;
    Context context;

    public AddUserAdapter(ArrayList<Users> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.sample_add_user,parent,false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AddUserAdapter.viewHolder holder, int position) {

        Users users = list.get(position);

        Picasso.get().load(users.getProfilepic()).placeholder(R.drawable.ic_user).into(holder.image);
        holder.userName.setText(users.getUserName());

        holder.addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference mDatabase;
                FirebaseAuth auth;

                mDatabase = FirebaseDatabase.getInstance().getReference();
                auth = FirebaseAuth.getInstance();

                mDatabase.child("addedUsers").child(auth.getUid()).child(users.getUserId()).setValue(users.getUserId());

                Toast.makeText(view.getContext(), users.getUserName() + " Added", Toast.LENGTH_SHORT).show();

            }
        });



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void filteredList(ArrayList<Users> filterList) {
        list = filterList;
        notifyDataSetChanged();
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView userName;
        Button addbtn;

        public viewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.profileimg);
            userName = itemView.findViewById(R.id.userName);
            addbtn = itemView.findViewById(R.id.addbtn);


        }

    }
}
