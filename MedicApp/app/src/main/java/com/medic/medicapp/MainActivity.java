package com.medic.medicapp;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.medic.medicapp.data.MedicDbHelper;

public class MainActivity extends AppCompatActivity {

    public static SQLiteDatabase mDb;
    private Button mLogInButton;
    private Button mRegisterButton;
    public static int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Creamos la BD
        MedicDbHelper dbHelper = new MedicDbHelper(this);
        mDb = dbHelper.getWritableDatabase();

        mLogInButton = (Button) findViewById(R.id.b_log_in);
        mRegisterButton = (Button) findViewById(R.id.b_register);

        //Listener del botón LOGIN
        mLogInButton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this,LogInActivity.class);

                startActivity(intent);
            }
        });

        //Listener del botón REGISTER
        mRegisterButton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                Context context = MainActivity.this;

                //Esta es la siguiente "actividad" que será llamada
                Class destinationActivity = RegisterActivity.class;

                // creamos un intento para iniciar RegisterActivity
                Intent intent = new Intent(context,destinationActivity);

                // Se inicia la pantalla del registro
                startActivity(intent);

            }
        });
    }
}
