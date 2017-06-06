package com.medic.medicapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.medic.medicapp.data.MedicContract;

import static com.medic.medicapp.MainActivity.id;
import static com.medic.medicapp.MainActivity.mDb;

public class AdminAccountActivity extends AppCompatActivity {

    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_account);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab_delete_user = (FloatingActionButton) findViewById(R.id.fab_delete_user);
        fab_delete_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO

            }
        });

        FloatingActionButton fab_edit_user = (FloatingActionButton) findViewById(R.id.fab_edit_user);
        fab_edit_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO
            }


        });

        //Esto es para saber el nombre del usuario
        Intent intentThatStartedThisActivity = getIntent();
        if(intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)){
            userName = intentThatStartedThisActivity.getStringExtra(Intent.EXTRA_TEXT);
        }

        setTitle(userName);

        Cursor cursor = getActionsNumber();
        int actionsNumber = cursor.getCount();

        cursor = getMemberSince();
        cursor.moveToFirst();
        String memberSince = cursor.getString(cursor.getColumnIndexOrThrow(MedicContract.AdminEntry.COLUMN_TIMESTAMP));
        String year = memberSince.substring(0,4);
        String month = memberSince.substring(5,7);
        String day = memberSince.substring(8);
        memberSince = day + " - " + month + " - " + year;

        TextView mActionsNumberTextView = (TextView) findViewById(R.id.adm_actions_number);
        TextView mMemberSince = (TextView) findViewById(R.id.member_since);

        mActionsNumberTextView.setText(String.valueOf(actionsNumber));
        mMemberSince.setText(memberSince);
    }

    public Cursor getActionsNumber() {
        return mDb.query(
                MedicContract.LogEntry.TABLE_NAME,
                null,
                MedicContract.LogEntry._ID + " = " + id ,
                null,
                null,
                null,
                null
        );
    }

    public Cursor getMemberSince() {
        return mDb.query(
                MedicContract.AdminEntry.TABLE_NAME,
                new String[]{MedicContract.AdminEntry.COLUMN_TIMESTAMP},
                MedicContract.AdminEntry._ID + " = " + id ,
                null,
                null,
                null,
                null
        );
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){


            if(data.hasExtra(Intent.EXTRA_TEXT)){
                userName = data.getStringExtra(Intent.EXTRA_TEXT);
            }

            this.setTitle(userName);

            Intent refresh = new Intent(this,AdminAccountActivity.class);
            refresh.putExtra(Intent.EXTRA_TEXT, userName);
            startActivity(refresh);
            this.finish();
        }
    }

}
