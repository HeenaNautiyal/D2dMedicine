package com.bizhawkz.d2dmedicine;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;

public class Chemis extends AppCompatActivity {
    Button btn_continue;
    Toolbar toolbar;
    TextView number,license,state,city,name,address,email,expiery;
    String aaa,bbb,ccc,ddd,eee,fff,ggg,hhh,iii,pin,mail1,mail;
    ImageView btnback;
    private CoordinatorLayout coordinatorLayout;
    SessionManager1 session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chemis);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .coordinatorLayout);
        session = new SessionManager1(getApplicationContext());
        initToolBar();
        HashMap<String, String> user = session.getUserDetails();
        mail1 = user.get(SessionManager1.KEY_EMAIL);
        session.checkLogin();

        number=(TextView)findViewById(R.id.number);
        address=(TextView)findViewById(R.id.Address);
        email=(TextView)findViewById(R.id.email);
        name=(TextView)findViewById(R.id.name);
        license=(TextView)findViewById(R.id.license);
        expiery=(TextView)findViewById(R.id.expiry);


        btn_continue=(Button)findViewById(R.id.btn_continue);


        Bundle b = getIntent().getExtras();
        aaa=(String)b.getCharSequence("name");
        ccc=(String)b.getCharSequence("address");
        ddd=(String)b.getCharSequence("city");

        fff=(String)b. getCharSequence("mobileno");
        ggg=(String)b. getCharSequence("licenseno");
        iii=(String)b. getCharSequence("expirydate");
        pin=(String)b.getCharSequence("pincode");
        mail=(String)b.getCharSequence("email2");
        session.createChemistLogin(mail.replaceAll(" ", ""));

        name.setText(aaa);
        email.setText("Email: "+mail);
        license.setText("License Number: "+ggg);
        number.setText("Phone Number: "+fff);

        address.setText("Address: "+ccc);
        expiery.setText("License Expiery: "+iii);

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Chemis.this);
                TextView myMsg = new TextView(Chemis.this);
                myMsg.setText("Alert!");
                myMsg.setGravity(Gravity.CENTER_HORIZONTAL);
                myMsg.setTextSize(20);
                myMsg.setTextColor(Color.BLACK);
                builder.setCustomTitle(myMsg);
                builder.setMessage("Your kind approval is highly appreciated");
                builder.setPositiveButton("ACCEPT",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int which) {
                                Intent it = new Intent(Chemis.this,DataActivity.class);

                                startActivity(it);
                            }
                        });
                builder.setNegativeButton("DECLINE",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        dialog.dismiss();
                        Intent it = new Intent(Chemis.this,Nearchemeist.class);
                        Bundle b = new Bundle();
                        b.putString("pincode", pin);
                        it.putExtras(b);
                        startActivity(it);
                    }
                });
                builder.show();
            }
        });
    }

    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("View Chemist Details");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.back2_icon);
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent It = new Intent(Chemis.this, Nearchemeist.class);
                        Bundle b = new Bundle();
                        b.putString("pincode", pin);
                        It.putExtras(b);
                        startActivity(It);
                    }
                });
    }
}
