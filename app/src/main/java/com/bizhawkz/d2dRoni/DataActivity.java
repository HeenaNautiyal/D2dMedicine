package com.bizhawkz.d2dRoni;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class DataActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ArrayList<Chemist> actorsList;
    ActorAdapter3 adapter;
    ImageView btn_FirstAid,btn_prescribed,btn_OTC,btn_Daily,btnbaby;
    EditText editsearch;
    SessionManager1 session;
    SharedPreferences settings, pwd;
    public static final String PREFS_NAME = "LoginPrefs";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        session = new SessionManager1(getApplicationContext());
        session.checkLogin();
        settings = getSharedPreferences(PREFS_NAME, 0);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        btn_FirstAid=(ImageView) findViewById(R.id.firstaid);
        btn_FirstAid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it= new Intent(DataActivity.this,FirstAidnew.class);
                startActivity(it);
            }
        });
        btn_prescribed=(ImageView)findViewById(R.id.prescribe);
        btn_prescribed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it= new Intent(DataActivity.this,UploadPresc.class);
                startActivity(it);
            }
        });

        btn_OTC=(ImageView)findViewById(R.id.home);
        btn_OTC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it= new Intent(DataActivity.this,OTCCounter.class);
                startActivity(it);
            }
        });

        btn_Daily=(ImageView)findViewById(R.id.collage);
        btn_Daily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it= new Intent(DataActivity.this,DailyCare.class);
                startActivity(it);
            }
        });
        btnbaby=(ImageView)findViewById(R.id.babycare);
        btnbaby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it= new Intent(DataActivity.this,BabyCare.class);
                startActivity(it);
            }
        });

        ViewPager mViewPager = (ViewPager) findViewById(R.id.viewPageAndroid);
        AndroidImageAdapter adapterView = new AndroidImageAdapter(this);
        mViewPager.setAdapter(adapterView);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         int id = item.getItemId();
         return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.iv_profile) {
            Intent it = new Intent( DataActivity.this,Profile.class);
            startActivity(it);

        }
        else if (id == R.id.iv_share) {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "D2D Medicine Lifeline to Life");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.bizhawkz.d2dmedicine&hl=en");
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
        }
        else if (id == R.id.iv_orderTrack) {
            Intent it = new Intent( DataActivity.this,OrderTrack.class);
            startActivity(it);
        }
        else if (id == R.id.iv_signout) {
            AlertDialog.Builder builder = new AlertDialog.Builder(DataActivity.this);
            TextView myMsg = new TextView(DataActivity.this);
            myMsg.setText("Warning!");
            myMsg.setGravity(Gravity.CENTER_HORIZONTAL);
            myMsg.setTextSize(20);
            myMsg.setTextColor(Color.BLACK);
            builder.setCustomTitle(myMsg).setMessage("Do you want to logout.")
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    session.logoutUser();
                                }
                            })
                    .show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }
}
