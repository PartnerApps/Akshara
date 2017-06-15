package org.akshara.fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedHashTreeMap;
import com.google.gson.reflect.TypeToken;

import org.akshara.BuildConfig;
import org.akshara.R;
import org.akshara.Util.TelemetryEventGenertor;
import org.akshara.Util.Util;
import org.akshara.activity.MainActivity;
import org.akshara.callback.CurrentGetUserResponseHandler;
import org.akshara.callback.CurrentUserResponseHandler;
import org.akshara.callback.EndSessionResponseHandler;
import org.akshara.callback.ICurrentGetUser;
import org.akshara.callback.ICurrentUser;
import org.akshara.callback.IEndSession;
import org.akshara.callback.IPartnerData;
import org.akshara.callback.ITelemetryData;
import org.akshara.callback.IUserCreateProfile;
import org.akshara.callback.PartnerDataResponseHandler;
import org.akshara.callback.TelemetryResponseHandler;
import org.akshara.callback.UserProfileCreateResponseHandler;
import org.akshara.customviews.CustomEditText;
import org.akshara.customviews.CustomTextView;
import org.akshara.customviews.MyProgressBar;
import org.akshara.db.StudentDAO;
import org.akshara.model.Age;
import org.akshara.model.UserModel;
import org.ekstep.genieresolvers.GenieSDK;
import org.ekstep.genieresolvers.partner.PartnerService;
import org.ekstep.genieresolvers.telemetry.TelemetryService;
import org.ekstep.genieresolvers.user.UserService;
import org.ekstep.genieservices.commons.bean.GenieResponse;
import org.ekstep.genieservices.commons.bean.PartnerData;
import org.ekstep.genieservices.commons.bean.Profile;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Dhruv on 10/7/2015.
 */
