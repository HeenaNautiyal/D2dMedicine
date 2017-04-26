package com.bizhawkz.d2dmedicine;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
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

public class Search extends AppCompatActivity {
    ArrayList<Chemist> actorsList;
    ImageView btnback;
    //ActorAdapter adapter;
    Button btn_continue;
    ListViewAdapter2 adapter;
    Snackbar snackbar;
    private CoordinatorLayout coordinatorLayout;
    SessionManager1 session;
    String TAG = "MainActivity";
    String abc,address, mail1,medid;
    TextView tv,medicine,composition,description,manufacture;;
    ImageView iv;
    EditText ed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .coordinatorLayout);
        session = new SessionManager1(getApplicationContext());

        HashMap<String, String> user = session.getUserDetails();
        mail1 = user.get(SessionManager1.KEY_EMAIL);

        actorsList = new ArrayList<Chemist>();

        ListView listview = (ListView) findViewById(R.id.list);

        ed=(EditText)findViewById(R.id.search);
        new JSONAsyncTask().execute("http://d2dmedicine.com/aPPmob_lie/insertdata.php?caseid=15&medicine="+abc.replaceAll(" ","%20")+"");
        ed.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                String text = ed.getText().toString();
                adapter.filter(text);
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

            }
        });

      /*  iv=(ImageView)findViewById(R.id.search);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abc=ed.getText().toString();
                new JSONAsyncTask().execute("http://d2dmedicine.com/aPPmob_lie/insertdata.php?caseid=15&medicine="+abc.replaceAll(" ","%20")+"");
            }
        });*/
        btnback = (ImageView) findViewById(R.id.imgback);
       /* btn_continue=(Button)findViewById(R.id.btn_continue);

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Chemist> countryList = adapter.actorList;
                for (int i = 0; i < countryList.size(); i++) {
                    Chemist actors = countryList.get(i);
                    if (actors.isSelected()) {
                        address=actors.getMedicine();
                        medid=actors.getMedicineid();
                        selectionorder();
                    }
                }
            }
        });*/
        adapter = new ListViewAdapter2(this,actorsList);

        listview.setAdapter(adapter);

        listview.setItemsCanFocus(false);
        listview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
                Chemist actors = actorsList.get(position);
                actors.setIsRowSelected(true);
                actorsList.set(position, actors);

                adapter.notifyDataSetChanged();

                if (actors.getIsRowSelected()) {
                    Log.e(TAG,"selected name: "+actors.getMedicine());
                    Log.e(TAG,"selected name: "+actors.getMenufacuter());
                    Log.e(TAG,"selected name: "+actors.getCompostion());
                    Log.e(TAG,"selected name: "+actors.getDescription());
                    selectionorder();
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

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent It = new Intent(Search.this, DataActivity.class);
                startActivity(It);
            }
        });
    }

    private void selectionorder() {
        Intent it = new Intent(Search.this,Ordernow.class);
        Bundle b = new Bundle();
        b.putString("medicine", address);

        it.putExtras(b);
        startActivity(it);
    }

    private void buildDialog(int dialogTheme, String s) {
        LayoutInflater layoutInflater = LayoutInflater.from(Search.this);
        final View promptView = layoutInflater.inflate(R.layout.input_dialog4, null);

        final android.support.v7.app.AlertDialog.Builder alertDialogBuilder =
                new android.support.v7.app.AlertDialog.Builder(Search.this);
        alertDialogBuilder.setView(promptView);
        android.support.v7.app.AlertDialog alert = alertDialogBuilder.create();

        medicine = (TextView) promptView.findViewById(R.id.textView);
        composition = (TextView) promptView.findViewById(R.id.textView3);
        description = (TextView) promptView.findViewById(R.id.textView4);
        manufacture = (TextView) promptView.findViewById(R.id.textView2);
    }

    class JSONAsyncTask extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           /* dialog = new ProgressDialog(Search.this);
            dialog.setMessage("Loading, please wait");
            dialog.setTitle("Connecting server");
            dialog.show();
            dialog.setCancelable(false);*/
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
                        actor.setId(object.getString("medicine_id"));
                        actor.setName(object.getString("medicine_name"));
                        actor.setCompostion(object.getString("medicine_composition"));
                        actor.setDescription(object.getString("medicine_description"));
                        actor.setPrice(object.getString("medicine_price"));
                        actor.setStock(object.getString("number_of_stock"));
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
         //   dialog.cancel();
            adapter.notifyDataSetChanged();
            if (result == false) {
                Toast.makeText(getApplicationContext(),"Medicine Not available.",Toast.LENGTH_LONG).show();
            }
        }
        public void onBackPressed() {
            moveTaskToBack(true);
        }
    }
}
