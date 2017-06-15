package org.akshara.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.gson.Gson;

import org.akshara.R;
import org.akshara.Util.Util;
import org.akshara.activity.MainActivity;
import org.akshara.fragment.DisplayChildProfileFragment;
import org.akshara.model.UserModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class TemplateAdapter extends BaseAdapter {
    Context mContext;
    private boolean D = Util.DEBUG;
    private String TAG = TemplateAdapter.class.getSimpleName();
    private HashMap<String, UserModel> mhashMapTemplate_data = new HashMap<>();

    public TemplateAdapter(Context context, HashMap<String, UserModel> hashMapTemplate_data) {
        this.mContext = context;
        this.mhashMapTemplate_data = hashMapTemplate_data;
        if (D)
            Log.d(TAG, "mhashMapTemplate_data:" + mhashMapTemplate_data);

    }

    @Override
    public int getCount() {
        return mhashMapTemplate_data.size();
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
            convertView = inflater.inflate(R.layout.template_child_list_item, parent, false);

            // well set up the ViewHolder
            childHolder = new ChildHolder();
            childHolder.textViewHeading = (TextView) convertView.findViewById(R.id.textViewHeading);
            //childHolder.textViewSubHeading = (TextView) convertView.findViewById(R.id.textViewSubHeading);
            //   childHolder.imageViewProfile = (ImageView) convertView.findViewById(R.id.imageViewProfile);

            // store the holder with the view.
            convertView.setTag(childHolder);

        } else {
            // we've just avoided calling findViewById() on resource everytime
            // just use the viewHolder
            childHolder = (ChildHolder) convertView.getTag();
        }
        List<String> l = new ArrayList<String>(mhashMapTemplate_data.keySet());
        final String key = l.get(position);
        if (D)
            Log.d(TAG, "key----->" + key);

        childHolder.textViewHeading.setText("" + key);

        childHolder.textViewHeading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DisplayChildProfileFragment displayChildProfileFragment = new DisplayChildProfileFragment();

                UserModel userModel = mhashMapTemplate_data.get(key);

                Bundle bundle = new Bundle();
                //Convert object to json string
                String json = new Gson().toJson(userModel);
                if (D)
                    Log.d(TAG, "json" + json);
                bundle.putString(Util.USERMODEL_DATA, json);
                bundle.putString(Util.TEMPLATE_NAME, key);
                displayChildProfileFragment.setArguments(bundle);
                ((MainActivity) mContext).switchContent(displayChildProfileFragment, 1, true);


            }
        });


        return convertView;
    }

    static class ChildHolder {
        TextView textViewHeading;
        // TextView textViewSubHeading;
        //ImageView imageViewProfile;
        //LinearLayout linearLayoutChild;

    }

}
