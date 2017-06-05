package com.medic.medicapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class UsersListActivity extends AppCompatActivity {
    public static String userName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_list);
        //Esto es para saber el nombre de admin
        Intent intentThatStartedThisActivity = getIntent();
        if(intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)) {
            userName = intentThatStartedThisActivity.getStringExtra(Intent.EXTRA_TEXT);
        }
    }
}
