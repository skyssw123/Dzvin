package com.svitla.dzvin.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.RefreshCallback;
import com.svitla.dzvin.app.DzvinReceiver;
import com.svitla.dzvin.app.R;
import com.svitla.dzvin.app.fragment.AlertsListFragment;
import com.svitla.dzvin.app.model.Alert;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends ActionBarActivity implements AlertsListFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ParseAnalytics.trackAppOpened(getIntent());

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new AlertsListFragment(), "fragment_alerts")
                    .commit();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        refreshData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Get the latest values from the ParseInstallation object.
    private void refreshData() {
        ParseInstallation.getCurrentInstallation().refreshInBackground(new RefreshCallback() {

            @Override
            public void done(ParseObject object, ParseException e) {
                if (e != null) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }
            }
        });

    }

    public Alert getAlert() throws ParseException {
        JSONObject json = (JSONObject) ParseInstallation.getCurrentInstallation().get(DzvinReceiver.KEY_LAST_ALARM_JSON);
        String alarmId = null;
        try {
            alarmId = json.getString("a");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (alarmId == null) {
            return null;
        }

        Alert alert = Alert.create(Alert.class);
        alert.setObjectId(alarmId);
        alert.fetch();

        return alert;
    }

    @Override
    public void onAlertClick(Alert alert) {
        Intent intent = new Intent(this, AlertDetailsActivity.class);
        intent.putExtra(AlertDetailsActivity.EXTRAS_ITEM_ID, alert.getObjectId());
        startActivity(intent);
    }
}
