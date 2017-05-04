package com.bizhawkz.d2dRoni;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Test2 extends AppCompatActivity {
    SessionManager1 session;
    ActorAdapter8 adapter;
    String TAG = "Test";
    Toolbar toolbar;
    ImageView btnback,btnimage;
    String abc, mobile, fname, address, city, state, license, email, Epdate, mail, mail1,image1,abc1;
    ArrayList<Chemist> actorsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);
        Bundle b= getIntent().getExtras();
        abc1=b.getString("email");


        session = new SessionManager1(getApplicationContext());
        session.checkLogin();
        HashMap<String, String> user = session.getUserDetails();
        mail = user.get(SessionManager1.KEY_EMAIL);
        actorsList = new ArrayList<Chemist>();
        initToolBar();



        //new JsonMedicine().execute("http://outsourcingservicesusa.com/d2d/insertdata.php?caseid=23&chemist_email="+mail+"&catid=12");
        actorsList = new ArrayList<Chemist>();
        //http://d2dmedicine.com/aPPmob_lie/insertdata.php?caseid=23&chemist_email="+mail+"&catid=16");
        new JSONAsyncTask().execute("http://d2dmedicine.com/aPPmob_lie/insertdata.php?caseid=23&chemist_email="+mail+"&catid=30");
        ListView listview = (ListView) findViewById(R.id.list);
        btnback = (ImageView) findViewById(R.id.imgback);



        adapter = new ActorAdapter8(getApplicationContext(), R.layout.row8, actorsList);


        listview.setAdapter(adapter);
        listview.setItemsCanFocus(false);
        listview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
                Chemist actors = actorsList.get(position);
                actors.setIsRowSelected(true);
                actorsList.set(position, actors);
                adapter.updateAdapter(actorsList);
                adapter.notifyDataSetChanged();

                if (actors.getIsRowSelected()) {

                    Log.e(TAG, "selected Chemist: " + actors.getName());

                    fname = actors.getName();
                    address = actors.getAddress();
                    city = actors.getCity();
                    state = actors.getState();
                    mobile = actors.getMobile();
                    license = actors.getLicense();
                    email = actors.getMail();
                    Epdate = actors.getExpieryDate();
                }
            }
        });
        listview.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(abc1);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.back2_icon);
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent It = new Intent(Test2.this, Summery.class);
                        startActivity(It);
                    }
                });
    }


    class JSONAsyncTask extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(Test2.this);
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

                        Chemist actor = new Chemist();
                        actor.setMedicine(object.getString("medicine_name"));
                        actor.setMenufacuter(object.getString("manufacture_by"));
                        actor.setCompostion(object.getString("composition"));
                        actor.setDescription(object.getString("description"));
                        actor.setPrice(object.getString("price"));
                        actor.setProductImage(object.getString("product_image"));
                        actor.setUserImage(object.getString("user_added_image"));
                        actor.setStock(object.getString("stock"));
                        actor.setCategory(object.getString("category_name"));
                        actorsList.add(actor);
                        image1=object.getString("user_added_image");
                    }
                    return true;
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return false;
        }

        protected void onPostExecute(Boolean result) {
            dialog.cancel();
            adapter.notifyDataSetChanged();
            if (result == false) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Test2.this);
                TextView myMsg = new TextView(Test2.this);
                myMsg.setText("Alert!");
                myMsg.setGravity(Gravity.CENTER_HORIZONTAL);
                myMsg.setTextSize(20);
                myMsg.setTextColor(Color.BLACK);
                builder.setCustomTitle(myMsg);
                builder.setMessage("No medicine has been uploaded yet");
                builder.setPositiveButton("Start uploading medicine",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                Intent it = new Intent(Test2.this, Chemistupload.class);

                                startActivity(it);
                            }
                        });
                builder.show();
            }
        }
    }
}
