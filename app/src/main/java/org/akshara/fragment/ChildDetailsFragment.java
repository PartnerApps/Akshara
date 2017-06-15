package org.akshara.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.akshara.R;
import org.akshara.Util.Util;
import org.akshara.activity.MainActivity;
import org.akshara.adapter.ChildAdapterDetails;

import java.util.ArrayList;

public class ChildDetailsFragment extends Fragment {
    ArrayList<String> list = new ArrayList<String>();
    Context mContext;
    private Fragment mFragment;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainActivity) mContext).showBackIconWithConnect("Profile", mFragment);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        mContext = getActivity();
        mFragment = this;
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.child_row_details, container, false);
        ListView listViewChildDetails = (ListView) rootView.findViewById(R.id.listViewChildDetails);

        list = getArguments().getStringArrayList("userinfo");
        if (list.size() != 0) {
            Util util = (Util) mContext.getApplicationContext();
            // util.setList(list);
            ChildAdapterDetails childAdapterDetails = new ChildAdapterDetails(mContext, list);
            listViewChildDetails.setAdapter(childAdapterDetails);
        }

        return rootView;
    }
}
