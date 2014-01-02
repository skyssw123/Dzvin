package com.svitla.dzvin.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parse.ParseObject;
import com.parse.ParseQueryAdapter;
import com.svitla.dzvin.app.DzvinUtils;
import com.svitla.dzvin.app.R;
import com.svitla.dzvin.app.model.Alert;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by slelyuk on 12/25/13.
 */
public class AlertsListAdapter extends ParseQueryAdapter<Alert> {

    private static final DateFormat mFormatter = SimpleDateFormat.getDateTimeInstance(DateFormat.DEFAULT,
            DateFormat.DEFAULT);
    private Context mContext;


    public AlertsListAdapter(Context context, Class<? extends ParseObject> clazz) {
        super(context, clazz);

        mContext = context;
    }

    @Override
    public View getItemView(Alert object, View v, ViewGroup parent) {
        View convertView = v;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.item_alert, null);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            viewHolder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
            viewHolder.rank_indicator = (LinearLayout) convertView.findViewById(R.id.rank_indicator);
            convertView.setTag(viewHolder);
        }

        ViewHolder holder = (ViewHolder) convertView.getTag();

        holder.tv_title.setText(object.getShortText());

        String periodString = DzvinUtils.getFormattedDateInterval(object);
        ;

        holder.tv_date.setText(periodString);

        int rankColorId;
        switch (object.getRank()) {
            case HIGH:
                rankColorId = R.color.rank_high;
                break;
            case MEDIUM:
                rankColorId = R.color.rank_medium;
                break;
            default:
                rankColorId = R.color.rank_low;
                break;
        }
        holder.rank_indicator.setBackgroundColor(mContext.getResources().getColor(rankColorId));

        return convertView;
    }

    static class ViewHolder {
        public TextView tv_title;
        public TextView tv_date;
        public LinearLayout rank_indicator;
    }
}
