package com.bizhawkz.d2dmedicine;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

public class UploadMedicine extends AppCompatActivity {

    Toolbar toolbar;
    ImageButton iv_profile;
    TextView btn,btnadmin,btnsummery,btnRecieved;
    TextView tvname,tvmail;
    private CoordinatorLayout coordinatorLayout;
    SessionManager1 session;
    String a, lat, lon, message2,mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_medicine);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .coordinatorLayout);

        session = new SessionManager1(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        mail = user.get(SessionManager1.KEY_EMAIL);
        tvname=(TextView)findViewById(R.id.textView3);
        tvname.setText(mail);


        iv_profile=(ImageButton)findViewById(R.id.iv_profile);
        initToolBar();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, "Do you want to logout", Snackbar.LENGTH_LONG)
                        .setAction("Yes", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                session.logoutUser();
                            }
                        });
                snackbar.setActionTextColor(Color.RED);
                View sbView = snackbar.getView();
                TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(Color.YELLOW);
                snackbar.show();
            }
        });
        btn=(TextView) findViewById(R.id.btn_upload);
        btnadmin=(TextView) findViewById(R.id.btn_Contact);
        btnsummery=(TextView) findViewById(R.id.btn_notify);
        btnRecieved=(TextView) findViewById(R.id.btnRecieve);

        btnRecieved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it=new Intent(UploadMedicine.this,OrderRecieve.class);
                startActivity(it);
            }
        });

        btnadmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(UploadMedicine.this, Admincla.class);
                startActivity(it);
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent it = new Intent(UploadMedicine.this, Chemistupload.class);
                startActivity(it);
            }
        });
        btnsummery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(UploadMedicine.this, Summery.class);
                startActivity(it);
            }
        });

        iv_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent it= new Intent(UploadMedicine.this,ChemistProfile.class);
                startActivity(it);*/
            }
        });

    }

    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Dashboard");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
    }


    private class Loggps extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... strings) {
            HttpClient httpClient = new DefaultHttpClient();
            String url = "http://d2dmedicine.com/aPPmob_lie/insertdata.php?caseid=21&email="+mail+"";
            String SetServerString = "";
            HttpGet httpget = new HttpGet(url);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            try {
                SetServerString = httpClient.execute(httpget, responseHandler);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d("Response: ", "> " + SetServerString);
            return SetServerString;
        }

        protected void onPostExecute(String result) {
            try {
                //pb.dismiss();
                JSONObject jsonResult = new JSONObject(result);
                String message = jsonResult.getString("udata");
                Log.d("Response: ", "> " + message);
                if (message.equals("1")) {
                    message2 = jsonResult.getString("result");
                    Intent It = new Intent(UploadMedicine.this, Nearchemeist.class);
                    Bundle b = new Bundle();
                    b.putString("pincode", message2);
                    It.putExtras(b);
                    startActivity(It);

                } else {
                    final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(UploadMedicine.this);
                    TextView myMsg = new TextView(UploadMedicine.this);
                    myMsg.setText("Warning!");
                    myMsg.setGravity(Gravity.CENTER_HORIZONTAL);
                    myMsg.setTextSize(20);
                    myMsg.setTextColor(Color.BLACK);
                    builder.setCustomTitle(myMsg);
                    builder.setMessage("Unable to get Location.");
                    builder.setPositiveButton("OK.",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.dismiss();
                                }
                            });
                    builder.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
