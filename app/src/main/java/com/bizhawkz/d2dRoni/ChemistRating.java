package com.bizhawkz.d2dRoni;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.util.HashMap;

public class ChemistRating extends AppCompatActivity {
    RatingBar ratingbar1;
    Button button;
    TextView tvchemname;
    EditText edcomment;
    ProgressDialog pb;
    SessionManager1 session;
    Toolbar toolbar;
    String rating,comment,mail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chemist_rating);
        ratingbar1=(RatingBar)findViewById(R.id.ratingBar1);
        edcomment=(EditText)findViewById(R.id.ed_comment);
      //  tvchemname=(TextView)findViewById(R.id.tv_chemistmail);
        session = new SessionManager1(getApplicationContext());

        HashMap<String, String> user = session.getUserDetails();
        mail = user.get(SessionManager1.KEY_EMAIL);
        button=(Button)findViewById(R.id.btn_Submit);

       // tvchemname.setText(mail);
        initToolBar();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 rating=String.valueOf(ratingbar1.getRating());
                 comment=edcomment.getText().toString();
                if (!comment.isEmpty()){
                    comment=edcomment.getText().toString();
                    System.out.println("comment :"+comment);
                }
                new Comment().execute();
                System.out.println("comment rating:"+rating);


            }
        });
    }

    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Chemist Rating");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.back2_icon);
        toolbar.inflateMenu(R.menu.mainmenu);

        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });
    }

    private class Comment extends AsyncTask<String, String, String> {
        ProgressDialog dialog;
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(ChemistRating.this);
            dialog.setMessage("Please wait while Loading...");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... urls) {
            HttpClient httpClient = new DefaultHttpClient();
            String url = "http://d2dmedicine.com/aPPmob_lie/insertdata.php?caseid=85&uemail=lata@outsourcingservicesusa.com" +
                    "&chemail=lata@outsourcingservicesusa.com&rate="+rating+"&comment="+comment.replaceAll(" ","")+"";
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

        protected void onPostExecute(String result) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ChemistRating.this);
            TextView myMsg = new TextView(ChemistRating.this);
            myMsg.setText("Thanx for your rating.");
            myMsg.setGravity(Gravity.CENTER_HORIZONTAL);
            myMsg.setTextSize(20);
            myMsg.setTextColor(Color.BLACK);
            builder.setCustomTitle(myMsg);
            builder.setMessage("Keep connecting with us");
            builder.show();
            edcomment.setText("");
            dialog.cancel();
            Intent it= new Intent(ChemistRating.this,Mem.class);
            startActivity(it);
        }
    }
           /* pb.cancel();
            try {
                pb.dismiss();
                JSONObject jsonResult = new JSONObject(result);
                String message = jsonResult.getString("udata");
                if (message.equals("1")) {

                    String message2 = jsonResult.getString("result");
                    if (message2.equals("1")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ChemistRating.this);
                        TextView myMsg = new TextView(ChemistRating.this);
                        myMsg.setText("Congratulations!");
                        myMsg.setGravity(Gravity.CENTER_HORIZONTAL);
                        myMsg.setTextSize(20);
                        myMsg.setTextColor(Color.BLACK);
                        builder.setCustomTitle(myMsg);
                        builder.setMessage("You have logged in successfully");
                        builder.setPositiveButton("Continue",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        Intent it = new Intent(ChemistRating.this, Mem.class);
                                        startActivity(it);
                                    }
                                });
                        builder.show();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ChemistRating.this);
                        TextView myMsg = new TextView(ChemistRating.this);
                        myMsg.setText("Congratulations!");
                        myMsg.setGravity(Gravity.CENTER_HORIZONTAL);
                        myMsg.setTextSize(20);
                        myMsg.setTextColor(Color.BLACK);
                        builder.setCustomTitle(myMsg);
                        builder.setMessage("You have logged in successfully");
                        builder.setPositiveButton("Continue",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        Intent it = new Intent(ChemistRating.this, UploadMedicine.class);
                                        Bundle b = new Bundle();
                                      //  b.putString("email", email);
                                        it.putExtras(b);
                                        startActivity(it);
                                    }
                                });
                        builder.show();


                    }
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(ChemistRating.this);
                    TextView myMsg = new TextView(ChemistRating.this);
                    myMsg.setText("Warning!");
                    myMsg.setGravity(Gravity.CENTER_HORIZONTAL);
                    myMsg.setTextSize(20);
                    myMsg.setTextColor(Color.BLACK);
                    builder.setCustomTitle(myMsg);
                    builder.setMessage("Please activate your account");
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
        }*/
}

