package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import entity.PlayList;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicViewHolder> {

    public PlayList musicList;

    private static int viewHolderCount;
    private int musicItems;
    private Context parent;

    public MusicAdapter(int musicItems, Context parent, PlayList musicList){
        this.musicList = musicList;
        this.musicItems = musicItems;
        this.parent = parent;
        viewHolderCount = 0;
    }
    @NonNull
    @Override
    public MusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.music_list_item;

        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, parent, false);

        MusicViewHolder viewHolder = new MusicViewHolder(view);

        viewHolderCount++;

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MusicViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return musicItems;
    }


    class MusicViewHolder extends RecyclerView.ViewHolder{
        int index;
        TextView artistName;
        TextView songName;


        public MusicViewHolder(@NonNull View itemView) {
            super(itemView);

            artistName = itemView.findViewById(R.id.tv_artist);
            songName = itemView.findViewById(R.id.tv_song);

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    ((MainActivity) view.getContext()).recyclerClick(view, musicList.get(index));
                }
            });
        }

        void bind(int listIndex){

            songName.setText(musicList.get(listIndex).getName());
            artistName.setText(musicList.get(listIndex).getArtist());
            index = listIndex;
        }
    }
}

