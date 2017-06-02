package com.medic.medicapp;


import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.medic.medicapp.data.MedicContract;

public class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.PatientViewHolder> {

    // cursor para mostrar los pacientes
    private Cursor mCursor;
    private Context mContext;

    /**
     * Contect es para la actividad que se llama
     * cursor es un cursor a la BD para los pacientes que se mostrarán
     */
    public PatientAdapter(Context context, Cursor cursor) {
        this.mContext = context;
        this.mCursor = cursor;
    }

    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        }
        return mCursor.getCount();
    }

    @Override
    public PatientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Coge el layout del item  que rellena el Recycler View

        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.patient_layout, parent, false);
        return new PatientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PatientViewHolder holder, int position) {
        // Movemos mCursor a la posición del item que se mostrará
        mCursor.moveToPosition(position);

        // Actualizar el view holder con la información que se necesita mostrar
        String patientName = mCursor.getString(mCursor.getColumnIndex(MedicContract.PatientEntry.COLUMN_PATIENT_NAME));
        final int id = mCursor.getInt(mCursor.getColumnIndex(MedicContract.PatientEntry.COLUMN_DNI));

        // Mostrar el nombre de la lista
        holder.itemView.setTag(id);
        holder.listNameTextView.setText(patientName);

    }


    //Esta función intercambia el cursor que se mantiene en el Adapter con otro y fuerza que se refresque la interfaz
    public Cursor swapCursor(Cursor newCursor) {

        // comprueba si este cursor es el mismo que el anterior (mCursor)
        if (mCursor == newCursor) {
            return null; // porque no ha cambiado nada
        }
        Cursor temp = mCursor;
        this.mCursor = newCursor; // nuevo cursor asignador¡

        //comprobar si el cursor es valido, y si sí, actualizarlo
        if (newCursor != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }


    /**
     * Clase interna para albergar las vistas que se necesitan para mostrar items individuales en la RecyclerView
     */
    class PatientViewHolder extends RecyclerView.ViewHolder {

        // Nombre de la lista
        TextView listNameTextView;


        public PatientViewHolder(View itemView) {
            super(itemView);
            listNameTextView = (TextView) itemView.findViewById(R.id.tv_patient_name);
        }



    }
}
