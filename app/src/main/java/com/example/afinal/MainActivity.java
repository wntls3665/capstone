package com.example.afinal;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private CallbackManager mCallbackManager;
    //GoogleSignInResult result;
    private AccessToken mToken = null;
    public static String selected_region = "";
    ArrayList<Vertex> arVertex;   // for save drawing



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        mCallbackManager = CallbackManager.Factory.create();
        mToken = AccessToken.getCurrentAccessToken();
/*        GoogleSignInAccount acct=result.getSignInAccount();
        acct.getIdToken();*/

        if (mToken == null) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }


        RelativeLayout myLayout = (RelativeLayout) findViewById(R.id.backImg);
        myLayout.setBackgroundResource(R.drawable.imageex2);
        final MyGraphicView myGraphicView = new MyGraphicView(this);
        myLayout.addView(myGraphicView);


        //show tab
        TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);
        tabHost.setup();

        TabSpec spec1 = tabHost.newTabSpec("Tab1").setContent(R.id.tab1)
                .setIndicator(getString(R.string.tab1));
        tabHost.addTab(spec1);

        TabSpec spec2 = tabHost.newTabSpec("Tab2").setContent(R.id.tab2)
                .setIndicator(getString(R.string.tab2));
        tabHost.addTab(spec2);

        TabSpec spec3 = tabHost.newTabSpec("Tab3").setContent(R.id.tab3)
                .setIndicator(getString(R.string.tab3));
        tabHost.addTab(spec3);

        //Report button
        Button btnReport = (Button) findViewById(R.id.btnReport);
        btnReport.setOnClickListener(new View.OnClickListener()

                                     {
                                         public void onClick(View v) {
                                             View dialogView = (View) View.inflate(MainActivity.this,
                                                     R.layout.report, null);
                                             AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);
                                             dlg.setTitle("신고하기");
                                             dlg.setView(dialogView);
                                             dlg.setPositiveButton("신고한다", new DialogInterface.OnClickListener() {
                                                 @Override
                                                 public void onClick(DialogInterface dialog, int which) {
                                                     Toast.makeText(getApplicationContext(), "신고횟수 증가", Toast.LENGTH_LONG).show();
                                                 }
                                             });
                                             dlg.setNegativeButton("닫기", null);
                                             dlg.show();
                                         }
                                     }
        );

        //Erase button
        Button btnErase = (Button)findViewById(R.id.btnErase);
        btnErase.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                arVertex.clear();
                myGraphicView.setVisibility(View.GONE);
                myGraphicView.setVisibility(View.VISIBLE);

            }
        });


        //GridView
        final GridView gv = (GridView) findViewById(R.id.gridView);
        MyGridAdapter gAdapter = new MyGridAdapter(this);
        gv.setAdapter(gAdapter);

        final GridView gvMy = (GridView) findViewById(R.id.gridViewMy);
        MyGridAdapter2 gridAdapterMy = new MyGridAdapter2(this);
        gvMy.setAdapter(gridAdapterMy);

        //spinner
        Spinner spinnerRegion = (Spinner) findViewById(R.id.selectRegion);
        final ArrayAdapter<CharSequence> adapterSpin = ArrayAdapter.createFromResource(this, R.array.region, android.R.layout.simple_spinner_item);
        adapterSpin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerRegion.setPrompt("Select Region");
        spinnerRegion.setSelection(0);
        spinnerRegion.setAdapter(adapterSpin);
/*
        spinnerRegion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selected_region=(String)adapterSpin.getItem(position);
            }
        });*/
    }


