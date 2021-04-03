package com.example.tfwapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ImageUploadActivity extends AppCompatActivity {
    public Bitmap bp;
    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    public static final int GALLERY_REQUEST_CODE = 105;
    public String picture_name;
    ImageView selectedImage;
    Button cameraBtn, galleryBtn, submitBtn, signoutBtn, songGallBtn;
    public Uri uriPhotoPath;
    public String currentPhotoPath;
    public File f1;

    OutputStream outputStream;

    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    public int dominantRed, dominantGreen, dominantBlue;

    private LinearLayout rootLayout;

    public static int[] getDominantColor(Bitmap bitmap) {

        int[] colorArray = {0,0,0};

        if (bitmap == null) {
            return colorArray;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int size = width * height;
        int pixels[] = new int[size];
        //Bitmap bitmap2 = bitmap.copy(Bitmap.Config.ARGB_4444, false);
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        int color;
        int r = 0;
        int g = 0;
        int b = 0;
        int a;
        int count = 0;
        for (int i = 0; i < pixels.length; i++) {
            color = pixels[i];
            a = Color.alpha(color);
            if (a > 0) {
                r += Color.red(color);
                g += Color.green(color);
                b += Color.blue(color);
                count++;
            }
        }
        r /= count;
        g /= count;
        b /= count;
        colorArray[0] = r;
        colorArray[1] = g;
        colorArray[2] = b;
/*
        r = (r << 16) & 0x00FF0000;
        g = (g << 8) & 0x0000FF00;
        b = b & 0x000000FF;
        color = 0xFF000000 | r | g | b;

        String rString = Integer.toString(r);
        String gString = Integer.toString(g);
        String bString = Integer.toString(b);

        Log.i("red ", rString);
        Log.i("green ", gString);
        Log.i("blue ", bString);
*/
        return colorArray;
    }
    private static final int PICK_IMAGE = 100;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.image_upload);

        selectedImage = findViewById(R.id.displayImageView);
        cameraBtn = findViewById(R.id.cameraBtn);
        galleryBtn = findViewById(R.id.galleryBtn);
        submitBtn = findViewById(R.id.submit_button);
        signoutBtn = findViewById(R.id.sign_out);

        //Get User instance
        user = FirebaseAuth.getInstance().getCurrentUser();

        //Get Firebasedatabase
        reference = FirebaseDatabase.getInstance().getReference("Users");

        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askCameraPermissions();
            }
        });

        galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ImageUploadActivity.this, "Gallery Button is Clicked.", Toast.LENGTH_SHORT).show();

                Intent gallery = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery, GALLERY_REQUEST_CODE);
                //openGallery();
            }
        });

        //Submit Button
        submitBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //Create bitmap of image selected
                Bitmap bitmap = ((BitmapDrawable)selectedImage.getDrawable()).getBitmap();
                //Calls getDominantColor and stores RBG values in an integer array
               int[] finalColorArray = getDominantColor(bitmap);

                dominantRed = finalColorArray[0];
                dominantGreen = finalColorArray[1];
                dominantBlue = finalColorArray[2];

                // Outputs rgb colors to terminal for debugging
                String rString = Integer.toString(dominantRed);
                String gString = Integer.toString(dominantGreen);
                String bString = Integer.toString(dominantBlue);
                Log.i("RED ", rString);
                Log.i("GREEN ", gString);
                Log.i("BLUE ", bString);

                //Puts values into Realtime database
/*              reference.child(user.getUid()).child("red").setValue(dominantRed);
                reference.child(user.getUid()).child("green").setValue(dominantGreen);
                reference.child(user.getUid()).child("blue").setValue(dominantBlue);

*/

                //starts music player activity
                Intent intent = new Intent(ImageUploadActivity.this,music_player.class);
                //Sends rgb to music player
                intent.putExtra("RED", dominantRed);
                intent.putExtra("GREEN", dominantGreen);
                intent.putExtra("BLUE", dominantBlue);

                intent.putExtra("CURRENT_PATH",currentPhotoPath);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);


                //creates file path for new file to be created
                File filepath = getExternalCacheDir();
                File dir = new File(filepath.getAbsolutePath()+"/TFW_Images/");
                //checks if directory for file exists, if not creates a new one
                if(!dir.exists()) {
                    dir.mkdir();
                }
                //file name
                File file = new File(dir, System.currentTimeMillis()+".jpg");
                //writing to output stream
                try{
                    outputStream = new FileOutputStream(file);
                } catch (FileNotFoundException e){
                    e.printStackTrace();
                }
                Log.i("tag","got this far");
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                Toast.makeText(getApplicationContext(),"Image Save to Internal !!!"
                ,Toast.LENGTH_SHORT).show();
                try {
                    outputStream.flush();
                } catch(IOException e){
                    e.printStackTrace();
                }
                try {
                    outputStream.close();
                } catch(IOException e){
                    e.printStackTrace();
                }
                //saving Uri from newly created file
                uriPhotoPath = Uri.fromFile(file);

                intent.putExtra("URI_PATH",uriPhotoPath);
                startActivity(intent);
            }
        });
        //sign out button
        signoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                finish();
            }
        });
        songGallBtn = findViewById(R.id.song_gallBtn);
        //Song Gallery Button
        songGallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ImageUploadActivity.this,Song_Gallery.class);
                startActivity(intent);
            }
        });

    }

    private void askCameraPermissions() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
        }else {
            dispatchTakePictureIntent();
        }

    }

    //Camera permissions
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == CAMERA_PERM_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                dispatchTakePictureIntent();
            }else {
                Toast.makeText(ImageUploadActivity.this, "Camera Permission is Required to Use Camera", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                File f = new File(currentPhotoPath);
                selectedImage.setImageURI(Uri.fromFile(f));
                Log.d("tag", "Absolute URL of Image is " + Uri.fromFile(f));
                //getting picture name
                picture_name = f.getName();

                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(f);
                mediaScanIntent.setData(contentUri);
                this.sendBroadcast(mediaScanIntent);
            }
        }
        if (requestCode == GALLERY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri contentUri = data.getData();
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "JPEG_" + timeStamp + "." + getFileExt(contentUri);
                Log.d("tag", "onActivityResult: Gallery Image Uri:  " + imageFileName);
                //getting picture name

                String example = contentUri.getPath();

                picture_name = imageFileName;

                uriPhotoPath = contentUri;
                selectedImage.setImageURI(contentUri);

            }
        }
    }

    private String getFileExt(Uri contentUri) {
        ContentResolver c = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(c.getType(contentUri));
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        //Supposed to be used to allow photo taken in app to be stored in device gallery
        //File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        //String example = image.toString();
        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }
    public static InputStream loadImageCache(Context context, Uri uri){
        InputStream inputStream = null;
        try {
            File file = new File(context.getFilesDir(), uri.getPath());
            inputStream = new BufferedInputStream(new FileInputStream(file));
        } catch (IOException e) {
            //Log.e(TAG, e.getMessage());
        }
        return inputStream;
    }

}