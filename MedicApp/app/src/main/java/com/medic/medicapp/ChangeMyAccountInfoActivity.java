package com.medic.medicapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.medic.medicapp.data.MedicContract;

import static com.medic.medicapp.MainActivity.id;
import static com.medic.medicapp.MainActivity.mDb;

public class ChangeMyAccountInfoActivity extends AppCompatActivity {

    private String userName;
    private EditText mNewUserEditText;
    private EditText mNewPassword_1;
    private EditText mNewPassword_2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_my_account_info);

        //Esto es para saber el nombre del admin
        Intent intentThatStartedThisActivity = getIntent();
        if(intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)){
            userName = intentThatStartedThisActivity.getStringExtra(Intent.EXTRA_TEXT);
        }

        TextView mOldUserNameTextView = (TextView) findViewById(R.id.tv_old_username);
        mOldUserNameTextView.setText(userName);

        mNewUserEditText = (EditText) findViewById(R.id.et_user_name);
        mNewPassword_1 = (EditText) findViewById(R.id.et_password_1);
        mNewPassword_2 = (EditText) findViewById(R.id.et_password_2);
    }

    public void onClickChangeUser(View view) {

        if(mNewUserEditText.getText().length()==0){
            return;
        }

        if(checkCategory()){ //Si checkCategory devuelve true es que es un usuario, si devuelve false, es un admin
            changeUserName();
        }else{
            changeAdminName();
        }


    }


    public void onClickChangePassword(View view) {
        if(mNewPassword_1.getText().length()==0 || mNewPassword_2.getText().length()==0){
            Toast.makeText(getBaseContext(), "Tienes que rellenar los dos campos", Toast.LENGTH_LONG).show();
            return;
        }else if(!mNewPassword_1.getText().toString().equals(mNewPassword_2.getText().toString())){
            Toast.makeText(getBaseContext(), "Los dos campos tienen que ser iguales", Toast.LENGTH_LONG).show();
            return;
        }

        if(changePassword(checkCategory())){
            Toast.makeText(getBaseContext(), "La contraseña se ha actualizado", Toast.LENGTH_LONG).show();
            finish();
        }else{
            Toast.makeText(getBaseContext(), "Error en la BD", Toast.LENGTH_LONG).show();
        }

    }

    public boolean changePassword(boolean category){ //si category es true: USUARIO, false: ADMIN

        ContentValues contentValues = new ContentValues();

        if(category){//USUARIO
            contentValues.put(MedicContract.UserEntry.COLUMN_PASSWORD, mNewPassword_1.getText().toString());
            String whereClause = MedicContract.UserEntry._ID + "= " + id;
            return mDb.update(MedicContract.UserEntry.TABLE_NAME, contentValues, whereClause, null)>0;

        }else{//ADMIN
            contentValues.put(MedicContract.AdminEntry.COLUMN_PASSWORD, mNewPassword_1.getText().toString());
            String whereClause = MedicContract.AdminEntry._ID + "= "+ id;
            return mDb.update(MedicContract.AdminEntry.TABLE_NAME, contentValues, whereClause, null)>0;
        }

    }

    //Comprueba si el userName pertenece a un usuario (devuelve true) o a un admin (devuelve false)
    private boolean checkCategory(){
        if(!checkUserName(userName)){
            return true;
        }else{
            return false;
        }

    }

    private void changeUserName(){
        if(checkUserName(mNewUserEditText.getText().toString())){
            //Si no hay más usuarios con el mismo nombre de usuario se cambia el nombre
            ContentValues contentValues = new ContentValues();
            contentValues.put(MedicContract.UserEntry.COLUMN_USER_NAME, mNewUserEditText.getText().toString());

            String whereClause = MedicContract.UserEntry._ID + "= " + id ;

            if(mDb.update(MedicContract.UserEntry.TABLE_NAME, contentValues, whereClause, null)>0){
                Toast.makeText(getBaseContext(), R.string.register_ok, Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getBaseContext(), "ERROR AL ACTUALIZAR LA BD", Toast.LENGTH_LONG).show();
            }

            Intent intent = getIntent();
            intent.putExtra(Intent.EXTRA_TEXT,  mNewUserEditText.getText().toString());

            setResult(RESULT_OK, intent);
            finish();

        }else{
            Toast.makeText(getBaseContext(), R.string.register_error, Toast.LENGTH_LONG).show();
            mNewUserEditText.getText().clear();
        }
    }

    private void changeAdminName(){
        if(checkAdminName(mNewUserEditText.getText().toString())){
            //Si no hay más usuarios con el mismo nombre de usuario se cambia el nombre
            ContentValues contentValues = new ContentValues();
            contentValues.put(MedicContract.AdminEntry.COLUMN_ADMIN_NAME, mNewUserEditText.getText().toString());

            String whereClause = MedicContract.AdminEntry._ID + "= " + id ;

            if(mDb.update(MedicContract.AdminEntry.TABLE_NAME, contentValues, whereClause, null)>0){
                Toast.makeText(getBaseContext(), R.string.register_ok, Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getBaseContext(), "ERROR AL ACTUALIZAR LA BD", Toast.LENGTH_LONG).show();
            }

            Intent intent = getIntent();
            intent.putExtra(Intent.EXTRA_TEXT,  mNewUserEditText.getText().toString());

            setResult(RESULT_OK, intent);
            finish();

        }else{
            Toast.makeText(getBaseContext(), R.string.register_error, Toast.LENGTH_LONG).show();
            mNewUserEditText.getText().clear();
        }
    }

    public boolean checkUserName(String userName){
        Cursor cursor;

        cursor = mDb.query(
                MedicContract.UserEntry.TABLE_NAME,
                null,
                MedicContract.UserEntry.COLUMN_USER_NAME + " = '" + userName + "'",
                null,
                null,
                null,
                null);

        if(cursor.getCount() > 0){
            return false; //si hay alguien con ese userName devuelve false
        }else{
            return true;
        }

    }

    public boolean checkAdminName(String userName){
        Cursor cursor;

        cursor = mDb.query(
                MedicContract.AdminEntry.TABLE_NAME,
                null,
                MedicContract.AdminEntry.COLUMN_ADMIN_NAME + " = '" + userName + "'",
                null,
                null,
                null,
                null);

        if(cursor.getCount() > 0){
            return false; //si hay alguien con ese userName devuelve false
        }else{
            return true;
        }

    }


}