// Draw Score

    public class MyGraphicView extends View {
        Paint mpaint;

        public MyGraphicView(Context context) {
            super(context);

            arVertex = new ArrayList<Vertex>();

            mpaint = new Paint();
            mpaint.setColor(Color.RED);
            mpaint.setStrokeWidth(20);
            mpaint.setAntiAlias(true);
        }

        @Override
        protected void onDraw(Canvas canvas) {

            super.onDraw(canvas);
            for (int i = 0; i < arVertex.size(); i++) {
                if (arVertex.get(i).draw) {
                    canvas.drawLine(arVertex.get(i - 1).x,
                            arVertex.get(i - 1).y,
                            arVertex.get(i).x,
                            arVertex.get(i).y,
                            mpaint);
                }
            }
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    arVertex.add(new Vertex(event.getX(), event.getY(), false));
                    return true;
                //break;
                case MotionEvent.ACTION_MOVE:
                    arVertex.add(new Vertex(event.getX(), event.getY(), true));
                    invalidate();
                    return true;
            }
            return false;
        }


    }


    protected void onResume(Bundle savedInstanceState) {
        AppEventsLogger.activateApp(this);
    }

    protected void onPause(Bundle savedInstanceState) {
        AppEventsLogger.deactivateApp(this);
    }






    //gridview setting ( ranking)
    public class MyGridAdapter extends BaseAdapter {
        Context context;

        public MyGridAdapter(Context c) {
            context = c;
        }

        public int getCount() {
            return imageID.length;
        }

        public Object getItem(int arg0) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        //load image
        Integer[] imageID = {
                R.drawable.imageex1, R.drawable.imageex2, R.drawable.imageex3,
                R.drawable.imageex1, R.drawable.imageex2, R.drawable.imageex3,
                R.drawable.imageex1, R.drawable.imageex2,
                R.drawable.imageex1, R.drawable.imageex2, R.drawable.imageex3,
                R.drawable.imageex1, R.drawable.imageex2, R.drawable.imageex3,
                R.drawable.imageex1, R.drawable.imageex2, R.drawable.imageex1,
                R.drawable.imageex2, R.drawable.imageex3,
                R.drawable.imageex1, R.drawable.imageex2, R.drawable.imageex3,
                R.drawable.imageex1, R.drawable.imageex2
        };

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ImageView imageview = new ImageView(context);
            imageview.setLayoutParams(new GridView.LayoutParams(300, 400));
            imageview.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageview.setPadding(10, 10, 10, 10);

            imageview.setImageResource(imageID[position]);

            final int pos = position;
            imageview.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    View dialogView = (View) View.inflate(MainActivity.this,
                            R.layout.detail, null);
                    AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);
                    ImageView ivImage = (ImageView) dialogView.findViewById(R.id.ivImage);
                    ivImage.setImageResource(imageID[pos]);
                    dlg.setView(dialogView);
                    dlg.setNegativeButton("닫기", null);
                    dlg.show();
                }

            });
            return imageview;

        }
    }







    //grid view setting (my photo)
    public class MyGridAdapter2 extends BaseAdapter {
        Context context;

        public MyGridAdapter2(Context c) {
            context = c;
        }

        public int getCount() {
            return imageID.length;
        }

        public Object getItem(int arg0) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        //load image
        Integer[] imageID = {
                R.drawable.imageex1, R.drawable.imageex2, R.drawable.imageex3,

        };

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ImageView imageview = new ImageView(context);
            imageview.setLayoutParams(new GridView.LayoutParams(300, 450));
            imageview.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageview.setPadding(3, 3, 3, 3);

            imageview.setImageResource(imageID[position]);

            final int pos = position;
            imageview.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    View dialogView = (View) View.inflate(MainActivity.this,
                            R.layout.detail, null);
                    AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);
                    ImageView ivImage = (ImageView) dialogView.findViewById(R.id.ivImage);
                    ivImage.setImageResource(imageID[pos]);
                    dlg.setView(dialogView);
                    dlg.setNegativeButton("닫기", null);
                    dlg.show();
                }

            });
            return imageview;
        }
    }


    public class Vertex {
        float x;
        float y;
        boolean draw;

        Vertex(float _x, float _y, boolean _d) {
            this.x = _x;
            this.y = _y;
            this.draw = _d;
        }
    }






}
