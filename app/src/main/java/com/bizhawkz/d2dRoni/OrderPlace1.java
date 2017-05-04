package com.bizhawkz.d2dRoni;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;

public class OrderPlace1 extends AppCompatActivity {
    TextView tv, city, address, odnum, odmed, odQuan;
    String mail1, address1, city1, id, med, Quan, chemmail;
    SessionManager1 session;
    Toolbar toolbar;
    ImageView iv, back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_place1);
        tv = (TextView) findViewById(R.id.mail2);
        initToolBar();
        address = (TextView) findViewById(R.id.edaddress);
        session.checkLogin();
        odnum = (TextView) findViewById(R.id.ordernumber);
        odmed = (TextView) findViewById(R.id.medicine);
        odQuan = (TextView) findViewById(R.id.Quantity);


        Bundle b = getIntent().getExtras();
        id = b.getString("id");
        address1 = b.getString("address");
        city1 = (String) b.getCharSequence("city");
        med = b.getString("medicinename");
        Quan = b.getString("quantity");
        chemmail = b.getString("chemistmail");

        session = new SessionManager1(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        mail1 = user.get(SessionManager1.KEY_EMAIL);
        tv.setText(mail1);
        odnum.setText(id);
        odmed.setText(med);
        odQuan.setText(Quan);
        address.setText(address1);
        city.setText(city1);


    }

    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Order Detail");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.back2_icon);
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent It = new Intent(OrderPlace1.this, DataActivity.class);
                        startActivity(It);
                    }
                });
    }
}