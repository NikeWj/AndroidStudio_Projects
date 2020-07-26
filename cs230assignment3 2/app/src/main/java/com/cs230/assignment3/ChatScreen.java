package com.cs230.assignment3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cs230.assignment3.adapter.MessageAdapter;
import com.cs230.assignment3.model.ChatMessage;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ChatScreen extends AppCompatActivity {
    private FirebaseFirestore mFirestore;
    TextView info;

    FirebaseUser f_user;
    ImageButton btn_send;
    EditText text_send;
    MessageAdapter messageAdapter;
    List<ChatMessage> mchat;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference reference_self,reference_chat;

    RecyclerView recyclerView;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts_detail);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        btn_send = findViewById(R.id.btn_send);
        text_send = findViewById(R.id.text_send);
        info = findViewById(R.id.show);
        intent = getIntent();
        final String userid = intent.getStringExtra("userid");
        f_user = FirebaseAuth.getInstance().getCurrentUser();

        System.out.println(f_user.getDisplayName());
        mFirestore = FirebaseFirestore.getInstance();


        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = text_send.getText().toString();
                ChatMessage chatMessage = new ChatMessage(f_user.getUid(),userid,msg);
                reference_self = db.collection("chatself");
                reference_chat = db.collection("chat");
                if(!msg.equals("")){
                    sendMessage(f_user.getUid(),userid,msg);
                    reference_self.add(chatMessage);
//                    reference_chat.add(chatMessage);
                }
                else{
                    Toast.makeText(ChatScreen.this, "You should send something", Toast.LENGTH_SHORT).show();
                }
//                info.setText(msg);
                reference_self.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        String data = "";

                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            ChatMessage contacts = documentSnapshot.toObject(ChatMessage.class);
                            String desc = contacts.getMessage();

                            data += "Message: " +desc+"\n"+"\n";
                        }

//                        info.setText(data);
                        text_send.setText("");
                        readMessage(f_user.getUid(),userid);
                    }
                });


            }
        });

//                reference_chat.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                     @Override
//                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                         readMessage(f_user.getUid(),userid);
//            }
//        });

    }
    private void sendMessage(String sender, String receiver, String message ){

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("message",message);

        reference_chat.add(hashMap);
    }

    private void readMessage(final String myid, final String userid){
        mchat = new ArrayList<>();
        reference_chat.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                mchat.clear();
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    ChatMessage chatMessage = documentSnapshot.toObject(ChatMessage.class);

                    if (chatMessage.getReceiver().equals(myid) && chatMessage.getSender().equals(userid) ||
                    chatMessage.getReceiver().equals(userid) && chatMessage.getSender().equals(myid)){
                        mchat.add(chatMessage);
                    }
                    else if (chatMessage.getReceiver().equals(myid) && chatMessage.getSender().equals(myid)) {
                        mchat.add(chatMessage);
                    }
                    messageAdapter = new MessageAdapter(mchat);
                    recyclerView.setAdapter(messageAdapter);
                }
            }
        });

        }

}
