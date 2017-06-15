package org.akshara.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.akshara.R;
import org.akshara.model.StudentInfo;

public class ChildsAdapter extends ArrayAdapter<StudentInfo> {

    final String TAG = ChildsAdapter.class.getSimpleName();

    Context mContext;
    int layoutResourceId;
    StudentInfo data[] = null;

    public ChildsAdapter(Context mContext, int layoutResourceId, StudentInfo[] data) {

        super(mContext, layoutResourceId, data);

        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        View v = super.getView(position, convertView, parent);
        final Activity activity = (Activity) mContext;

        try {
            if (convertView == null) {
                // inflate the layout
                LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
                convertView = inflater.inflate(layoutResourceId, parent, false);
            }

            // object item based on the position
            StudentInfo objectItem = data[position];
            if (objectItem != null) {
                // get the TextView and then set the text (item name) and tag (item ID) values
                TextView textViewItem = (TextView) convertView.findViewById(R.id.spinnerData);
                textViewItem.setText(Html.fromHtml("<b color='#000000' >Name: </b>" + objectItem.getChild_name()
                        + "<br><b color='#000000' >Class: </b>" + objectItem.get_class()
                        + "<br><b color='#000000' >KLPID: </b>" + objectItem.getStudent_id()));
//                    + "<br><b color='#000000' >Father's Name: </b>" + objectItem.getFather_name()));
                textViewItem.setOnTouchListener(new View.OnTouchListener() {

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        if (activity.getCurrentFocus() != null) {
                            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
                        }

                        return false;
                    }
                });
            }


            // in case you want to add some style, you can do something like:
            // textViewItem.setBackgroundColor(Color.CYAN);

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;

    }
}