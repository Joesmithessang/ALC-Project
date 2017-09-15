package com.joesmith.devlist.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.joesmith.devlist.About;
import com.joesmith.devlist.data.Developer;
import com.joesmith.devlist.data.DeveloperContracts;
import com.joesmith.devlist.data.DeveloperContracts.DeveloperSchema;
import com.joesmith.devlist.network.CheckNetworkConn;
import com.joesmith.devlist.network.LoadDevelopers;
import com.joesmith.devlist.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<Developer>>{

    ImageView mNoNetwork;
    TextView mEmpty;
    DeveloperAdapter adapter;
    View loadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize views
        loadingIndicator = findViewById(R.id.progressBar);
        ListView list = (ListView) findViewById(R.id.listDevelopers);
        mEmpty = (TextView) findViewById(R.id.empty_text);
        mNoNetwork = (ImageView) findViewById(R.id.no_network);

        //set adapter and attach it to list view
        adapter = new DeveloperAdapter(this, new ArrayList<Developer>());
        list.setAdapter(adapter);
        list.setEmptyView(mNoNetwork);
        list.setEmptyView(mEmpty);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                Developer currentDev = adapter.getItem(position);
                Bundle args = new Bundle();
                args.putString(DeveloperSchema.KEY_NAME, currentDev.getmDevName());
                args.putString(DeveloperSchema.KEY_IMAGE_URL, currentDev.getmImageUrl());
                args.putString(DeveloperSchema.KEY_URL, currentDev.getmDevUrl());
                args.putString(DeveloperSchema.KEY_REPOS_URL, currentDev.getmDevReposUrl());
                intent.putExtras(args);
                startActivity(intent);
            }
        });

        //Check for network connection using A custom class CheckNetworkConn.
        if (CheckNetworkConn.isConnected(this)){

            //initialize the loader
            getSupportLoaderManager().initLoader(DeveloperContracts.DEVELOPERS_LOADER_ID, null, this);
        }
        else{

            // if no network set loading view to inform
            loadingIndicator.setVisibility(View.GONE);
            mEmpty.setVisibility(View.GONE);
            mNoNetwork.setImageResource(R.drawable.ic_no_network);
            displaySnackBar();
        }

    }

    /**
     *  set menu in action bar
     * @param menu view to inflate
     * @return the inflated menu view
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /***
     *
     * @param item each item/action on the menu
     * @return selected item in the action bar menu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.exit:
                finish(); //exit app
                return true;
            case R.id.about: {
                Intent about = new Intent(MainActivity.this, About.class);
                startActivity(about);
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        if (!CheckNetworkConn.isConnected(this)){  //Check for network connection to
            displaySnackBar();                     // if no network display snackbar
            mEmpty.setVisibility(View.GONE);
            mNoNetwork.setVisibility(View.VISIBLE);
            mNoNetwork.setImageResource(R.drawable.ic_no_network);
        }
        super.onResume();
    }

    /**
     * function to display snackbar in app screen
     * Used during no network connection
     */
    private void displaySnackBar(){
        Snackbar snackbar = Snackbar.make(findViewById(R.id.activity_main),
                getString(R.string.no_network), Snackbar.LENGTH_SHORT);
        snackbar.setAction(R.string.setting_text, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS);
                startActivity(intent);
            }
        });
        snackbar.show();
    }

    @Override
    public Loader<List<Developer>> onCreateLoader(int id, Bundle args) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String sort_by = sharedPreferences.getString(
                getString(R.string.settings_sort_by_key),
                getString(R.string.settings_sort_by_default));

        /*return Developers from the api call
          @param DeveloperContracts.JSON_RESPONSE_URL the api url
          @param DeveloperContracts.LOAD_DEV loads developers using the JSON_RESPONSE_URL
          @param sort_by set the preference value to the
         */
        return new LoadDevelopers(MainActivity.this, DeveloperContracts.JSON_RESPONSE_URL,
                DeveloperContracts.LOAD_DEV, sort_by);
    }

    @Override
    public void onLoadFinished(Loader<List<Developer>> loader, List<Developer> data) {
        adapter.clear(); //clear adapter before populating it with data

        // set progress bar to not display
        loadingIndicator.setVisibility(View.GONE);

        // set empty listView textView visibility as GONE
        mEmpty.setVisibility(View.GONE);

        if (CheckNetworkConn.isConnected(this)) {
            // set no network image to not display
            mNoNetwork.setVisibility(View.GONE);

            //set no developer text to display since no developer was return
            mEmpty.setVisibility(View.VISIBLE);
            mEmpty.setText(R.string.no_developer);

            // Check if data is available and populate the adapter
            if (data != null && !data.isEmpty()) {
                adapter.addAll(data);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Developer>> loader) {
        // Clear adapter on loader reset
        adapter.clear();
    }

}
