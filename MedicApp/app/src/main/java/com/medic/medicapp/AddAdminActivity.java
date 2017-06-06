package com.medic.medicapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.medic.medicapp.data.MedicContract;

import static com.medic.medicapp.MainActivity.id;
import static com.medic.medicapp.MainActivity.mDb;

public class AddAdminActivity extends AppCompatActivity {

    private EditText mNewUserEditText;
    private EditText mNewPasswordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_admin);

        mNewUserEditText = (EditText) findViewById(R.id.et_user_name);
        mNewPasswordEditText = (EditText) findViewById(R.id.et_user_password);
    }

    //Este es el método que se llama al pulsar el botón de CREAR ADMIN
    public void addUser(View view) {
        if(mNewUserEditText.getText().length()==0 || mNewPasswordEditText.getText().length()==0){
            return;
        }

        if(addNewUserToDB(mNewUserEditText.getText().toString(), mNewPasswordEditText.getText().toString())){
            new LogEntry(id, mNewUserEditText.getText().toString()).newAdmin();
            Toast.makeText(getBaseContext(), R.string.register_ok, Toast.LENGTH_LONG).show();
            finish();

        }else{
            Toast.makeText(getBaseContext(), R.string.register_error, Toast.LENGTH_LONG).show();
            mNewPasswordEditText.getText().clear();
            mNewUserEditText.getText().clear();
            return;
        }
    }

    //Este método añade al usuario a la BD
    public boolean addNewUserToDB(String userName, String password){

        ContentValues contentValues = new ContentValues();
        contentValues.put(MedicContract.AdminEntry.COLUMN_ADMIN_NAME, userName);
        contentValues.put(MedicContract.AdminEntry.COLUMN_PASSWORD, password);
        contentValues.put(MedicContract.AdminEntry.COLUMN_CREATED_BY, id);

        if(checkUserName(userName)){
            mDb.insert(MedicContract.AdminEntry.TABLE_NAME, null, contentValues);
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
}
