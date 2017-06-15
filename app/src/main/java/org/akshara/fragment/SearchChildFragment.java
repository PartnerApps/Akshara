package org.akshara.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import org.akshara.R;
import org.akshara.Util.Util;
import org.akshara.activity.MainActivity;
import org.akshara.db.DatabaseHelper;
import org.akshara.db.StudentDAO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchChildFragment extends Fragment {
    private boolean D = Util.DEBUG;
    private String TAG = SearchChildFragment.class.getSimpleName();
    private Context mContext;
    private DatabaseHelper mDatabaseHandler;
    private Spinner spinnerDistric, spinnerBlock,
            spinnerCluster, spinnerSchool;

    private String spinnerDistric_selected, spinnerBlock_selected,
            spinnerCluster_selected, spinnerSchool_selected,
            spinnerSchoolCode_selected;
    private String spinnerDistric_selected1, spinnerBlock_selected1,
            spinnerCluster_selected1, spinnerSchool_selected1;

    private Fragment mFragment;
    private Util util;
    private ArrayList<String> schoolList = new ArrayList<>();
    private ArrayList<String> codeList = new ArrayList<>();
    private ArrayAdapter<String> adapterSchool;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        mContext = getActivity();
        mFragment = this;
        mDatabaseHandler = new DatabaseHelper(mContext);
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.search_child, container, false);
        spinnerDistric = (Spinner) rootView.findViewById(R.id.spinnerDistric);
        spinnerBlock = (Spinner) rootView.findViewById(R.id.spinnerBlock);
        spinnerCluster = (Spinner) rootView.findViewById(R.id.spinnerCluster);
        spinnerSchool = (Spinner) rootView.findViewById(R.id.spinnerSchool);
        Button SearchChild_btn = (Button) rootView.findViewById(R.id.SearchChild_btn);
        Util.hideKeyboard(getActivity(), mContext);

        SearchChild_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spinnerDistric_selected.equals(getString(R.string.district_default))) {
                    Toast.makeText(mContext, getString(R.string.district_default), Toast.LENGTH_LONG).show();
                } else if (spinnerBlock_selected.equals(getString(R.string.block_default))) {
                    Toast.makeText(mContext, getString(R.string.block_default), Toast.LENGTH_LONG).show();
                } else if (spinnerCluster_selected.equals(getString(R.string.cluster_default))) {
                    Toast.makeText(mContext, getString(R.string.cluster_default), Toast.LENGTH_LONG).show();
                } else if (!spinnerSchool_selected.equals(getString(R.string.school_default))) {
                    NewChildListFragment childListFragment = new NewChildListFragment();
                    util.setSpinnerDistric_selected(spinnerDistric_selected);
                    util.setSpinnerBlock_selected(spinnerBlock_selected);
                    util.setSpinnerCluster_selected(spinnerCluster_selected);
                    util.setSpinnerSchool_selected(spinnerSchool_selected);
                    int pos = adapterSchool.getPosition(spinnerSchool_selected);
                    spinnerSchoolCode_selected = codeList.get(pos);
                    util.setSpinnerSchoolCode_selected(spinnerSchoolCode_selected);
                    Bundle bundle = new Bundle();
                    bundle.putString("spinnerDistric_selected", spinnerDistric_selected);
                    bundle.putString("spinnerBlock_selected", spinnerBlock_selected);
                    bundle.putString("spinnerCluster_selected", spinnerCluster_selected);
                    bundle.putString("spinnerSchool_selected", spinnerSchool_selected);
                    bundle.putString("spinnerSchoolCode_selected", spinnerSchoolCode_selected);
                    childListFragment.setArguments(bundle);
                    ((MainActivity) mContext).switchContent(childListFragment, 1, true);
                } else {
                    Toast.makeText(mContext, getString(R.string.school_default), Toast.LENGTH_LONG).show();
                }


            }
        });

        ArrayList<String> districtList = StudentDAO.getInstance().getUniqueFieldData(
                StudentDAO.COLUMN_DISTRICT, getString(R.string.district_default));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, R.layout.spinner_item,
                districtList);
        spinnerDistric.setAdapter(adapter);
        adapter.setDropDownViewResource(R.layout.spinner_popup_item);
        spinnerDistric.setAdapter(adapter);
        //  spinnerDistric.setBackgroundDrawable(R.drawable.btn_dropdown_normal);


        util = (Util) mContext.getApplicationContext();
        spinnerDistric_selected1 = util.getSpinnerDistric_selected();
        spinnerBlock_selected1 = util.getSpinnerBlock_selected();
        spinnerCluster_selected1 = util.getSpinnerCluster_selected();
        spinnerSchool_selected1 = util.getSpinnerSchool_selected();
       /* if(D)
            Log.d(TAG,"spinnerDistric_selected:"+spinnerDistric_selected1);
        if(D)
            Log.d(TAG,"spinnerBlock_selected:"+spinnerBlock_selected1);

        if(D)
            Log.d(TAG,"spinnerCluster_selected:"+spinnerCluster_selected1);
*/
        if (spinnerDistric_selected1 != null) {
            int pos = adapter.getPosition(spinnerDistric_selected1);
            spinnerDistric.setSelection(pos);

        }

        spinnerDistric.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerDistric_selected = parent.getItemAtPosition(position).toString();
                if (!spinnerDistric_selected.equals(spinnerDistric_selected1)) {
                    spinnerDistric_selected1 = null;
                    spinnerBlock_selected1 = null;
                    spinnerCluster_selected1 = null;
                    spinnerSchool_selected1 = null;
                }
                setBlockData(spinnerDistric_selected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerBlock.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerBlock_selected = parent.getItemAtPosition(position).toString();
                if (!spinnerBlock_selected.equals(spinnerBlock_selected1)) {
                    spinnerDistric_selected1 = null;
                    spinnerBlock_selected1 = null;
                    spinnerCluster_selected1 = null;
                    spinnerSchool_selected1 = null;
                }
                setClusterData(spinnerDistric_selected, spinnerBlock_selected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerCluster.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerCluster_selected = parent.getItemAtPosition(position).toString();
                if (!spinnerCluster_selected.equals(spinnerCluster_selected1)) {
                    spinnerDistric_selected1 = null;
                    spinnerBlock_selected1 = null;
                    spinnerCluster_selected1 = null;
                    spinnerSchool_selected1 = null;
                }
                setSchoolData(spinnerDistric_selected, spinnerBlock_selected, spinnerCluster_selected);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerSchool.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerSchool_selected = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        return rootView;
    }

    private void setBlockData(String district) {
        ArrayList<String> blockList = StudentDAO.getInstance()
                .getUniqueFieldData(StudentDAO.COLUMN_BLOCK, getString(R.string.block_default),
                        StudentDAO.COLUMN_DISTRICT, district);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, R.layout.spinner_item,
                blockList);
        spinnerBlock.setAdapter(adapter);
        adapter.setDropDownViewResource(R.layout.spinner_popup_item);
        spinnerBlock.setAdapter(adapter);

        if (spinnerBlock_selected1 != null) {
            int pos = adapter.getPosition(spinnerBlock_selected1);
            spinnerBlock.setSelection(pos);
        }
    }

    private void setClusterData(String district, String block) {
        ArrayList<String> clusterList = StudentDAO.getInstance()
                .getUniqueFieldData(StudentDAO.COLUMN_CLUSTER, getString(R.string.cluster_default),
                        StudentDAO.COLUMN_DISTRICT, StudentDAO.COLUMN_BLOCK, district, block);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, R.layout.spinner_item,
                clusterList);
        spinnerCluster.setAdapter(adapter);
        adapter.setDropDownViewResource(R.layout.spinner_popup_item);
        spinnerCluster.setAdapter(adapter);
        if (spinnerCluster_selected1 != null) {
            int pos = adapter.getPosition(spinnerCluster_selected1);
            spinnerCluster.setSelection(pos);
        }
    }

    private void setSchoolData(String district, String block, String cluster) {
        Map<String, String> hashMap = new HashMap<>();

        hashMap = StudentDAO.getInstance().getAllSchool(district, block, cluster);

        // if(hashMap.size()>1){
        schoolList.clear();
        codeList.clear();
        for (String s : hashMap.keySet()) {
            codeList.add(s);
        }
        for (int i = 0; i < codeList.size(); i++) {
            schoolList.add(hashMap.get(codeList.get(i)));
        }
        // }


        if (D)
            Log.d(TAG, "schoolList:" + schoolList);
        if (D)
            Log.d(TAG, "codeList:" + codeList);
        schoolList.add(0, getString(R.string.school_default));
        codeList.add(0, "key");
        adapterSchool = new ArrayAdapter<String>(mContext, R.layout.spinner_item, schoolList);
        spinnerSchool.setAdapter(adapterSchool);
        adapterSchool.setDropDownViewResource(R.layout.spinner_popup_item);
        spinnerSchool.setAdapter(adapterSchool);
        //by default  PLEASE SELECT SCHOOL
        int posdefault = adapterSchool.getPosition(getString(R.string.school_default));
        spinnerSchool.setSelection(posdefault);
        if (spinnerSchool_selected1 != null) {
            int pos = adapterSchool.getPosition(spinnerSchool_selected1);
            spinnerSchool.setSelection(pos);
        }


    }


}
