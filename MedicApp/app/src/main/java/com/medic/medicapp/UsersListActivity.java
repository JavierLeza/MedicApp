package com.medic.medicapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.medic.medicapp.data.MedicContract;

import static com.medic.medicapp.MainActivity.mDb;

public class UsersListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    //Lista de usuarios que hay en el sistema
    //Es lo primero que ve el administrador al acceder

    private UserListAdapter mAdapter;
    public static String userName;
    RecyclerView mRecyclerView;

    // Constantes para logging y para referirse a un loader unico
    private static final String TAG = UsersListActivity.class.getSimpleName();
    private static final int TASK_LOADER_ID = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_list);
        //Esto es para saber el nombre de admin
        Intent intentThatStartedThisActivity = getIntent();
        if(intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)) {
            userName = intentThatStartedThisActivity.getStringExtra(Intent.EXTRA_TEXT);
        }
        mRecyclerView = (RecyclerView) this.findViewById(R.id.recyclerViewUsers);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));



        Cursor cursor = getUsers();
        mAdapter = new UserListAdapter(this, cursor);
        mRecyclerView.setAdapter(mAdapter);


        getSupportLoaderManager().initLoader(TASK_LOADER_ID, null, this);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.menu_options_admins, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id){
            case R.id.action_admins:
                startActivity(new Intent(this, AdminListActivity.class).putExtra(Intent.EXTRA_TEXT, userName));
                return true;

            case  R.id.action_logs:
                startActivity(new Intent(this, LogActivity.class));
                return true;

            case  R.id.action_my_account:
                startActivity(new Intent(this, AdminAccountActivity.class).putExtra(Intent.EXTRA_TEXT, userName));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private Cursor getUsers(){
        return mDb.query(
                MedicContract.UserEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                MedicContract.UserEntry.COLUMN_TIMESTAMP
        );
    }
    //Este método se llama si esta actividad se pausa o se reinicia.
    @Override
    protected void onResume() {
        super.onResume();
        getSupportLoaderManager().restartLoader(TASK_LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, final Bundle loaderArgs) {

        return new AsyncTaskLoader<Cursor>(this) {

            Cursor mListData = null;

            // onStartLoading() is called when a loader first starts loading data
            @Override
            protected void onStartLoading() {
                if (mListData != null) {
                    // Delivers any previously loaded data immediately
                    deliverResult(mListData);
                } else {
                    // Force a new load
                    forceLoad();
                }
            }

            // loadInBackground() performs asynchronous loading of data
            @Override
            public Cursor loadInBackground() {
                // Will implement to load data

                // Query and load all task data in the background; sort by priority
                // [Hint] use a try/catch block to catch any errors in loading data

                try {
                    return getUsers();

                } catch (Exception e) {
                    Log.e(TAG, "Failed to asynchronously load data.");
                    e.printStackTrace();
                    return null;
                }
            }

            // deliverResult sends the result of the load, a Cursor, to the registered listener
            public void deliverResult(Cursor data) {
                mListData = data;
                super.deliverResult(data);
            }
        };

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Update the data that the adapter uses to create ViewHolders
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        mAdapter.swapCursor(null);
    }

    public void userDetail(View view) {
        TextView tv = (TextView) view;

        String userName = tv.getText().toString();

        Toast.makeText(getBaseContext(),"Se ha pulsado un usuario:" + userName, Toast.LENGTH_LONG).show();

        Intent intent = new Intent(UsersListActivity.this,UserDetailActivity.class);

        intent.putExtra(Intent.EXTRA_TEXT, userName);


        startActivity(intent);

    }

}
