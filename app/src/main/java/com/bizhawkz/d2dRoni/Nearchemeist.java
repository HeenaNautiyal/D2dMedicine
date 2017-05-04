package com.bizhawkz.d2dRoni;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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

public class Nearchemeist extends AppCompatActivity {
    ArrayList<Chemist> actorsList;
    Toolbar toolbar;
    ActorAdapter adapter;
    Button btn_continue;
    Snackbar snackbar;
    private CoordinatorLayout coordinatorLayout;
    SessionManager1 session;
    String TAG = "MainActivity";
    String abc, mobile, fname, address, city, state, license, email, Epdate, mail, mail1;
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearchemeist);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .coordinatorLayout);
        session = new SessionManager1(getApplicationContext());
        session.checkLogin();
        initToolBar();
        Bundle b = getIntent().getExtras();
        abc = (String) b.getCharSequence("pincode");

        HashMap<String, String> user = session.getUserDetails();
        mail1 = user.get(SessionManager1.KEY_EMAIL);

        actorsList = new ArrayList<Chemist>();
        new JSONAsyncTask().execute("http://d2dmedicine.com/aPPmob_lie/insertdata.php?caseid=8&pincode=" + abc.replaceAll(" ", "") + "");
        ListView listview = (ListView) findViewById(R.id.list);

        adapter = new ActorAdapter(getApplicationContext(), R.layout.row, actorsList);

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
                    fname = actors.getName();
                    address = actors.getAddress();
                    city = actors.getCity();
                    state = actors.getState();
                    mobile = actors.getMobile();
                    license = actors.getLicense();
                    email = actors.getMail();
                    Epdate = actors.getExpieryDate();

                    Intent it = new Intent(Nearchemeist.this, Chemis.class);
                    Bundle b = new Bundle();
                    b.putString("name", fname);
                    b.putString("address", address);
                    b.putString("city", city);
                    b.putString("state", state);
                    b.putString("mobileno", mobile);
                    b.putString("licenseno", license);
                    b.putString("email2", email);
                    b.putString("expirydate", Epdate);
                    b.putString("pincode", abc);
                    it.putExtras(b);
                    startActivity(it);

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
        toolbar.setTitle("Chemist Near You");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.back2_icon);
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent It = new Intent(Nearchemeist.this, Mem.class);
                        startActivity(It);
                    }
                });
    }

    class JSONAsyncTask extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(Nearchemeist.this);
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

                        actor.setName(object.getString("store_name"));
                        actor.setAddress(object.getString("address"));
                        actor.setCity(object.getString("city"));
                        actor.detState(object.getString("state"));
                        actor.setLicense(object.getString("licenseno"));
                        actor.setExpieryDate(object.getString("expirydate"));
                        actor.setEmail(object.getString("email"));
                        actor.setMobile(object.getString("mobileno"));
                        actorsList.add(actor);
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
            adapter.notifyDataSetChanged();
            if (result == false) {
                Toast.makeText(getApplicationContext(),"No Nearest Chemsit Found under "+abc.replaceAll(" ","")+" Pin code",Toast.LENGTH_LONG).show();
            }
        }

        public void onBackPressed() {
            moveTaskToBack(true);
        }
    }

}
