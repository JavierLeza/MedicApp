package com.medic.medicapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

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
                //TODO

            }


        });

        FloatingActionButton fab_delete_user = (FloatingActionButton) findViewById(R.id.fab_delete);
        fab_delete_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO
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


}
