package com.example.tfwapp;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;

public class Song_Gallery extends ImageUploadActivity {
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
        //click listener for displayed songs

        adapter.setOnItemClickListener(new SongListAdapter.ClickListener(){
            @Override
            public void onItemClick(View v, int position){
                Intent intent = new Intent(Song_Gallery.this, music_player.class);
                Song current = SongListAdapter.mSongs.get(position);
                String example = current.getImage();
                Uri ur = Uri.parse(example);
                intent.putExtra("URI_PATH",ur);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(intent);
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
        Button back = findViewById(R.id.Back_btn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NEW_SONG_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Intent intent = getIntent();
            Uri current_path = intent.getParcelableExtra("URI_PATH");
            String current_path_string = current_path.toString();
            Song song = new Song(data.getStringExtra(New_Song_activity.EXTRA_REPLY), current_path_string);
            mSongViewModel.insert(song);

            Log.i("tag",  " and the URI path is "+current_path_string);

        } else {
            Context context = getApplicationContext();
            CharSequence text = "Error";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }

}
