package com.example.tfwapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.concurrent.TimeUnit;

public class music_player_song extends AppCompatActivity {
    //init variables
    TextView playerPosition, playerDuration;
    SeekBar seekBar;
    ImageView btRew, btPlay, btPause, btFf, image;
    Button b3;



    MediaPlayer mediaPlayer;
    Handler handler = new Handler();
    Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player_song);


        //Assign vars test
        playerPosition = findViewById(R.id.player_position_2);
        playerDuration = findViewById(R.id.player_duration_2);
        seekBar = findViewById(R.id.seek_bar_2);
        btRew = findViewById(R.id.bt_rew_2);
        btPlay = findViewById(R.id.bt_play_2);
        btPause = findViewById(R.id.bt_pause_2);
        btFf = findViewById(R.id.bt_ff_2);
        b3 = findViewById(R.id.button32);
        image = findViewById(R.id.image_music_player_2);
        Intent intent = getIntent();
        Uri current_path = intent.getParcelableExtra("URI_PATH");
        String GallerySongName = intent.getStringExtra("GALLERYSONGNAME");
        Log.i("tag",  "Gallery Song Name in Music Player song: "+GallerySongName);

        image.setImageURI(current_path);

        //init media player
        //maroon song
        if (GallerySongName.equals("maroon")) {
            mediaPlayer = MediaPlayer.create(this, R.raw.maroon);
        }
        // red song
        else if (GallerySongName.equals("red")) {
            mediaPlayer = MediaPlayer.create(this, R.raw.red);
        }
        // olive song
        else if (GallerySongName.equals("olive")) {
            mediaPlayer = MediaPlayer.create(this, R.raw.olive);
        }
        // yellow song
        else if (GallerySongName.equals("yellow")) {
            mediaPlayer = MediaPlayer.create(this, R.raw.yellow);
        }
        // green song
        else if (GallerySongName.equals("green")) {
            mediaPlayer = MediaPlayer.create(this, R.raw.green);
        }
        // lime song
        else if (GallerySongName.equals("lime")) {
            mediaPlayer = MediaPlayer.create(this, R.raw.lime);
        }
        // teal song
        else if (GallerySongName.equals("teal")) {
            mediaPlayer = MediaPlayer.create(this, R.raw.teal);
        }
        // cyan song
        else if (GallerySongName.equals("cyan")) {
            mediaPlayer = MediaPlayer.create(this, R.raw.cyan);
        }
        // navy song
        else if (GallerySongName.equals("navy")) {
            mediaPlayer = MediaPlayer.create(this, R.raw.navy);
        }
        // blue song
        else if (GallerySongName.equals("blue")) {
            mediaPlayer = MediaPlayer.create(this, R.raw.blue);
        }
        // purple song
        else if (GallerySongName.equals("purple")) {
            mediaPlayer = MediaPlayer.create(this, R.raw.purple);
        }
        // magenta song
        else if (GallerySongName.equals("magenta")) {
            mediaPlayer = MediaPlayer.create(this, R.raw.magenta);
        }
        // black song
        else if (GallerySongName.equals("black")) {
            mediaPlayer = MediaPlayer.create(this, R.raw.black);
        }
        // gray song
        else if (GallerySongName.equals("gray")) {
            mediaPlayer = MediaPlayer.create(this, R.raw.gray);
        }
        // silver song
        else if (GallerySongName.equals("silver")) {
            mediaPlayer = MediaPlayer.create(this, R.raw.silver);
        }
        // white song
        else if (GallerySongName.equals("white")) {
            mediaPlayer = MediaPlayer.create(this, R.raw.white);
        }
        else{
            mediaPlayer = MediaPlayer.create(this, R.raw.music);
        }

        //init runnable
        runnable = new Runnable() {
            @Override
            public void run() {
                //set progress on seek bar
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                //handler post delay for 0.5 seconds
                handler.postDelayed(this, 500);
            }
        };

        //get duration of media player
        int duration = mediaPlayer.getDuration();
        //convert ms to min and sec
        String sDuration = convertFormat(duration);
        //set duration on text view
        playerDuration.setText(sDuration);

        btPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //hide play btn
                btPlay.setVisibility(View.GONE);
                //show pause btn
                btPause.setVisibility(View.VISIBLE);
                //start media player
                mediaPlayer.start();
                //set max on seek bar
                seekBar.setMax(mediaPlayer.getDuration());
                //start handler
                handler.postDelayed(runnable, 0);
            }
        });

        btPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //hide pause btn
                btPause.setVisibility(View.GONE);
                //show play btn
                btPlay.setVisibility(View.VISIBLE);
                //pause media player
                mediaPlayer.pause();
                //pause handler
                handler.removeCallbacks(runnable);
            }
        });

        btFf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPosition = mediaPlayer.getCurrentPosition();
                //get duration of media player
                int duration = mediaPlayer.getDuration();
                //check condition
                if(mediaPlayer.isPlaying() && duration != currentPosition) {
                    //when media is player and duration is not equal to current position
                    //fast forward for 5 seconds
                    currentPosition = currentPosition + 5000;
                    //set current position on text view
                    playerPosition.setText(convertFormat(currentPosition));
                    //set progress on seek bar
                    mediaPlayer.seekTo(currentPosition);
                }
            }
        });

        btRew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get current position of media player
                int currentPosition = mediaPlayer.getCurrentPosition();
                //check condition
                if(mediaPlayer.isPlaying() && currentPosition > 5000) {
                    //when media is player and current position is great than 5 seconds
                    //rewind for seconds
                    currentPosition = currentPosition - 5000;
                    //get current position on text view
                    playerPosition.setText(convertFormat(currentPosition));
                    //set progress on seek bar
                    mediaPlayer.seekTo(currentPosition);
                }
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //check condition
                if(fromUser) {
                    //when dragging the seek bar
                    //set progress on seek bar
                    mediaPlayer.seekTo(progress);
                }
                //set current position on text view
                playerPosition.setText(convertFormat(mediaPlayer.getCurrentPosition()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                //hide pause button
                btPause.setVisibility(View.GONE);
                //show play button
                btPlay.setVisibility(View.VISIBLE);
                //set media player to initialize position
                mediaPlayer.seekTo(0);
            }
        });
    }

    @SuppressLint("DefaultLocale")
    private String convertFormat(int duration) {
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(duration),
                TimeUnit.MILLISECONDS.toSeconds(duration) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));
    }
}