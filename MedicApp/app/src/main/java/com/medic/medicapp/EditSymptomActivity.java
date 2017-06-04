package com.medic.medicapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.medic.medicapp.data.MedicContract;

import static com.medic.medicapp.MainActivity.id;
import static com.medic.medicapp.MainActivity.mDb;

public class EditSymptomActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private int symptomID;
    private EditText description;
    private EditText mNewName;
    private String oldName;
    private int mOldPriority;
    private int mNewPriority;
    private int oldState;
    private int newState;

    private String patientDni;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_symptom);

        //Esto es para saber el id
        Intent intentThatStartedThisActivity = getIntent();
        if(intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)){
            symptomID = Integer.parseInt(intentThatStartedThisActivity.getStringExtra(Intent.EXTRA_TEXT));
        }

        Cursor cursor = getSymptom();
        cursor.moveToFirst();

        TextView mOldName = (TextView) findViewById(R.id.tv_old_symptom_name);
        description = (EditText) findViewById(R.id.tv_description);

        mNewName = (EditText) findViewById(R.id.new_symptom_name);

        oldName = cursor.getString(cursor.getColumnIndex(MedicContract.SymptomEntry.COLUMN_SYMPTOM));
        mOldName.setText(oldName);

        patientDni = cursor.getString(cursor.getColumnIndex(MedicContract.SymptomEntry.COLUMN_PATIENT));

        description.setHint(cursor.getString(cursor.getColumnIndex(MedicContract.SymptomEntry.COLUMN_DESCRIPTION)));

        mOldPriority = cursor.getInt(cursor.getColumnIndex(MedicContract.SymptomEntry.COLUMN_PRIORITY));
        switch (mOldPriority){
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

        mNewPriority = mOldPriority;

        Spinner dropdown = (Spinner) findViewById(R.id.spinner_state);
        String[] items = new String[]{getResources().getString(R.string.state_active), getResources().getString(R.string.state_inactive)};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setOnItemSelectedListener(this);
        dropdown.setAdapter(adapter);

        oldState = cursor.getInt(cursor.getColumnIndex(MedicContract.SymptomEntry.COLUMN_STATE));
        dropdown.setSelection(oldState);
        newState = oldState;

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


    public void onClickUpdate(View view) {
        ContentValues contentValues = new ContentValues();
        String whereClause = MedicContract.SymptomEntry._ID + " = " + symptomID;
        String newName = oldName;

        if(mNewName.getText().length()!=0){
            if(nameExists(mNewName.getText().toString())){
                Toast.makeText(getBaseContext(), "Ya existe otro síntoma con el mismo nombre.", Toast.LENGTH_LONG).show();
                return;
            }else{
                newName = mNewName.getText().toString();
                contentValues.put(MedicContract.SymptomEntry.COLUMN_SYMPTOM, newName);
            }
        }

        if(mNewPriority!= mOldPriority){
            contentValues.put(MedicContract.SymptomEntry.COLUMN_PRIORITY, mNewPriority);
        }
        if(newState!= oldState){
            contentValues.put(MedicContract.SymptomEntry.COLUMN_STATE, newState);
        }
        if(description.getText().length()!=0){
            contentValues.put(MedicContract.SymptomEntry.COLUMN_DESCRIPTION, description.getText().toString());
        }

        if(mDb.update(MedicContract.SymptomEntry.TABLE_NAME,contentValues, whereClause, null)>0){

            Toast.makeText(getBaseContext(), "La información se ha actualizado con éxito", Toast.LENGTH_LONG).show();
            Intent intent = getIntent();
            intent.putExtra(Intent.EXTRA_TEXT,  String.valueOf(symptomID));
            intent.putExtra("symptom", newName);

            setResult(RESULT_OK, intent);
            finish();
        }
    }

    private boolean nameExists(String name) {
        Cursor cursor;

        cursor = mDb.query(
                MedicContract.SymptomEntry.TABLE_NAME,
                null,
                MedicContract.SymptomEntry.COLUMN_SYMPTOM + " = '" + name
                        + "' AND " + MedicContract.SymptomEntry.COLUMN_USER +" = " + id + " AND "
                        + MedicContract.SymptomEntry.COLUMN_PATIENT + " = '" + patientDni + "'",
                null,
                null,
                null,
                null);

        if(cursor.getCount() > 0){
            return true;
        }

        return false;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                newState = 1; //ACTIVO
                break;
            case 1:
                newState = 0; //INACTIVO
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void onPrioritySelected(View view) {
        if (((RadioButton) findViewById(R.id.radButton1)).isChecked()) {
            mNewPriority = 1; //Prioridad alta
        } else if (((RadioButton) findViewById(R.id.radButton2)).isChecked()) {
            mNewPriority = 2; //Prioridad media
        } else if (((RadioButton) findViewById(R.id.radButton3)).isChecked()) {
            mNewPriority = 3; //Prioridad baja
        }
    }
}
