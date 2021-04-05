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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.concurrent.TimeUnit;


public class music_player extends AppCompatActivity {
    //init variables
    TextView playerPosition, playerDuration;
    SeekBar seekBar;
    ImageView btRew, btPlay, btPause, btFf, image;
    Button b3;


    MediaPlayer mediaPlayer;
    Handler handler = new Handler();
    Runnable runnable;

    public String songName = "test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);


        //Assign vars test
        playerPosition = findViewById(R.id.player_position);
        playerDuration = findViewById(R.id.player_duration);
        seekBar = findViewById(R.id.seek_bar);
        btRew = findViewById(R.id.bt_rew);
        btPlay = findViewById(R.id.bt_play);
        btPause = findViewById(R.id.bt_pause);
        btFf = findViewById(R.id.bt_ff);
        b3 = findViewById(R.id.button3);
        image = findViewById(R.id.image_music_player);
        Intent intent = getIntent();
        int r = intent.getIntExtra("RED",0);
        int g = intent.getIntExtra("GREEN",0);
        int b = intent.getIntExtra("BLUE",0);
        Uri current_path = intent.getParcelableExtra("URI_PATH");

        image.setImageURI(current_path);


        //init media player
        //maroon song
        if ((64<=r && r<=191) && g<=64 && b<=64) {
            mediaPlayer = MediaPlayer.create(this, R.raw.maroon);
            songName = "maroon";
        }
        // red song
        else if (r>=192 && g<=64 && b<=64) {
                mediaPlayer = MediaPlayer.create(this, R.raw.red);
            songName = "red";
            }
        // olive song
        else if ((65<=r && r<=191) && (65<=g && g<=191) && b<=64) {
            mediaPlayer = MediaPlayer.create(this, R.raw.olive);
            songName = "olive";
        }
        // yellow song
        else if (r>=192 && g>=192 && b<=64) {
            mediaPlayer = MediaPlayer.create(this, R.raw.yellow);
            songName = "yellow";
        }
        // green song
        else if (r<=64 && (65<=g && g<=191) && b<=64) {
            mediaPlayer = MediaPlayer.create(this, R.raw.green);
            songName = "green";
        }
        // lime song
        else if (r<=64 && g>=192 && b<=64) {
            mediaPlayer = MediaPlayer.create(this, R.raw.lime);
            songName = "lime";
        }
        // teal song
        else if (r<=64 && (65<=g && g<=191) && (65<=b && b<=191)) {
            mediaPlayer = MediaPlayer.create(this, R.raw.teal);
            songName = "teal";
        }
        // cyan song
        else if (r<=64 && g>=192 && b>=192) {
            mediaPlayer = MediaPlayer.create(this, R.raw.cyan);
            songName = "cyan";
        }
        // navy song
        else if (r<=64 && g<=64 && (65<=b && b<=191)) {
            mediaPlayer = MediaPlayer.create(this, R.raw.navy);
            songName = "navy";
        }
        // blue song
        else if (r<=64 && g<=64 && b>=192) {
            mediaPlayer = MediaPlayer.create(this, R.raw.blue);
            songName = "blue";
        }
        // purple song
        else if ((65<r && r<=191) && g<=64 && (65<=b && b<=191)) {
            mediaPlayer = MediaPlayer.create(this, R.raw.purple);
            songName = "purple";
        }
        // magenta song
        else if (r>=192 && g<=64 && b<=192) {
            mediaPlayer = MediaPlayer.create(this, R.raw.magenta);
            songName = "magenta";
        }
        // black song
        else if (r<=64 && g<=64 && b<=64) {
            mediaPlayer = MediaPlayer.create(this, R.raw.black);
            songName = "black";
        }
        // gray song
        else if ((65<=r && r<=128) && (65<=g && g<=128) && (65<=b && b<=128)) {
            mediaPlayer = MediaPlayer.create(this, R.raw.gray);
            songName = "gray";
        }
        // silver song
        else if ((129<=r && r<=191) && (129<=g && g<=191) && (129<=b && b<=191)) {
            mediaPlayer = MediaPlayer.create(this, R.raw.silver);
            songName = "silver";
        }
        // white song
        else if (r>=192 && g>=192 && b>=192) {
            mediaPlayer = MediaPlayer.create(this, R.raw.white);
            songName = "white";
        }
        else{
            mediaPlayer = MediaPlayer.create(this, R.raw.music);
            songName = "music";
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
                Intent intent = new Intent(music_player.this,Song_Gallery.class);
                intent.putExtra("URI_PATH",current_path);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                finish();
                intent.putExtra("SONGNAME",songName);

                startActivity(intent);
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