package com.bizhawkz.d2dmedicine;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ParseException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Addressadd extends AppCompatActivity {
    Button btnsubmit1;
    Toolbar toolbar;
    ImageView edt;
    Intent it;
    ProgressDialog pb;
    Integer a=0;
    RatingBar ratingbar1;
    TextView sk, Saved_Address, num, address, city1, pincode1, name, state;
    SessionManager1 session;
    String  strJson,chemmail,rating;
    Context context;
    EditText address2, city2, state2, pincode2, num1, edmail2,ed;
    String sadd, mail2, mail, name1, number1, address1, city, state1, pincode, stad, stcty, ststat, stpin, stnum,smail;
    private CoordinatorLayout coordinatorLayout;
    String TAG = "D2d";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addressadd);
        session = new SessionManager1(getApplicationContext());
        session.checkLogin();

        address2 = (EditText) findViewById(R.id.ed_address);
        city2 = (EditText) findViewById(R.id.ed_city);
        state2 = (EditText) findViewById(R.id.ed_state);
        pincode2 = (EditText) findViewById(R.id.ed_pincode);
        num1 = (EditText) findViewById(R.id.ed_numn);
        edmail2 = (EditText) findViewById(R.id.ed_mail2);
        stad = address2.getText().toString();

        ststat = state2.getText().toString();
        stpin = pincode2.getText().toString();
        stnum = num1.getText().toString();
        smail=edmail2.getText().toString();


        pb = new ProgressDialog(Addressadd.this);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .coordinatorLayout);
        it = getIntent();
        strJson = it.getStringExtra("jsonArray");
        a = it.getIntExtra("number", 0);

     //   btnsubmit=(Button)findViewById(R.id.btn_continue1);
        initToolBar();
        btnsubmit1=(Button)findViewById(R.id.btn_1);
        btnsubmit1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stcty = city2.getText().toString();
                ststat = state2.getText().toString();
                stpin = pincode2.getText().toString();
                stnum = num1.getText().toString();
                smail=edmail2.getText().toString();
                sadd=address2.getText().toString();
                if (sadd.matches("") || stcty.matches("") || ststat.matches("") ||
                        stpin.matches("") || stnum.matches("")||smail.matches("") ) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(Addressadd.this);
                    TextView myMsg = new TextView(Addressadd.this);
                    myMsg.setText("Warning!");
                    myMsg.setGravity(Gravity.CENTER_HORIZONTAL);
                    myMsg.setTextSize(20);

                    myMsg.setTextColor(Color.BLACK);
                    builder.setCustomTitle(myMsg);
                    builder.setMessage("All fields are mandatory.");
                    builder.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.cancel();
                                }
                            });
                    builder.show();
                }
                else{
                    new Registration().execute();
                }
            }
        });
       /* btnsubmit.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        stcty = city2.getText().toString();
        ststat = state2.getText().toString();
        stpin = pincode2.getText().toString();
        stnum = num1.getText().toString();
        smail=edmail2.getText().toString();

        if (stad.matches("") || stcty.matches("") || ststat.matches("") ||
                stpin.matches("") || stnum.matches("")||smail.matches("") ) {

            AlertDialog.Builder builder = new AlertDialog.Builder(Addressadd.this);
            TextView myMsg = new TextView(Addressadd.this);
            myMsg.setText("Warning!");
            myMsg.setGravity(Gravity.CENTER_HORIZONTAL);
            myMsg.setTextSize(20);

            myMsg.setTextColor(Color.BLACK);
            builder.setCustomTitle(myMsg);
            builder.setMessage("All fields are mandatory.");
            builder.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int which) {
                            dialog.cancel();
                        }
                    });
            builder.show();
        }
        else{
            new Registration().execute();
        }
    }
});*/




       /* address2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(stad.toString().trim().length()==0){
                    btnsubmit.setEnabled(false);
                } else {
                    btnsubmit.setEnabled(true);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/

        HashMap<String, String> user = session.getUserDetails();
        mail = user.get(SessionManager1.KEY_EMAIL);
        chemmail=user.get(SessionManager1.KEY_CHEMEMAIL);
        Saved_Address=(TextView)findViewById(R.id.svadd);
        Saved_Address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Logmem().execute("http://d2dmedicine.com/aPPmob_lie/insertdata.php?caseid=19&email="+mail.replaceAll(" ","")+"");
            }
        });
    }

    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Delivery Location");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.back2_icon);
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                        /*Intent it = new Intent(Addressadd.this, UploadPresc.class);
                        Bundle b = new Bundle();
                        b.putString("name", aaa);
                        it.putExtras(b);
                        startActivity(it);*/
                    }
                });
    }

    public void onBackPressed() {
        moveTaskToBack(true);
    }

    private class Logmem extends AsyncTask<String, Void, Boolean> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(Addressadd.this);
            dialog.setMessage("Loading, please wait");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(String... urls) {
            try {
                HttpGet httppost = new HttpGet(urls[0]);
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = httpclient.execute(httppost);
                int status = response.getStatusLine().getStatusCode();

                if (status == 200) {
                    HttpEntity entity = response.getEntity();
                    String data = EntityUtils.toString(entity);

                    JSONObject jsono = new JSONObject(data);
                    JSONArray jarray = jsono.getJSONArray("result");

                    for (int i = 0; i < jarray.length(); i++) {
                        JSONObject object = jarray.getJSONObject(i);
                        name1 = object.getString("first_name");
                        number1 = object.getString("mobileno");
                        address1 = object.getString("address");
                        city = object.getString("city");
                        state1 = object.getString("state");
                        pincode = object.getString("pincode");
                    }
                    return true;
                }

            } catch (ParseException e1) {
                e1.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return false;
        }

        protected void onPostExecute(Boolean result) {
            dialog.cancel();
            if (result == false) {
                Toast.makeText(getApplicationContext(), "Unable to fetch data from server", Toast.LENGTH_LONG).show();
            }
            else {
                Log.e("Saved  Address", address1);
                String add1=address1+","+city+","+state1;
                           loginUser(mail, "4", strJson, add1);
               new nofication().execute();
            }
        }
    }

    private void loginUser(String cat, String subname, String med, String address) {
        Call<ResponseBody> callback = Network.getBaseInstance().loginUser(Constants.CASE_ID, cat, subname, med, address);
        callback.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String res = response.body().string();
                    Log.e(TAG, "my response" + res);
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Addressadd.this);
                    TextView myMsg = new TextView(Addressadd.this);
                    myMsg.setText("Order Confirmed!");
                    myMsg.setGravity(Gravity.CENTER_HORIZONTAL);
                    myMsg.setTextSize(20);
                    myMsg.setTextColor(Color.BLACK);
                    builder.setCustomTitle(myMsg);
                    builder.setMessage("Your Order has been Confirmed and sent to chemist.");
                    builder.setPositiveButton("Continue",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {

                                    dialog.dismiss();
                                    session = new SessionManager1(getApplicationContext());
                                    session.checkRegister();
                                    launchMarket();
                                }
                            });
                    builder.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
    }

    private void launchMarket() {
        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
        myAppLinkToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(myAppLinkToMarket);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, " unable to find market app", Toast.LENGTH_LONG).show();
        }
    }

    private void buildDialog2(int dialogTheme, String s) {
        boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("isFirstRun", true);
        if (isFirstRun){
            // Place your dialog code here to display the dialog

         /*   getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                    .edit()
                    .putBoolean("isFirstRun", false)
                    .apply();
            final LayoutInflater layoutInflater = LayoutInflater.from(Addressadd.this);
            final View promptView = layoutInflater.inflate(R.layout.input_dialog3, null);
            ratingbar1=(RatingBar)promptView.findViewById(R.id.ratingBar1);
            final android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(Addressadd.this);
            alertDialogBuilder.setView(promptView);

            final android.support.v7.app.AlertDialog alert = alertDialogBuilder.create();


            OK1 = (Button) promptView.findViewById(R.id.btn_ok);
            OK1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert.dismiss();
                    Toast.makeText(getApplicationContext(),"Thanks for connecting with us",Toast.LENGTH_LONG).show();
                    Intent it= new Intent(Addressadd.this,DataActivity.class);
                    startActivity(it);
                }
            });


            alert.getWindow().getAttributes().windowAnimations = dialogTheme;
            alert.requestWindowFeature(Window.FEATURE_NO_TITLE);
            WindowManager.LayoutParams wmlp = alert.getWindow().getAttributes();
            wmlp.gravity = Gravity.BOTTOM;
            alert.show();*/
            rating();
        }
        else{
            Intent it= new Intent(Addressadd.this,DataActivity.class);
            startActivity(it);
        }

    }


        private void rating() {
            boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("isFirstRun", true);
            if (isFirstRun) {
                Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
                }
            }

    }


    private class Registration extends AsyncTask<String, String, String> {
        ProgressDialog dialog;

        protected void onPreExecute() {
            dialog = new ProgressDialog(Addressadd.this);
            dialog.setMessage("Loading, please wait");
            dialog.show();
            dialog.setCancelable(false);



            stad = address2.getText().toString();
            stcty = city2.getText().toString();
            ststat = state2.getText().toString();
            stpin = pincode2.getText().toString();
            stnum = num1.getText().toString();
            mail2 = edmail2.getText().toString();


        }

        @Override
        protected String doInBackground(String... strings) {
            HttpClient httpClient = new DefaultHttpClient();
            String url = "http://d2dmedicine.com/aPPmob_lie/insertdata.php?caseid=18&email=" + mail + "" +
                    "&mobileno=" + stnum + "&pincode=" + stpin + "&address=" + stad.replaceAll(" ", "%20") + "" +
                    "&city=" + stcty.replaceAll(" ", "%20") + "&state=" + ststat.replaceAll(" ", "%20") + "&newemail=" + mail2.replaceAll(" ", "");
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
                dialog.dismiss();
                JSONObject jsonResult = new JSONObject(result);
                String message = jsonResult.getString("udata");
                Log.d("Response: ", "> " + message);
                if (message.equals("1")) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(Addressadd.this);
                    TextView myMsg = new TextView(Addressadd.this);
                    myMsg.setText("Congratulations!");
                    myMsg.setGravity(Gravity.CENTER_HORIZONTAL);
                    myMsg.setTextSize(20);
                    myMsg.setTextColor(Color.BLACK);
                    builder.setCustomTitle(myMsg);
                    builder.setMessage("Your alternate Delivery address has been changed");
                    builder.setPositiveButton("Continue.",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    String add2=stad.replaceAll(" ","%20")+","+stcty.replaceAll(" ", "%20")+","+ststat.replaceAll(" ", "%20");
                                    loginUser(mail, "4", strJson,add2.replaceAll(" ","%20"));
                                    new nofication().execute();
                                }
                            });
                    builder.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class nofication extends AsyncTask<String, String, String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pb.setMessage("Please wait while Loading...");
            pb.show();
        }


        @Override
        protected String doInBackground(String... params) {
            HttpClient httpClient = new DefaultHttpClient();
            String url = "http://d2dmedicine.com/aPPmob_lie/insertdata.php?caseid=51" +
                    "&usname=" + mail + "&chname="+chemmail+"";
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

