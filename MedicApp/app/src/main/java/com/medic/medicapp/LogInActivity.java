package com.medic.medicapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

public class LogInActivity extends AppCompatActivity {

    private EditText mUserNameEditText;
    private EditText mPasswordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        mUserNameEditText = (EditText) findViewById(R.id.et_user_name);
        mPasswordEditText = (EditText) findViewById(R.id.et_user_password);
    }

}
