package com.example.assignmentmusic;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicViewHolder> implements Filterable {

    private static final String TAG = "MusicAdapter";
    private List<Music> listMusic;
    private Context context;
    private List<Music> listMusicFull;

    public interface OnMusicListener{
        void OnMusicClick(int position);
    }

    private OnMusicListener myOnMusicListener;

    public MusicAdapter(Context context,List<Music> listMusic,OnMusicListener OnEmailListener){
        this.context = context;
        this.listMusic = listMusic;
        this.myOnMusicListener = OnEmailListener;
        listMusicFull = new ArrayList<>(listMusic);

    }
    @NonNull
    @Override
    public MusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.music_item,parent,false);
        MusicViewHolder holder = new MusicViewHolder(v,myOnMusicListener);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MusicViewHolder holder, int position) {
        Log.d(TAG,"onbind called");
        Music m = listMusic.get(position);
        holder.textView1.setText(m.getName());
        holder.textView2.setText(m.getSinger());
        holder.textView3.setText(toTime((int) m.getTime()));

    }

    @Override
    public int getItemCount() {
        return listMusic.size();
    }

    public class MusicViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView textView1;
        private TextView textView2;
        private TextView textView3;

        OnMusicListener onMusicListener;
        public MusicViewHolder(@NonNull View itemView,OnMusicListener onMusicListener) {
            super(itemView);
            this.textView1 = itemView.findViewById(R.id.music_item_name);
            this.textView2 = itemView.findViewById(R.id.music_item_singer);
            this.textView3 = itemView.findViewById(R.id.music_item_time);
            this.onMusicListener = onMusicListener;

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            myOnMusicListener.OnMusicClick(getAdapterPosition());
        }
    }

    public String toTime(int time) {
        time /= 1000;
        int minute = time / 60;
        int hour = minute / 60;
        int second = time % 60;
        minute %= 60;
        return String.format("%02d:%02d", minute, second);
    }

    @Override
    public Filter getFilter() {
        return musicFilter;
    }
    private Filter musicFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Music> filterList = new ArrayList<>();

            if(constraint == null || constraint.length() == 0){
                filterList.addAll(listMusicFull);
            }else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Music item : listMusicFull){
                    if (item.getTitle().toLowerCase().contains(filterPattern)){
                        filterList.add(item);
                    }
                    else if (item.getName().toLowerCase().contains(filterPattern)){
                        filterList.add(item);
                    }
                    else if (item.getSinger().toLowerCase().contains(filterPattern)){
                        filterList.add(item);
                    }
                    else if (item.getAlbum().toLowerCase().contains(filterPattern)){
                        filterList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filterList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            listMusic.clear();
            listMusic.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };
}
