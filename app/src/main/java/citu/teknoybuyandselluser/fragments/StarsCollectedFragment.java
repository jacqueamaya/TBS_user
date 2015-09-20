package citu.teknoybuyandselluser.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import citu.teknoybuyandselluser.LoginActivity;
import citu.teknoybuyandselluser.R;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StarsCollectedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StarsCollectedFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "user";
    private TextView txtNumberStars;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param user Parameter 1.
     * @return A new instance of fragment StarsCollectedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StarsCollectedFragment newInstance(String user) {
        StarsCollectedFragment fragment = new StarsCollectedFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, user);
        fragment.setArguments(args);
        return fragment;
    }

    public StarsCollectedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = null;
        view = inflater.inflate(R.layout.fragment_stars_collected, container, false);
        txtNumberStars = (TextView) view.findViewById(R.id.txtNumberStars);
        txtNumberStars.setText(getStars()+" stars");
        return view;
    }

    public int getStars(){
        SharedPreferences prefs = this.getActivity().getSharedPreferences(LoginActivity.MY_PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getInt("stars_collected", 0);
    }
}
