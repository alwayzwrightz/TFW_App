package com.example.tfwapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

public class New_Song_activity extends ImageUploadActivity {
    public static final String EXTRA_REPLY = "com.example.tfwapp.REPLY";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_song_activity);
        Intent intent = getIntent();
        String songName = intent.getStringExtra("SONGNAME");
        Log.i("tag",  " song name: "+songName );
        
        Intent replyIntent = new Intent();

        if (songName == "") {
            setResult(RESULT_CANCELED, replyIntent);
        } else {
            String song = songName;
            replyIntent.putExtra(EXTRA_REPLY, song);
            setResult(RESULT_OK, replyIntent);
        }
        finish();
    }

}
