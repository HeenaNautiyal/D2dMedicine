package com.bizhawkz.d2dRoni;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;

public class Summery extends AppCompatActivity {
    ImageView btnback;
    SessionManager1 session;
    String mail;
    Toolbar toolbar;
    TextView tv;
    TextView btnotc,btnpres,btndaily,btnbaby,btnfirst;

    String TAG="MainActivity",fname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summery);
        tv=(TextView)findViewById(R.id.textView4);
        initToolBar();

        session = new SessionManager1(getApplicationContext());
        session.checkLogin();
        HashMap<String, String> user = session.getUserDetails();
        mail = user.get(SessionManager1.KEY_EMAIL);

        btnotc=(TextView) findViewById(R.id.linearlayout11);
        btnpres=(TextView)findViewById(R.id.linearlayout12);
        btndaily=(TextView)findViewById(R.id.linearlayout15);
        btnbaby=(TextView)findViewById(R.id.linearlayout13);
        btnfirst=(TextView)findViewById(R.id.linearlayout14);

        btnotc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Summery.this,Test.class);
                Bundle b = new Bundle();
                b.putString("email", "OTC (Over The Counter)");
                it.putExtras(b);
                startActivity(it);

            }
        });
        btnpres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Summery.this,Test1.class);
                Bundle b = new Bundle();
                b.putString("email", "Prescribed");
                it.putExtras(b);
                startActivity(it);

            }
        });
        btndaily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Summery.this,Test2.class);
                Bundle b = new Bundle();
                b.putString("email", "Daily Care");
                it.putExtras(b);
                startActivity(it);

            }
        });
        btnbaby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Summery.this,Test3.class);
                Bundle b = new Bundle();
                b.putString("email", "Baby Care");
                it.putExtras(b);
                startActivity(it);
            }
        });
        btnfirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Summery.this,Test4.class);
                Bundle b = new Bundle();
                b.putString("email", " First Aid");
                it.putExtras(b);
                startActivity(it);
            }
        });

    }

    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Medicine Summary");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.back2_icon);
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent It = new Intent(Summery.this, UploadMedicine.class);
                        startActivity(It);
                    }
                });
    }
}

