package com.medic.medicapp.data;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MedicDbHelper extends SQLiteOpenHelper {

    //El nombre de la base de datos:
    static final String DATABASE_NAME = "medic.db";

    //Versión de la BD.
    static final int DATABASE_VERSION = 1;

    public MedicDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //Creamos los strings que crearán las tablas
        String SQL_CREATE_USER_TABLE = "CREATE TABLE "
                + MedicContract.UserEntry.TABLE_NAME + " ("
                + MedicContract.UserEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MedicContract.UserEntry.COLUMN_USER_NAME + " TEXT NOT NULL UNIQUE, "
                + MedicContract.UserEntry.COLUMN_PASSWORD + " TEXT NOT NULL, "
                + MedicContract.UserEntry.COLUMN_TIMESTAMP + " DATE DEFAULT (date('now','localtime'))"
                + "); ";

        String SQL_CREATE_ADMIN_TABLE = "CREATE TABLE "
                + MedicContract.AdminEntry.TABLE_NAME + " ("
                + MedicContract.AdminEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MedicContract.AdminEntry.COLUMN_ADMIN_NAME + " TEXT NOT NULL UNIQUE,"
                + MedicContract.AdminEntry.COLUMN_PASSWORD + " TEXT NOT NULL, "
                + MedicContract.AdminEntry.COLUMN_TIMESTAMP + " DATE DEFAULT (date('now','localtime')), "
                + MedicContract.AdminEntry.COLUMN_CREATED_BY + " INTEGER DEFAULT 1, "
                + " FOREIGN KEY ("
                + MedicContract.AdminEntry.COLUMN_CREATED_BY
                + ") REFERENCES "
                + MedicContract.AdminEntry.TABLE_NAME
                + " ( "
                + MedicContract.AdminEntry._ID
                + ") "
                + "); ";


        sqLiteDatabase.execSQL(SQL_CREATE_USER_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_ADMIN_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MedicContract.UserEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MedicContract.AdminEntry.TABLE_NAME);

        onCreate(sqLiteDatabase);
    }
}
