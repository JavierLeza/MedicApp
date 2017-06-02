package com.medic.medicapp;


import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.PatientViewHolder> {

    // cursor para mostrar los pacientes
    private Cursor mCursor;
    private Context mContext;

    /**
     * Contect es para la actividad que se llama
     * cursor es un cursor a la BD para los pacientes que se mostrarán
     */
    public PatientAdapter(Context context, Cursor cursor) {

    }

    @Override
    public int getItemCount() {

        return 0;
    }

    @Override
    public PatientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return null; //CAMBIAR!!!!!!!!!!!!!!!!!!
    }

    @Override
    public void onBindViewHolder(PatientViewHolder holder, int position) {


    }


    /**
     * Clase interna para albergar las vistas que se necesitan para mostrar items individuales en la RecyclerView
     */
    class PatientViewHolder extends RecyclerView.ViewHolder {

        // Nombre de la lista
        TextView listNameTextView;


        public PatientViewHolder(View itemView) {
            super(itemView);

        }



    }
}
