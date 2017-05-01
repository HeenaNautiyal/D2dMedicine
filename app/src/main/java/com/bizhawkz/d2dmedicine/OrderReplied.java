package com.bizhawkz.d2dmedicine;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class OrderReplied extends AppCompatActivity  {
    Toolbar toolbar;
    EditText eddate,edcomment;
    String flag,stock,name,mail,chemmail,eddate1,message,orderid,medicine;
    Button btnstock,btndate,btnsubmit;
    private DatePickerDialog fromDatePickerDialog;
    private SimpleDateFormat dateFormatter;
    private CoordinatorLayout coordinatorLayout;
    SessionManager1 session;
    ProgressDialog pb;
    TextView tvdate;
    private RadioGroup radioGroup;
    CheckBox chk;
    Calendar newDate = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pb = new ProgressDialog(OrderReplied.this);
        setContentView(R.layout.activity_order_replied);

        Bundle b = getIntent().getExtras();
        chemmail = (String) b.getCharSequence("byeremail");
       chemmail=chemmail.replaceAll("\r","");
       chemmail=chemmail.replaceAll("\n","");
        orderid=(String)b.getCharSequence("orderid");
        medicine=(String)b.getCharSequence("medicine");

        btnsubmit=(Button)findViewById(R.id.btn_continue);
        edcomment=(EditText)findViewById(R.id.ed_commenmt);
        eddate=(EditText)findViewById(R.id.ed_expier);
        tvdate=(TextView)findViewById(R.id.tv_date);
        chk = (CheckBox) findViewById(R.id.checkBox1);
        radioGroup=(RadioGroup) findViewById(R.id.myRadioGroup);
        session = new SessionManager1(getApplicationContext());
        session.checkLogin();
        dateFormatter = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .coordinatorLayout);
        initToolBar();
        setDateTimeField();

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(tvdate.getWindowToken(), 0);

        HashMap<String, String> user = session.getUserDetails();
        mail = user.get(SessionManager1.KEY_EMAIL);


        eddate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromDatePickerDialog.show();
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rd_1) {
                    flag="1";
                } else if (checkedId == R.id.rb_2) {
                    flag="2";
                } else if (checkedId == R.id.rb_3) {
                    flag="3";
                } else if(checkedId == R.id.rb_4){
                    flag="4";
                }
            }

        });

        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag=="2") {
                    message="This%20medicineis%20out%20of%20stock%20in%20our%20store.%20Please%20Order%20again%20and%20select%20another%20" +
                            "store.%20Thank%20you%20for%20Placing%20order.";
                    System.out.println("message: "+message);
                    new nofication().execute();
                               }
                else{
                    message="Your%20order%20has%20been%20deliver%20till%20"+eddate1;
                    System.out.println("message: "+message);
                    new nofication().execute();
                }
            }
        });
    }


    private void setDateTimeField() {
        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                newDate.set(year, monthOfYear, dayOfMonth);
                eddate.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        eddate.setText(dateFormatter.format(newDate.getTime()));
        eddate1=eddate.getText().toString();
        tvdate.setText(eddate1);
    }

    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Order Confirmation Mail");
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



    private class nofication extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*pb.setMessage("Please wait while Loading...");
            pb.show();*/
        }

        @Override
        protected String doInBackground(String... params) {
            HttpClient httpClient = new DefaultHttpClient();
            String url ="http://d2dmedicine.com/aPPmob_lie/insertdata.php?caseid=97&uemail="+chemmail+"&message="+message+"&order_id="+orderid+"";

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
           /* pb.dismiss();*/
            Intent it= new Intent(OrderReplied.this,UploadMedicine.class);
            startActivity(it);
        }
    }
}
