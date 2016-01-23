package citu.teknoybuyandselluser.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import citu.teknoybuyandselluser.R;
import citu.teknoybuyandselluser.adapters.GridAdapter;

/**
 * Created by Janna Tapitha on 1/20/2016.
 */

public class Gridview extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gridview, container, false);

        GridView gridView = (GridView) view.findViewById(R.id.gridview);
        GridAdapter gridAdapter = new GridAdapter(getActivity());
        gridView.setAdapter(gridAdapter);

        return view;
    }



}
