package com.example.tfwapp;

import android.app.Application;
import android.content.ClipData;
import android.net.Uri;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class SongViewModel extends AndroidViewModel {

    private SongRepository mRepository;
    private LiveData<List<Song>> mAllSongs;

    public SongViewModel(Application application) {
        super(application);
        mRepository = new SongRepository(application);
        mAllSongs = mRepository.getAllSongs();
    }

    LiveData<List<Song>> getAllSongs() { return mAllSongs; }

    public void insert(Song song) { mRepository.insert(song); }

}