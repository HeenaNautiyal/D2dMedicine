package com.bizhawkz.d2dmedicine;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class OrderReplied extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar;
    EditText eddate,edstock;
    String date,stock,stdate,mail,chemmail,eddate1,edstock1;
    Button btnstock,btndate,btnsubmit;
    private DatePickerDialog fromDatePickerDialog;
    private SimpleDateFormat dateFormatter;
    private CoordinatorLayout coordinatorLayout;
    SessionManager1 session;
    ProgressDialog pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pb = new ProgressDialog(OrderReplied.this);
        setContentView(R.layout.activity_order_replied);
        btndate=(Button)findViewById(R.id.btndeparture);
        btnstock=(Button)findViewById(R.id.btnstock);
        btnsubmit=(Button)findViewById(R.id.btn_continue);
        eddate=(EditText)findViewById(R.id.ed_expier);



        session = new SessionManager1(getApplicationContext());
        session.checkLogin();
        dateFormatter = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .coordinatorLayout);


        eddate.setVisibility(View.INVISIBLE);

        btndate.setOnClickListener(this);
        btnstock.setOnClickListener(this);
        initToolBar();

        HashMap<String, String> user = session.getUserDetails();
        mail = user.get(SessionManager1.KEY_EMAIL);
        chemmail=user.get(SessionManager1.KEY_CHEMEMAIL);

        eddate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDateTimeField();
            }
        });
        eddate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    fromDatePickerDialog.show();
                }else {                                 }
            }
        });
        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eddate1=eddate.getText().toString();
                edstock1=eddate1;
              if (!eddate1.isEmpty()){
                  eddate1="Your%20order%20will%20be%20deliever%20till"+date+"";
                  new nofication().execute();
                  Log.e("Order",date);
              }
            }
        });



    }

    private void setDateTimeField() {
        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                eddate.setText(dateFormatter.format(newDate.getTime()));
                date=dateFormatter.format(newDate.getTime());
                stdate=eddate.getText().toString();
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

    }

    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Order Reply");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.back2_icon);
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent It = new Intent(OrderReplied.this, OrderRecieve.class);
                        startActivity(It);
                    }
                });
    }

    @Override
    public void onClick(View v) {
        if (v == btndate) {
            setDateTimeField();
            eddate.setVisibility(View.VISIBLE);
        }
        if (v == btnstock) {
            new nofication1().execute();
            eddate.setVisibility(View.INVISIBLE);
        }

    }

    private class nofication extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pb.setMessage("Please wait while Loading...");
            pb.show();
        }


        @Override
        protected String doInBackground(String... params) {
            HttpClient httpClient = new DefaultHttpClient();
            String url = "http://d2dmedicine.com/aPPmob_lie/insertdata.php?caseid=92" +
                    "&usname="+mail+"&chname="+chemmail+"&message="+eddate1+"";
            String SetServerString = "";
            HttpGet httpget = new HttpGet(url);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            try {
                SetServerString = httpClient.execute(httpget, responseHandler);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return SetServerString;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                pb.dismiss();
                JSONObject jsonResult = new JSONObject(result);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class nofication1 extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pb.setMessage("Please wait while Loading...");
            pb.show();
        }


        @Override
        protected String doInBackground(String... params) {
            HttpClient httpClient = new DefaultHttpClient();
            String url = "http://d2dmedicine.com/aPPmob_lie/insertdata.php?caseid=92" +
                    "&usname="+mail+"&chname="+chemmail+"&message=Sorry%20this%20product%20is%20currently%20out%20of%20stock";
            String SetServerString = "";
            HttpGet httpget = new HttpGet(url);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            try {
                SetServerString = httpClient.execute(httpget, responseHandler);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return SetServerString;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                pb.dismiss();
                JSONObject jsonResult = new JSONObject(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
