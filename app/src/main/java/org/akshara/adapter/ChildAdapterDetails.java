package org.akshara.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.akshara.R;
import org.akshara.Util.Util;

import java.util.ArrayList;

public class ChildAdapterDetails extends BaseAdapter {
    Context mContext;
    ArrayList<String> mUserInfoArrayList;
    private boolean D = Util.DEBUG;
    private String TAG = ChildAdapterDetails.class.getSimpleName();

    public ChildAdapterDetails(Context context, ArrayList<String> userInfoArrayList) {
        this.mContext = context;
        this.mUserInfoArrayList = userInfoArrayList;
        if (D)
            Log.d(TAG, "mUserInfoArrayList:" + mUserInfoArrayList);

    }

    @Override
    public int getCount() {
        return mUserInfoArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        // inflate the layout
        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        convertView = inflater.inflate(R.layout.child_list_item_details, parent, false);
        TextView textLabel = (TextView) convertView.findViewById(R.id.textLabel);
        TextView textLabelValue = (TextView) convertView.findViewById(R.id.textLabelValue);


        if (position == 0) {
            textLabel.setText("Child Name");
            textLabelValue.setText(mUserInfoArrayList.get(position));
        } else if (position == 1) {
            textLabel.setText("DOB");
            textLabelValue.setText(mUserInfoArrayList.get(position));
        } else if (position == 2) {
            textLabel.setText("Sex");
            textLabelValue.setText(mUserInfoArrayList.get(position));
        } else if (position == 3) {
            textLabel.setText("Fatrher's Name");
            textLabelValue.setText(mUserInfoArrayList.get(position));
        } else if (position == 4) {
            textLabel.setText("Mother's Name");
            textLabelValue.setText(mUserInfoArrayList.get(position));
        } else if (position == 5) {
            textLabel.setText("Mother Tongue");
            textLabelValue.setText(mUserInfoArrayList.get(position));
        } else if (position == 6) {
            textLabel.setText("School Name");
            textLabelValue.setText(mUserInfoArrayList.get(position));
        } else if (position == 7) {
            textLabel.setText("School Code");
            textLabelValue.setText(mUserInfoArrayList.get(position));
        } else if (position == 8) {
            textLabel.setText("Class");
            textLabelValue.setText(mUserInfoArrayList.get(position));
        } else if (position == 9) {
            textLabel.setText("Student Id");
            textLabelValue.setText(mUserInfoArrayList.get(position));
        } else if (position == 10) {
            textLabel.setText("District");
            textLabelValue.setText(mUserInfoArrayList.get(position));
        } else if (position == 11) {
            textLabel.setText("Block");
            textLabelValue.setText(mUserInfoArrayList.get(position));
        } else if (position == 12) {
            textLabel.setText("Cluster");
            textLabelValue.setText(mUserInfoArrayList.get(position));
        } else if (position == 13) {
            textLabel.setText("District Code");
            textLabelValue.setText(mUserInfoArrayList.get(position));
        }


        return convertView;
    }


}
