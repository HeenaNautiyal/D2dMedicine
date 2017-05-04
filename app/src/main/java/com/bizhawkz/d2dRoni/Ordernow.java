package com.bizhawkz.d2dRoni;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Ordernow extends AppCompatActivity {
    //implements AdapterView.OnItemSelectedListener {
    String abc, name, mail, address,  mail1, mobile;
    TextView tvmed;
    Toolbar toolbar;
    ActorAdapter6 adapter;
    ProgressDialog pb;
    ArrayList<Chemist> actorsList;
    Button OK1, btn, chk1, con;
    SessionManager1 session;
    ArrayList<String> list = new ArrayList<String>();
    String  strJson;
    Intent intent;
    Context context;
    Integer a = 0;

    String TAG = "MainActivity";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordernow);

        session = new SessionManager1(getApplicationContext());
        session.checkLogin();

        actorsList = new ArrayList<Chemist>();
        pb = new ProgressDialog(Ordernow.this);
        tvmed = (TextView) findViewById(R.id.medicine);

        ListView listview = (ListView) findViewById(R.id.list);

        adapter = new ActorAdapter6(getApplicationContext(), R.layout.row5, actorsList);

        HashMap<String, String> user = session.getUserDetails();
        mail = user.get(SessionManager1.KEY_EMAIL);

        listview.setAdapter(adapter);
        TextView output = (TextView) findViewById(R.id.textView1);
        btn = (Button) findViewById(R.id.btn_continue);

        intent = getIntent();
        strJson = intent.getStringExtra("jsonArray");
        a = intent.getIntExtra("number", 0);
        initToolBar();

        try {

            JSONArray jsonArray = new JSONArray(strJson);

            for (int i = 0; i < jsonArray.length(); i++) {

                Chemist actor = new Chemist();

                JSONObject jsonObject = jsonArray.getJSONObject(i);

                actor.setMedid(Integer.parseInt(jsonObject.optString("medicine_id").toString()));
                actor.setMedicine(jsonObject.optString("medicine_name").toString());
                actor.setQuantity(jsonObject.optString("quantity").toString());
                actorsList.add(actor);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buildDialog(R.style.DialogTheme, "Left - Right Animation!");

            }
        });

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void buildDialog(int animationSource, String s) {
        final LayoutInflater layoutInflater = LayoutInflater.from(Ordernow.this);
        final View promptView = layoutInflater.inflate(R.layout.input_dialog2, null);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Ordernow.this);
        alertDialogBuilder.setView(promptView);

        final AlertDialog alert = alertDialogBuilder.create();

        OK1 = (Button) promptView.findViewById(R.id.btn_cart);

        chk1 = (Button) promptView.findViewById(R.id.btn_chk);
        chk1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
                Intent it= new Intent(Ordernow.this,Addressadd.class);
                it.putExtra("jsonArray",strJson );
                it.putExtra("number",a);
                startActivity(it);
            }

        });
        con = (Button) promptView.findViewById(R.id.btn_cart);
        con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnBackPressed();
            }
        });
        alert.getWindow().getAttributes().windowAnimations = animationSource;
        alert.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams wmlp = alert.getWindow().getAttributes();
        wmlp.gravity = Gravity.BOTTOM;
        alert.show();
    }

    private void OnBackPressed() {
        super.onBackPressed();
    }

    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("My Cart");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.back2_icon);
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent It = new Intent(Ordernow.this, DataActivity.class);
                        startActivity(It);
                    }
                });
    }


    @Override
    public void onStart() {
        super.onStart();

       /* client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Ordernow Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.bizhawkz.d2dmedicine/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);*/
    }

    @Override
    public void onStop() {
        super.onStop();

      /*  // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Ordernow Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.bizhawkz.d2dmedicine/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();*/
    }
}

