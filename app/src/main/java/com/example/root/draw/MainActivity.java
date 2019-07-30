package com.example.root.draw;


import android.Manifest;
import android.annotation.*;
import android.app.Dialog;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;
import java.util.UUID;

import yuku.ambilwarna.AmbilWarnaDialog;

@SuppressWarnings("SpellCheckingInspection")
public class MainActivity extends AppCompatActivity {



    private DrawPadView hbView;

    private int paintWidth;
    SeekBar widthSb;

    LinearLayout control;

    Dialog d, dask;
    View save, newC, ask;



    Button show;


    @SuppressLint("InflateParams")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        try{
            this.getSupportActionBar().hide();
        } catch (Exception e){
            log("Expc::::;" +e.getMessage());
        }

        setContentView(R.layout.activity_main);


        d = new Dialog(MainActivity.this);
        dask = new Dialog(MainActivity.this);
        save = getLayoutInflater().inflate(R.layout.save, null );
        newC = getLayoutInflater().inflate(R.layout.new_file,null);
        ask = getLayoutInflater().inflate(R.layout.ask,null);


        dask.setContentView(ask);



        ask.findViewById(R.id.yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1 );
                dask.dismiss();

            }
        });
        ask.findViewById(R.id.no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dask.dismiss();
            }
        });

        if(ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            dask.show();
        }



        hbView = findViewById(R.id.drawPadView1);

        widthSb = findViewById(R.id.seekBar1);
        control = findViewById(R.id.controlls);

        //show = findViewById(R.id.showC);

        widthSb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                log(" width == "+progress+1);
                paintWidth = progress+1;

                hbView.setPaintWidth(paintWidth);
            }
        });

    }

    public void setC(View view) {
        log(""+view.getTag());
        String color = (String) view.getTag();

        if(color.equals("#ffffff")){
           // hbView.setPaintWidth(100);
            widthSb.setProgress(100);
            control.setBackgroundColor(Color.WHITE);
            findViewById(R.id.mainl).setBackgroundColor(Color.WHITE
            );

        }
        if(color.equals("pencil")){
       //     color = "#000000";
            hbView.setColor(Color.BLACK);
            widthSb.setProgress(3);
            control.setBackgroundColor(Color.BLACK);
            findViewById(R.id.mainl).setBackgroundColor(Color.BLACK);

        }
        else {
            hbView.setColor(Color.parseColor(color));
         //   widthSb.setProgress(3);
            control.setBackgroundColor(Color.parseColor(color));
            findViewById(R.id.mainl).setBackgroundColor(Color.parseColor(color));


        }

    }








    private void log(String s) {

        Log.e("","Log::"+s);

      //  hbView.setColor(Color.GREEN);

    }



    //------------------------------------------------__Saving
    public void save(View view) {




        d.setContentView(save);

        //-------------------positive
        save.findViewById(R.id.yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*
             ...........   SAVING FILE
                 */

                hbView.setDrawingCacheEnabled(true);

                String imgSaved = MediaStore.Images.Media.insertImage(
                        getContentResolver(), hbView.getDrawingCache(),
                        UUID.randomUUID().toString()+".png", "drawing");


                if(imgSaved!=null){
                    Toast savedToast = Toast.makeText(getApplicationContext(),
                            "Drawing saved to Gallery!", Toast.LENGTH_SHORT);
                    savedToast.show();
                }
                else{
                    Toast unsavedToast = Toast.makeText(getApplicationContext(),
                            "Oops! Image could not be saved.", Toast.LENGTH_SHORT);
                    unsavedToast.show();
                }
                hbView.destroyDrawingCache();
                d.dismiss();

            }
        });

        save.findViewById(R.id.no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });

        d.show();
        if(ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            dask.show();
        }




    }

    //--------------------------------------------------___Reset

    public void reset(View view) {


        d.setContentView(newC);
        newC.findViewById(R.id.yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hbView.clearScreen();
                d.dismiss();
            }
        });

        newC.findViewById(R.id.no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });

        d.show();


    }

    //---------------------------------------__Color Peeker

    public void colorPeek(View view) {


        AmbilWarnaDialog dialog = new AmbilWarnaDialog(this, Color.RED, false , new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                hbView.setColor(color);
                control.setBackgroundColor(color);
                findViewById(R.id.mainl).setBackgroundColor(color);
           }

            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
                // cancel was selected by the user
            }

        });


        dialog.show();

    }

    public void onBackPressed(){
        View exit = getLayoutInflater().inflate(R.layout.exit,null);
        d.setContentView(exit);

        exit.findViewById(R.id.yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }
        });

        exit.findViewById(R.id.no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });

        d.show();



    }





}