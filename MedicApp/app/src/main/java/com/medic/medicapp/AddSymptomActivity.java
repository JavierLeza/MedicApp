package com.medic.medicapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.medic.medicapp.data.MedicContract;

import static android.R.attr.name;
import static com.medic.medicapp.MainActivity.id;
import static com.medic.medicapp.MainActivity.mDb;

public class AddSymptomActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private int mPriority = 0;
    private boolean mState = false;
    private String patientDni;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_symptom);

        //Drop down menu para seleccionar si un síntoma está activo o inactivo
        Spinner dropdown = (Spinner) findViewById(R.id.spinner_state);
        String[] items = new String[]{getResources().getString(R.string.state_active), getResources().getString(R.string.state_inactive)};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)) {
            patientDni = intentThatStartedThisActivity.getStringExtra(Intent.EXTRA_TEXT);
        }
    }


    //Este método se llama al pulsar el botón "AÑADIR"
    public void onClickAddSymptom(View view) {

        String symptom = ((EditText) findViewById(R.id.add_symptom)).getText().toString();

        String description = ((EditText) findViewById(R.id.add_description)).getText().toString();

        if (symptom.length() == 0 || mPriority == 0 ) {
            Toast.makeText(getBaseContext(), R.string.add_patient_error, Toast.LENGTH_LONG).show();
            return;
        }

        //Para comprobar si el paciente ya existe en la base de datos
        if(symtomExists(symptom)){
            Toast.makeText(getBaseContext(), R.string.add_symptom_error, Toast.LENGTH_LONG).show();
            finish();
        }


        ContentValues contentValues = new ContentValues();
        contentValues.put(MedicContract.SymptomEntry.COLUMN_SYMPTOM, symptom);
        contentValues.put(MedicContract.SymptomEntry.COLUMN_STATE, mState);
        contentValues.put(MedicContract.SymptomEntry.COLUMN_PRIORITY, mPriority);
        contentValues.put(MedicContract.SymptomEntry.COLUMN_PATIENT, patientDni);
        contentValues.put(MedicContract.SymptomEntry.COLUMN_USER, id);
        contentValues.put(MedicContract.SymptomEntry.COLUMN_DESCRIPTION, description);

        mDb.insert(MedicContract.SymptomEntry.TABLE_NAME, null, contentValues);


        Toast.makeText(getBaseContext(),"Síntoma \"" + symptom +  "\" añadido con éxito", Toast.LENGTH_LONG).show();



        //Se acaba la actividad y volvemos a la pantalla "mis listas"
        finish();

    }

    private boolean symtomExists(String symptom) {
        Cursor cursor;

        cursor = mDb.query(
                MedicContract.SymptomEntry.TABLE_NAME,
                null,
                MedicContract.SymptomEntry.COLUMN_SYMPTOM + " = '" + symptom
                        + "' AND " + MedicContract.SymptomEntry.COLUMN_USER +" = '" + id + "' "
                        + " AND " + MedicContract.SymptomEntry.COLUMN_PATIENT + " = '" + patientDni + "'",
                null,
                null,
                null,
                null);

        if(cursor.getCount() > 0){
            return true;
        }
        return false;
    }

    //Para seleccionar el estado del síntoma
    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

        switch (position) {
            case 0:
                mState = true;
                break;
            case 1:
                mState = false;
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        mState = true;
    }

    //Para seleccionar la gravedad/prioridad del síntoma
    public void onPrioritySelected(View view) {
        if (((RadioButton) findViewById(R.id.radButton1)).isChecked()) {
            mPriority = 1; //Prioridad alta
        } else if (((RadioButton) findViewById(R.id.radButton2)).isChecked()) {
            mPriority = 2; //Prioridad media
        } else if (((RadioButton) findViewById(R.id.radButton3)).isChecked()) {
            mPriority = 3; //Prioridad baja
        }
    }
}
