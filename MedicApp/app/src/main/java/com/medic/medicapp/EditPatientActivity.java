package com.medic.medicapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.medic.medicapp.data.MedicContract;

import static com.medic.medicapp.MainActivity.mDb;

public class EditPatientActivity extends AppCompatActivity {

    private String patientDni;

    private EditText mNewName;
    private EditText mNewAdress;
    private EditText mNewDni;
    private EditText mNewSSNumber;
    private String mNewSex;
    private EditText mNewAddress;
    private String mOldSex;
    private String mNewBirthdate;
    private String mNewAdmissionDate;

    private String newDni;

    private String birthdateDay;
    private String birthdateMonth;
    private String birthdateYear;
    private String admissionDay;
    private String admissionMonth;
    private String admissionYear;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_patient);

        //Esto es para saber el dni del paciente
        Intent intentThatStartedThisActivity = getIntent();
        if(intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)){
            patientDni = intentThatStartedThisActivity.getStringExtra(Intent.EXTRA_TEXT);
        }

        Cursor cursor = getPatient();
        cursor.moveToFirst();

        TextView mOldName = (TextView) findViewById(R.id.tv_old_name);
        TextView mOldDni = (TextView) findViewById(R.id.tv_old_dni);
        TextView mOldBirthdate = (TextView) findViewById(R.id.tv_old_birthdate);
        TextView mOldAddress = (TextView) findViewById(R.id.tv_old_address);
        TextView mOldSSNumber = (TextView) findViewById(R.id.tv_old_ss);
        TextView mOldAdmission = (TextView) findViewById(R.id.tv_old_admission_date);



        mNewName = (EditText) findViewById(R.id.new_name);
        mNewDni = (EditText) findViewById(R.id.new_dni);
        mNewAddress =  (EditText) findViewById(R.id.edit_address);
        mNewSSNumber =  (EditText) findViewById(R.id.edit_ssNumber);

        birthdateDay = ((EditText) findViewById(R.id.add_day_birthdate)).getText().toString();
        birthdateMonth = ((EditText) findViewById(R.id.add_month_birthdate)).getText().toString();
        birthdateYear = ((EditText) findViewById(R.id.add_year_birthdate)).getText().toString();

        admissionDay = ((EditText) findViewById(R.id.add_day_admission)).getText().toString();
        admissionMonth = ((EditText) findViewById(R.id.add_month_admission)).getText().toString();
        admissionYear = ((EditText) findViewById(R.id.add_year_admission)).getText().toString();

        mOldName.setText(cursor.getString(cursor.getColumnIndex(MedicContract.PatientEntry.COLUMN_PATIENT_NAME)));
        mOldDni.setText(cursor.getString(cursor.getColumnIndex(MedicContract.PatientEntry.COLUMN_DNI)));
        mOldAddress.setText(cursor.getString(cursor.getColumnIndex(MedicContract.PatientEntry.COLUMN_ADDRESS)));
        mOldSSNumber.setText(cursor.getString(cursor.getColumnIndex(MedicContract.PatientEntry.COLUMN_SOCIAL_NUMBER)));

        String oldBirthdate = cursor.getString(cursor.getColumnIndexOrThrow(MedicContract.PatientEntry.COLUMN_BIRTH_DATE));
        if(oldBirthdate != null){
            String year = oldBirthdate.substring(0,4);
            String month = oldBirthdate.substring(5,7);
            String day = oldBirthdate.substring(8);
            mOldBirthdate.setText(day + " - " + month + " - " + year);
        }

        String oldAdmission = cursor.getString(cursor.getColumnIndexOrThrow(MedicContract.PatientEntry.COLUMN_ADMISSION_DATE));
        if(oldAdmission != null){
            String year = oldAdmission.substring(0,4);
            String month = oldAdmission.substring(5,7);
            String day = oldAdmission.substring(8);
            mOldAdmission.setText(day + " - " + month + " - " + year);
        }else{
            Toast.makeText(getBaseContext(), "La fecha de admision es nula", Toast.LENGTH_LONG).show();
        }

        mOldSex = cursor.getString(cursor.getColumnIndex(MedicContract.PatientEntry.COLUMN_SEX));
        switch (mOldSex){
            case "F":
                ((RadioButton) findViewById(R.id.radButton1)).setChecked(true);
                break;
            case "M":
                ((RadioButton) findViewById(R.id.radButton2)).setChecked(true);
                break;
        }

        mNewSex = mOldSex;
    }


    private Cursor getPatient(){
        return mDb.query(
                MedicContract.PatientEntry.TABLE_NAME,
                null,
                MedicContract.PatientEntry.COLUMN_DNI + " = " + patientDni ,
                null,
                null,
                null,
                null);
    }

    public void onClickUpdate(View view) {
        ContentValues contentValues = new ContentValues();
        String whereClause = MedicContract.PatientEntry.COLUMN_DNI + " = " + patientDni;
        newDni = null;
        boolean dniChanged = false;

        if(mNewDni.getText().length()!=0){
            newDni = mNewDni.getText().toString();
            if(dniExists(newDni)){
                Toast.makeText(getBaseContext(), "El DNI elegido ya existe.", Toast.LENGTH_LONG).show();
                return;
            }else{
                dniChanged = true;
                contentValues.put(MedicContract.PatientEntry.COLUMN_DNI, newDni);
            }
        }

        if(mNewName.getText().length()!=0){
            contentValues.put(MedicContract.PatientEntry.COLUMN_PATIENT_NAME, mNewName.getText().toString());
        }
        if(mNewAddress.getText().length() !=0){
            contentValues.put(MedicContract.PatientEntry.COLUMN_ADDRESS, mNewAddress.getText().toString());
        }
        if(mNewSSNumber.getText().length() !=0){
            contentValues.put(MedicContract.PatientEntry.COLUMN_SOCIAL_NUMBER, mNewSSNumber.getText().toString());
        }
        if(birthdateDay.length() != 0 && birthdateMonth.length() != 0 && birthdateYear.length() != 0){
            mNewBirthdate = birthdateYear + "-" + birthdateMonth + "-" + birthdateDay;
            contentValues.put(MedicContract.PatientEntry.COLUMN_BIRTH_DATE, mNewBirthdate);
        }
        if(admissionDay.length() != 0 && admissionMonth.length() != 0 && admissionYear.length() != 0){
            mNewAdmissionDate = admissionYear + "-" + admissionMonth + "-" + admissionDay;
            contentValues.put(MedicContract.PatientEntry.COLUMN_ADMISSION_DATE, mNewAdmissionDate);
        }
        if(mNewSex!= mOldSex){
            contentValues.put(MedicContract.PatientEntry.COLUMN_SEX, mNewSex);
        }

        if(mDb.update(MedicContract.PatientEntry.TABLE_NAME,contentValues, whereClause, null)>0){
            if(dniChanged){
                updatePatientUserTable(newDni);
                patientDni = newDni;
            }

            Toast.makeText(getBaseContext(), "La información se ha actualizado con éxito", Toast.LENGTH_LONG).show();
            Intent intent = getIntent();
            intent.putExtra(Intent.EXTRA_TEXT,  patientDni);

            setResult(RESULT_OK, intent);
            finish();
        }
    }

    private void updatePatientUserTable(String newDni) {
        ContentValues contentValues = new ContentValues();
        String whereClause = MedicContract.PatientUserEntry.COLUMN_PATIENT_DNI + " = " + patientDni;
        contentValues.put(MedicContract.PatientUserEntry.COLUMN_PATIENT_DNI, newDni);
        mDb.update(MedicContract.PatientUserEntry.TABLE_NAME,contentValues, whereClause, null);
    }

    private boolean dniExists (String newDni){
        Cursor cursor;

        cursor = mDb.query(
                MedicContract.PatientEntry.TABLE_NAME,
                null,
                MedicContract.PatientEntry.COLUMN_DNI + " = '" + newDni + "'",
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
            mNewSex = "F";
        } else if (((RadioButton) findViewById(R.id.radButton2)).isChecked()) {
            mNewSex = "M";
        }
    }
}
