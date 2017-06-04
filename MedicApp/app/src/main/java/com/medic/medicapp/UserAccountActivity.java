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

import com.medic.medicapp.data.MedicContract;

import static com.medic.medicapp.MainActivity.id;
import static com.medic.medicapp.MainActivity.mDb;

public class UserAccountActivity extends AppCompatActivity {
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab_delete_user = (FloatingActionButton) findViewById(R.id.fab_delete_user);
        fab_delete_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup();
            }
        });

        FloatingActionButton fab_edit_user = (FloatingActionButton) findViewById(R.id.fab_edit_user);
        fab_edit_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                TODO Intent intent = new Intent(UserAccountActivity.this, ChangeMyAccountInfoActivity.class);
//
//                intent.putExtra(Intent.EXTRA_TEXT, userName);
//
//                startActivityForResult(intent, 1);
            }


        });

        //Esto es para saber el nombre del usuario
        userName = getUserName();
        setTitle(userName);

        Cursor cursor = getPatientNumber();
        int patientNumber = cursor.getCount();

        cursor = getSymptomNumber();
        int symptomNumber = cursor.getCount();

        cursor = getMemberSince();
        cursor.moveToFirst();
        String memberSince = cursor.getString(cursor.getColumnIndexOrThrow(MedicContract.UserEntry.COLUMN_TIMESTAMP));
        String year = memberSince.substring(0,4);
        String month = memberSince.substring(5,7);
        String day = memberSince.substring(8);
        memberSince = day + " - " + month + " - " + year;

        TextView mListNumberTextView = (TextView) findViewById(R.id.patient_number);
        TextView mElemNumberTextView = (TextView) findViewById(R.id.symptom_number);
        TextView mMemberSince = (TextView) findViewById(R.id.member_since);

        mListNumberTextView.setText(String.valueOf(patientNumber));
        mElemNumberTextView.setText(String.valueOf(symptomNumber));
        mMemberSince.setText(memberSince);
    }

    private Cursor getMemberSince() {
        return  mDb.query(
                MedicContract.UserEntry.TABLE_NAME,
                null,
                MedicContract.UserEntry._ID + " = " + id ,
                null,
                null,
                null,
                null
        );
    }

    private Cursor getSymptomNumber() {
        return  mDb.query(
                MedicContract.SymptomEntry.TABLE_NAME,
                null,
                MedicContract.SymptomEntry.COLUMN_USER + " = " + id + " AND "
                + MedicContract.SymptomEntry.COLUMN_STATE + " = 1" , //Para mostrar solo estados activos
                null,
                null,
                null,
                null
        );
    }

    private Cursor getPatientNumber() {
        return  mDb.query(
                MedicContract.PatientUserEntry.TABLE_NAME,
                null,
                MedicContract.PatientUserEntry.COLUMN_USER_ID + " = " + id ,
                null,
                null,
                null,
                null
        );
    }

    private String getUserName() {
        Cursor cursor = mDb.query(
                MedicContract.UserEntry.TABLE_NAME,
                null,
                MedicContract.UserEntry._ID + " = " + id ,
                null,
                null,
                null,
                null
        );

        cursor.moveToFirst();

        return cursor.getString(cursor.getColumnIndex(MedicContract.UserEntry.COLUMN_USER_NAME));

    }

    
}
