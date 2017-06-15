package org.akshara.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.akshara.R;
import org.akshara.Util.Util;
import org.akshara.activity.MainActivity;
import org.akshara.fragment.ChildDetailsFragment;
import org.akshara.model.StudentInfo;

import java.util.ArrayList;
import java.util.List;


public class ChildAdapter extends BaseAdapter {
    Context mContext;
    List<StudentInfo> mStudentInfoArrayList;
    private boolean D = Util.DEBUG;
    private String TAG = ChildAdapter.class.getSimpleName();

    public ChildAdapter(Context context, List<StudentInfo> StudentInfoArrayList) {
        this.mContext = context;
        this.mStudentInfoArrayList = StudentInfoArrayList;
        if (D)
            Log.d(TAG, "mStudentInfoArrayList:" + mStudentInfoArrayList);

    }

    @Override
    public int getCount() {
        return mStudentInfoArrayList.size();
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
        ChildHolder childHolder;
        if (convertView == null) {

            // inflate the layout
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(R.layout.child_list_item, parent, false);

            // well set up the ViewHolder
            childHolder = new ChildHolder();
            childHolder.textViewHeading = (TextView) convertView.findViewById(R.id.textViewHeading);
            childHolder.textViewSubHeading = (TextView) convertView.findViewById(R.id.textViewSubHeading);
            //   childHolder.imageViewProfile = (ImageView) convertView.findViewById(R.id.imageViewProfile);
            childHolder.linearLayoutChild = (LinearLayout) convertView.findViewById(R.id.linearLayoutChild);

            // store the holder with the view.
            convertView.setTag(childHolder);

        } else {
            // we've just avoided calling findViewById() on resource everytime
            // just use the viewHolder
            childHolder = (ChildHolder) convertView.getTag();
        }


        // assign values if the object is not null
        if (mStudentInfoArrayList.size() != 0) {
            if (D)
                Log.d(TAG, "getSchoolName:" + mStudentInfoArrayList.get(position).getSchool_name());
            // get the TextView from the ViewHolder and then set the text (item name) and tag (item ID) values
            childHolder.textViewHeading.setText(mStudentInfoArrayList.get(position).getChild_name());
            childHolder.textViewSubHeading.setText(mStudentInfoArrayList.get(position).getSchool_name());
        }


        childHolder.linearLayoutChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChilddetails(position);
            }
        });


        return convertView;
    }

    private void showChilddetails(int position) {
        if (mStudentInfoArrayList.size() != 0) {
            ChildDetailsFragment fragment = new ChildDetailsFragment();
            //district    block     clust       school_code school_name |Matching(M)| dise_code _class student_id child_name sex dob mother_tongue father_name mother_name

            ArrayList<String> list = new ArrayList<String>();
            list.add(mStudentInfoArrayList.get(position).getChild_name());
            list.add(mStudentInfoArrayList.get(position).getDob());
            list.add(mStudentInfoArrayList.get(position).getSex());
            list.add(mStudentInfoArrayList.get(position).getFather_name());
            list.add(mStudentInfoArrayList.get(position).getMother_name());
            list.add(mStudentInfoArrayList.get(position).getMother_tongue());
            list.add(mStudentInfoArrayList.get(position).getSchool_name());
            list.add(mStudentInfoArrayList.get(position).getSchool_code());
            list.add(mStudentInfoArrayList.get(position).get_class());
            list.add(mStudentInfoArrayList.get(position).getStudent_id());
            list.add(mStudentInfoArrayList.get(position).getDistrict());
            list.add(mStudentInfoArrayList.get(position).getBlock());
            list.add(mStudentInfoArrayList.get(position).getClust());
            list.add(mStudentInfoArrayList.get(position).getDise_code());

            Bundle args = new Bundle();
            args.putStringArrayList("userinfo", list);


            fragment.setArguments(args);
            ((MainActivity) mContext).switchContent(fragment, 1, true);

        }
    }

    static class ChildHolder {
        TextView textViewHeading;
        TextView textViewSubHeading;
        //ImageView imageViewProfile;
        LinearLayout linearLayoutChild;

    }

}
