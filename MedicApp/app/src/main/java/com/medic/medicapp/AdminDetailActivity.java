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

import static com.medic.medicapp.MainActivity.id;
import static com.medic.medicapp.MainActivity.mDb;

public class AdminDetailActivity extends AppCompatActivity {

    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        Cursor cursor = getActionsNumber();
        int actionsNumber = cursor.getCount();

        cursor = getMemberSince();
        cursor.moveToFirst();
        String memberSince = cursor.getString(cursor.getColumnIndexOrThrow(MedicContract.UserEntry.COLUMN_TIMESTAMP));
        String year = memberSince.substring(0,4);
        String month = memberSince.substring(5,7);
        String day = memberSince.substring(8);
        memberSince = day + " - " + month + " - " + year;

        TextView mActionsNumberTextView = (TextView) findViewById(R.id.action_admins);
        TextView mMemberSince = (TextView) findViewById(R.id.member_since);

        mActionsNumberTextView.setText(String.valueOf(actionsNumber));
        mMemberSince.setText(memberSince);
    }

    public Cursor getActionsNumber() {
        Cursor cursor = mDb.query(
                MedicContract.AdminEntry.TABLE_NAME,
                null,
                MedicContract.AdminEntry.COLUMN_ADMIN_NAME + "='" + userName + "'",
                null,
                null,
                null,
                null
        );

        cursor.moveToFirst();
        int adminID = cursor.getInt(cursor.getColumnIndex(MedicContract.AdminEntry._ID));

        return mDb.query(
                MedicContract.LogEntry.TABLE_NAME,
                null,
                MedicContract.LogEntry.COLUMN_ADMIN_ID + "='" + adminID + "'",
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
                MedicContract.AdminEntry.COLUMN_ADMIN_NAME + "='" + userName + "'",
                null,
                null,
                null,
                null
        );
    }

    private void showPopup() {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(AdminDetailActivity.this, R.style.MyDialogTheme);
        } else {
            builder = new AlertDialog.Builder(AdminDetailActivity.this);
        }
        builder.setTitle(R.string.delete_account)
                .setMessage(R.string.delete_account_text)
                .setPositiveButton(R.string.accept_delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with discharge
                        deleteAdmin();
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
    public void deleteAdmin() {

        String whereClause = MedicContract.AdminEntry.COLUMN_ADMIN_NAME + " = '" + userName + "'";

        mDb.delete(MedicContract.AdminEntry.TABLE_NAME, whereClause,null);
        new LogEntry(id, userName).deleteAdmin();
        Toast.makeText(getBaseContext(),"El administrador " + userName + " ha sido borrado con éxito",  Toast.LENGTH_LONG).show();
        finish();
    }
}
