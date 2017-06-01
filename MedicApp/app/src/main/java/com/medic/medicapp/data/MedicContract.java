package com.medic.medicapp.data;


import android.provider.BaseColumns;

public class MedicContract {

    final static public class UserEntry implements BaseColumns {
        //Nombre de la tabla
        public static final String TABLE_NAME = "users";

        //Columnas de la tabla
        public static final String COLUMN_USER_NAME = "userName";
        public static final String COLUMN_PASSWORD = "password";
        public static final String COLUMN_TIMESTAMP = "timestamp";
    }

    final static public class AdminEntry implements BaseColumns{
        //Nombre de la tabla
        public static final String TABLE_NAME = "admins";

        //Columnas de la tabla
        public static final String COLUMN_ADMIN_NAME = "adminName";
        public static final String COLUMN_PASSWORD = "password";
        public static final String COLUMN_CREATED_BY = "createdBy";
        public static final String COLUMN_TIMESTAMP = "timestamp";

        public static final String FIRST_ADMIN_NAME = "admin";
        public static final String FIRST_ADMIN_PASSWORD = "admin";
    }
}
