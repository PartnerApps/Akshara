package org.akshara.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedHashTreeMap;
import com.google.gson.reflect.TypeToken;

import org.akshara.BuildConfig;
import org.akshara.R;
import org.akshara.Util.TelemetryEventGenertor;
import org.akshara.Util.Util;
import org.akshara.activity.MainActivity;
import org.akshara.adapter.ChildsAdapter;
import org.akshara.callback.CurrentUserResponseHandler;
import org.akshara.callback.EndSessionResponseHandler;
import org.akshara.callback.ICurrentUser;
import org.akshara.callback.IEndSession;
import org.akshara.callback.IPartnerData;
import org.akshara.callback.ITelemetryData;
import org.akshara.callback.IUserCreateProfile;
import org.akshara.callback.PartnerDataResponseHandler;
import org.akshara.callback.TelemetryResponseHandler;
import org.akshara.callback.UserProfileCreateResponseHandler;
import org.akshara.customviews.CustomAutoCompleteView;
import org.akshara.customviews.MyProgressBar;
import org.akshara.db.StudentDAO;
import org.akshara.model.StudentInfo;
import org.ekstep.genieresolvers.GenieSDK;
import org.ekstep.genieresolvers.partner.PartnerService;
import org.ekstep.genieresolvers.telemetry.TelemetryService;
import org.ekstep.genieresolvers.user.UserService;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.PartnerData;
import org.ekstep.genieservices.commons.bean.Profile;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewChildListFragment extends Fragment implements IEndSession, IUserCreateProfile,
        ICurrentUser, IPartnerData, ITelemetryData {

    private static final String TAG = "NewChildListFragment";
    private static final boolean DEBUG = BuildConfig.DEBUG;

    private MyProgressBar mProgressBarView;

    private ImageView mSearchIcon;

    private CustomAutoCompleteView mAutoCompleteView;

    private Button mCreateProfileButton;

    private TextView mNoDataTextView;


    private ChildsAdapter mChildAdapter;

    private boolean myAutoCompleteTextChange = false;


    private StudentInfo[] mStudentInfoArray;

    private Map<String, Object> mSelectedStudentMap = new HashMap<>();


    private String mSelectedDistrict;
    private String mSelectedBlock;
    private String mSelectedClustor;
    private String mSelectedSchool;
    private String mSelectedSchoolCode;

    private String mSelectedStudentId;
    private String mSelectedStudentUID;

    /**
     * Flag value to decide we're creating a new Child or using Existing Child
     * By default it'll be false.
     */
    private boolean mIsCreateNewChild = false;


    private PartnerService mPartnerService;
    private UserService mUserService;

    private Gson mGson = new Gson();


    public NewChildListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mSelectedDistrict = getArguments().getString("spinnerDistric_selected");
            mSelectedBlock = getArguments().getString("spinnerBlock_selected");
            mSelectedClustor = getArguments().getString("spinnerCluster_selected");
            mSelectedSchool = getArguments().getString("spinnerSchool_selected");
            mSelectedSchoolCode = getArguments().getString("spinnerSchoolCode_selected");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_child_list, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mProgressBarView = (MyProgressBar) view.findViewById(R.id.sendingDetails);
        mSearchIcon = (ImageView) view.findViewById(R.id.icn_search);
        mAutoCompleteView = (CustomAutoCompleteView) view.findViewById(R.id.autoCompleteTextView1);
        mNoDataTextView = (TextView) view.findViewById(R.id.textNoData);

        mCreateProfileButton = (Button) view.findViewById(R.id.create_profile_btn);

        mCreateProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createProfile();
            }
        });

        initAutoCompleteTextView();
    }


    private void initAutoCompleteTextView() {
        mAutoCompleteView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view;
                String str = textView.getText().toString();
                mAutoCompleteView.setText(str);
                sendPartnerDataToGenie(position);
            }
        });
        showKeyboard();

        StudentInfo[] objectItemData = new StudentInfo[0];
        mChildAdapter = new ChildsAdapter(getActivity(), R.layout.spinner_popup_item,
                objectItemData);


        mAutoCompleteView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence userInput, int start, int before, int count) {
                if (mAutoCompleteView.isPerformingCompletion()) {
                    if (DEBUG) Log.d(TAG, "onTextChanged-- isPerformingCompletion----------------");
                    myAutoCompleteTextChange = true;
                    // An item has been selected from the list. Ignore.
                } else if (!myAutoCompleteTextChange) {
                    try {
                        mSearchIcon.setVisibility(View.GONE);

                        // get suggestions from the database

                        mStudentInfoArray = StudentDAO.getInstance().getAllStudentInfoObject(
                                mSelectedSchoolCode, userInput.toString());

                        // update the adapter
                        mChildAdapter = new ChildsAdapter(getActivity(), R.layout.spinner_popup_item, mStudentInfoArray);
                        mAutoCompleteView.setAdapter(mChildAdapter);
                        // update the adapater
                        mChildAdapter.notifyDataSetChanged();

                        if (mStudentInfoArray.length == 0) {
                            mSearchIcon.setVisibility(View.VISIBLE);
                            mNoDataTextView.setVisibility(View.VISIBLE);
                            mCreateProfileButton.setVisibility(View.VISIBLE);
                            hideKeyboard();
                        } else {


                            mNoDataTextView.setVisibility(View.GONE);
                            mCreateProfileButton.setVisibility(View.GONE);
                            // showKeyboard();
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }


    /* To show keyboard */
    private void showKeyboard() {
        if (getActivity().getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager)
                    getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
        }
    }

    /* To hide keyboard */
    private void hideKeyboard() {
        if (getActivity().getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager)
                    getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }
    }


    private void createProfile() {
        if (getActivity() != null && getActivity() instanceof MainActivity) {
            TemplateChildFragment templateChildFragment = new TemplateChildFragment();
            ((MainActivity) getActivity()).switchContent(templateChildFragment, 1, true);
        }
    }

    private void sendPartnerDataToGenie(int position) {
        mProgressBarView.setVisibility(View.VISIBLE);

        mSelectedStudentMap.put(StudentDAO.COLUMN_DISTRICT, mStudentInfoArray[position].getDistrict().trim());
        mSelectedStudentMap.put(StudentDAO.COLUMN_BLOCK, mStudentInfoArray[position].getBlock().trim());
        mSelectedStudentMap.put(StudentDAO.COLUMN_CLUSTER, mStudentInfoArray[position].getClust().trim());
        mSelectedStudentMap.put(StudentDAO.COLUMN_SCHOOL_CODE, mStudentInfoArray[position].getSchool_code().trim());
        // mSelectedStudentMap.put(StudentDAO.dise_code, mStudentInfoArray[position].getDise_code().trim());
        mSelectedStudentMap.put(StudentDAO.COLUMN_SCHOOL_NAME, mStudentInfoArray[position].getSchool_name().trim());
        mSelectedStudentMap.put(StudentDAO.COLUMN_CLASS, mStudentInfoArray[position].get_class().trim());
        mSelectedStudentMap.put(StudentDAO.COLUMN_STUDENT_ID, mStudentInfoArray[position].getStudent_id().trim());
        mSelectedStudentMap.put(StudentDAO.COLUMN_CHILD_NAME, mStudentInfoArray[position].getChild_name().trim());
        mSelectedStudentMap.put(StudentDAO.COLUMN_SEX, mStudentInfoArray[position].getSex().trim());
        //mSelectedStudentMap.put(StudentDAO.mother_tongue, mStudentInfoArray[position].getMother_tongue().trim());
//        mSelectedStudentMap.put(StudentDAO.dob, mStudentInfoArray[position].getDob().trim());
        mSelectedStudentMap.put(StudentDAO.COLUMN_FATHER_NAME, mStudentInfoArray[position].getFather_name().trim());
//        mSelectedStudentMap.put(StudentDAO.mother_name, mStudentInfoArray[position].getMother_name().trim());

        mSelectedStudentUID = mStudentInfoArray[position].getUid();
        mSelectedStudentId = mStudentInfoArray[position].getStudent_id().trim();

        if (DEBUG) {
            Log.i(TAG, "sendPartnerDataToGenie: Selected Student Details : " + mSelectedStudentMap);
        }

        PartnerData partnerData = new PartnerData(null, null, Util.partnerId, null, Util.publicKey);

        mPartnerService = GenieSDK.getGenieSDK().getPartnerService();
        EndSessionResponseHandler endSessionResponseHandler = new EndSessionResponseHandler(this);
        mPartnerService.endPartnerSession(partnerData, endSessionResponseHandler);
    }


    @Override
    public void onSuccessEndSession(GenieResponse genieResponse) {
        String response = mGson.toJson(genieResponse);

        if (DEBUG) {
            Log.i(TAG, "onSuccessEndSession: " + response);
        }

        mUserService = GenieSDK.getGenieSDK().getUserService();


        if (!TextUtils.isEmpty(mSelectedStudentUID)) {
            if (DEBUG) {
                Log.i(TAG, "onSuccessEndSession: UID Exists : " + mSelectedStudentUID);
            }

            mIsCreateNewChild = false;

            CurrentUserResponseHandler currentUserResponseHandler
                    = new CurrentUserResponseHandler(this);
            mUserService.setUser(mSelectedStudentUID, currentUserResponseHandler);

        } else {
            if (DEBUG) {
                Log.i(TAG, "onSuccessEndSession: UID not exists");
            }

            String language = "en";

            String handle = (String) mSelectedStudentMap.get(StudentDAO.COLUMN_CHILD_NAME);
            String gender = (String) mSelectedStudentMap.get(StudentDAO.COLUMN_SEX);
            int standard = -1;

            try {
                standard = Integer.parseInt(mSelectedStudentMap.get(StudentDAO.COLUMN_CLASS)
                        .toString());
            } catch (Exception ex) {
                if (DEBUG) {
                    Log.e(TAG, "onSuccessEndSession: ", ex);
                }
            }

            Profile profile = new Profile(handle, Util.avatar, language);
            profile.setGender(gender);
            profile.setStandard(standard);


            UserProfileCreateResponseHandler userProfileCreateResponseHandler
                    = new UserProfileCreateResponseHandler(this);

            mUserService.createUserProfile(profile, userProfileCreateResponseHandler);
        }
    }

    @Override
    public void onFailureEndSession(GenieResponse genieResponse) {
        mProgressBarView.setVisibility(View.GONE);
    }


    @Override
    public void onSuccessUserProfile(GenieResponse<Map> genieResponse) {
        String response = mGson.toJson(genieResponse);

        if (DEBUG) {
            Log.i(TAG, "onSuccessUserProfile: " + response);
        }

        Type type = new TypeToken<LinkedHashTreeMap<String, String>>(){}.getType();
        Map<String, String> data = mGson.fromJson(response, type);

        mSelectedStudentUID = data.get("result");
        mSelectedStudentMap.put(StudentDAO.COLUMN_UID, mSelectedStudentUID);
        StudentDAO.getInstance().updateStudentUID(mSelectedStudentId, mSelectedStudentUID);


        CurrentUserResponseHandler currentUserResponseHandler
                = new CurrentUserResponseHandler(this);

        mUserService.setUser(mSelectedStudentUID, currentUserResponseHandler);
    }

    @Override
    public void onFailureUserProfile(GenieResponse<Map> genieResponse) {
        if (DEBUG) {
            Log.i(TAG, "onFailureUserProfile: " + genieResponse.getMessage());
        }
    }


    @Override
    public void onSuccessCurrentUser(GenieResponse genieResponse) {
        if (DEBUG) {
            Log.i(TAG, "onSuccessCurrentUser: " + mGson.toJson(genieResponse));
        }


        mSelectedStudentMap.put(StudentDAO.COLUMN_UID, mSelectedStudentUID);

        HashMap<String, Object> partnerDataMap = new HashMap<>();
        partnerDataMap.put(StudentDAO.COLUMN_UID, mSelectedStudentUID);
        partnerDataMap.put(StudentDAO.COLUMN_STUDENT_ID,
                mSelectedStudentMap.get(StudentDAO.COLUMN_STUDENT_ID));

        if (mIsCreateNewChild) {
            partnerDataMap.putAll(mSelectedStudentMap);
        }

        JSONObject partnerJSONObject = new JSONObject(partnerDataMap);
        PartnerDataResponseHandler responseHandler = new PartnerDataResponseHandler(this);

        PartnerData partnerData = new PartnerData(null, null, Util.partnerId,
                partnerJSONObject.toString(), Util.publicKey);

        mPartnerService.sendPartnerData(partnerData, responseHandler);

    }

    @Override
    public void onFailureCurrentUser(GenieResponse genieResponse) {
        String result = new Gson().toJson(genieResponse);

        if (DEBUG) {
            Log.i(TAG, "onFailureCurrentUser: " + result);
        }

        if (genieResponse.getError() != null
                && genieResponse.getError().equals("INVALID_USER")) {


            if (DEBUG) {
                Log.i(TAG, "onFailureCurrentUser: Invalid UID creating the Child Again");
            }

            String handle = mSelectedStudentMap.get(StudentDAO.COLUMN_CHILD_NAME).toString();
            String gender = mSelectedStudentMap.get(StudentDAO.COLUMN_SEX).toString();
            Profile profile = new Profile(handle, "Avatar","en");

            if (!TextUtils.isEmpty(gender)) {
                profile.setGender(gender);
            }


            if (mUserService == null) {
                mUserService = GenieSDK.getGenieSDK().getUserService();
            }

            mIsCreateNewChild = true;

            UserProfileCreateResponseHandler userProfileResponseHandler
                    = new UserProfileCreateResponseHandler(this);
            mUserService.createUserProfile(profile, userProfileResponseHandler);
        }
    }


    @Override
    public void onSuccessPartner(GenieResponse genieResponse) {
        if (DEBUG) {
            Log.d(TAG, "onSuccessPartner: " + mGson.toJson(genieResponse));
        }

        Util util = (Util) getActivity().getApplicationContext();

        String telemetryJSONData = TelemetryEventGenertor.generateOEEndEvent(getActivity(),
                util.getStartTime(), System.currentTimeMillis(), mSelectedStudentUID).toString();

        TelemetryService telemetryService = GenieSDK.getGenieSDK().getTelemetryService();
        TelemetryResponseHandler telemetryResponseHandler = new TelemetryResponseHandler(this);
        telemetryService.saveTelemetryEvent(telemetryJSONData, telemetryResponseHandler);

    }

    @Override
    public void onFailurePartner(GenieResponse genieResponse) {
        if (DEBUG) {
            Log.i(TAG, "onFailurePartner: " + mGson.toJson(genieResponse));
        }
    }


    @Override
    public void onSuccessTelemetry(GenieResponse genieResponse) {
        if (DEBUG) {
            Log.i(TAG, "onSuccessTelemetry: " + mGson.toJson(genieResponse));
        }
        Util.processSuccess(getActivity(), genieResponse);

        if (getActivity() != null && getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).exitApp();
        }

    }

    @Override
    public void onFailureTelemetry(GenieResponse genieResponse) {
        if (DEBUG) {
            Log.i(TAG, "onFailureTelemetry: " + mGson.toJson(genieResponse));
        }
    }
}
