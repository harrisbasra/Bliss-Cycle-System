package com.cyclesystem.bliss;

import android.annotation.SuppressLint;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowInsets;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cyclesystem.bliss.databinding.ActivityViewRecordsBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class ViewRecords extends AppCompatActivity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler(Looper.myLooper());

    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {

        }
    };

    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }

        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (AUTO_HIDE) {
                        delayedHide(AUTO_HIDE_DELAY_MILLIS);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    view.performClick();
                    break;
                default:
                    break;
            }
            return false;
        }
    };
    private ActivityViewRecordsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityViewRecordsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mVisible = true;
        Date h = new Date();
        String A = String.valueOf(h.getDate());
        String B = String.valueOf(h.getMonth());
        String F = "Today,  "+A+"-"+B+"-"+"2022";
        TextView tev = (TextView) findViewById(R.id.textView5);
        tev.setText(F);
        String Banana = "";

        try {
            FileInputStream fin = openFileInput("Records.txt");
            int a;
            StringBuilder temp = new StringBuilder();
            while ((a = fin.read()) != -1) {
                temp.append((char)a);
            }
            Banana = temp.toString();
            fin.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        String Funny[] = arrayreturner(Banana);
        ListView DB = (ListView) findViewById(R.id.lvlv);

        ArrayAdapter<String> Daniyal ;
        Daniyal= new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, Funny );
        DB.setAdapter(Daniyal);

        /////////////////...........................................................................

        FloatingActionButton Plus = findViewById(R.id.floatingActionButton3);
        Plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent o = new Intent(ViewRecords.this, MapsActivity.class);
                startActivity(o);
            }
        });

        FloatingActionButton Export = findViewById(R.id.floatingActionButton2);
        Export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String SEL = "" ;
                try {
                    FileInputStream fin = openFileInput("Records.txt");
                    int a;
                    StringBuilder temp = new StringBuilder();
                    while ((a = fin.read()) != -1) {
                        temp.append((char)a);
                    }
                    SEL = temp.toString();
                    fin.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }

                SEL.replaceAll("|", "\n");


                FileOutputStream outputStream = null;
                try {
                    String fileName = "Bliss Cycle System Report.txt";
                    File documentsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
                    File file = new File(documentsDir, fileName);
                    outputStream = new FileOutputStream(file);
                    outputStream.write(SEL.getBytes());
                    outputStream.close();
                    Toast.makeText(ViewRecords.this, "File has been exported to Documents as Bliss Cycle System Report.txt", Toast.LENGTH_SHORT).show();
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }


            }
        });

    }
    String[] arrayreturner(String arr){
        int slashes=1;
        for (int i = 0; i < arr.length(); i++) {
            if(arr.charAt(i)=='|'){
                slashes++;
            }
        }

        String divided[] = new String[slashes];
        int ii=0;
        for(int i=0;i<slashes;i++){
            divided[i]="";
        }
        for(int i=0;i<arr.length();i++){
            if(arr.charAt(i)=='|'){
                ii++;
            }
            else{
                divided[ii] = divided[ii]+arr.charAt(i);
            }
        }
        return divided;

    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    private void toggle() {

    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    private void show() {

        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
}