package com.bizhawkz.d2dRoni;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class RegisterOption extends AppCompatActivity {
Button Chem,user;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register_option);
        Chem=(Button) findViewById(R.id.btn_chemist);
        user=(Button) findViewById(R.id.btn_user);
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(RegisterOption.this,Register.class);
                startActivity(it);
            }
        });
        Chem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(RegisterOption.this,CheRegis.class);
                startActivity(it);
            }
        });
        initToolBar();
    }

    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(" Registration Options");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.back2_icon);
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent It = new Intent(RegisterOption.this, Login.class);
                        startActivity(It);
                    }
                });
    }
}
