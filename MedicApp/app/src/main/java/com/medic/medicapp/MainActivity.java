package com.medic.medicapp;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.medic.medicapp.data.MedicDbHelper;

public class MainActivity extends AppCompatActivity {

    public static SQLiteDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Creamos la BD
        MedicDbHelper dbHelper = new MedicDbHelper(this);
        mDb = dbHelper.getWritableDatabase();
    }
}
