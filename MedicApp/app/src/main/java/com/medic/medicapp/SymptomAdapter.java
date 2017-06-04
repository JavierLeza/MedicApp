package com.medic.medicapp;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.medic.medicapp.data.MedicContract;

public class SymptomAdapter extends RecyclerView.Adapter<SymptomAdapter.SymptomViewHolder> {

    // cursor para mostrar los sintomas.
    private Cursor mCursor;
    private Context mContext;

    /**
     * Context es para la actividad que se llama
     * cursor es un cursor a la BD para los sintomas que se mostrarán
     */
    public SymptomAdapter(Context context, Cursor cursor) {
        this.mContext = context;
        this.mCursor = cursor;
    }


    @Override
    public SymptomAdapter.SymptomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Coge el layout del item  que rellena el Recycler View

        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.symptom_layout, parent, false);
        return new SymptomAdapter.SymptomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SymptomAdapter.SymptomViewHolder holder, int position) {
        // Movemos mCursor a la posición del item que se mostrará
        mCursor.moveToPosition(position);

        // Actualizar el view holder con la información que se necesita mostrar
        String symptom = mCursor.getString(mCursor.getColumnIndex(MedicContract.SymptomEntry.COLUMN_SYMPTOM));
        final int id = mCursor.getInt(mCursor.getColumnIndex(MedicContract.SymptomEntry._ID));

        // Mostrar el nombre del elemento
        holder.itemView.setTag(id);
        holder.symptomTextView.setText(symptom);

    }

    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        }
        return mCursor.getCount();
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
    class SymptomViewHolder extends RecyclerView.ViewHolder {

        // Nombre del elemento
        TextView symptomTextView;


        public SymptomViewHolder(View itemView) {
            super(itemView);
            symptomTextView = (TextView) itemView.findViewById(R.id.tv_symptom);
        }



    }
}
