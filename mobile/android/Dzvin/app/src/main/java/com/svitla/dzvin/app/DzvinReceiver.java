package com.svitla.dzvin.app;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.svitla.dzvin.app.activity.AlertDetailsActivity;
import com.svitla.dzvin.app.activity.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by slelyuk on 12/21/13.
 */
public class DzvinReceiver extends BroadcastReceiver {
    private static final String TAG = "DzvinBroadcastReceiver";

    public static final String ACTION = "com.svitla.dzvin.app.ALERT";
    public static final String PARSE_EXTRA_DATA_KEY = "com.parse.Data";
    public static final String PARSE_JSON_ALERT_KEY = "text";
    public static final String PARSE_JSON_CHANNELS_KEY = "com.parse.Channel";

    public static final String KEY_LAST_ALARM_JSON = "lastAlarmJson";
    public static final String KEY_NEW_ALERT_ID = "newAlertId";
    public static final String KEY_ALERT_ID = "a";

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            JSONObject json = new JSONObject(intent.getExtras().getString(PARSE_EXTRA_DATA_KEY));
            if (json.has(KEY_ALERT_ID)) {
                context.getSharedPreferences("default", 0).edit()
                        .putString(KEY_NEW_ALERT_ID, json.getString(KEY_ALERT_ID))
                        .commit();
            }

            notify(context, intent, json);
        } catch (JSONException e) {
            Log.d(TAG, "JSONException: " + e.getMessage());
        }
    }

    private void notify(Context ctx, Intent i, JSONObject dataObject)
            throws JSONException {
        String tickerText = dataObject.getString(PARSE_JSON_ALERT_KEY);
        long when = System.currentTimeMillis();

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(ctx)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(ctx.getString(R.string.app_name))
                        .setContentText(tickerText)
                        .setSound(Uri.parse("android.resource://" + ctx.getPackageName()
                                + "/" + R.raw.notification_sound))
                        .setAutoCancel(true)
                        .setWhen(when);

        Intent intent;
        if (dataObject.has(KEY_ALERT_ID)) {
            intent = new Intent(ctx, AlertDetailsActivity.class);
        } else {
            intent = new Intent(ctx, MainActivity.class);
        }

        intent.putExtra(PARSE_EXTRA_DATA_KEY, i.getExtras().getString(PARSE_EXTRA_DATA_KEY));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent notifyIntent =
                PendingIntent.getActivity(
                        ctx,
                        0,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        mBuilder.setContentIntent(notifyIntent);

        NotificationManager mNotificationManager =
                (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);

        int notifyID = 1;

        mNotificationManager.notify(
                notifyID,
                mBuilder.build());
    }

}
