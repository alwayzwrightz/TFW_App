package com.example.tfwapp;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;

import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class SongListAdapter extends RecyclerView.Adapter<SongListAdapter.SongViewHolder> {

    public static List<Song> mSongs;
    private static ClickListener clickListener;
    int position;
    class SongViewHolder extends RecyclerView.ViewHolder {
        private TextView songItemView;

        public SongViewHolder(View itemView) {
            super(itemView);
            songItemView = itemView.findViewById(R.id.textView);
            //using interface to set up click listener for list
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    int position = getAdapterPosition();
                    Song current = mSongs.get(position);
                    String example = current.getImage();
                        Log.i("tag","Button Clicked at " + getAdapterPosition() + " and the URI path is "+example);
                        clickListener.onItemClick(v,getAdapterPosition());
                }
            });
        }
    }

    private final LayoutInflater mInflater;

    SongListAdapter(Context context) { mInflater = LayoutInflater.from(context); }

    @Override
    public SongViewHolder onCreateViewHolder (ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);
        return new SongViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SongViewHolder holder, int position) {
        Song current = mSongs.get(position);
        //holder.songItemView.setText(current.getSong() + " " + current.getImage());
        holder.songItemView.setText(current.getSong());
    }

    void setSongs(List<Song> songs){
        mSongs = songs;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mSongs != null)
            return mSongs.size();
        else return 0;
    }

    public interface ClickListener{
        void onItemClick(View v, int position);
    }
    public void setOnItemClickListener(SongListAdapter.ClickListener clickListener){
        SongListAdapter.clickListener = clickListener;
    }
}


