package com.svitla.dzvin.app.fragment;

import android.app.Activity;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.ParseQueryAdapter;
import com.svitla.dzvin.app.adapter.AlertsListAdapter;
import com.svitla.dzvin.app.model.Alert;

import java.util.List;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AlertsListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AlertsListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AlertsListFragment extends ListFragment implements ParseQueryAdapter.OnQueryLoadListener<Alert> {
    private static final String TAG = "AlertsListFragment";

    private OnFragmentInteractionListener mListener;
    private ParseQueryAdapter<Alert> mAdapter = null;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AlertsListFragment.
     */
    public static AlertsListFragment newInstance() {
        AlertsListFragment fragment = new AlertsListFragment();
        return fragment;
    }

    public AlertsListFragment() {

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        if (mListener != null) {
            mListener.onAlertClick(mAdapter.getItem(position));
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }

        mAdapter = new AlertsListAdapter(activity, Alert.class);
        mAdapter.setAutoload(true);
        mAdapter.addOnQueryLoadListener(this);

        setListAdapter(mAdapter);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onLoading() {

    }

    @Override
    public void onLoaded(List<Alert> alerts, Exception e) {
        if (e != null) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
            return;
        }

        Log.d(TAG, "onLoaded: " + alerts.toString());
    }

    public interface OnFragmentInteractionListener {
        public void onAlertClick(Alert alert);
    }

}
