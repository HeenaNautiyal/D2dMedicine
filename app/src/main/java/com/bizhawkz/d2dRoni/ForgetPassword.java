package com.bizhawkz.d2dRoni;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
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

public class ForgetPassword extends AppCompatActivity {
    EditText etpf;
    Button btn;
    String email;
    SharedPreferences settings, pwd;
    String Expn ="^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                    + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                    + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                    + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";
    ImageView iv;
    public static final String PREFS_NAME = "LoginPrefs";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        etpf=(EditText)findViewById(R.id.email);
        btn=(Button)findViewById(R.id.btnsubmit);
       iv=(ImageView)findViewById(R.id.imgback);

        settings = getSharedPreferences(PREFS_NAME, 0);

        etpf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = etpf.getText().toString();
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* if (email.matches(Expn) && email.length() > 0) {

                }*/
                new fpass().execute();
            }
        });

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it= new Intent(ForgetPassword.this,Login.class);
                startActivity(it);
            }
        });
    }

    private class fpass extends AsyncTask<String,String,String> {
        protected void onPreExecute() {
            super.onPreExecute();
            email = etpf.getText().toString();
        }
        @Override
        protected String doInBackground(String... strings) {
            HttpClient httpClient = new DefaultHttpClient();
            String url ="http://d2dmedicine.com/aPPmob_lie/insertdata.php?caseid=6&email="+email.replaceAll(" ","")+"";
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
                JSONObject jsonResult = new JSONObject(result);
                String message = jsonResult.getString("udata");
                Log.d("Response: ", "> " + message);
                if (message.equals("1")) {
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("logged", "logged");
                    editor.putString("username", email);
                    editor.commit();
                    AlertDialog.Builder builder = new AlertDialog.Builder(ForgetPassword.this);
                    TextView myMsg = new TextView(ForgetPassword.this);
                    myMsg.setText("Thank You!");
                    myMsg.setGravity(Gravity.CENTER_HORIZONTAL);
                    myMsg.setTextSize(20);
                    myMsg.setTextColor(Color.BLACK);
                    builder.setCustomTitle(myMsg);
                    builder.setMessage("Your password has been sent to your registered id.");
                    builder.setPositiveButton("Continue",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    Intent it = new Intent(ForgetPassword.this, Login.class);
                                    startActivity(it);
                                    etpf.setText("");
                                }
                            });
                    builder.show();
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(ForgetPassword.this);
                    TextView myMsg = new TextView(ForgetPassword.this);
                    myMsg.setText("Warning!");
                    myMsg.setGravity(Gravity.CENTER_HORIZONTAL);
                    myMsg.setTextSize(20);
                    myMsg.setTextColor(Color.BLACK);
                    builder.setCustomTitle(myMsg);
                    builder.setMessage("This email id is not registered with us");
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
    public void onBackPressed() {
        moveTaskToBack(true);
    }

}
