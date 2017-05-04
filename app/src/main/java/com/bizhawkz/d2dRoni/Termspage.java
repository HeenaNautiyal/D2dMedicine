package com.bizhawkz.d2dRoni;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

public class Termspage extends AppCompatActivity {
    ImageView iv;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_termspage);
        initToolBar();

    }

    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Terms & Conditions");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.back2_icon);
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent It = new Intent(Termspage.this, Login.class);
                        startActivity(It);
                    }
                });
    }
}
