package com.medic.medicapp;

import android.content.ContentValues;
import android.database.Cursor;

import com.medic.medicapp.data.MedicContract;

import static com.medic.medicapp.MainActivity.mDb;

public class LogEntry {

    private int adminID;
    private int userID;
    private String userName;
    private ContentValues contentValues;


    LogEntry (int adminID, String userName){
        this.adminID = adminID;
        this.userName = userName;
        contentValues = new ContentValues();
        contentValues.put(MedicContract.LogEntry.COLUMN_ADMIN_ID, adminID);
    }

    public void newAdmin(){
        String description = "El admin " + getAdminName() + " ha creado al admin " + userName;
        contentValues.put(MedicContract.LogEntry.COLUMN_DESCRIPTION, description);
        //contentValues.put(MedicContract.LogEntry.COLUMN_USER_NAME, getAdminID());
        mDb.insert(MedicContract.LogEntry.TABLE_NAME, null, contentValues);
    }

    public void deleteAdmin(){
        String description = "El admin " + getAdminName() + " ha eliminado al admin: " + userName;
        contentValues.put(MedicContract.LogEntry.COLUMN_DESCRIPTION, description);
        mDb.insert(MedicContract.LogEntry.TABLE_NAME, null, contentValues);
    }

    public void updateUser(){
        String description = "El admin " + getAdminName() + " ha cambiado el nombre del usuario: " + userName;
        contentValues.put(MedicContract.LogEntry.COLUMN_DESCRIPTION, description);
        //contentValues.put(MedicContract.LogEntry.COLUMN_USER_NAME, getUserID());
        mDb.insert(MedicContract.LogEntry.TABLE_NAME, null, contentValues);
    }

    public void deleteUser(){
        String description = "El admin " + getAdminName() + " ha eliminado al usuario: " + userName ;
        contentValues.put(MedicContract.LogEntry.COLUMN_DESCRIPTION, description);
        mDb.insert(MedicContract.LogEntry.TABLE_NAME, null, contentValues);
    }



    private String getAdminName(){
        Cursor cursor = mDb.query(
                MedicContract.AdminEntry.TABLE_NAME,
                null,
                MedicContract.AdminEntry._ID + " = " + adminID ,
                null,
                null,
                null,
                null);
        cursor.moveToFirst();
        return cursor.getString(cursor.getColumnIndex(MedicContract.AdminEntry.COLUMN_ADMIN_NAME));
    }


}
