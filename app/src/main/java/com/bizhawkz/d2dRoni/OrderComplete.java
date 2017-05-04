package com.bizhawkz.d2dRoni;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

public class OrderComplete extends AppCompatActivity {
    ArrayList<Chemist> actorsList;
    ActorAdapter3 adapter;
    SessionManager1 session;
    String mail,byeremail,orderid,byerid;
    ImageView iv;
    Toolbar toolbar;
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_complete);
        actorsList = new ArrayList<Chemist>();

        session = new SessionManager1(getApplicationContext());
        session.checkLogin();
        HashMap<String, String> user = session.getUserDetails();
        mail = user.get(SessionManager1.KEY_EMAIL);
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       //http://d2dmedicine.com/aPPmob_lie/insertdata.php?caseid=29&email="+mail.replaceAll(" ","")+"
        new JSONAsyncTask().execute("http://d2dmedicine.com/aPPmob_lie/insertdata.php?caseid=89&email="+mail+"");
        initToolBar();

        ListView listview = (ListView)findViewById(R.id.list);
        adapter = new ActorAdapter3(getApplicationContext(), R.layout.row6, actorsList);
        listview.setAdapter(adapter);
        listview.setItemsCanFocus(false);
        listview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listview.setSelector(R.color.blue);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
                Chemist actors = actorsList.get(position);
                actors.setIsRowSelected(true);
                actorsList.set(position, actors);
                adapter.updateAdapter(actorsList);
                adapter.notifyDataSetChanged();

                if (actors.getIsRowSelected()) {
                  /*  Intent it= new Intent(OrderComplete.this,ChemistRating.class);
                    startActivity(it);*/
                   /* byerid = actors.getDate();
                    orderid = actors.getId();
                    medicine = actors.getMedicine();
                    Intent it= new Intent(OrderRecieve.this,OrderReplied.class);
                    Bundle b = new Bundle();
                    b.putString("byeremail", byerid);
                    b.putString("orderid",orderid);
                    b.putString("medicine",medicine);
                    it.putExtras(b);
                    startActivity(it);*/
                }
            }
        });
    }

    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Complete Details");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.back2_icon);
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent It = new Intent(OrderComplete.this,UploadMedicine.class);
                        startActivity(It);
                    }
                });
    }

    class JSONAsyncTask extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(OrderComplete.this);
            dialog.setMessage("Please wait while Loading...");
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
                        actor.setMedicine(object.getString("med_name"));
                        actor.setdate(object.getString("user_email"));
                        actor.setId(object.getString("id"));
                        actor.setStatusid(object.getString("order_status"));
                        actor.setAddress(object.getString("user_address"));
                        actor.setQuantity(object.getString("med_qty"));
                        actorsList.add(actor);

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
            if(result == false)
                Toast.makeText(getApplicationContext(), "No order has been recieved yet!", Toast.LENGTH_LONG).show();

        }
    }
}
