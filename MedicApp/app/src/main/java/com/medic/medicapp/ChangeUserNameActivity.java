package com.medic.medicapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.medic.medicapp.data.MedicContract;

import static com.medic.medicapp.MainActivity.id;
import static com.medic.medicapp.MainActivity.mDb;

public class ChangeUserNameActivity extends AppCompatActivity {

    private TextView mOldUserNameTextView;
    private String userName;
    private EditText mNewUserEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_user_name);

        //Esto es para saber el nombre del usuario
        Intent intentThatStartedThisActivity = getIntent();
        if(intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)){
            userName = intentThatStartedThisActivity.getStringExtra(Intent.EXTRA_TEXT);
        }

        mOldUserNameTextView = (TextView) findViewById(R.id.tv_old_username);
        mOldUserNameTextView.setText(userName);

        mNewUserEditText = (EditText) findViewById(R.id.et_user_name);

    }

    public void onClickChangeUser(View view) {

        if(mNewUserEditText.getText().length()==0){
            return;
        }

        if(checkUserName(mNewUserEditText.getText().toString())){
            //Si no hay más usuarios con el mismo nombre de usuario se cambia el nombre
            ContentValues contentValues = new ContentValues();
            contentValues.put(MedicContract.UserEntry.COLUMN_USER_NAME, mNewUserEditText.getText().toString());

            String whereClause = MedicContract.UserEntry.COLUMN_USER_NAME + "= '" + userName + "'";

            if(mDb.update(MedicContract.UserEntry.TABLE_NAME, contentValues, whereClause, null)>0){
                new LogEntry(id, userName).updateUser();
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
            return false;
        }else{
            return true;
        }

    }
}