package com.medic.medicapp;


import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.medic.medicapp.data.MedicContract;

public class LogAdapter extends RecyclerView.Adapter<LogAdapter.LogViewHolder> {

    // cursor para mostrar las listas.
    private Cursor mCursor;
    private Context mContext;

    /**
     * Contect es para la actividad que se llama
     * cursor es un cursor a la BD para las listas que se mostrarán
     */
    public LogAdapter(Context context, Cursor cursor) {
        this.mContext = context;
        this.mCursor = cursor;
    }


    @Override
    public LogAdapter.LogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Coge el layout del item  que rellena el Recycler View

        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.log_item_layout, parent, false);
        return new LogAdapter.LogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LogAdapter.LogViewHolder holder, int position) {
        // Movemos mCursor a la posición del item que se mostrará
        mCursor.moveToPosition(position);

        // Actualizar el view holder con la información que se necesita mostrar
        String description = mCursor.getString(mCursor.getColumnIndex(MedicContract.LogEntry.COLUMN_DESCRIPTION));
        String timestamp = mCursor.getString(mCursor.getColumnIndex(MedicContract.LogEntry.COLUMN_TIMESTAMP));

        String year = timestamp.substring(0,4);
        String month = timestamp.substring(5,7);
        String day = timestamp.substring(8);
        timestamp = day + " - " + month + " - " + year;

        final int id = mCursor.getInt(mCursor.getColumnIndex(MedicContract.LogEntry._ID));

        // Mostrar el nombre de la lista
        holder.itemView.setTag(id);
        holder.description.setText(description);
        holder.timestamp.setText(timestamp);

    }

    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        }
        return mCursor.getCount();
    }

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
    class LogViewHolder extends RecyclerView.ViewHolder {
        TextView description;
        TextView timestamp;

        public LogViewHolder(View itemView) {
            super(itemView);
            description = (TextView) itemView.findViewById(R.id.tv_description);
            timestamp = (TextView) itemView.findViewById(R.id.tv_timestamp);
        }
    }

}
