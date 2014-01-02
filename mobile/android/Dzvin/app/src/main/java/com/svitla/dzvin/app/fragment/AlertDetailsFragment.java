package com.svitla.dzvin.app.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.svitla.dzvin.app.R;
import com.svitla.dzvin.app.model.Alert;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Use the {@link AlertDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AlertDetailsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_ITEM_ID = "com.svitla.dzvin.app.ITEM_ID";

    private String mItemId;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param itemId alert id from Parse.
     * @return A new instance of fragment AlertDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AlertDetailsFragment newInstance(String itemId) {
        AlertDetailsFragment fragment = new AlertDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ITEM_ID, itemId);
        fragment.setArguments(args);
        return fragment;
    }

    public AlertDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mItemId = getArguments().getString(ARG_ITEM_ID);
        }

        if (mItemId == null)
            return;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_alert_details, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        final Alert alert = ParseObject.createWithoutData(Alert.class, mItemId);
        alert.fetchIfNeededInBackground(new GetCallback<Alert>() {
            @Override
            public void done(Alert object, ParseException e) {
                if (e != null) {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }

                updateLayout(alert);
            }
        });
    }

    private void updateLayout(Alert alert) {
        View v = getView();
        if (v == null || alert == null)
            return;

        TextView tv_title = (TextView) v.findViewById(R.id.tv_title);
        tv_title.setText(alert.getShortText());
    }

}
