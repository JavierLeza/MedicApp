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
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.medic.medicapp.data.MedicContract;

import static com.medic.medicapp.MainActivity.mDb;

public class SymptomDetailsActivity extends AppCompatActivity {

    private int symptomID;
    private String symptom;
    private int mPriority;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptom_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Esto es para saber el id
        Intent intentThatStartedThisActivity = getIntent();
        if(intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)){
            symptomID = Integer.parseInt(intentThatStartedThisActivity.getStringExtra(Intent.EXTRA_TEXT));
        }
        if(intentThatStartedThisActivity.hasExtra("symptom")){
            symptom = intentThatStartedThisActivity.getStringExtra("symptom");
        }



        FloatingActionButton fab_edit = (FloatingActionButton) findViewById(R.id.fab_edit);
        fab_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SymptomDetailsActivity.this, EditSymptomActivity.class);

                intent.putExtra(Intent.EXTRA_TEXT, String.valueOf(symptomID));

                startActivityForResult(intent, 1);

            }


        });

        FloatingActionButton fab_delete_symptom = (FloatingActionButton) findViewById(R.id.fab_delete);
        fab_delete_symptom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup();
            }
        });


        Cursor cursor = getSymptom();
        cursor.moveToFirst();

        setTitle(symptom);

        TextView state = (TextView) findViewById(R.id.tv_state);
        if(cursor.getInt(cursor.getColumnIndex(MedicContract.SymptomEntry.COLUMN_STATE)) == 1){
            state.setText(R.string.state_active);
        }else{
            state.setText(R.string.state_inactive);
        }


        TextView description = (TextView) findViewById(R.id.tv_description);
        description.setText(cursor.getString(cursor.getColumnIndex(MedicContract.SymptomEntry.COLUMN_DESCRIPTION)));




        mPriority = cursor.getInt(cursor.getColumnIndex(MedicContract.SymptomEntry.COLUMN_PRIORITY));
        switch (mPriority){
            case 1:
                ((RadioButton) findViewById(R.id.radButton1)).setChecked(true);
                break;
            case 2:
                ((RadioButton) findViewById(R.id.radButton2)).setChecked(true);
                break;
            case 3:
                ((RadioButton) findViewById(R.id.radButton3)).setChecked(true);
                break;
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){


            if(data.hasExtra(Intent.EXTRA_TEXT)){
                symptomID = Integer.parseInt(data.getStringExtra(Intent.EXTRA_TEXT));
            }

            if(data.hasExtra("symptom")){
                symptom = data.getStringExtra("symptom");
            }

            this.setTitle(symptom);

            Intent refresh = new Intent(this, SymptomDetailsActivity.class);
            refresh.putExtra(Intent.EXTRA_TEXT, String.valueOf(symptomID));
            refresh.putExtra("symptom", symptom);
            startActivity(refresh);
            this.finish();
        }
    }

    private Cursor getSymptom() {
        return mDb.query(
                MedicContract.SymptomEntry.TABLE_NAME,
                null,
                MedicContract.SymptomEntry._ID + " = " + symptomID ,
                null,
                null,
                null,
                null);
    }


    private void showPopup() {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(SymptomDetailsActivity.this, R.style.MyDialogTheme);
        } else {
            builder = new AlertDialog.Builder(SymptomDetailsActivity.this);
        }
        builder.setTitle(R.string.delete_symptom)
                .setMessage(R.string.delete_symptom_text)
                .setPositiveButton(R.string.accept_delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        deleteSymptom();
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

    public void deleteSymptom() {

        String whereClause = MedicContract.SymptomEntry._ID + " = " + symptomID;

        mDb.delete(MedicContract.SymptomEntry.TABLE_NAME, whereClause ,null);


        Toast.makeText(getBaseContext(),"El síntoma se ha eliminado",  Toast.LENGTH_LONG).show();
        finish();
    }
}
