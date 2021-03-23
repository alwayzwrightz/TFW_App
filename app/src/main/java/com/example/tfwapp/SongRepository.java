package com.example.tfwapp;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;


public class SongRepository {

    private SongDao mSongDao;
    private LiveData<List<Song>> mAllSongs;

    SongRepository(Application application) {
        SongRoomDatabase db = SongRoomDatabase.getDatabase(application);
        mSongDao = db.songDao();
        mAllSongs = mSongDao.getAlphabetizedSongs();
    }

    LiveData<List<Song>> getAllSongs() {
        return mAllSongs;
    }

    public void insert (Song song) {
        new insertAsyncTask(mSongDao).execute(song);
    }

    private static class insertAsyncTask extends AsyncTask<Song, Void, Void> {

        private SongDao mAsyncTaskDao;

        insertAsyncTask(SongDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Song... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
}
