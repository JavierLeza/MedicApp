package com.medic.medicapp.data;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MedicDbHelper extends SQLiteOpenHelper {

    //El nombre de la base de datos:
    static final String DATABASE_NAME = "medic.db";

    //Versión de la BD.
    static final int DATABASE_VERSION = 3;

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


        String SQL_CREATE_FIRST_ADMIN = "INSERT INTO "
                + MedicContract.AdminEntry.TABLE_NAME
                + " ("
                + MedicContract.AdminEntry.COLUMN_ADMIN_NAME + ", "
                + MedicContract.AdminEntry.COLUMN_PASSWORD + ") "
                + " VALUES ("
                + "'" + MedicContract.AdminEntry.FIRST_ADMIN_NAME + "' ,"
                + "'" + MedicContract.AdminEntry.FIRST_ADMIN_PASSWORD + "');";

        String SQL_CREATE_PATIENTS_TABLE = "CREATE TABLE "
                + MedicContract.PatientEntry.TABLE_NAME + " ("
                + MedicContract.PatientEntry.COLUMN_DNI + " TEXT PRIMARY KEY , "
                + MedicContract.PatientEntry.COLUMN_PATIENT_NAME + " TEXT NOT NULL, "
                + MedicContract.PatientEntry.COLUMN_SEX + " TEXT NOT NULL, "
                + MedicContract.PatientEntry.COLUMN_ADMISSION_DATE + " DATE DEFAULT (date('now','localtime')), "
                + MedicContract.PatientEntry.COLUMN_DISCHARGE_DATE + " DATE , "
                + MedicContract.PatientEntry.COLUMN_ADDRESS + " TEXT NOT NULL , "
                + MedicContract.PatientEntry.COLUMN_SOCIAL_NUMBER + " INTEGER NOT NULL, "
                + MedicContract.PatientEntry.COLUMN_STATE + " BOOLEAN NOT NULL, "
                + MedicContract.PatientEntry.COLUMN_BIRTH_DATE + " DATE  "
                + "); ";

        String SQL_CREATE_PATIENTS_USERS_TABLE = "CREATE TABLE "
                + MedicContract.PatientUserEntry.TABLE_NAME + " ("
                + MedicContract.PatientUserEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MedicContract.PatientUserEntry.COLUMN_PATIENT_DNI + " TEXT NOT NULL , "
                + MedicContract.PatientUserEntry.COLUMN_USER_ID + " INTEGER NOT NULL, "
                + "UNIQUE ("
                + MedicContract.PatientUserEntry.COLUMN_PATIENT_DNI
                + ", "
                + MedicContract.PatientUserEntry.COLUMN_USER_ID
                + "),"
                + " FOREIGN KEY ("
                + MedicContract.PatientUserEntry.COLUMN_PATIENT_DNI
                + ") REFERENCES "
                + MedicContract.PatientEntry.TABLE_NAME
                + " ( "
                + MedicContract.PatientEntry.COLUMN_DNI
                + "), "
                + " FOREIGN KEY ("
                + MedicContract.PatientUserEntry.COLUMN_USER_ID
                + ") REFERENCES "
                + MedicContract.UserEntry.TABLE_NAME
                + " ( "
                + MedicContract.UserEntry._ID
                + ") "
                + "); ";

        String SQL_CREATE_SYMPTOMS_TABLE = "CREATE TABLE "
                + MedicContract.SymptomEntry.TABLE_NAME + " ("
                + MedicContract.SymptomEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MedicContract.SymptomEntry.COLUMN_SYMPTOM + " TEXT NOT NULL , "
                + MedicContract.SymptomEntry.COLUMN_STATE + " BOOLEAN NOT NULL DEFAULT TRUE, "
                + MedicContract.SymptomEntry.COLUMN_PRIORITY + " TEXT, "
                + MedicContract.SymptomEntry.COLUMN_PATIENT + " TEXT NOT NULL, " //DNI del paciente
                + MedicContract.SymptomEntry.COLUMN_USER + " INTEGER NOT NULL,"
                + MedicContract.SymptomEntry.COLUMN_DESCRIPTION + " TEXT, "
                + "UNIQUE ("
                + MedicContract.SymptomEntry.COLUMN_SYMPTOM
                + ", "
                + MedicContract.SymptomEntry.COLUMN_PATIENT
                + ", "
                + MedicContract.SymptomEntry.COLUMN_USER
                + "),"
                + " FOREIGN KEY ("
                + MedicContract.SymptomEntry.COLUMN_PATIENT
                + ") REFERENCES "
                + MedicContract.PatientEntry.TABLE_NAME
                + " ( "
                + MedicContract.PatientEntry.COLUMN_DNI
                + "), "
                + " FOREIGN KEY ("
                + MedicContract.SymptomEntry.COLUMN_USER
                + ") REFERENCES "
                + MedicContract.UserEntry.TABLE_NAME
                + " ( "
                + MedicContract.UserEntry._ID
                + ") "
                + "); ";

        String SQL_CREATE_LOG_TABLE = "CREATE TABLE "
                + MedicContract.LogEntry.TABLE_NAME + " ("
                + MedicContract.LogEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MedicContract.LogEntry.COLUMN_ADMIN_ID + " INTEGER , "
                + MedicContract.LogEntry.COLUMN_USER_ID + " INTEGER , "
                + MedicContract.LogEntry.COLUMN_PATIENT_DNI + " TEXT , "
                + MedicContract.LogEntry.COLUMN_SYMPTOM_ID + " INTEGER , "
                + MedicContract.LogEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, "
                + MedicContract.LogEntry.COLUMN_TIMESTAMP + " DATE DEFAULT (date('now','localtime')),  "
                + " FOREIGN KEY ("
                + MedicContract.LogEntry.COLUMN_USER_ID
                + ") REFERENCES "
                + MedicContract.UserEntry.TABLE_NAME
                + " ( "
                + MedicContract.UserEntry._ID
                + "), "
                + " FOREIGN KEY ("
                + MedicContract.LogEntry.COLUMN_PATIENT_DNI
                + ") REFERENCES "
                + MedicContract.PatientEntry.TABLE_NAME
                + " ( "
                + MedicContract.PatientEntry.COLUMN_DNI
                + "), "
                + " FOREIGN KEY ("
                + MedicContract.LogEntry.COLUMN_SYMPTOM_ID
                + ") REFERENCES "
                + MedicContract.SymptomEntry.TABLE_NAME
                + " ( "
                + MedicContract.SymptomEntry._ID
                + "), "
                + " FOREIGN KEY ("
                + MedicContract.LogEntry.COLUMN_ADMIN_ID
                + ") REFERENCES "
                + MedicContract.AdminEntry.TABLE_NAME
                + " ( "
                + MedicContract.AdminEntry._ID
                + ") "
                + "); ";

        sqLiteDatabase.execSQL(SQL_CREATE_USER_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_ADMIN_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_PATIENTS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_SYMPTOMS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_PATIENTS_USERS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_FIRST_ADMIN);
        sqLiteDatabase.execSQL(SQL_CREATE_LOG_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MedicContract.UserEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MedicContract.AdminEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MedicContract.PatientEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MedicContract.SymptomEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MedicContract.PatientUserEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MedicContract.LogEntry.TABLE_NAME);

        onCreate(sqLiteDatabase);
    }
}
