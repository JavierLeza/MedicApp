package com.medic.medicapp;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.medic.medicapp.data.MedicContract;

import static com.medic.medicapp.MainActivity.id;
import static com.medic.medicapp.MainActivity.mDb;

public class LogInActivity extends AppCompatActivity {

    private EditText mUserNameEditText;
    private EditText mPasswordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        mUserNameEditText = (EditText) findViewById(R.id.et_user_name);
        mPasswordEditText = (EditText) findViewById(R.id.et_user_password);
    }

    //Este es el método que se llama al pulsar el botón de ACCEDER
    public void logInUser(View view) {
        if (mUserNameEditText.getText().length() == 0 || mPasswordEditText.getText().length() == 0) {
            return;
        }
        //Para los usuarios
        if(checkUser(mUserNameEditText.getText().toString(), mPasswordEditText.getText().toString())){
            Toast.makeText(getBaseContext(), R.string.login_ok, Toast.LENGTH_LONG).show();

            id = getUserId(mUserNameEditText.getText().toString());

            //El primer parámetro es el context y el segundo la siguiente actividad que se abrirá
            /*////////Intent intent = new Intent(LogInActivity.this,PatientActivity.class);

            intent.putExtra(Intent.EXTRA_TEXT, mUserNameEditText.getText().toString());

            // Se inicia la pantalla de las listas
            startActivity(intent);
            *///////////////////

            //Para los admins
        }else if(checkAdmin(mUserNameEditText.getText().toString(), mPasswordEditText.getText().toString())){
            Toast.makeText(getBaseContext(), R.string.login_ok, Toast.LENGTH_LONG).show();

            id = getAdminId(mUserNameEditText.getText().toString());
            /*///////////////////////
            //El primer parámetro es el context y el segundo la siguiente actividad que se abrirá
            Intent intent = new Intent(LogInActivity.this,UsersListActivity.class);

            intent.putExtra(Intent.EXTRA_TEXT, mUserNameEditText.getText().toString());

            // Se inicia la pantalla de los usuarios
            startActivity(intent);
            *//////////////////////

        }else{
            Toast.makeText(getBaseContext(), R.string.login_error, Toast.LENGTH_LONG).show();
        }



        mPasswordEditText.getText().clear();
        mUserNameEditText.getText().clear();
    }

    private int getUserId(String name) {
        Cursor cursor;

        cursor = mDb.query(
                MedicContract.UserEntry.TABLE_NAME,
                null,
                MedicContract.UserEntry.COLUMN_USER_NAME + " = '" + name + "'",
                null,
                null,
                null,
                null);

        cursor.moveToFirst();
        return cursor.getInt(cursor.getColumnIndex(MedicContract.UserEntry._ID));

    }

    private int getAdminId(String name) {
        Cursor cursor;

        cursor = mDb.query(
                MedicContract.AdminEntry.TABLE_NAME,
                null,
                MedicContract.AdminEntry.COLUMN_ADMIN_NAME + " = '" + name + "'",
                null,
                null,
                null,
                null);

        cursor.moveToFirst();
        return cursor.getInt(cursor.getColumnIndex(MedicContract.AdminEntry._ID));

    }

    private boolean checkAdmin(String admin, String password) {
        Cursor cursor;

        cursor = mDb.query(
                MedicContract.AdminEntry.TABLE_NAME,
                null,
                MedicContract.AdminEntry.COLUMN_ADMIN_NAME + " = '" + admin + "' AND "
                        + MedicContract.AdminEntry.COLUMN_PASSWORD + " = '" + password +"'",null,
                null,
                null,
                null);

        if(cursor.getCount() == 1){
            return true;
        }else{
            return false;
        }
    }


    public boolean checkUser(String userName, String password){
        Cursor cursor;

        cursor = mDb.query(
                MedicContract.UserEntry.TABLE_NAME,
                null,
                MedicContract.UserEntry.COLUMN_USER_NAME + " = '" + userName + "' AND "
                        + MedicContract.UserEntry.COLUMN_PASSWORD + " = '" + password +"'",
                null,
                null,
                null,
                null);

        if(cursor.getCount() == 1){
            return true;
        }else{
            return false;
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        /////////////////inflater.inflate(R.menu.menu_help, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id){
            /////////////////case R.id.action_help:
                /////////////////startActivity(new Intent(this, RegisterHelpActivity.class));
              ////  return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