public class DisplayChildProfileFragment extends Fragment
        implements IEndSession, IUserCreateProfile, ICurrentUser, ICurrentGetUser,
        IPartnerData, ITelemetryData {

    private static final String TAG = "DisplayChildProfileFrag";
    private static final boolean DEBUG = BuildConfig.DEBUG;

    LayoutInflater layoutInflater;
    int editRow = 0;
    boolean flagFocus1stEditText = false;
    int row = 0;
    private boolean D = Util.DEBUG;
    private Context mContext;
    private Fragment mFragment;
    private Button Register_btn;
    private Spinner spinnerLanguage, spinnerGender, spinnerClass;
    private LinearLayout displayLayout, akshara_templateLayout;
    private TextView child_nameLabel, father_nameLabel, child_classLabel, genderLabel, dobLabel, languageLabel;
    private EditText child_name, father_name;
    private UserModel userModel = new UserModel();
    private HashMap<Integer, String> hashMap_order = new HashMap<>();
    private Map<String, Object> hashMapData = new HashMap<>();
    private CustomTextView[] textViews = new CustomTextView[60];
    private CustomEditText[] editTexts = new CustomEditText[60];
    private Spinner[] spinners = new Spinner[60];
    private int sizeOfMap = 0;
    private ArrayList<String> language_List = new ArrayList<>();
    private String[] gender_array = {"MALE", "FEMALE"};
    private String[] child_classarray = {"4", "5"};
    private HashMap<String, String> code_language_List = new HashMap<>();
    private List<HashMap<String, String>> hashMapLanguage;
    private DatePickerDialog mDatePickerDialog = null;
    private int year;
    private int month;
    private int day;
    private String mSelectedDob = "";
    private TextView mTxt_dob = null;
    private View rootView;
    private GradientDrawable gradientDrawable;
    private String UID;
    private String code;
    private String handle = "";
    private MyProgressBar progressBar;
    private ScrollView scrollViewChildContainer;
    private CheckBox[] checkBoxes = new CheckBox[60];
    private RadioGroup[] radioGroups = new RadioGroup[120];
    private RadioButton[] radioButtons = new RadioButton[60];
    private String selectedRB = "";
    private boolean ischkRB = false;
    private DatePickerDialog.OnDateSetListener mDatePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker dp, int selectedYear, int monthOfYear,
                              int dayOfMonth) {
            year = selectedYear;
            month = monthOfYear;
            day = dayOfMonth;
            String strDay;
            String strMonth;
            if (day < 10) {
                strDay = "0" + day;
            } else {
                strDay = "" + day;
            }

            if ((month + 1) < 10) {
                strMonth = "0" + (month + 1);
            } else {
                strMonth = "" + (month + 1);
            }

            //String date=mon +"-"+ strDay + "-" +year;

            //String date=year +"-"+ mon + "-" +strDay;
            String date = year + "-" + strMonth + "-" + strDay;
            int requiredAge = Age.getChildAge(mContext, date);

            if (requiredAge != 0) {
                mSelectedDob = date;
                mTxt_dob.setText(date);
                mTxt_dob.setTextColor(getResources().getColor(android.R.color.black));

            }


        }
    };


    private PartnerService mPartnerService;

    private UserService mUserService;

    private Gson mGson = new Gson();

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // ((MainActivity)mContext).showBackIcon("Child Registration",mFragment);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mContext = getActivity();
        mFragment = this;
        super.onCreate(savedInstanceState);
    }

    private void getLanguage() {
        Util util = (Util) mContext.getApplicationContext();
        String language = util.getLanguage();
        if (D)
            Log.d(TAG, "getLanguage language :" + language);
        if (language != null) {
            Type type = new TypeToken<List<HashMap<String, String>>>() {
            }.getType();
            hashMapLanguage = new Gson().fromJson(language, type);
            for (int i = 0; i < hashMapLanguage.size(); i++) {
                language_List.add(hashMapLanguage.get(i).get("name"));
                code_language_List.put(hashMapLanguage.get(i).get("name"), hashMapLanguage.get(i).get("code"));
            }

        } else {
            Toast.makeText(mContext, getString(R.string.lang_not), Toast.LENGTH_LONG).show();
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getLanguage();

        layoutInflater = getLayoutInflater(savedInstanceState);
        rootView = inflater.inflate(R.layout.display_child_profile, container, false);
        Register_btn = (Button) rootView.findViewById(R.id.Register_btn);
        displayLayout = (LinearLayout) rootView.findViewById(R.id.displayLayout);
        akshara_templateLayout = (LinearLayout) rootView.findViewById(R.id.akshara_templateLayout);
        progressBar = (MyProgressBar) rootView.findViewById(R.id.sendingDetails);
        scrollViewChildContainer = (ScrollView) rootView.findViewById(R.id.scrollViewChildContainer);
        if (getArguments() != null) {
            akshara_templateLayout.setVisibility(View.GONE);
            String json = getArguments().getString(Util.USERMODEL_DATA);
            String TEMPLATE_NAME = getArguments().getString(Util.TEMPLATE_NAME);
            ((MainActivity) mContext).showBackIcon(TEMPLATE_NAME, mFragment);
            // Now convert the JSON string back to your java object
            Type type = new TypeToken<UserModel>() {
            }.getType();
            userModel = new Gson().fromJson(json, type);

            if (D)
                Log.d(TAG, "userModel:" + userModel);

            hashMap_order = userModel.getLabel_order();

            if (D)
                Log.d(TAG, "hashMap_order>" + hashMap_order);
            sizeOfMap = hashMap_order.size();

            if (sizeOfMap != 0)
                createChildForm(rootView);

        } else {
            ((MainActivity) mContext).showBackIcon(Util.TEMPLATE_NAME, mFragment);

            displayAksharaTemplate(rootView);
        }

        Register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getArguments() != null) {
                    boolean flag = true, noEmptyField = true;
                    int checkEditFieldBlank = 0, noOfEditText = 0;
                    for (int i = 0; i < sizeOfMap; i++) {
                        String Key_value = userModel.getLabel_order().get("" + (i + 1)).toString();
                        if (D)
                            Log.d(TAG, "Key_value" + Key_value);
                        String Key_type = userModel.getLabel_single_data().get(Key_value).toString();
                        if (D)
                            Log.d(TAG, "Key_type" + Key_type);
                        if (Key_type.equals("text")) { // label Value
                            checkEditFieldBlank++;
                            //Validation of field
                            if (editTexts[i].getText().toString().trim().isEmpty()) {
                                Util.showToastmessage(mContext, "Please enter " + Key_value);
                                checkEditFieldBlank = 0;
                                noEmptyField = false;
                                break;
                            } else {
                                if (flag) {
                                    handle = editTexts[i].getText().toString();
                                    flag = false;
                                }
                            }


                            hashMapData.put(textViews[i].getText().toString(), editTexts[i].getText().toString());
                        } else if (Key_type.equals("comment")) {
                            if (editTexts[i].getText().toString().trim().isEmpty()) {
                                Util.showToastmessage(mContext, "Please enter " + Key_value);
                                noEmptyField = false;
                                break;
                            }
                            hashMapData.put(textViews[i].getText().toString(), editTexts[i].getText().toString());

                        } else if (Key_type.equals("Multi-select(checkboxes)")) {
                            HashMap<String, ArrayList<String>> dropdownMap = userModel.getLabel_multiple_data();
                            ArrayList<String> Optionlist = dropdownMap.get(Key_value);
                            ArrayList<String> OptionSelected = new ArrayList<String>();

                            boolean ischkBox = false;
                            for (int j = 0; j < Optionlist.size(); j++) {
                                if (checkBoxes[j].isChecked()) {
                                    ischkBox = true;
                                    OptionSelected.add(checkBoxes[j].getText().toString());

                                }
                            }
                            if (!ischkBox) {
                                Util.showToastmessage(mContext, "Please select at least one " + Key_value);
                                noEmptyField = false;
                                break;
                            }
                            hashMapData.put(textViews[i].getText().toString(), OptionSelected);


                        } else if (Key_type.equals("Single-select(radio-buttons)")) {
                            HashMap<String, ArrayList<String>> dropdownMap = userModel.getLabel_multiple_data();
                            ArrayList<String> Optionlist = dropdownMap.get(Key_value);
                            radioGroups[i].setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                                    // Toast.makeText(mContext,"i=>"+i,Toast.LENGTH_LONG).show();
                                    RadioButton rb = (RadioButton) radioGroup.findViewById(i);
                                    if (null != rb && i > -1) {
                                        selectedRB = rb.getText().toString();
                                        //ischkRB = true;
                                        if (D)
                                            Toast.makeText(mContext, ischkRB + "->ischkRB...>" + rb.getText(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                            hashMapData.put(textViews[i].getText().toString(), selectedRB);


                        } else if (Key_type.equals("dropdown")) { //dropdown value
                            hashMapData.put(textViews[i].getText().toString(), spinners[i].getSelectedItem().toString());
                        }

                    }

                    if (noEmptyField) {
                        if (checkEditFieldBlank == 0) {
                            handle = "ChildName_BLANK";
                        }
                        code = code_language_List.get(spinners[sizeOfMap].getSelectedItem().toString());
                        registerChild();
                    }
                } else {
                    String childName = child_name.getText().toString();
                    String fatheName = father_name.getText().toString();
                    if (D)
                        Log.d(TAG, "childName :" + childName);
                    if (D)
                        Log.d(TAG, "fatheName :" + fatheName);


                    Pattern pattern1 = Pattern.compile("[a-zA-Z ]+");
                    Matcher matcher1 = pattern1.matcher(childName);
                    Boolean childNamepattern = matcher1.matches();

                    Pattern pattern2 = Pattern.compile("[a-zA-Z ]+");
                    Matcher matcher2 = pattern2.matcher(fatheName);
                    Boolean fatheNamepattern = matcher2.matches();


                    if (childName.trim().isEmpty()) {
                        Util.showToastmessage(mContext, "Please enter " + child_nameLabel.getText().toString());
                    } else if (!childNamepattern) {
                        Util.showToastmessage(mContext, "Please enter valid " + child_nameLabel.getText().toString());
                    } else if (childName.length() < 3) {
                        Util.showToastmessage(mContext, child_nameLabel.getText().toString() + " can't be less than 3 character.");
                    } else if (childName.length() > 50) {
                        Util.showToastmessage(mContext, "Please enter " + child_nameLabel.getText().toString() + " less than 50 character.");
                    } else if (fatheName.isEmpty()) {
                        Util.showToastmessage(mContext, "Please enter " + father_nameLabel.getText().toString());
                    } else if (!fatheNamepattern) {
                        Util.showToastmessage(mContext, "Please enter valid " + father_nameLabel.getText().toString());
                    } else if (fatheName.length() < 3) {
                        Util.showToastmessage(mContext, father_nameLabel.getText().toString() + " can't be less than 3 character.");
                    } else if (fatheName.length() > 50) {
                        Util.showToastmessage(mContext, "Please enter " + father_nameLabel.getText().toString() + " less than 50 character.");
                    } else if (mTxt_dob.getText().toString().isEmpty()) {
                        Util.showToastmessage(mContext, "Please select " + dobLabel.getText().toString());
                    } else {
                        //send Data to Genie services
                        handle = child_name.getText().toString();
                        hashMapData.put(StudentDAO.COLUMN_CHILD_NAME, child_name.getText().toString().trim());
                        hashMapData.put(StudentDAO.COLUMN_FATHER_NAME, father_name.getText().toString().trim());
                        hashMapData.put(StudentDAO.COLUMN_CLASS, spinnerClass.getSelectedItem().toString().trim());
                        hashMapData.put(StudentDAO.COLUMN_SEX, spinnerGender.getSelectedItem().toString().toLowerCase().trim());
//                        hashMapData.put(StudentInfoDb.dob, mTxt_dob.getText().toString().trim());
                        code = code_language_List.get(spinnerLanguage.getSelectedItem().toString());
                        registerChild();

                    }
                }
                //


            }
        });
        return rootView;
    }

    private void registerChild() {
        progressBar.setVisibility(View.VISIBLE);
        scrollViewChildContainer.setVisibility(View.GONE);

        Util util = (Util) mContext.getApplicationContext();
        hashMapData.put(StudentDAO.COLUMN_DISTRICT, util.getSpinnerDistric_selected().trim());
        hashMapData.put(StudentDAO.COLUMN_BLOCK, util.getSpinnerBlock_selected().trim());
        hashMapData.put(StudentDAO.COLUMN_CLUSTER, util.getSpinnerCluster_selected().trim());
        hashMapData.put(StudentDAO.COLUMN_SCHOOL_NAME, util.getSpinnerSchool_selected().trim());
        hashMapData.put(StudentDAO.COLUMN_SCHOOL_CODE, util.getSpinnerSchoolCode_selected().toLowerCase().trim());


        // hashMapData.put(StudentInfoDb.mother_tongue,code.trim());
        //1. End the session
        mPartnerService = GenieSDK.getGenieSDK().getPartnerService();
        PartnerData partnerData = new PartnerData(null, null, Util.partnerId, null, Util.publicKey);
        EndSessionResponseHandler endSessionResponseHandler = new EndSessionResponseHandler(this);
        mPartnerService.endPartnerSession(partnerData, endSessionResponseHandler);


        if (D)
            Log.d(TAG, "hashMapData------>" + hashMapData);
    }

    private void displayAksharaTemplate(View rootView) {
        akshara_templateLayout.setVisibility(View.VISIBLE);
        spinnerClass = (Spinner) rootView.findViewById(R.id.spinnerClass);
        spinnerGender = (Spinner) rootView.findViewById(R.id.spinnerGender);
        spinnerLanguage = (Spinner) rootView.findViewById(R.id.spinnerLanguage);
        child_nameLabel = (TextView) rootView.findViewById(R.id.child_nameLabel);
        father_nameLabel = (TextView) rootView.findViewById(R.id.father_nameLabel);
        child_classLabel = (TextView) rootView.findViewById(R.id.child_classLabel);
        genderLabel = (TextView) rootView.findViewById(R.id.genderLabel);
        dobLabel = (TextView) rootView.findViewById(R.id.dobLabel);
        languageLabel = (TextView) rootView.findViewById(R.id.languageLabel);
        child_name = (EditText) rootView.findViewById(R.id.child_name);
        father_name = (EditText) rootView.findViewById(R.id.father_name);

        ArrayAdapter<String> adapterclass = new ArrayAdapter<String>(mContext, R.layout.spinner_item, child_classarray);
        spinnerClass.setAdapter(adapterclass);
        adapterclass.setDropDownViewResource(R.layout.spinner_popup_item);
        spinnerClass.setAdapter(adapterclass);

        ArrayAdapter<String> adapterGender = new ArrayAdapter<String>(mContext, R.layout.spinner_item, gender_array);
        spinnerGender.setAdapter(adapterGender);
        adapterGender.setDropDownViewResource(R.layout.spinner_popup_item);
        spinnerGender.setAdapter(adapterGender);

        ArrayAdapter<String> adapterLanguage = new ArrayAdapter<String>(mContext, R.layout.spinner_item, language_List);
        spinnerLanguage.setAdapter(adapterLanguage);
        adapterLanguage.setDropDownViewResource(R.layout.spinner_popup_item);
        spinnerLanguage.setAdapter(adapterLanguage);
        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
//         mTxt_dob.setText(day+""+"-"+(month+1)+"-"+year);

        mDatePickerDialog = new DatePickerDialog(getActivity(), mDatePickerListener, year, month, day);


        mTxt_dob = ((TextView) rootView.findViewById(R.id.txt_dob));
        mTxt_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatePickerDialog.show();
                // Toast.makeText(mContext,"mTxt_dob"+mTxt_dob.getText(),Toast.LENGTH_LONG).show();
            }
        });

    }

    private void createChildForm(View rootView) {
        try {


            if (D)
                Log.d(TAG, "size :" + sizeOfMap);
            //Layout inflater
            View view;
            for (int i = 0; i < sizeOfMap; i++) {
                String Key_value = userModel.getLabel_order().get("" + (i + 1)).toString();
                if (D)
                    Log.d(TAG, "Key_value" + Key_value);
                String Key_type = userModel.getLabel_single_data().get(Key_value).toString();
                if (D)
                    Log.d(TAG, "Key_type" + Key_type);
                //Heading as Label
                view = layoutInflater.inflate(R.layout.textview_template, displayLayout, false);
                textViews[i] = (CustomTextView) view.findViewById(R.id.textTemplate);
                textViews[i].setText(Key_value);
                displayLayout.addView(textViews[i]);
                if (Key_type.equals("text")) { // label Value
                    view = layoutInflater.inflate(R.layout.edit_text_template, displayLayout, false);
                    editTexts[i] = (CustomEditText) view.findViewById(R.id.editTextTemplate);
                    editTexts[i].setHint("Please enter your " + Key_value);
                    displayLayout.addView(editTexts[i]);
                    editRow = i;
                    if (!flagFocus1stEditText) {
                        flagFocus1stEditText = true;
                        editTexts[i].post(new Runnable() {
                            public void run() {
                                editTexts[editRow].requestFocusFromTouch();
                                InputMethodManager lManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                lManager.showSoftInput(editTexts[editRow], 0);
                            }
                        });
                    }

                    editTexts[i].addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if (D)
                                Log.d(TAG, "onTextChanged");
                            editTexts[editRow].setImeOptions(EditorInfo.IME_ACTION_NEXT);
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });


                } else if (Key_type.equals("comment")) { // label Value type="comment";
                    view = layoutInflater.inflate(R.layout.comment_template, displayLayout, false);
                    editTexts[i] = (CustomEditText) view.findViewById(R.id.commentTemplate);
                    editTexts[i].setHint("Please enter your " + Key_value);
                    displayLayout.addView(editTexts[i]);

                } else if (Key_type.equals("Multi-select(checkboxes)")) { //dropdown value MultipleChoice {"Single-select(radio-buttons)","Multi-select(checkboxes)"}
                    HashMap<String, ArrayList<String>> dropdownMap = userModel.getLabel_multiple_data();
                    ArrayList<String> Optionlist = dropdownMap.get(Key_value);
                    for (int j = 0; j < Optionlist.size(); j++) {
                        view = layoutInflater.inflate(R.layout.checkbox_text_template, displayLayout, false);
                        checkBoxes[j] = (CheckBox) view.findViewById(R.id.checkBoxTemplate);
                        checkBoxes[j].setText(Optionlist.get(j));
                        displayLayout.addView(checkBoxes[j]);

                    }


                } else if (Key_type.equals("Single-select(radio-buttons)")) {
                    HashMap<String, ArrayList<String>> dropdownMap = userModel.getLabel_multiple_data();
                    ArrayList<String> Optionlist = dropdownMap.get(Key_value);
                    view = layoutInflater.inflate(R.layout.radiogroup_template, displayLayout, false);
                    radioGroups[i] = (RadioGroup) view.findViewById(R.id.radioGroupTemplate);


                    for (int j = 0; j < Optionlist.size(); j++) {
                        view = layoutInflater.inflate(R.layout.radio_text_template, displayLayout, false);
                        radioButtons[j] = (RadioButton) view.findViewById(R.id.radioTemplate);
                        radioButtons[j].setText(Optionlist.get(j));
                        radioButtons[j].setId(j);
                        if (j == 0)
                            radioButtons[j].setChecked(true);
                        radioGroups[i].addView(radioButtons[j]);

                    }

                    displayLayout.addView(radioGroups[i]);


                } else if (Key_type.equals("dropdown")) { //dropdown value
                    view = layoutInflater.inflate(R.layout.spinner_template, displayLayout, false);
                    spinners[i] = (Spinner) view.findViewById(R.id.spinnerTemplate);
                    HashMap<String, ArrayList<String>> dropdownMap = userModel.getLabel_multiple_data();
                    ArrayList<String> dropdownlist = dropdownMap.get(Key_value);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, R.layout.spinner_item, dropdownlist);
                    spinners[i].setAdapter(adapter);
                    adapter.setDropDownViewResource(R.layout.spinner_popup_item);
                    spinners[i].setAdapter(adapter);
                    displayLayout.addView(spinners[i]);

                }

            }

            //Adding Language
            if (language_List.size() != 0) {
                //Heading as Label
                view = layoutInflater.inflate(R.layout.textview_template, displayLayout, false);
                if (D)
                    Log.d(TAG, "view" + view);

                textViews[sizeOfMap] = (CustomTextView) view.findViewById(R.id.textTemplate);
                textViews[sizeOfMap].setText("Language");
                displayLayout.addView(textViews[sizeOfMap]);

                view = layoutInflater.inflate(R.layout.spinner_template, displayLayout, false);
                spinners[sizeOfMap] = (Spinner) view.findViewById(R.id.spinnerTemplate);
                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(mContext, R.layout.spinner_item, language_List);
                spinners[sizeOfMap].setAdapter(adapter1);
                adapter1.setDropDownViewResource(R.layout.spinner_popup_item);
                spinners[sizeOfMap].setAdapter(adapter1);
                displayLayout.addView(spinners[sizeOfMap]);
            }


        } catch (Exception e) {
            if (D)
                Log.e(TAG, "createChildForm Ex :" + e);

        }

    }

    private void createDropDown(View rootView) {
        LinearLayout.LayoutParams layoutMargin = new LinearLayout.LayoutParams
                (new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        layoutMargin.setMargins(0, 20, 0, 20);
        createRow(rootView);


    }

    private void createRow(final View rootView) {
        editTexts[row] = new CustomEditText(rootView.getContext());
        editTexts[row].setTextAppearance(rootView.getContext(), R.style.EditText_style);
        editTexts[row].setHint("Option" + (row + 1));
        editTexts[row].setTypeface(Typeface.create("", R.style.EditText_style));

        displayLayout.addView(editTexts[row]);
        editTexts[row].addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 1) {
                    row++;
                    createRow(rootView);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    @Override
    public void onSuccessEndSession(GenieResponse genieResponse) {
        if (DEBUG) {
            Log.i(TAG, "onSuccessEndSession: " + mGson.toJson(genieResponse));
        }

        Profile profile = new Profile(handle, Util.avatar, code);
        profile.setGender(hashMapData.get(StudentDAO.COLUMN_SEX).toString());

        try {
            profile.setStandard(
                    Integer.parseInt(hashMapData.get(StudentDAO.COLUMN_CLASS).toString()));
        } catch (Exception ex) {
            if (DEBUG) {
                Log.e(TAG, "onSuccessEndSession: ", ex);
            }
        }

        UserProfileCreateResponseHandler userProfileCreateResponseHandler
                = new UserProfileCreateResponseHandler(this);

        mUserService = GenieSDK.getGenieSDK().getUserService();

        mUserService.createUserProfile(profile, userProfileCreateResponseHandler);
    }

    @Override
    public void onFailureEndSession(GenieResponse genieResponse) {
        if (DEBUG) {
            Log.i(TAG, "onFailureEndSession: " + mGson.toJson(genieResponse));
        }
    }

    @Override
    public void onSuccessUserProfile(GenieResponse<Map> genieResponse) {
        Gson gson = new Gson();
        String responseData = gson.toJson(genieResponse);

        if (DEBUG) {
            Log.d(TAG, "onSuccessUserProfile: " + responseData);
        }

        Type type = new TypeToken<LinkedHashTreeMap<String, String>>(){}.getType();
        Map<String, String> data = gson.fromJson(responseData, type);

        String uid = data.get("result");
        hashMapData.put(StudentDAO.COLUMN_UID, uid);

        UID = hashMapData.get("uid").toString();


        HashMap<String, Object> studentInfo = new HashMap<>();
        studentInfo.putAll(hashMapData);
        studentInfo.put(StudentDAO.COLUMN_UID, UID);
        studentInfo.put(StudentDAO.COLUMN_SYNC, 1);

        StudentDAO.getInstance().addNewStudent(studentInfo);


        // TODO: 13/6/17 Later We can enable this
//        if (getActivity() != null) {
//            Intent intent = new Intent(getActivity(), WritePartnerDataInFile.class);
//            getActivity().startService(intent);
//        }

        CurrentUserResponseHandler currentUserResponseHandler = new CurrentUserResponseHandler(this);

        mUserService.setUser(UID, currentUserResponseHandler);

    }

    @Override
    public void onFailureUserProfile(GenieResponse<Map> genieResponse) {
        if (DEBUG) {
            Log.i(TAG, "onFailureUserProfile: " + mGson.toJson(genieResponse));
        }
    }

    @Override
    public void onSuccessCurrentUser(GenieResponse genieResponse) {
        if (DEBUG) {
            Log.i(TAG, "onSuccessCurrentUser: " + mGson.toJson(genieResponse));
        }

        CurrentGetUserResponseHandler currentGetUserResponseHandler
                = new CurrentGetUserResponseHandler(this);

        mUserService.getCurrentUser(currentGetUserResponseHandler);
    }

    @Override
    public void onFailureCurrentUser(GenieResponse genieResponse) {
        if (DEBUG) {
            Log.i(TAG, "onFailureCurrentUser: " + mGson.toJson(genieResponse));
        }
    }

    @Override
    public void onSuccessCurrentGetUser(GenieResponse genieResponse) {
        if (DEBUG) {
            Log.i(TAG, "onSuccessCurrentGetUser: " + mGson.toJson(genieResponse));
        }

        hashMapData.put(StudentDAO.COLUMN_UID, UID);

        JSONObject partnerJSONObject = new JSONObject(hashMapData);

        PartnerDataResponseHandler responseHandler = new PartnerDataResponseHandler(this);

        PartnerData partnerData = new PartnerData(null, null, Util.partnerId,
                partnerJSONObject.toString(), Util.publicKey);

        mPartnerService.sendPartnerData(partnerData, responseHandler);
    }

    @Override
    public void onFailureCurrentGetUser(GenieResponse genieResponse) {
        if (DEBUG) {
            Log.i(TAG, "onFailureCurrentGetUser: " + mGson.toJson(genieResponse));
        }
    }

    @Override
    public void onSuccessPartner(GenieResponse genieResponse) {
        if (DEBUG) {
            Log.i(TAG, "onSuccessPartner: " + mGson.toJson(genieResponse));
        }

        Util util = (Util) getActivity().getApplicationContext();

        TelemetryResponseHandler telemetryResponseHandler = new TelemetryResponseHandler(this);

        String telemetryJSONData = TelemetryEventGenertor.generateOEEndEvent(getActivity(),
                util.getStartTime(), System.currentTimeMillis(), UID).toString();

        TelemetryService telemetryService = GenieSDK.getGenieSDK().getTelemetryService();

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

        Util.processSuccess(mContext, genieResponse);
        //8 exit the app
        ((MainActivity) mContext).exitApp();
    }

    @Override
    public void onFailureTelemetry(GenieResponse genieResponse) {
        if (DEBUG) {
            Log.i(TAG, "onFailureTelemetry: " + mGson.toJson(genieResponse));
        }
    }

    //    @Override
//    public void onSuccessEndSession(GenieResponse genieResponse) {
//        String result = new Gson().toJson(genieResponse.getResult());
//        if (D)
//            Log.d(TAG, "onSuccessEndSession :" + result);
//        //2. Create Profile
//        profile = new Profile(handle, Util.avatar, code);
//        try {
//            profile.setStandard(Integer.parseInt("" + hashMapData.get(StudentDAO.COLUMN_CLASS)));
//            profile.setGender("" + hashMapData.get(StudentDAO.COLUMN_SEX));
////            profile.setAge(Age.getChildAge(mContext,"" + hashMapData.get(StudentInfoDb.dob)));
//        } catch (Exception e) {
//            if (D) Log.e(TAG, "Exception in profile while setting dynamic form" + e);
//        }
//        //userProfile=new UserProfile(getActivity());
//        Util util = (Util) mContext.getApplicationContext();
//        userProfile = util.getUserProfile();
//        userProfileResponseHandler = new UserProfileResponseHandler(DisplayChildProfileFragment.this);
//        userProfile.createUserProfile(profile, userProfileResponseHandler);
//
//
//    }
//
//    @Override
//    public void onFailureEndSession(GenieResponse genieResponse) {
//        String result = new Gson().toJson(genieResponse.getResult());
//        if (D)
//            Log.d(TAG, "onFailureEndSession :" + result);
//        progressBar.setVisibility(View.GONE);
//        scrollViewChildContainer.setVisibility(View.VISIBLE);
//
//    }
//
//    @Override
//    public void onSuccessSession(GenieResponse genieResponse) {
//        String result = new Gson().toJson(genieResponse.getResult());
//        if (D)
//            Log.d(TAG, "onSuccessSession :" + result + "  UID==>" + UID);
//        //6. send partner data to Genie services
//        partnerDataResponseHandler = new PartnerDataResponseHandler(this);
//       /* Map<String,Object> partnerData=new HashMap<>();
//        partnerData.put(UID,hashMapData);
//       */
//        hashMapData.put(Util.UID, UID);
//        if (D)
//            Log.d(TAG, "partnerData==>" + hashMapData);
//
//        partner.sendData(Util.partnerId, hashMapData, partnerDataResponseHandler);
//
//    }
//
//    @Override
//    public void onFailureSession(GenieResponse genieResponse) {
//        String result = new Gson().toJson(genieResponse.getResult());
//        if (D)
//            Log.d(TAG, "onFailureSession :" + result);
//        progressBar.setVisibility(View.GONE);
//        scrollViewChildContainer.setVisibility(View.VISIBLE);
//
//    }
//
//    @Override
//    public void onSuccessPartner(GenieResponse genieResponse) {
//        String result = new Gson().toJson(genieResponse.getResult());
//        if (D)
//            Log.d(TAG, "onSuccessPartner :" + result);
//        progressBar.setVisibility(View.GONE);
//        //7 end telemetry
//        Util util = (Util) mContext.getApplicationContext();
//        telemetry = util.getTelemetry();
//        telemetryResponseHandler = new TelemetryResponseHandler(DisplayChildProfileFragment.this);
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
//        progressBar.setVisibility(View.GONE);
//        scrollViewChildContainer.setVisibility(View.VISIBLE);
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
//        currentuserSetResponseHandler = new CurrentuserResponseHandler(this);
//        UID = hashMap.get("uid");
//
//
//        HashMap<String, Object> studentInfo = new HashMap<>();
//        studentInfo.putAll(hashMapData);
//        studentInfo.put(StudentDAO.COLUMN_UID, UID);
//        studentInfo.put(StudentDAO.COLUMN_SYNC, 1);
//
//        StudentDAO.getInstance().addNewStudent(studentInfo);
//
//
//        if (getActivity() != null) {
//            Intent intent = new Intent(getActivity(), WritePartnerDataInFile.class);
//            getActivity().startService(intent);
//        }
//
//        if (D)
//            Log.d(TAG, "onSuccessUserProfile profile.getUid() :" + UID);
//        userProfile.setCurrentUser(UID, currentuserSetResponseHandler);
//
//    }
//
//    @Override
//    public void onFailureUserprofile(GenieResponse genieResponse) {
//        String result = new Gson().toJson(genieResponse.getResult());
//        if (D)
//            Log.d(TAG, "onFailureUserprofile :" + result);
//        progressBar.setVisibility(View.GONE);
//        scrollViewChildContainer.setVisibility(View.VISIBLE);
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
//        userProfile.getCurrentUser(currentGetuserResponseHandler);
//
//    }
//
//    @Override
//    public void onFailureCurrentUser(GenieResponse genieResponse) {
//        String result = new Gson().toJson(genieResponse.getResult());
//        if (D)
//            Log.d(TAG, "onFailureCurrentUser :" + result);
//        progressBar.setVisibility(View.GONE);
//        scrollViewChildContainer.setVisibility(View.VISIBLE);
//
//    }
//
//    @Override
//    public void onSuccessCurrentGetUser(GenieResponse genieResponse) {
//        String result = new Gson().toJson(genieResponse.getResult());
//        if (D)
//            Log.d(TAG, "onSuccessCurrentGetUser :" + result);
//        //5. start session
//        //partner=new Partner(getActivity());
//        startSessionResponseHandler = new StartSessionResponseHandler(this);
//        partner.startPartnerSession(Util.partnerId, startSessionResponseHandler);
//    }
//
//    @Override
//    public void onFailureCurrentGetUser(GenieResponse genieResponse) {
//        String result = new Gson().toJson(genieResponse.getResult());
//        if (D)
//            Log.d(TAG, "onFailureCurrentGetUser :" + result);
//        progressBar.setVisibility(View.GONE);
//        scrollViewChildContainer.setVisibility(View.VISIBLE);
//
//    }
//
//
//    @Override
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
}
