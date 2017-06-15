//package org.akshara.fragment;
//
//import android.content.Context;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.text.Editable;
//import android.text.TextUtils;
//import android.text.TextWatcher;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.afollestad.materialdialogs.MaterialDialog;
//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;
//
//import org.akshara.BuildConfig;
//import org.akshara.R;
//import org.akshara.Util.TelemetryEventGenertor;
//import org.akshara.Util.Util;
//import org.akshara.activity.MainActivity;
//import org.akshara.adapter.ChildsAdapter;
//import org.akshara.callback.CurrentGetUserResponseHandler;
//import org.akshara.callback.CurrentGetuserResponseHandler;
//import org.akshara.callback.CurrentuserResponseHandler;
//import org.akshara.callback.EndSessionResponseHandler;
//import org.akshara.callback.ICurrentGetUser;
//import org.akshara.callback.ICurrentUser;
//import org.akshara.callback.IEndSession;
//import org.akshara.callback.IPartnerData;
//import org.akshara.callback.IStartSession;
//import org.akshara.callback.ITelemetryData;
//import org.akshara.callback.IUserProfile;
//import org.akshara.callback.PartnerDataResponseHandler;
//import org.akshara.callback.StartSessionResponseHandler;
//import org.akshara.callback.TelemetryResponseHandler;
//import org.akshara.callback.UserProfileCreateResponseHandler;
//import org.akshara.callback.UserProfileResponseHandler;
//import org.akshara.customviews.CustomAutoCompleteView;
//import org.akshara.customviews.MyProgressBar;
//import org.akshara.db.DatabaseHelper;
//import org.akshara.db.StudentDAO;
//import org.akshara.db.StudentInfoDb;
//import org.akshara.model.StudentInfo;
//import org.ekstep.genieresolvers.GenieSDK;
//import org.ekstep.genieresolvers.partner.PartnerService;
//import org.ekstep.genieresolvers.user.UserService;
//import org.ekstep.genieservices.commons.bean.GenieResponse;
//import org.ekstep.genieservices.commons.bean.PartnerData;
//import org.ekstep.genieservices.commons.bean.Profile;
//
//import java.lang.reflect.Type;
//import java.util.HashMap;
//import java.util.Map;
//
//public class ChildListFragment extends Fragment implements IEndSession, IStartSession, IPartnerData,
//        IUserProfile, ICurrentUser, ICurrentGetUser, ITelemetryData {
//    private boolean D = BuildConfig.DEBUG;
//    private String TAG = ChildListFragment.class.getSimpleName();
//    private Context mContext;
//    private DatabaseHelper mDatabaseHandler;
//    private TextView textNoData;
//    private String spinnerDistric_selected, spinnerBlock_selected,
//            spinnerCluster_selected, spinnerSchool_selected,
//            spinnerSchoolCode_selected;
//    private Fragment mFragment;
//    private Button create_profile_btn;
//    private StudentInfo[] studentInfo;
//    private MaterialDialog materialDialog;
//    private ImageView icn_search;
//    // private String handle="";
//    private MyProgressBar progressBar;
//    /*
//    * Change to type CustomAutoCompleteView instead of AutoCompleteTextView
//    * since we are extending to customize the view and disable filter
//    * The same with the XML view, type will be CustomAutoCompleteView
//    */
//    private CustomAutoCompleteView myAutoComplete;
//    private ArrayAdapter<StudentInfo> myAdapter;
//    private Profile mProfile;
//    private UserService mUserService;
//    private UserProfileCreateResponseHandler userProfileResponseHandler;
//    private CurrentGetUserResponseHandler currentGetUserResponseHandler;
//    private CurrentGetuserResponseHandler currentGetuserResponseHandler;
//    private PartnerService mPartnerService;
//    private StartSessionResponseHandler startSessionResponseHandler;
//    private EndSessionResponseHandler endSessionResponseHandler;
//    private PartnerDataResponseHandler partnerDataResponseHandler;
//    private Telemetry telemetry;
//    private TelemetryResponseHandler telemetryResponseHandler;
//    private String UID;
//    private Map<String, Object> hashMap = new HashMap<>();
//    private boolean myAutoCompleteTextChange = false;
//
//    private String mSelectedStudentId;
//
//    /**
//     * Flag value to decide we're creating a new Child or using Existing Child
//     * By default it'll be false.
//     */
//    private boolean mIsCreateNewChild = false;
//
//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//
//        ((MainActivity) mContext).showBackIcon(getString(R.string.childListHeading), mFragment);
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//
//        mContext = getActivity();
//        mFragment = this;
//        mDatabaseHandler = new DatabaseHelper(mContext);
//        super.onCreate(savedInstanceState);
//    }
//
//    /* To show keyboard */
//    private void showKeyboard() {
//        if (getActivity().getCurrentFocus() != null) {
//            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
//
//
//        }
//    }
//
//    /* To hide keyboard */
//    private void hideKeyboard() {
//        if (getActivity().getCurrentFocus() != null) {
//            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
//
//
//        }
//    }
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View rootView = inflater.inflate(R.layout.child_list, container, false);
//        textNoData = (TextView) rootView.findViewById(R.id.textNoData);
//        icn_search = (ImageView) rootView.findViewById(R.id.icn_search);
//        create_profile_btn = (Button) rootView.findViewById(R.id.create_profile_btn);
//        myAutoComplete = (CustomAutoCompleteView) rootView.findViewById(R.id.autoCompleteTextView1);
//        progressBar = (MyProgressBar) rootView.findViewById(R.id.sendingDetails);
//        showKeyboard();
//
//        myAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View arg1, int pos, long id) {
//
//                TextView txtvw = (TextView) arg1;
//                String str = txtvw.getText().toString();
//                myAutoComplete.setText(str);
//                sendPartnerDataToGenie(pos);
//                //showMaterialDialog(getString(R.string.map_genie), "", getString(R.string.map_genie_yes), getString(R.string.map_genie_no), pos);
//                // materialDialog.show();
//
//            }
//
//        });
//
//
//        // add the listener so it will tries to suggest while the user types
//        myAutoComplete.addTextChangedListener(new CustomAutoCompleteTextChangedListener(mContext));
//
//        // ObjectItemData has no value at first
//        StudentInfo[] ObjectItemData = new StudentInfo[0];
//
//
//        // set the custom ArrayAdapter
//        myAdapter = new ChildsAdapter(getActivity(), R.layout.spinner_popup_item, ObjectItemData);
//        myAutoComplete.setAdapter(myAdapter);
//
//
//        create_profile_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                createProfile();
//
//            }
//        });
//
//        return rootView;
//    }
//
//    private void createProfile() {
//
//        TemplateChildFragment templateChildFragment = new TemplateChildFragment();
//        ((MainActivity) mContext).switchContent(templateChildFragment, 1, true);
//
//    }
//
//    private void sendPartnerDataToGenie(int pos) {
//        progressBar.setVisibility(View.VISIBLE);
//
//        // Util util=(Util)mContext.getApplicationContext();
//        hashMap.put(StudentInfoDb.DISTRICT, studentInfo[pos].getDistrict().trim());
//        hashMap.put(StudentInfoDb.BLOCK, studentInfo[pos].getBlock().trim());
//        hashMap.put(StudentInfoDb.CLUST, studentInfo[pos].getClust().trim());
//        hashMap.put(StudentInfoDb.SCHOOL_CODE, studentInfo[pos].getSchool_code().trim());
//        // hashMap.put(StudentInfoDb.dise_code, studentInfo[pos].getDise_code().trim());
//        hashMap.put(StudentInfoDb.SCHOOL_NAME, studentInfo[pos].getSchool_name().trim());
//        hashMap.put(StudentInfoDb._CLASS, studentInfo[pos].get_class().trim());
//        hashMap.put(StudentInfoDb.STUDENT_ID, studentInfo[pos].getStudent_id().trim());
//        hashMap.put(StudentInfoDb.CHILD_NAME, studentInfo[pos].getChild_name().trim());
//        hashMap.put(StudentInfoDb.SEX, studentInfo[pos].getSex().trim());
//        //hashMap.put(StudentInfoDb.mother_tongue, studentInfo[pos].getMother_tongue().trim());
////        hashMap.put(StudentInfoDb.dob, studentInfo[pos].getDob().trim());
//        hashMap.put(StudentInfoDb.FATHER_NAME, studentInfo[pos].getFather_name().trim());
////        hashMap.put(StudentInfoDb.mother_name, studentInfo[pos].getMother_name().trim());
//
//        UID = studentInfo[pos].getUid();
//
//        mSelectedStudentId = studentInfo[pos].getStudent_id().trim();
//
//        if (D)
//            Log.d(TAG, "hashMap---->" + hashMap);
//        //1. End the session
//
//        PartnerData partnerData = new PartnerData(null, null, Util.partnerId, null,
//                Util.publicKey);
//        mPartnerService = GenieSDK.getGenieSDK().getPartnerService();
//        // handle=studentInfo[pos].getChild_name();
//
//        endSessionResponseHandler = new EndSessionResponseHandler(ChildListFragment.this);
//        mPartnerService.endPartnerSession(partnerData, endSessionResponseHandler);
//
//
//    }
//
//    @Override
//    public void onSuccessEndSession(GenieResponse genieResponse) {
//        String result = new Gson().toJson(genieResponse.getResult());
//        if (D)
//            Log.d(TAG, "onSuccessEndSession :" + result);
//       /* String language= mDatabaseHandler.getmotherToungue(spinnerDistric_selected, spinnerBlock_selected, spinnerCluster_selected, spinnerSchool_selected, spinnerSchoolCode_selected);
//        if(language.equalsIgnoreCase("kannada"))
//            language="kn";
//        else if(language.equalsIgnoreCase("hindi"))
//            language="hi";
//        else
//       */
//
//        mUserService = GenieSDK.getGenieSDK().getUserService();
//
//       if (!TextUtils.isEmpty(UID)) {
//           currentGetUserResponseHandler = new CurrentGetUserResponseHandler(this);
//           if (D)
//               Log.d(TAG, "onSuccessEndSession UID :" + UID);
//           mIsCreateNewChild = false;
//           mUserService.setUser(UID, currentGetUserResponseHandler);
//       } else {
//           String language = "en";
//           mIsCreateNewChild = true;
//           //2. Create Profile
//           try {
//               mProfile = new Profile("" + hashMap.get(StudentInfoDb.CHILD_NAME), Util.avatar, language);
//               mProfile.setStandard(Integer.parseInt("" + hashMap.get(StudentInfoDb._CLASS)));
//               mProfile.setGender("" + hashMap.get(StudentInfoDb.SEX));
////            mProfile.setAge(Age.getChildAge(mContext,"" + hashMap.get(StudentInfoDb.dob)));
//               mProfile.setDay(-1);
//               mProfile.setMonth(-1);
//               mProfile.setGroupUser(false);
//               //day -1 month -1  group false
//           } catch (Exception e) {
//               if (D) Log.e(TAG, "Exception in mProfile" + e);
//           }
//
//           //mUserService=new UserProfile(getActivity());
//
//           userProfileResponseHandler = new UserProfileCreateResponseHandler(ChildListFragment.this);
//           mUserService.createUserProfile(mProfile, userProfileResponseHandler);
//       }
//
//
//
//
//    }
//
//    @Override
//    public void onFailureEndSession(GenieResponse genieResponse) {
//        String result = new Gson().toJson(genieResponse.getResult());
//        if (D)
//            Log.d(TAG, "onFailureEndSession :" + result);
//
//    }
//
//    @Override
//    public void onSuccessSession(GenieResponse genieResponse) {
//        String result = new Gson().toJson(genieResponse.getResult());
//        if (D)
//            Log.d(TAG, "onSuccessSession :" + result + "  UID==>" + UID);
//        //6. send mPartnerService data to Genie services
//        partnerDataResponseHandler = new PartnerDataResponseHandler(this);
//       /* Map<String,Object> partnerData=new HashMap<>();
//        partnerData.put(UID, hashMap);
//       */
//        hashMap.put(Util.UID, UID);
//
//        HashMap<String, Object> partnerData = new HashMap<>();
//        partnerData.put(Util.UID, UID);
//        partnerData.put(StudentInfoDb.STUDENT_ID, hashMap.get(StudentInfoDb.STUDENT_ID));
//
//        if (mIsCreateNewChild) {
//            partnerData.putAll(hashMap);
//        }
//
//        StudentDAO.getInstance().updateStudentUID(mSelectedStudentId, UID);
//
//        if (D)
//            Log.d(TAG, "partnerData==>" + partnerData);
//        mPartnerService.sendData(Util.partnerId, partnerData, partnerDataResponseHandler);
//
//    }
//
//    @Override
//    public void onFailureSession(GenieResponse genieResponse) {
//        String result = new Gson().toJson(genieResponse.getResult());
//        if (D)
//            Log.d(TAG, "onFailureSession :" + result);
//
//    }
//
//    @Override
//    public void onSuccessPartner(GenieResponse genieResponse) {
//        String result = new Gson().toJson(genieResponse.getResult());
//        if (D)
//            Log.d(TAG, "onSuccessPartner :" + result);
//        progressBar.setVisibility(View.GONE);
//
//        //7 end telemetry
//        Util util = (Util) mContext.getApplicationContext();
//        telemetry = util.getTelemetry();
//        telemetryResponseHandler = new TelemetryResponseHandler(ChildListFragment.this);
//        telemetry.send(TelemetryEventGenertor.generateOEEndEvent(mContext, util.getStartTime(), System.currentTimeMillis(), UID).toString(), telemetryResponseHandler);
//
//
//    }
//
//    @Override
//    public void onFailurePartner(GenieResponse genieResponse) {
//        String result = new Gson().toJson(genieResponse.getResult());
//        if (D)
//            Log.d(TAG, "onFailurePartner :" + result);
//
//    }
//
//    @Override
//    public void onSuccessUserProfile(GenieResponse genieResponse) {
//        String json = new Gson().toJson(genieResponse.getResult());
//        if (D)
//            Log.d(TAG, "onSuccessUserProfile json :" + json);
//        Type type = new TypeToken<HashMap<String, String>>() {
//        }.getType();
//        HashMap<String, String> hashMap = new Gson().fromJson(json, type);
//
//        //3. setCurrentUser
//        currentGetUserResponseHandler = new CurrentuserResponseHandler(this);
//        UID = hashMap.get("uid");
//        if (D)
//            Log.d(TAG, "onSuccessUserProfile mProfile.getUid() :" + UID);
//        mUserService.setCurrentUser(UID, currentGetUserResponseHandler);
//
//    }
//
//    @Override
//    public void onFailureUserprofile(GenieResponse genieResponse) {
//        String result = new Gson().toJson(genieResponse.getResult());
//        if (D)
//            Log.d(TAG, "onFailureUserprofile :" + result + ", error:" + genieResponse.getError());
//
//    }
//
//    @Override
//    public void onSuccessCurrentUser(GenieResponse genieResponse) {
//        String result = new Gson().toJson(genieResponse.getResult());
//        if (D)
//            Log.d(TAG, "onSuccessCurrentUser :" + result);
//        //4. getCurrentUser
//        currentGetuserResponseHandler = new CurrentGetuserResponseHandler(this);
//        mUserService.getCurrentUser(currentGetuserResponseHandler);
//
//    }
//
//    @Override
//    public void onFailureCurrentUser(GenieResponse genieResponse) {
//        String result = new Gson().toJson(genieResponse.getResult());
//        if (D)
//            Log.d(TAG, "onFailureCurrentUser :" + result);
//
//
//        if (D) {
//            Log.d(TAG, "onFailureCurrentUser :" + result);
//        }
//
//        if (genieResponse.getError() != null
//                && genieResponse.getError().equals("INVALID_USER")) {
//
//
//            if (D) {
//                Log.i(TAG, "onFailureCurrentUser: Invalid UID creating the Child Again");
//            }
//
//            String handle = hashMap.get(StudentInfoDb.CHILD_NAME).toString();
//            String gender = hashMap.get(StudentInfoDb.SEX).toString();
//            Profile profile = new Profile(handle, "Avatar","en");
//
//            if (!TextUtils.isEmpty(gender)) {
//                profile.setGender(gender);
//            }
//
//
//            if (mUserService == null) {
//                Util util = (Util) mContext.getApplicationContext();
//                mUserService = util.getUserProfile();
//            }
//
//            mIsCreateNewChild = true;
//
//            userProfileResponseHandler = new UserProfileCreateResponseHandler(ChildListFragment.this);
//            mUserService.createUserProfile(profile, userProfileResponseHandler);
//        }
//
//    }
//
//    @Override
//    public void onSuccessCurrentGetUser(GenieResponse genieResponse) {
//        String result = new Gson().toJson(genieResponse.getResult());
//        if (D)
//            Log.d(TAG, "onSuccessCurrentGetUser :" + result);
//        //5. start session
//        //mPartnerService=new Partner(getActivity());
//        startSessionResponseHandler = new StartSessionResponseHandler(this);
//        mPartnerService.startPartnerSession(Util.partnerId, startSessionResponseHandler);
//    }
//
//    @Override
//    public void onFailureCurrentGetUser(GenieResponse genieResponse) {
//        String result = new Gson().toJson(genieResponse.getResult());
//        if (D)
//            Log.d(TAG, "onFailureCurrentGetUser :" + result);
//
//    }
//
//
//
//    public void onSuccessTelemetry(GenieResponse genieResponse) {
//        if (D)
//            Log.d(TAG, "-------------------------------------------------------------------onSuccessTelemetry");
//
//        Util.processSuccess(mContext, genieResponse);
//        //8 exit the app
//        ((MainActivity) mContext).exitApp();
//
//    }
//
//    @Override
//    public void onFailureTelemetry(GenieResponse genieResponse) {
//        if (D)
//            Log.d(TAG, "-------------------------------------------------------------------onFailureTelemetry");
//
//        Util.processSendFailure(mContext, genieResponse);
//    }
//
//    private class CustomAutoCompleteTextChangedListener implements TextWatcher {
//
//        private final String TAG = CustomAutoCompleteTextChangedListener.class.getSimpleName();
//        Context context;
//
//        public CustomAutoCompleteTextChangedListener(Context context) {
//            this.context = context;
//        }
//
//        @Override
//        public void afterTextChanged(Editable s) {
//        }
//
//        @Override
//        public void beforeTextChanged(CharSequence s, int start, int count,
//                                      int after) {
//        }
//
//        @Override
//        public void onTextChanged(CharSequence userInput, int start, int before, int count) {
//            //don't call onTextChanged while setOnItemClicked
//            if (myAutoComplete.isPerformingCompletion()) {
//                if (D) Log.d(TAG, "onTextChanged-- isPerformingCompletion----------------");
//                myAutoCompleteTextChange = true;
//                // An item has been selected from the list. Ignore.
//                return;
//            } else if (!myAutoCompleteTextChange) {
//                try {
//                    icn_search.setVisibility(View.GONE);
//
//                    // get suggestions from the database
//                    spinnerDistric_selected = getArguments().getString("spinnerDistric_selected");
//                    spinnerBlock_selected = getArguments().getString("spinnerBlock_selected");
//                    spinnerCluster_selected = getArguments().getString("spinnerCluster_selected");
//                    spinnerSchool_selected = getArguments().getString("spinnerSchool_selected");
//                    spinnerSchoolCode_selected = getArguments().getString("spinnerSchoolCode_selected");
//                    studentInfo = StudentDAO.getInstance().getAllStudentInfoObject(
//                            spinnerSchoolCode_selected, myAutoComplete.getText().toString().toUpperCase());
//
//                    // update the adapter
//                    myAdapter = new ChildsAdapter(getActivity(), R.layout.spinner_popup_item, studentInfo);
//                    myAutoComplete.setAdapter(myAdapter);
//                    // update the adapater
//                    myAdapter.notifyDataSetChanged();
//
//                    if (studentInfo.length == 0) {
//                        icn_search.setVisibility(View.VISIBLE);
//                        textNoData.setVisibility(View.VISIBLE);
//                        create_profile_btn.setVisibility(View.VISIBLE);
//                        hideKeyboard();
//                    } else {
//
//
//                        textNoData.setVisibility(View.GONE);
//                        create_profile_btn.setVisibility(View.GONE);
//                        // showKeyboard();
//                    }
//
//
//                } catch (NullPointerException e) {
//                    e.printStackTrace();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            }
//
//
//        }
//
//
//    }
//
//
//}
