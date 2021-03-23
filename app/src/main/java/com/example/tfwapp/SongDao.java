package com.example.tfwapp;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface SongDao {

    @Query("SELECT * from song_table ORDER BY song ASC")
    LiveData<List<Song>> getAlphabetizedSongs();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Song song);

    @Query("DELETE FROM song_table")
    void deleteAll();
}
