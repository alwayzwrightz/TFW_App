package com.example.tfwapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Song_Gallery extends AppCompatActivity {
    public static final int NEW_SONG_ACTIVITY_REQUEST_CODE = 1;

    private SongViewModel mSongViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_gallery);


        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final SongListAdapter adapter = new SongListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mSongViewModel = ViewModelProviders.of(this).get(SongViewModel.class);

        mSongViewModel.getAllSongs().observe(this, new Observer<List<Song>>() {
            @Override
            public void onChanged(@Nullable final List<Song> songs) {
                // Update the cached copy of the words in the adapter.
                adapter.setSongs(songs);
            }
        });

        Button b5 = findViewById(R.id.button5);
        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Song_Gallery.this, New_Song_activity.class);
                startActivityForResult(intent, NEW_SONG_ACTIVITY_REQUEST_CODE);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NEW_SONG_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Song song = new Song(data.getStringExtra(New_Song_activity.EXTRA_REPLY));
            mSongViewModel.insert(song);
        } else {
            Context context = getApplicationContext();
            CharSequence text = "Error";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }
}
