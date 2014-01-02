package com.svitla.dzvin.app;

import com.svitla.dzvin.app.model.Alert;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by slelyuk on 12/25/13.
 */
public class DzvinUtils {

    private static final DateFormat mFormatter = SimpleDateFormat.getDateTimeInstance(DateFormat.DEFAULT,
            DateFormat.DEFAULT);

    public static String getFormattedDateInterval(Alert object) {
        if (object.isSimple()) {
            return mFormatter.format(object.getUpdatedAt());
        }

        String formattedStartDate = null;

        if (object.getStartDate() != null) {
            formattedStartDate = mFormatter.format(object.getStartDate());
        }

        String formattedEndDate = null;
        if (object.getEndDate() != null) {
            formattedEndDate = mFormatter.format(object.getEndDate());
        }

        String periodString = "";
        if (formattedStartDate != null && formattedStartDate.length() != 0)
            periodString += formattedStartDate;

        if (formattedEndDate != null && formattedEndDate.length() != 0)
            periodString += " - " + formattedEndDate;

        return periodString;
    }

}
