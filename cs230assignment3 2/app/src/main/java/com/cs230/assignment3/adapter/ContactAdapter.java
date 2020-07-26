package com.cs230.assignment3.adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cs230.assignment3.model.Contact;
import com.cs230.assignment3.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

public class ContactAdapter extends FirestoreAdapter<ContactAdapter.ViewHolder> {

    public interface OnContactSelectedListener {

        void onContactSelected(DocumentSnapshot contact);
    }

    private OnContactSelectedListener mListener;

    public ContactAdapter(Query query, OnContactSelectedListener listener) {
        super(query);
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.item_contacts, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getSnapshot(position), mListener);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView nameView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.contact_item_image);
            nameView = itemView.findViewById(R.id.contact_item_name);
        }

        public void bind(final DocumentSnapshot snapshot,
                         final OnContactSelectedListener listener) {

            Contact contact = snapshot.toObject(Contact.class);


            nameView.setText(contact.getName());


            // Click listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onContactSelected(snapshot);
                    }
                }
            });
        }

    }
}
