package com.example.feedpage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;


import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.brouding.doubletaplikeview.DoubleTapLikeView;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

public class MainActivity extends AppCompatActivity{
ImageButton download,share,comment;
    ImageView imageView;
    ImageButton bookmark;
    TextView textView;
    int i=0;
    private DoubleTapLikeView mDoubleTapLikeView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trollrecycler);
        imageView=findViewById(R.id.imageView);
        bookmark=findViewById(R.id.bookmark);
        comment=findViewById(R.id.comment);
        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        textView=findViewById(R.id.yu);
        share=findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bitmap bm = ((android.graphics.drawable.BitmapDrawable) imageView.getDrawable()).getBitmap();
                try {
                    java.io.File file = new java.io.File(getExternalCacheDir() + "/image.jpg");
                    java.io.OutputStream out = new java.io.FileOutputStream(file);
                    bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    out.flush();
                    out.close();
                } catch (Exception e) { e.toString(); }
                Intent iten = new Intent(android.content.Intent.ACTION_SEND);
                iten.setType("*/*");
                iten.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new java.io.File(getExternalCacheDir() + "/image.jpg")));
                startActivity(Intent.createChooser(iten, "Share Troll"));
            }
        });
        download=findViewById(R.id.download);
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BitmapDrawable draw = (BitmapDrawable) imageView.getDrawable();
                Bitmap bitmap = draw.getBitmap();

                FileOutputStream outStream = null;
                File sdCard = Environment.getExternalStorageDirectory().getAbsoluteFile();
                File dir = new File(sdCard.getAbsolutePath() + "/Psctrolls");
                dir.mkdirs();
                String fileName = String.format("%d.jpg", System.currentTimeMillis());
                File outFile = new File(dir, fileName);
                Toast.makeText(MainActivity.this,"Troll Downloaded",Toast.LENGTH_LONG).show();
                try {
                    outStream = new FileOutputStream(outFile);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                try {
                    outStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    outStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                intent.setData(Uri.fromFile(outFile));
                sendBroadcast(intent);
            }
        });
       imageView.setOnLongClickListener(new View.OnLongClickListener() {
           @Override
           public boolean onLongClick(View v) {
               new PhotoFullPopupWindow(getApplicationContext(), R.layout.popup_photo_full, v, "https://i.pinimg.com/originals/48/43/72/484372e4bc29a428dd32843f8653255c.jpg", null);
               return false;
           }
       });

if(readState())
{
    bookmark.setImageDrawable(getResources().getDrawable(R.drawable.bookmark));
}
else
{
    bookmark.setImageDrawable(getResources().getDrawable(R.drawable.bookmarked));
}

        mDoubleTapLikeView = findViewById(R.id.layout_double_tap_like);
        bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isFavourite = readState();

                if (isFavourite) {
                 bookmark.setImageDrawable(getResources().getDrawable(R.drawable.bookmarked));
                    isFavourite = false;
                    saveState(isFavourite);
                    Toast.makeText(MainActivity.this,"hi",Toast.LENGTH_LONG).show();

                } else {
                    bookmark.setImageDrawable(getResources().getDrawable(R.drawable.bookmark));
                    isFavourite = true;
                    saveState(isFavourite);
                    Toast.makeText(MainActivity.this,"hi1",Toast.LENGTH_LONG).show();

                }
            }
        });
        mDoubleTapLikeView.setOnTapListener(new DoubleTapLikeView.OnTapListener() {
            @Override
            public void onDoubleTap(View view) {
               // Toast.makeText(MainActivity.this, "Double TAPPED !", Toast.LENGTH_SHORT).show();

                i=i+1;
                textView.setText(i+"Likes");

            }

            @Override
            public void onTap() {
                // This method will be called if user didn't tap again after PRESS_TIME_TERM (default is 200)
                // So keep PRESS_TIME_GAP short ( 200~400 )^.
                // Due to Thread for single Tap, if you want to change UI through "onTap()", you should use Activity.runOnUiThread()
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                       // Toast.makeText(MainActivity.this, "Single TAPPED !", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    private void saveState(boolean isFavourite) {
        SharedPreferences aSharedPreferences = this.getSharedPreferences(
                "Favourite", Context.MODE_PRIVATE);
        SharedPreferences.Editor aSharedPreferencesEdit = aSharedPreferences
                .edit();
        aSharedPreferencesEdit.putBoolean("State", isFavourite);
        aSharedPreferencesEdit.apply();
    }
    private boolean readState() {
        SharedPreferences aSharedPreferences = this.getSharedPreferences(
                "Favourite", Context.MODE_PRIVATE);
        return aSharedPreferences.getBoolean("State", true);
    }
    private void shareImage(Uri imagePath) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        sharingIntent.setType("image/*");
        sharingIntent.putExtra(Intent.EXTRA_STREAM, imagePath);
        startActivity(Intent.createChooser(sharingIntent, "Share Image Using"));
    }


}

