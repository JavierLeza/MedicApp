package com.medic.medicapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.medic.medicapp.data.MedicContract;

import static com.medic.medicapp.MainActivity.id;
import static com.medic.medicapp.MainActivity.mDb;

public class PatientSymptomsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{


    private String patientDni;
    private SymptomAdapter mAdapter;
    RecyclerView mRecyclerView;


    // Constantes para logging y para referirse a un loader unico
    private static final String TAG = PatientSymptomsActivity.class.getSimpleName();
    private static final int TASK_LOADER_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_symptoms);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Esto es para saber el dni del paciente
        Intent intentThatStartedThisActivity = getIntent();
        if(intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)){
            patientDni = intentThatStartedThisActivity.getStringExtra(Intent.EXTRA_TEXT);
        }

        setTitle(getPatientName(patientDni));

        FloatingActionButton fab_edit = (FloatingActionButton) findViewById(R.id.fab_edit);
        fab_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PatientSymptomsActivity.this, EditPatientActivity.class);

                intent.putExtra(Intent.EXTRA_TEXT, String.valueOf(patientDni));

                startActivityForResult(intent, 1);
            }


        });

        //Botón para dar de alta al paciente
        FloatingActionButton fab_discharge_user = (FloatingActionButton) findViewById(R.id.fab_discharge);
        fab_discharge_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup();
            }
        });

        //Añadir nuevo elemento
        FloatingActionButton fab_new_symptom = (FloatingActionButton) findViewById(R.id.fab_new_symtpm);
        fab_new_symptom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Al hacer click podemos añadir un nuevo síntoma
                Intent addSymtomIntent = new Intent(PatientSymptomsActivity.this, AddSymptomActivity.class);
                addSymtomIntent.putExtra(Intent.EXTRA_TEXT, String.valueOf(patientDni));
                startActivity(addSymtomIntent);
            }
        });

        mRecyclerView = (RecyclerView) this.findViewById(R.id.recyclerViewSymptoms);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        Cursor cursor = getSymptoms();
        mAdapter = new SymptomAdapter(this, cursor);
        mRecyclerView.setAdapter(mAdapter);


        getSupportLoaderManager().initLoader(TASK_LOADER_ID, null, this);
    }

    private Cursor getSymptoms() {
        return mDb.query(
                MedicContract.SymptomEntry.TABLE_NAME,
                null,
                MedicContract.SymptomEntry.COLUMN_USER + " = '" + id + "' AND "
                        + MedicContract.SymptomEntry.COLUMN_PATIENT + " = '" + patientDni + "' ",
                null,
                null,
                null,
                null
        );
    }

    //Este método se llama si esta actividad se pausa o se reinicia.
    @Override
    protected void onResume() {
        super.onResume();
        getSupportLoaderManager().restartLoader(TASK_LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, final Bundle loaderArgs) {

        return new AsyncTaskLoader<Cursor>(this) {

            Cursor mSymptomData = null;

            // onStartLoading() is called when a loader first starts loading data
            @Override
            protected void onStartLoading() {
                if (mSymptomData != null) {
                    // Delivers any previously loaded data immediately
                    deliverResult(mSymptomData);
                } else {
                    // Force a new load
                    forceLoad();
                }
            }

            // loadInBackground() performs asynchronous loading of data
            @Override
            public Cursor loadInBackground() {
                // Will implement to load data

                // Query and load all task data in the background; sort by priority
                // [Hint] use a try/catch block to catch any errors in loading data

                try {
                    return getSymptoms();

                } catch (Exception e) {
                    Log.e(TAG, "Failed to asynchronously load data.");
                    e.printStackTrace();
                    return null;
                }
            }

            // deliverResult sends the result of the load, a Cursor, to the registered listener
            public void deliverResult(Cursor data) {
                mSymptomData = data;
                super.deliverResult(data);
            }
        };

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Update the data that the adapter uses to create ViewHolders
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        mAdapter.swapCursor(null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){


            if(data.hasExtra(Intent.EXTRA_TEXT)){
                patientDni = data.getStringExtra(Intent.EXTRA_TEXT);
            }

            this.setTitle(getPatientName(patientDni));

            Intent refresh = new Intent(this, PatientSymptomsActivity.class);
            refresh.putExtra(Intent.EXTRA_TEXT, patientDni);
            startActivity(refresh);
            this.finish();
        }
    }

    private void showPopup() {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(PatientSymptomsActivity.this, R.style.MyDialogTheme);
        } else {
            builder = new AlertDialog.Builder(PatientSymptomsActivity.this);
        }
        builder.setTitle(R.string.discharge_patient)
                .setMessage(R.string.discharge_patient_description)
                .setPositiveButton(R.string.accept_discharge, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with discharge
                        dischargeUser();
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



    public void dischargeUser() {
        String whereClauseSymptoms =
                MedicContract.SymptomEntry.COLUMN_PATIENT + " = '" + patientDni + "' AND "
                        + MedicContract.SymptomEntry.COLUMN_USER + " = '" + id + "'";

        String whereClausePatientUser =
                MedicContract.PatientUserEntry.COLUMN_PATIENT_DNI + " = '" + patientDni + "' AND "
                        + MedicContract.PatientUserEntry.COLUMN_USER_ID + " = '" + id + "'";

        //Primero se borran los síntomas
        mDb.delete(MedicContract.SymptomEntry.TABLE_NAME, whereClauseSymptoms ,null);

        //Luego la lista
        mDb.delete(MedicContract.PatientUserEntry.TABLE_NAME, whereClausePatientUser ,null);

        Toast.makeText(getBaseContext(),"El paciente ha sido dado de alta",  Toast.LENGTH_LONG).show();
        finish();
    }

    public void symptomDetail (View view){
        TextView tv = (TextView) view;

        String symptom = tv.getText().toString();
        int symptomId = getSymtomId(symptom);

        Toast.makeText(getBaseContext(),"Se ha pulsado un síntoma:" + symptom, Toast.LENGTH_LONG).show();

        Intent intent = new Intent(PatientSymptomsActivity.this, SymptomDetailsActivity.class);

        intent.putExtra(Intent.EXTRA_TEXT, String.valueOf(symptomId));
        intent.putExtra("symptomId", symptomId);

        // Se inicia la pantalla del detalle de los elementos
        startActivity(intent);
    }

    private int getSymtomId(String symptom) {
        Cursor cursor = mDb.query(
                MedicContract.SymptomEntry.TABLE_NAME,
                null,
                MedicContract.SymptomEntry.COLUMN_SYMPTOM + " = '" + symptom + "' AND "
                        + MedicContract.SymptomEntry.COLUMN_PATIENT + " = '" + patientDni + "' AND "
                        + MedicContract.SymptomEntry.COLUMN_USER + " = " + id,
                null,
                null,
                null,
                null
        );
        cursor.moveToFirst();
        return cursor.getInt(cursor.getColumnIndex(MedicContract.SymptomEntry._ID));
    }


    private String getPatientName(String patientDni) {
        Cursor cursor = mDb.query(
                MedicContract.PatientEntry.TABLE_NAME,
                null,
                MedicContract.PatientEntry.COLUMN_DNI + " = '" + patientDni + "'",
                null,
                null,
                null,
                null
        );
        cursor.moveToFirst();
        return cursor.getString(cursor.getColumnIndex(MedicContract.PatientEntry.COLUMN_PATIENT_NAME));

    }
}
