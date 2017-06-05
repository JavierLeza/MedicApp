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

    final static public class PatientEntry implements BaseColumns{
        //Nombre de la tabla
        public static final String TABLE_NAME = "patients";

        //Columnas de la tabla
        public static final String COLUMN_DNI = "dni";
        public static final String COLUMN_PATIENT_NAME = "patientName";
        public static final String COLUMN_SEX = "sex";
        public static final String COLUMN_ADDRESS = "address";
        public static final String COLUMN_ADMISSION_DATE = "admissionDate";
        public static final String COLUMN_DISCHARGE_DATE = "dischargeDate";
        public static final String COLUMN_SOCIAL_NUMBER = "socialNumber";
        public static final String COLUMN_BIRTH_DATE = "birthDate";
        public static final String COLUMN_STATE = "state";
    }

    //Tabla que relaciona a cada paciente con sus médicos
    final static public class PatientUserEntry implements BaseColumns{
        //Nombre de la tabla
        public static final String TABLE_NAME = "patientUser";

        //Columnas de la tabla
        public static final String COLUMN_PATIENT_DNI = "dni"; //dni del paciente
        public static final String COLUMN_USER_ID = "user"; //id del médico
    }


    final static public class SymptomEntry implements BaseColumns{
        //Nombre de la tabla
        public static final String TABLE_NAME = "symptoms";

        //Columnas de la tabla
        public static final String COLUMN_SYMPTOM = "symptom";
        public static final String COLUMN_PRIORITY = "priority";
        public static final String COLUMN_STATE = "state";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_PATIENT = "patient";
        public static final String COLUMN_USER = "user";
    }

    final static public class LogEntry implements BaseColumns{
        //Nombre de la tabla
        public static final String TABLE_NAME = "log";

        //Columnas de la tabla
        public static final String COLUMN_ADMIN_ID = "adminID";
        public static final String COLUMN_USER_ID = "userID";
        public static final String COLUMN_PATIENT_DNI = "patientDNI";
        public static final String COLUMN_SYMPTOM_ID = "symptomID";
        public static final String COLUMN_TIMESTAMP = "timestamp";
        public static final String COLUMN_DESCRIPTION = "description";


    }
}
