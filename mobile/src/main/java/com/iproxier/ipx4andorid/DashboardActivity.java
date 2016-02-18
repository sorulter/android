package com.iproxier.ipx4andorid;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class DashboardActivity extends AppCompatActivity {

    public static final String IPX_API_TOKEN = "com.iproxier.ipx.IPX_API_TOKEN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        SharedPreferences sharedPref = DashboardActivity.this.getSharedPreferences("IPX_API_TOKEN", Context.MODE_PRIVATE);
    }

}
