package com.example.assignment1_emaillist;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class EmailAdapter extends RecyclerView.Adapter<EmailAdapter.EmailViewHolder> {

    //create an interface
    public interface OnEmailListener{
        void OnEmailClick(int position);
    }
    private ArrayList<Email> myEmailList;
    private OnEmailListener myOnEmailListener;

    ///This Class (email adapter class) constructor
    public EmailAdapter(ArrayList<Email>  emailList, OnEmailListener OnEmailListener){
        this.myEmailList = emailList;
        this.myOnEmailListener = OnEmailListener;
    }

    ///View holder inner class
    public static class EmailViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener,View.OnClickListener {
        public TextView text1;
        public TextView text2;
        public OnEmailListener onEmailListener;

        public EmailViewHolder(@NonNull View itemView, OnEmailListener onEmailListener) {  //inner class constructor
            super(itemView);
            this.text1 = itemView.findViewById(R.id.date);
            this.text2 = itemView.findViewById(R.id.subtittle);
            this.onEmailListener=onEmailListener;
            itemView.setOnClickListener(this); //"this" is the interface implemented above
            itemView.setOnCreateContextMenuListener(this);
        }
        @Override
        public void onClick(View view) {
            onEmailListener.OnEmailClick(getAdapterPosition());

        }
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

            menu.setHeaderTitle("Sure Wanna Delete ?");
            MenuItem Yes = menu.add(Menu.NONE, 1, 1, "Yup");
            MenuItem No = menu.add(Menu.NONE, 2, 2, "Nah");
            Yes.setOnMenuItemClickListener(onEditMenu);
            No.setOnMenuItemClickListener(onEditMenu);
        }

        private final MenuItem.OnMenuItemClickListener onEditMenu = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case 1:
                        Toast.makeText(itemView.getContext(), "That Email has being deleted",Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(itemView.getContext(), "Nah i've changed my mind",Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        };


    }

    ///Take email_item.xml as the view and create a new view holder using that view
    @NonNull
    @Override
    public EmailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.email_item,parent,false);
        EmailViewHolder evh = new EmailViewHolder(v,myOnEmailListener); //the inner class created above
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull EmailViewHolder holder, int position) {
        Email currentemail = myEmailList.get(position);
        holder.text1.setText(currentemail.getText1());
        holder.text2.setText(currentemail.getText2());
    }

    @Override
    public int getItemCount() {
        return myEmailList.size();
    }


}

