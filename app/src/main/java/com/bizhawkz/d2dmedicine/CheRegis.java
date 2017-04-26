package com.bizhawkz.d2dmedicine;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;

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
import java.util.Locale;

public class CheRegis extends AppCompatActivity {
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    Button submit;
    ImageView iv;
    String regId;
    ProgressDialog pb;
    SharedPreferences settings,pwd;
    String TAG="Firebase Reg Id";
    EditText fname, lname, email, password, number, pincode, licence, date,addr,city,state,storename1;
    String fnamw1, lname1, email1, password1, number1, pincode1, lcnumber, date1,addr1,city1,state1,store;
    private DatePickerDialog fromDatePickerDialog;

    String Expn =
            "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                    + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                    + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                    + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

    private SimpleDateFormat dateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_che_regis);

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {

                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

                    displayFirebaseRegId();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");

                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

                    //txtMessage.setText(message);
                }
            }
        };

        displayFirebaseRegId();

        pb = new ProgressDialog(CheRegis.this);
        dateFormatter = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

        findViewsById();

        setDateTimeField();
        submit = (Button) findViewById(R.id.btn_Submit);
        fname = (EditText) findViewById(R.id.ef_fname);
        lname = (EditText) findViewById(R.id.ef_lstname);
        email = (EditText) findViewById(R.id.ed_email);
        password = (EditText) findViewById(R.id.ed_password);
        number = (EditText) findViewById(R.id.ed_mobile);
        pincode = (EditText) findViewById(R.id.ed_pincode);
        licence = (EditText) findViewById(R.id.ed_license);
        addr=(EditText)findViewById(R.id.ed_address);
        city=(EditText)findViewById(R.id.ed_city);
        state=(EditText)findViewById(R.id.ed_state);
        iv=(ImageView)findViewById(R.id.back);
        storename1=(EditText)findViewById(R.id.ef_store);


        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(CheRegis.this,RegisterOption.class);
                startActivity(it);
            }
        });


        date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    fromDatePickerDialog.show();
                }else {                                 }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fnamw1 = fname.getText().toString();
                lname1 = lname.getText().toString();
                email1 = email.getText().toString();
                password1 = password.getText().toString();
                store=storename1.getText().toString();
                if(TextUtils.isEmpty(password1) || password1.length() < 6)
                {
                    password.requestFocus();
                    password.setError("You must have 6 characters in your password");
                    return;
                }
                number1 = number.getText().toString();
                if(TextUtils.isEmpty(number1) || number1.length() < 10)
                {   number.requestFocus();
                    number.setError("You must have 11 number in your Phone Number");
                    return;
                }
                pincode1 = pincode.getText().toString();
                lcnumber  = licence.getText().toString();
                date1 = date.getText().toString();
                addr1 = addr.getText().toString();
                city1=city.getText().toString();
                state1=state.getText().toString();

                if (fnamw1.matches("") || lname1.matches("") || email1.matches("") ||
                        password1.matches("") || number1.matches("") || pincode1.matches("")
                        || lcnumber.matches("") || city1.matches("") || state1.matches("")
                        || date1.matches("") || addr1.matches("")||store.matches(""))
                {

                    AlertDialog.Builder builder = new AlertDialog.Builder(CheRegis.this);
                    TextView myMsg = new TextView(CheRegis.this);
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
                } else
                {
                    if (email1.matches(Expn) && email1.length() > 0) {
                        new Registration().execute();

                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(CheRegis.this);
                        TextView myMsg = new TextView(CheRegis.this);
                        myMsg.setText("Warning!");
                        myMsg.setGravity(Gravity.CENTER_HORIZONTAL);
                        myMsg.setTextSize(20);

                        myMsg.setTextColor(Color.BLACK);
                        builder.setCustomTitle(myMsg);
                        builder.setMessage("Please enter a valid mail ID!");
                        builder.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.cancel();
                                    }
                                });
                        builder.show();
                    }
                }
            }                            // new Registration().execute();

        });
    }

    private void displayFirebaseRegId() {

        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        regId = pref.getString("regId", null);

        Log.e(TAG, "Firebase reg id: " + regId);

        if (!TextUtils.isEmpty(regId))
            Log.d("Firebase Reg Id: " , regId);

        else
            Log.d("Firebase Reg Id: ","Not Recieved yet");
    }


    private void setDateTimeField() {
        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                date.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }


    private void findViewsById() {
        date = (EditText) findViewById(R.id.ed_expier);
        date.setInputType(InputType.TYPE_NULL);
        date.requestFocus();
    }

    private class Registration extends AsyncTask<String, String, String> {
        protected void onPreExecute() {
            super.onPreExecute();

            pb.setMessage("Please wait while Loading...");
            pb.show();

            fnamw1 = fname.getText().toString();
            lname1 = lname.getText().toString();
            email1 = email.getText().toString();
            password1 = password.getText().toString();
            number1 = number.getText().toString();
            pincode1 = pincode.getText().toString();
            lcnumber = licence.getText().toString();
            date1 = date.getText().toString();
            addr1=addr.getText().toString();
            city1=city.getText().toString();
            state1=state.getText().toString();

        }

        @Override
        protected String doInBackground(String... strings) {
            HttpClient httpClient = new DefaultHttpClient();

            String url1="http://d2dmedicine.com/aPPmob_lie/insertdata.php?caseid=2&email="+email1.replaceAll(" ","")+"&password="+password1.replaceAll(" ","")+"";

            String SetServerString = "";
            HttpGet httpget = new HttpGet(url1);
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
                pb.dismiss();
                JSONObject jsonResult = new JSONObject(result);
                String message = jsonResult.getString("udata");
                Log.d("Response: ", "> " + message);
                if (message.equals("1")) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(CheRegis.this);
                    TextView myMsg = new TextView(CheRegis.this);
                    myMsg.setText("Warning!");
                    myMsg.setGravity(Gravity.CENTER_HORIZONTAL);
                    myMsg.setTextSize(20);
                    myMsg.setTextColor(Color.BLACK);
                    builder.setCustomTitle(myMsg);
                    builder.setMessage("Already Registered ");
                    builder.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.dismiss();
                                }
                            });
                    builder.show();
                } else {
                    new Registration2().execute();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    public void onBackPressed() {
        moveTaskToBack(true);
    }


    private class Registration2 extends AsyncTask<String, String, String>{
        protected void onPreExecute() {
            super.onPreExecute();

            pb.setMessage("Please wait while Loading...");
            pb.show();

            fnamw1 = fname.getText().toString();
            lname1 = lname.getText().toString();
            email1 = email.getText().toString();
            password1 = password.getText().toString();
            number1 = number.getText().toString();
            pincode1 = pincode.getText().toString();
            lcnumber = licence.getText().toString();
            date1 = date.getText().toString();
            addr1=addr.getText().toString();
            city1=city.getText().toString();
            state1=state.getText().toString();
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpClient httpClient = new DefaultHttpClient();
            String url="http://d2dmedicine.com/aPPmob_lie/insertdata.php?caseid=4&fname="+fnamw1.replaceAll(" ", "")+"&lname="+lname1.replaceAll(" ", "")+"" +
                    "&email="+email1.replaceAll(" ", "")+"&password="+password1.replaceAll(" ", "")+"&mobileno="+number1+"" +
                    "&address="+addr1.replaceAll(" ","")+"&city="+city1.replaceAll(" ","")+"" +
                    "&state="+state1.replaceAll(" ","")+"&pincode="+pincode1+"" +
                    "&licenseno="+lcnumber.replaceAll(" ", "")+"&expirydate="+date1.replaceAll(" ", "")+"&date=&token="+regId+"&type=0&store_name="+store.replaceAll(" ","")+"";

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
                pb.dismiss();
                JSONObject jsonResult = new JSONObject(result);
                String message = jsonResult.getString("udata");
                Log.d("Response: ", "> " + message);
                if (message.equals("1")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CheRegis.this);
                    TextView myMsg = new TextView(CheRegis.this);
                    myMsg.setText("Congratulations.........");
                    myMsg.setGravity(Gravity.CENTER_HORIZONTAL);
                    myMsg.setTextSize(20);
                    myMsg.setTextColor(Color.BLACK);
                    builder.setCustomTitle(myMsg);
                    builder.setMessage("You will receive an email shortly to activate your account.");
                    builder.setPositiveButton("Continue",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    fname.setText("");
                                    lname.setText("");
                                    email.setText("");
                                    password.setText("");
                                    number.setText("");
                                    pincode.setText("");
                                    licence.setText("");
                                    date.setText("");
                                    addr.setText("");
                                    city.setText("");
                                    state.setText("");
                                    storename1.setText("");
                                    Intent it = new Intent(CheRegis.this, Login.class);
                                    startActivity(it);
                                }
                            });
                    builder.show();
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(CheRegis.this);
                    TextView myMsg = new TextView(CheRegis.this);
                    myMsg.setText("Warning!");
                    myMsg.setGravity(Gravity.CENTER_HORIZONTAL);
                    myMsg.setTextSize(20);
                    myMsg.setTextColor(Color.BLACK);
                    builder.setCustomTitle(myMsg);
                    builder.setMessage("Already Registered ");
                    builder.setPositiveButton("OK",
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
    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }
}
