package com.medic.medicapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


import com.medic.medicapp.data.MedicContract;

import static com.medic.medicapp.MainActivity.id;
import static com.medic.medicapp.MainActivity.mDb;

public class AddPatientActivity extends AppCompatActivity {

    private String mSex = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient);
    }

    //Este método se llama al pulsar el botón "AÑADIR"
    public void onClickAddPatient(View view) {

        String name = ((EditText) findViewById(R.id.add_name)).getText().toString();

        String dni = ((EditText) findViewById(R.id.add_dni)).getText().toString();
        String address = ((EditText) findViewById(R.id.add_address)).getText().toString();
        String ssNumber = ((EditText) findViewById(R.id.add_ssNumber)).getText().toString();

        String birthdateDay = ((EditText) findViewById(R.id.add_day_birthdate)).getText().toString();
        String birthdateMonth = ((EditText) findViewById(R.id.add_month_birthdate)).getText().toString();
        String birthdateYear = ((EditText) findViewById(R.id.add_year_birthdate)).getText().toString();

        String birthdate;
        birthdate = birthdateYear + "-" + birthdateMonth + "-" + birthdateDay;


        String admissionDay = ((EditText) findViewById(R.id.add_day_admission)).getText().toString();
        String admissionMonth = ((EditText) findViewById(R.id.add_month_admission)).getText().toString();
        String admissionYear = ((EditText) findViewById(R.id.add_year_admission)).getText().toString();

        String admission;
        if (admissionDay.length() == 0 || admissionMonth.length() == 0 || admissionYear.length() == 0) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            Date date = new Date();
            admission = dateFormat.format(date);
        }else{
            admission = admissionYear + "-" + admissionMonth + "-" + admissionDay;
        }

        //Para comprobar que se introducen todos los campos
        if (    name.length() == 0 ||
                dni.length()== 0 ||
                address.length() == 0 ||
                ssNumber.length() == 0 ||
                birthdateDay.length() == 0 ||
                birthdateMonth.length() == 0 ||
                birthdateYear.length() == 0 ||
                mSex == null) {
            Toast.makeText(getBaseContext(), R.string.add_patient_error, Toast.LENGTH_LONG).show();
            return;
        }

        //Para comprobar si el paciente ya existe en la base de datos
        if(checkDni(dni)){
            finish();
        }


        ContentValues contentValues = new ContentValues();
        contentValues.put(MedicContract.PatientEntry.COLUMN_PATIENT_NAME, name);
        contentValues.put(MedicContract.PatientEntry.COLUMN_DNI, dni);
        contentValues.put(MedicContract.PatientEntry.COLUMN_SEX, mSex);
        contentValues.put(MedicContract.PatientEntry.COLUMN_BIRTH_DATE, birthdate);
        contentValues.put(MedicContract.PatientEntry.COLUMN_ADDRESS, address);
        contentValues.put(MedicContract.PatientEntry.COLUMN_ADMISSION_DATE, admission);
        contentValues.put(MedicContract.PatientEntry.COLUMN_SOCIAL_NUMBER, ssNumber);

         mDb.insert(MedicContract.PatientEntry.TABLE_NAME, null, contentValues);

        //Añadir la relación entre médico y paciente
        contentValues = new ContentValues();
        contentValues.put(MedicContract.PatientUserEntry.COLUMN_USER_ID, id);
        contentValues.put(MedicContract.PatientUserEntry.COLUMN_PATIENT_DNI, dni);

        mDb.insert(MedicContract.PatientUserEntry.TABLE_NAME, null, contentValues);

        Toast.makeText(getBaseContext(),"Paciente " + name +  " añadido/a con éxito", Toast.LENGTH_LONG).show();



        //Se acaba la actividad y volvemos a la pantalla anterior
        finish();
    }

    private boolean checkDni(String dni) {
        Cursor cursor;

        cursor = mDb.query(
                MedicContract.PatientEntry.TABLE_NAME,
                null,
                MedicContract.PatientEntry.COLUMN_DNI + " = '" + dni + "'",
                null,
                null,
                null,
                null);

        if(cursor.getCount() > 0){
            return true;
        }

        return false;
    }


    public void onSexSelected(View view) {
        if (((RadioButton) findViewById(R.id.radButton1)).isChecked()) {
            mSex = "F"; //Sexo fememino
        } else if (((RadioButton) findViewById(R.id.radButton2)).isChecked()) {
            mSex = "M"; //Sexo masculino
        } 
    }
}
