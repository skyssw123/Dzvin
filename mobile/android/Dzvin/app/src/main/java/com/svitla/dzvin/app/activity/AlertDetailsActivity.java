package com.svitla.dzvin.app.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.svitla.dzvin.app.DzvinUtils;
import com.svitla.dzvin.app.R;
import com.svitla.dzvin.app.model.Alert;
import com.svitla.dzvin.app.model.Point;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class AlertDetailsActivity extends ActionBarActivity {
    private static final String TAG = "AlertDetailsActivity";

    public static final String EXTRAS_ITEM_ID = "com.svitla.dzvin.app.EXTRAS_ITEM_ID";

    private String mItemId;
    private GoogleMap mMap;
    private Alert mAlert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_details);

        SharedPreferences preferences = getSharedPreferences("default", 0);
        if (preferences.contains("newAlertId")) {
            mItemId = preferences.getString("newAlertId", "");

            preferences.edit().remove("newAlertId")
                    .remove("newAlertText").commit();
        } else {
            Bundle extras = getIntent().getExtras();
            if (extras != null && extras.containsKey(EXTRAS_ITEM_ID)) {
                mItemId = (String) extras.get(EXTRAS_ITEM_ID);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        mAlert = ParseObject.createWithoutData(Alert.class, mItemId);
        mAlert.fetchIfNeededInBackground(new GetCallback<Alert>() {
            @Override
            public void done(Alert object, ParseException e) {
                if (e != null) {
                    Toast.makeText(AlertDetailsActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }

                TextView tv_title = (TextView) AlertDetailsActivity.this.findViewById(R.id.tv_title);
                String title = object.getFullText();
                if (title == null || title.length() == 0) {
                    title = object.getShortText();
                }
                tv_title.setText(title);

                String periodString = DzvinUtils.getFormattedDateInterval(object);
                TextView tv_date = (TextView) AlertDetailsActivity.this.findViewById(R.id.tv_date);
                tv_date.setText(periodString);

                findViewById(R.id.progress_bar).setVisibility(View.GONE);
                findViewById(R.id.content).setVisibility(View.VISIBLE);

                if (!object.isSimple()) {
                    setUpMapIfNeeded(mAlert);
                } else {
                    findViewById(R.id.map_container).setVisibility(View.GONE);
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.alert_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpMapIfNeeded(Alert alert) {
        if (mMap == null) {
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            if (mMap != null) {
                setUpMap(alert);
            }
        }
    }

    private void setUpMap(Alert alert) {
        alert.fetchIfNeededInBackground(new GetCallback<Alert>() {
            @Override
            public void done(Alert parseObject, ParseException e) {
                List<Point> geoPoints = null;
                try {
                    geoPoints = parseObject.getPoints();
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }

                if (geoPoints == null || geoPoints.size() == 0) {
                    return;
                }

                List<MarkerOptions> markers = new ArrayList<MarkerOptions>();
                for (Point point : geoPoints) {
                    MarkerOptions marker = new MarkerOptions().position(new LatLng(point.getLat(),
                            point.getLon())).title(point.getTitle());
                    mMap.addMarker(marker);
                    markers.add(marker);
                }

                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                for (MarkerOptions marker : markers) {
                    builder.include(marker.getPosition());
                }
                LatLngBounds bounds = builder.build();

                int padding = 50;
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                mMap.moveCamera(cu);
            }
        });
    }

}
