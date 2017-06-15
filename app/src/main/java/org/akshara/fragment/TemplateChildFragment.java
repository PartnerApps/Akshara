package org.akshara.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import org.akshara.R;
import org.akshara.Util.Util;
import org.akshara.activity.MainActivity;
import org.akshara.adapter.TemplateAdapter;
import org.akshara.model.UserModel;

import java.util.HashMap;


public class TemplateChildFragment extends Fragment {
    private boolean D = Util.DEBUG;
    private String TAG = TemplateChildFragment.class.getSimpleName();
    private Context mContext;
    private Fragment mFragment;
    private Button create_template_btn;
    private HashMap<String, UserModel> hashMapTemplate_data = new HashMap<>();
    private ListView listViewTemplate;
    private LinearLayout akshara_template;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainActivity) mContext).showBackIcon("Create Child Profile", mFragment);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mContext = getActivity();
        mFragment = this;
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.child_template, container, false);
        create_template_btn = (Button) rootView.findViewById(R.id.create_template_btn);
        listViewTemplate = (ListView) rootView.findViewById(R.id.listViewTemplate);
        akshara_template = (LinearLayout) rootView.findViewById(R.id.akshara_template);

        akshara_template.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DisplayChildProfileFragment displayChildProfileFragment = new DisplayChildProfileFragment();
                ((MainActivity) mContext).switchContent(displayChildProfileFragment, 1, true);

            }
        });

        hashMapTemplate_data = new Util(mContext).getUserModel();


        if (hashMapTemplate_data != null) {
            TemplateAdapter templateAdapter = new TemplateAdapter(mContext, hashMapTemplate_data);
            listViewTemplate.setAdapter(templateAdapter);
        }


        create_template_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateChildProfileFragment createChildProfileFragment = new CreateChildProfileFragment();
                ((MainActivity) mContext).switchContent(createChildProfileFragment, 1, true);


            }


        });

        return rootView;
    }


}
