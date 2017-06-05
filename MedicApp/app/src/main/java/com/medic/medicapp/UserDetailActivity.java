package com.medic.medicapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.medic.medicapp.data.MedicContract;

import static com.medic.medicapp.MainActivity.mDb;

public class UserDetailActivity extends AppCompatActivity {

    private String userName;
    private int userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab_edit_user = (FloatingActionButton) findViewById(R.id.fab_edit_user);
        fab_edit_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               //////// Intent intent = new Intent(UserDetailActivity.this, ChangeUserNameActivity.class);

               ///// intent.putExtra(Intent.EXTRA_TEXT, userName);

              //////  startActivityForResult(intent, 1);
            }


        });
        FloatingActionButton fab_delete_user = (FloatingActionButton) findViewById(R.id.fab_delete_user);
        fab_delete_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup();
            }
        });
        //Esto es para saber el nombre del usuario
        Intent intentThatStartedThisActivity = getIntent();
        if(intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)){
            userName = intentThatStartedThisActivity.getStringExtra(Intent.EXTRA_TEXT);
        }
        setTitle(userName);

        userID = getUserID();

        Cursor cursor = getListNumber();
        int listNumber = cursor.getCount();

        cursor = getListElem();
        int elemNumber = cursor.getCount();

        cursor = getMemberSince();
        cursor.moveToFirst();
        String memberSince = cursor.getString(cursor.getColumnIndexOrThrow(MedicContract.UserEntry.COLUMN_TIMESTAMP));
        String year = memberSince.substring(0,4);
        String month = memberSince.substring(5,7);
        String day = memberSince.substring(8);
        memberSince = day + " - " + month + " - " + year;

        TextView mListNumberTextView = (TextView) findViewById(R.id.list_number);
        TextView mElemNumberTextView = (TextView) findViewById(R.id.elem_number);
        TextView mMemberSince = (TextView) findViewById(R.id.member_since);

        mListNumberTextView.setText(String.valueOf(listNumber));
        mElemNumberTextView.setText(String.valueOf(elemNumber));
        mMemberSince.setText(memberSince);
    }
    private int getUserID() {
        Cursor cursor =  mDb.query(
                MedicContract.UserEntry.TABLE_NAME,
                null,
                MedicContract.UserEntry.COLUMN_USER_NAME + "='" + userName + "'",
                null,
                null,
                null,
                null
        );
        cursor.moveToFirst();
        return cursor.getInt(cursor.getColumnIndex(MedicContract.UserEntry._ID));
    }
    public Cursor getListNumber() {
        return mDb.query(
                MedicContract.PatientEntry.TABLE_NAME,
                null,
                MedicContract.PatientEntry.COLUMN_DNI + "='" + userID + "'",
                null,
                null,
                null,
                null
        );
    }
    public Cursor getListElem() {
        return mDb.query(
                MedicContract.SymptomEntry.TABLE_NAME,
                null,
                MedicContract.SymptomEntry.COLUMN_USER + " = " + userID,
                null,
                null,
                null,
                null
        );
    }


    public Cursor getMemberSince() {
        return mDb.query(
                MedicContract.UserEntry.TABLE_NAME,
                new String[]{MedicContract.UserEntry.COLUMN_TIMESTAMP},
                MedicContract.UserEntry._ID + "=" + userID,
                null,
                null,
                null,
                null
        );
    }

    private void showPopup() {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(UserDetailActivity.this, R.style.MyDialogTheme);
        } else {
            builder = new AlertDialog.Builder(UserDetailActivity.this);
        }
        builder.setTitle(R.string.delete_account)
                .setMessage(R.string.delete_account_text)
                .setPositiveButton(R.string.accept_delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with discharge
                        deleteUser();
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
    public void deleteUser() {
        String whereClausePatients = MedicContract.PatientUserEntry.COLUMN_USER_ID + " = " + userID;
        mDb.delete(MedicContract.PatientUserEntry.TABLE_NAME, whereClausePatients ,null);

        String whereClauseSymptom = MedicContract.SymptomEntry.COLUMN_USER + " = " + userID;
        mDb.delete(MedicContract.SymptomEntry.TABLE_NAME, whereClauseSymptom ,null);

        String whereClauseUser = MedicContract.UserEntry._ID + " = " + userID;
        mDb.delete(MedicContract.UserEntry.TABLE_NAME, whereClauseUser ,null);


        Toast.makeText(getBaseContext(),"El usuario \"" + userName + "\" ha sido borrado con éxito.",  Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){


            if(data.hasExtra(Intent.EXTRA_TEXT)){
                userName = data.getStringExtra(Intent.EXTRA_TEXT);
            }

            this.setTitle(userName);

            Intent refresh = new Intent(this, UserDetailActivity.class);
            refresh.putExtra(Intent.EXTRA_TEXT, userName);
            startActivity(refresh);
            this.finish();
        }
    }
}
