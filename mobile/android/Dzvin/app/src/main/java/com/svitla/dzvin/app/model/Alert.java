package com.svitla.dzvin.app.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.svitla.dzvin.app.ERank;

import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by slelyuk on 12/23/13.
 */

@ParseClassName("Alert")
public class Alert extends ParseObject {
    public Alert() {
        // A default constructor is required.
    }

    public String getShortText() {
        return getString("pushText");
    }

    public String getFullText() {
        return getString("fullText");
    }

    public Date getStartDate() {
        return getDate("startDate");
    }

    public Date getEndDate() {
        return getDate("endDate");
    }

    public Boolean isSimple() {
        return getBoolean("isSimple");
    }

    public ERank getRank() {
        if (!containsKey("rank")) {
            return ERank.LOW;
        }

        Number rank = getNumber("rank");
        if (rank == null) {
            return ERank.LOW;
        }

        switch (rank.intValue()) {
            case 3:
                return ERank.HIGH;
            case 2:
                return ERank.MEDIUM;
            default:
                return ERank.LOW;
        }
    }

    public List<Point> getPoints() throws JSONException {
        String pointsString = getString("points");

        Type type = new TypeToken<ArrayList<Point>>() {
        }.getType();
        Gson gson = new GsonBuilder().create();

        ArrayList<Point> points = gson.fromJson(pointsString, type);
        if (points == null)
            points = new ArrayList<Point>();

        return points;
    }

}
