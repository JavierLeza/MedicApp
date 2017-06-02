package com.medic.medicapp;

import android.content.ContentValues;
import android.content.Context;
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

public class RegisterActivity extends AppCompatActivity {

    private EditText mNewUserEditText;
    private EditText mNewPasswordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mNewUserEditText = (EditText) findViewById(R.id.et_user_name);
        mNewPasswordEditText = (EditText) findViewById(R.id.et_user_password);
    }
    //Este es el método que se llama al pulsar el botón de REGISTRARSE
    public void addUser(View view) {
        if(mNewUserEditText.getText().length()==0 || mNewPasswordEditText.getText().length()==0){
            return;
        }
        if(addNewUserToDB(mNewUserEditText.getText().toString(), mNewPasswordEditText.getText().toString())){
            Toast.makeText(getBaseContext(), R.string.register_ok, Toast.LENGTH_LONG).show();

            id = getUserId(mNewUserEditText.getText().toString());
            //Si se ha registrado correctamente puede acceder a su nueva cuenta
            Context context = RegisterActivity.this;

            //Esta es la siguiente "actividad" que será llamada
            //////////////////////////Class destinationActivity = PatientActivity.class;

            // creamos un intento para iniciar RegisterActivity
            //////////////Intent intent = new Intent(context,destinationActivity);

            //intent.putExtra(Intent.EXTRA_TEXT, mNewUserEditText.getText().toString());

            // Se inicia la pantalla de las listas
            ///startActivity(intent);


        }else{
            Toast.makeText(getBaseContext(), R.string.register_error, Toast.LENGTH_LONG).show();
        }



        mNewPasswordEditText.getText().clear();
        mNewUserEditText.getText().clear();
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

    //Este método añade al usuario a la BD
    public boolean addNewUserToDB(String userName, String password){

        ContentValues contentValues = new ContentValues();
        contentValues.put(MedicContract.UserEntry.COLUMN_USER_NAME, userName);
        contentValues.put(MedicContract.UserEntry.COLUMN_PASSWORD, password);

        if(checkUserName(userName)){
            mDb.insert(MedicContract.UserEntry.TABLE_NAME, null, contentValues);
            return true;
        }else{
            return false;
        }
    }

    public boolean checkUserName(String userName){
        Cursor cursorUser;

        cursorUser = mDb.query(
                MedicContract.UserEntry.TABLE_NAME,
                null,
                MedicContract.UserEntry.COLUMN_USER_NAME + " = '" + userName + "'",
                null,
                null,
                null,
                null);

        Cursor cursorAdmin;
        cursorAdmin = mDb.query(
                MedicContract.AdminEntry.TABLE_NAME,
                null,
                MedicContract.AdminEntry.COLUMN_ADMIN_NAME + " = '" + userName + "'",
                null,
                null,
                null,
                null);

        if(cursorUser.getCount() > 0 || cursorAdmin.getCount() >0){
            return false;
        }else{
            return true;
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
               ////////////// startActivity(new Intent(this, RegisterHelpActivity.class));
               ///////////////// return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
