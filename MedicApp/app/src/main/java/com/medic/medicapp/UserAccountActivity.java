package com.medic.medicapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
                Intent intent = new Intent(UserAccountActivity.this, ChangeMyAccountInfoActivity.class);

                intent.putExtra(Intent.EXTRA_TEXT, userName);

                startActivityForResult(intent, 1);
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

    private void showPopup() {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(UserAccountActivity.this, R.style.MyDialogTheme);
        } else {
            builder = new AlertDialog.Builder(UserAccountActivity.this);
        }
        builder.setTitle(R.string.delete_account)
                .setMessage(R.string.delete_account_text)
                .setPositiveButton(R.string.accept_delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        deleteAccount();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(R.mipmap.heartbeat)
                .show();
    }

    public void deleteAccount(){

        String whereClausePatients = MedicContract.PatientUserEntry.COLUMN_USER_ID + " = " + id;
        mDb.delete(MedicContract.PatientUserEntry.TABLE_NAME, whereClausePatients ,null);

        String whereClauseSymptom = MedicContract.SymptomEntry.COLUMN_USER + " = " + id;
        mDb.delete(MedicContract.SymptomEntry.TABLE_NAME, whereClauseSymptom ,null);

        String whereClauseUser = MedicContract.UserEntry._ID + " = " + id;
        mDb.delete(MedicContract.UserEntry.TABLE_NAME, whereClauseUser ,null);


        Toast.makeText(getBaseContext(),"El usuario \"" + userName + "\" ha sido borrado con éxito.",  Toast.LENGTH_LONG).show();
        startActivity(new Intent(UserAccountActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP ));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){


            if(data.hasExtra(Intent.EXTRA_TEXT)){
                userName = data.getStringExtra(Intent.EXTRA_TEXT);
            }

            this.setTitle(userName);

            Intent refresh = new Intent(this,UserAccountActivity.class);
            refresh.putExtra(Intent.EXTRA_TEXT, userName);
            startActivity(refresh);
            this.finish();
        }
    }
}
