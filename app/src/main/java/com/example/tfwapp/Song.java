package com.example.tfwapp;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "song_table")
public class Song {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "song")
    private String mSong;

    public Song(@NonNull String song) {
        this.mSong = song;}

    public String getSong(){return this.mSong;}
}