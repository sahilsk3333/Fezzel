package com.sahilpc.fezzle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sahilpc.fezzle.Adapters.AddUserAdapter;

import com.sahilpc.fezzle.Loaders.llottiedialogfragment;
import com.sahilpc.fezzle.Models.Users;
import com.sahilpc.fezzle.databinding.ActivityAddUserBinding;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AddUserActivity extends AppCompatActivity {
    ActivityAddUserBinding binding;

    ArrayList<Users> list = new ArrayList<>();
    private FirebaseDatabase database;
    AddUserAdapter adapter = new   AddUserAdapter(list, AddUserActivity.this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        final llottiedialogfragment lottie = new llottiedialogfragment(this);

        database = FirebaseDatabase.getInstance();


        binding.rvAddUser.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(AddUserActivity.this);
        binding.rvAddUser.setLayoutManager(layoutManager);

        lottie.show();
        database.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Users users = dataSnapshot.getValue(Users.class);
                    users.setUserId(dataSnapshot.getKey());
                    if (!users.getUserId().equals(FirebaseAuth.getInstance().getUid())){
                        list.add(users);
                    }
                }
                adapter.notifyDataSetChanged();lottie.dismiss();

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                lottie.dismiss();

            }
        });

        binding.searchUsers.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });

    }



    private void filter(String text) {

        ArrayList<Users> filterList = new ArrayList<>();

        for(Users item: list){

            if(item.getUserName().toLowerCase().contains(text.toLowerCase())){

                filterList.add(item);

            }

        }

       adapter.filteredList(filterList);

    }


}
