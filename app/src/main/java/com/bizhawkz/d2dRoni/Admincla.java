package com.bizhawkz.d2dRoni;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class Admincla extends AppCompatActivity {
    ImageView btnback;
    TextView tvmail;
    EditText rem,tvQuery;
    ProgressDialog pb;
    String mail, remark,Query;
    SessionManager1 session;
    Button OK1,submit;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admincla);
        pb = new ProgressDialog(Admincla.this);
        session = new SessionManager1(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        mail = user.get(SessionManager1.KEY_EMAIL);


        tvmail = (TextView) findViewById(R.id.ed_mail);
        tvmail.setText(mail);
        rem = (EditText) findViewById(R.id.addr_edittext);
        tvQuery=(EditText)findViewById(R.id.ed_Query);
        submit=(Button)findViewById(R.id.btn_Submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Queryadmin().execute();
            }
        });
        initToolBar();

    }

    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Contact Admin");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.back2_icon);
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent It = new Intent(Admincla.this, UploadMedicine.class);
                        startActivity(It);
                    }
                });
    }

    private class Queryadmin extends AsyncTask<String, String, String> {
        protected void onPreExecute() {
            super.onPreExecute();
            pb.setMessage("Please wait while Loading...");
            pb.show();
            remark = rem.getText().toString();
            Query=tvQuery.getText().toString();
        }

        @Override
        protected String doInBackground(String... urls) {
            HttpClient httpClient = new DefaultHttpClient();
            String url ="http://d2dmedicine.com/aPPmob_lie/insertdata.php?caseid=30&email="+mail+"&subject="+Query.replaceAll(" ","%20")+"&enquery="+remark.replaceAll(" ","%20")+"";
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
            pb.cancel();
            try {
                pb.dismiss();
                JSONObject jsonResult = new JSONObject(result);
                String message = jsonResult.getString("udata");
                Log.d("Response: ", "> " + message);
                if (message.equals("1")) {
                    buildDialog(R.style.DialogTheme, "Left - Right Animation!");
                    rem.setText("");
                    tvQuery.setText("");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void buildDialog(int animationSource, String s) {
        LayoutInflater layoutInflater = LayoutInflater.from(Admincla.this);
        final View promptView = layoutInflater.inflate(R.layout.custome_dialog, null);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Admincla.this);
        alertDialogBuilder.setView(promptView);
        final AlertDialog alert = alertDialogBuilder.create();

        alert.getWindow().getAttributes().windowAnimations = animationSource;
        alert.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams wmlp = alert.getWindow().getAttributes();
        wmlp.gravity = Gravity.BOTTOM ;
        wmlp.x = 100;   //x position
        wmlp.y = 100;   //y position

        alert.show();
    }
}

