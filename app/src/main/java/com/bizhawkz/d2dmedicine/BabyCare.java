package com.bizhawkz.d2dmedicine;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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

public class BabyCare extends AppCompatActivity {

    ArrayList<Chemist> actorsList;
    ImageView btnback;
    ArrayList<String> list = new ArrayList<String>();
    int abcd =0;

    Toolbar toolbar;
    Button btn_continue;
    private ArrayList<Chemist> worldpopulationlist;
    EditText editsearch;
    JSONArray jsonArray;
    ListViewAdapter2 adapter2;
    private CoordinatorLayout coordinatorLayout;
    SessionManager1 session;
    String TAG = "MainActivity";
    int abc;
    String mobile;
    String fname;
    String address;
    String medid;
    String state;
    String license;
    String email;
    String mail;
    String mail1,quan;
    String chemmail;
    TextView tv,medicine,composition,description;

    public int count=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otccounter);
        initToolBar();

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .coordinatorLayout);
        session = new SessionManager1(getApplicationContext());
        session.checkLogin();
        HashMap<String, String> user = session.getUserDetails();
        mail1 = user.get(SessionManager1.KEY_EMAIL);
        chemmail = user.get(SessionManager1.KEY_CHEMEMAIL);

        actorsList = new ArrayList<Chemist>();
        worldpopulationlist = new ArrayList<Chemist>();

        editsearch = (EditText) findViewById(R.id.search);
        new JSONAsyncTask().execute("http://d2dmedicine.com/aPPmob_lie/insertdata.php?caseid=27&catid=32");

        final ListView listview = (ListView) findViewById(R.id.list);
        adapter2 = new ListViewAdapter2(this, actorsList);

        editsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                String text = editsearch.getText().toString();
                if (text.length() > 0) {
                    String text2 = text.substring(0, 1).toUpperCase();
                    text2.equalsIgnoreCase(text2);
                    adapter2.filter(text2);
                } else {
                    adapter2.clearData();
                    new JSONAsyncTask().execute("http://d2dmedicine.com/aPPmob_lie/insertdata.php?caseid=27&catid=32");
                }
            }
        });


        btn_continue = (Button) findViewById(R.id.btn_continue);

        listview.setAdapter(adapter2);
        listview.setItemsCanFocus(false);
        listview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {

                Chemist actors = actorsList.get(position);
                actors.setSelected(true);
                actorsList.set(position, actors);
                adapter2.updateAdapter(actorsList);
                adapter2.notifyDataSetChanged();

            }
        });

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JSONArray jsonArray = new JSONArray();
                ArrayList<Chemist> countryList = adapter2.worldpopulationlist;
                for (int i = 0; i < countryList.size(); i++) {
                    Chemist actors = countryList.get(i);
                    if (actors.isSelected()) {
                        address = actors.getMedicine();
                        medid = actors.getMedicineid();
                        quan=actors.getQuantity();
                        mobile = chemmail;

                        JSONObject student1 = new JSONObject();
                        try {
                            student1.put("medicine_name", address);
                            student1.put("medicine_id", medid);
                            student1.put("Chem_name", mobile);
                            student1.put("quantity",quan);
                            jsonArray.put(student1);
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    abcd=jsonArray.length();

                }

                //  mCounter.setText(abc);
                System.out.println("Length: " + abcd);

                Intent intent = new Intent(BabyCare.this, Ordernow.class);
                intent.putExtra("jsonArray", jsonArray.toString());
                intent.putExtra("number",abcd);
                startActivity(intent);
            }
        });


    }



    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("BabyCare");
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

//        mCounter.setText(String.valueOf(abcd));
    }

    class JSONAsyncTask extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(BabyCare.this);
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

                        actor.setMedicineid(object.getString("medicine_id"));
                        actor.setMedicine(object.getString("medicine_name"));
                        actor.setMenufacuter(object.getString("manufacture_by"));
                        actor.setCompostion(object.getString("composition"));
                        actor.setDescription(object.getString("description"));
                        actor.setPrice(object.getString("price"));
                        actor.setProductImage(object.getString("product_image"));
                        actor.setUserImage(object.getString("user_added_image"));
                        actor.setStock(object.getString("stock"));
                        actor.setCategory(object.getString("category_name"));
                        actor.setQuantity(object.getString("qty"));
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
            adapter2.notifyDataSetChanged();
            if (result == false) {
                Toast.makeText(getApplicationContext(), "Medicine not found", Toast.LENGTH_LONG).show();
            }
        }

        public void onBackPressed() {
            moveTaskToBack(true);
        }
    }
}
