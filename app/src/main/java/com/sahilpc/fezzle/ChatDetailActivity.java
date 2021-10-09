package com.sahilpc.fezzle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sahilpc.fezzle.Adapters.ChatAdapter;
import com.sahilpc.fezzle.Loaders.llottiedialogfragment;
import com.sahilpc.fezzle.Models.MessageModel;

import com.sahilpc.fezzle.databinding.ActivityChatDetailBinding;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


public class ChatDetailActivity extends AppCompatActivity {

    ActivityChatDetailBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        final String senderId = auth.getUid();
        String recieveId = getIntent().getStringExtra("userId");
        String profilePic = getIntent().getStringExtra("profilePic");
        String userName = getIntent().getStringExtra("userName");

        //loading dialog
        final llottiedialogfragment lottie = new llottiedialogfragment(this);

        binding.chatUserName.setText(userName);
        Picasso.get().load(profilePic).placeholder(R.drawable.ic_user).into(binding.chatUserImg);

        final ArrayList<MessageModel> messageModels = new ArrayList<>();
        final ChatAdapter chatAdapter = new ChatAdapter(messageModels,this,recieveId);
        binding.chatRecyclerView.setAdapter(chatAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.chatRecyclerView.setLayoutManager(layoutManager);




        final String senderRoom = senderId + recieveId;
        final String recieverRoom = recieveId + senderId;
        

        lottie.show();
        database.getReference().child("chats")
                .child(senderRoom)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        messageModels.clear();
                        for (DataSnapshot snapshot1 : snapshot.getChildren()){

                            MessageModel model = snapshot1.getValue(MessageModel.class);
                            model.setMessageId(snapshot1.getKey());

                            messageModels.add(model);
                        }chatAdapter.notifyDataSetChanged();
                        binding.chatRecyclerView.scrollToPosition(binding.chatRecyclerView.getAdapter().getItemCount() - 1);
                        lottie.dismiss();



                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {
                        lottie.dismiss();

                    }
                });

         checkonline(recieveId);

        binding.sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String message = binding.etMessage.getText().toString();

                if(message.trim().isEmpty()){
                    return;
                }

                final MessageModel model = new MessageModel(senderId,message);
                model.setTimestamp(new Date().getTime());
                binding.etMessage.setText("");

                database.getReference().child("chats")
                        .child(senderRoom)
                        .push()
                        .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                       database.getReference().child("chats")
                       .child(recieverRoom)
                       .push()
                       .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                           @Override
                           public void onSuccess(Void unused) {
                               binding.chatRecyclerView.smoothScrollToPosition(binding.chatRecyclerView.getAdapter().getItemCount() - 1);
                           }
                       });
                    }
                });

            }
        });

    }

    private void checkonline(String recieveId) {

        database.getReference().child("Users").child(recieveId).child("status")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                        if (snapshot.getValue().equals("online")){
                            binding.imgOn.setVisibility(View.VISIBLE);
                            binding.statusTxt.setVisibility(View.VISIBLE);
                            binding.imgOff.setVisibility(View.GONE);
                        } else {
                            binding.imgOn.setVisibility(View.GONE);
                            binding.statusTxt.setVisibility(View.GONE);
                            binding.imgOff.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });

    }

    public void onBackPressed() {
        super.onBackPressed();
        return;
    }


    private void status(String status){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);
        reference.updateChildren(hashMap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");

    }

    @Override
    protected void onPause() {
        super.onPause();
        status("offline");
    }

}