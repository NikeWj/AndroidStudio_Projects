package com.cs230.assignment3.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cs230.assignment3.R;
import com.cs230.assignment3.model.ChatMessage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {


    public static final int MSG_TYPE_LEFT =0;
    public static final int MSG_TYPE_RIGHT =1;
    static FirebaseUser db;
    public static List<ChatMessage> mChat;

    public MessageAdapter(List<ChatMessage> mChat ) {
        this.mChat = mChat;
    }


    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == MSG_TYPE_RIGHT){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_right,parent,false);
            return new MessageAdapter.ViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_left,parent,false);
            return new MessageAdapter.ViewHolder(view);
        }
    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {
        ChatMessage chatMessage = mChat.get(position);

        holder.showView.setText(chatMessage.getMessage());

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView showView;

        public ViewHolder(View itemView) {
            super(itemView);
            showView = itemView.findViewById(R.id.show_message);
        }


    }
}
