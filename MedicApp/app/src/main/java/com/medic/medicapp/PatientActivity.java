package com.medic.medicapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.medic.medicapp.data.MedicContract;

import static com.medic.medicapp.MainActivity.id;
import static com.medic.medicapp.MainActivity.mDb;


public class PatientActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private PatientAdapter mAdapter;
    public static String userName;
    RecyclerView mRecyclerView;

    // Constantes para logging y para referirse a un loader unico
    private static final String TAG = PatientActivity.class.getSimpleName();
    private static final int TASK_LOADER_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);

        //Esto es para saber el nombre de usuario
        Intent intentThatStartedThisActivity = getIntent();
        if(intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)){
            userName = intentThatStartedThisActivity.getStringExtra(Intent.EXTRA_TEXT);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_new_list);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Al hacer click podemos añadir un nuevo paciente
                Intent addPatientIntent = new Intent(PatientActivity.this, AddPatientActivity.class);
                startActivity(addPatientIntent);
            }
        });

        mRecyclerView = (RecyclerView) this.findViewById(R.id.recyclerViewPatients);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));



        Cursor cursor = getUserPatients();
        Toast.makeText(getBaseContext(), "numero de pacientes del medico: " + cursor.getCount(), Toast.LENGTH_LONG).show();

        mAdapter = new PatientAdapter(this, cursor);
        mRecyclerView.setAdapter(mAdapter);


        getSupportLoaderManager().initLoader(TASK_LOADER_ID, null, this);
    }

    private Cursor getUserPatients(){
        return mDb.query(
                MedicContract.PatientUserEntry.TABLE_NAME,
                null,
                MedicContract.PatientUserEntry.COLUMN_USER_ID+ " = '" + id + "'",
                null,
                null,
                null,
                null
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.menu_user_my_account, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int userID = id;
        int id = item.getItemId();

        switch (id){
            case R.id.action_my_account:
                Intent intent = new Intent(this, UserAccountActivity.class);
                startActivity(intent);
                return true;

        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public Loader<Cursor> onCreateLoader(int id, final Bundle loaderArgs) {

        return new AsyncTaskLoader<Cursor>(this) {

            Cursor mListData = null;

            // onStartLoading() is called when a loader first starts loading data
            @Override
            protected void onStartLoading() {
                if (mListData != null) {
                    // Delivers any previously loaded data immediately
                    deliverResult(mListData);
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

                try {
                    return getUserPatients();

                } catch (Exception e) {
                    Log.e(TAG, "Failed to asynchronously load data.");
                    e.printStackTrace();
                    return null;
                }
            }

            // deliverResult sends the result of the load, a Cursor, to the registered listener
            public void deliverResult(Cursor data) {
                mListData = data;
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

    //Este método se llama si esta actividad se pausa o se reinicia.
    @Override
    protected void onResume() {
        super.onResume();
        getSupportLoaderManager().restartLoader(TASK_LOADER_ID, null, this);
    }


    public void patientDetail(View view) {
        TextView tv = (TextView) view;

        String text = tv.getText().toString();
        String patientDni = getPatientDni(text);

        Toast.makeText(getBaseContext(),"Se ha pulsado el paciente con DNI:" + patientDni, Toast.LENGTH_LONG).show();

        Intent intent = new Intent(PatientActivity.this,PatientSymptomsActivity.class);

        intent.putExtra(Intent.EXTRA_TEXT, patientDni);

        // Se inicia la pantalla del detalle de la lista junto con sus elementos
        startActivity(intent);
    }

    private String getPatientDni(String text) {
        String[] parts = text.split("DNI: ");
        String name = parts[0]; // Nombre
        String dni = parts[1]; // Dni

        return dni;

    }
}
