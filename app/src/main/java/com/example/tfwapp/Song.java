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
    private String mImage;

    public Song(@NonNull String song, @NonNull String image) {
        this.mSong = song;
        this.mImage = image;}

    public String getSong(){return this.mSong;}
    public String getImage(){return this.mImage;}
}