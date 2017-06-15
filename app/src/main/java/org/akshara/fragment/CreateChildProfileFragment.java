package org.akshara.fragment;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import org.akshara.R;
import org.akshara.Util.Util;
import org.akshara.activity.MainActivity;
import org.akshara.customviews.CustomEditText;
import org.akshara.model.UserModel;

import java.util.ArrayList;
import java.util.HashMap;


public class CreateChildProfileFragment extends Fragment {
    int editRow = 0;
    boolean flagFocus1stEditText = false;
    LayoutInflater layoutInflater;
    int row = 0;
    private boolean D = Util.DEBUG;
    private String TAG = CreateChildProfileFragment.class.getSimpleName();
    private Context mContext;
    private Fragment mFragment;
    private Button generateText, generateDropDown, Create_btn, Cancel_btn, save_template_btn;
    private TextView labelName, selectionTypeLabel;
    private EditText labelNameValue, edittemplateName;
    private LinearLayout displayLayout;
    private UserModel userModel = new UserModel();
    private HashMap<String, UserModel> hashMapTemplate_data = new HashMap<>();
    private HashMap<String, UserModel> hashMapTemplate_dataPrevious = new HashMap<>();
    private HashMap<String, String> hashMap_single = new HashMap<>(); //Single value against each label
    private HashMap<String, ArrayList<String>> hashMap_multiple = new HashMap<>(); //Multiple value against each label
    private HashMap<Integer, String> hashMap_order = new HashMap<>();
    private int order = 0;
    private String type = "";
    private EditText[] editTexts = new EditText[Util.dropDownItemSize];
    private boolean isDropDownCreated = false, isMultichoicesCreated = false;
    private FloatingActionMenu menuLabelsRight;
    private FloatingActionButton fabGenerateText, fabGenerateDropDown,
            fabGenerateCommentBox, fabGenerateMultipleChoice, fabGenerateMatrix;
    private String templateName;
    private LinearLayout templateHeading;
    private View rootView;
    private String[] selection_array = {"Single-select(radio-buttons)", "Multi-select(checkboxes)"};
    private Spinner spinnerselectionType;
    private GradientDrawable gradientDrawable;
    private TextView[] textViews = new TextView[50];
    // private EditText[] editTexts=new EditText[10];
    private Spinner[] spinners = new Spinner[50];
    private CheckBox[] checkBoxes = new CheckBox[60];
    private RadioButton[] radioButtons = new RadioButton[60];
    private int sizeOfMap = 0;

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
        layoutInflater = getLayoutInflater(savedInstanceState);

        rootView = inflater.inflate(R.layout.child_profile, container, false);
        edittemplateName = (EditText) rootView.findViewById(R.id.edittemplateName);
        generateText = (Button) rootView.findViewById(R.id.generateText);
        generateDropDown = (Button) rootView.findViewById(R.id.generateDropDown);
        Create_btn = (Button) rootView.findViewById(R.id.Create_btn);
        Cancel_btn = (Button) rootView.findViewById(R.id.Cancel_btn);
        save_template_btn = (Button) rootView.findViewById(R.id.save_template_btn);
        labelName = (TextView) rootView.findViewById(R.id.labelName);
        labelNameValue = (EditText) rootView.findViewById(R.id.labelNameValue);
        displayLayout = (LinearLayout) rootView.findViewById(R.id.displayLayout);
        menuLabelsRight = (FloatingActionMenu) rootView.findViewById(R.id.menu_labels_right);
        fabGenerateText = (FloatingActionButton) rootView.findViewById(R.id.fabGenerateText);
        fabGenerateDropDown = (FloatingActionButton) rootView.findViewById(R.id.fabGenerateDropDown);
        fabGenerateCommentBox = (FloatingActionButton) rootView.findViewById(R.id.fabGenerateCommentBox);
        fabGenerateMultipleChoice = (FloatingActionButton) rootView.findViewById(R.id.fabGenerateMultipleChoice);
        fabGenerateMatrix = (FloatingActionButton) rootView.findViewById(R.id.fabGenerateMatrix);
        selectionTypeLabel = (TextView) rootView.findViewById(R.id.selectionTypeLabel);
        spinnerselectionType = (Spinner) rootView.findViewById(R.id.spinnerselectionType);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, R.layout.spinner_item, selection_array);
        adapter.setDropDownViewResource(R.layout.spinner_item_dropdown);
        spinnerselectionType.setAdapter(adapter);

        menuLabelsRight.setOnMenuButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*if (menuLabelsRight.isOpened()) {
                    Toast.makeText(mContext, menuLabelsRight.getMenuButtonLabelText(), Toast.LENGTH_SHORT).show();
                }*/

                menuLabelsRight.toggle(true);
            }
        });


        fabGenerateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuLabelsRight.toggle(false);
                menuLabelsRight.setVisibility(View.GONE);
                labelName.setVisibility(View.VISIBLE);
                labelNameValue.setVisibility(View.VISIBLE);
                Create_btn.setVisibility(View.VISIBLE);
                Cancel_btn.setVisibility(View.VISIBLE);
                type = "text";

                templateHeading.setVisibility(View.GONE);
                displayLayout.removeAllViews();


            }
        });

        Cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuLabelsRight.setVisibility(View.VISIBLE);
                labelNameValue.setText("");
                labelName.setVisibility(View.GONE);
                labelNameValue.setVisibility(View.GONE);
                displayLayout.removeAllViews();
                Create_btn.setVisibility(View.GONE);
                Cancel_btn.setVisibility(View.GONE);

                //Save data
                templateHeading.setVisibility(View.VISIBLE);
                isDropDownCreated = false;
                isMultichoicesCreated = false;
                spinnerselectionType.setVisibility(View.GONE);
                selectionTypeLabel.setVisibility(View.GONE);


                createChildForm();
            }
        });

        fabGenerateCommentBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                menuLabelsRight.toggle(false);
                menuLabelsRight.setVisibility(View.GONE);
                labelName.setVisibility(View.VISIBLE);
                labelNameValue.setVisibility(View.VISIBLE);
                Create_btn.setVisibility(View.VISIBLE);
                Cancel_btn.setVisibility(View.VISIBLE);
                type = "comment";

                templateHeading.setVisibility(View.GONE);
                displayLayout.removeAllViews();


            }
        });
        fabGenerateMultipleChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuLabelsRight.toggle(false);
                menuLabelsRight.setVisibility(View.GONE);
                templateHeading.setVisibility(View.GONE);
                displayLayout.removeAllViews();

                labelName.setVisibility(View.VISIBLE);
                labelName.setText("Enter Question Text");
                labelNameValue.setVisibility(View.VISIBLE);
                labelNameValue.setHint("Enter Question Text");
                Create_btn.setVisibility(View.VISIBLE);
                Cancel_btn.setVisibility(View.VISIBLE);
                spinnerselectionType.setVisibility(View.VISIBLE);
                selectionTypeLabel.setVisibility(View.VISIBLE);
                isMultichoicesCreated = true;
                if (!isDropDownCreated)
                    createDropDown(rootView);
                else
                    Toast.makeText(mContext, "Please complete this MultipleChoice field.", Toast.LENGTH_LONG).show();

            }
        });
        fabGenerateDropDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuLabelsRight.toggle(false);
                menuLabelsRight.setVisibility(View.GONE);
                templateHeading.setVisibility(View.GONE);
                displayLayout.removeAllViews();

                labelName.setVisibility(View.VISIBLE);
                labelNameValue.setVisibility(View.VISIBLE);
                Create_btn.setVisibility(View.VISIBLE);
                Cancel_btn.setVisibility(View.VISIBLE);
                type = "dropdown";
                if (!isDropDownCreated)
                    createDropDown(rootView);
                else
                    Toast.makeText(mContext, "Please complete this dropdown field.", Toast.LENGTH_LONG).show();

            }
        });
        Create_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.hideKeyboard(getActivity(), mContext);

                String label_value = labelNameValue.getText().toString();
                if (label_value.equals("")) {
                    Toast.makeText(mContext, "Please enter label name.", Toast.LENGTH_LONG).show();
                } else if (isDropDownCreated) {
                    if (isMultichoicesCreated) {
                        type = spinnerselectionType.getSelectedItem().toString();
                        isMultichoicesCreated = false;
                    }


                    if (D)
                        Log.d(TAG, "row-->" + row);
                    if (row >= 1) {
                        isDropDownCreated = false;
                        //CheckDropDown
                        ArrayList<String> multipleList = new ArrayList<String>();
                        for (int i = 0; i < row; i++) {
                            multipleList.add(editTexts[i].getText().toString());
                        }
                        hashMap_multiple.put(label_value, multipleList);
                        if (D)
                            Log.d(TAG, "multipleList" + multipleList);
                        row = 0;
                        saveFields(label_value);
                    } else
                        Util.showToastmessage(mContext, "Please enter value for option1.");
                } else {
                    saveFields(label_value);

                }


            }
        });


        save_template_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (D)
                        Log.d(TAG, "hashMap_multiple==>" + hashMap_multiple);
                    templateName = edittemplateName.getText().toString().trim();
                    if (D)
                        Log.d(TAG, "templateName:" + templateName);
                    if (templateName.isEmpty()) {
                        Util.showToastmessage(mContext, "Please enter template name.");
                    } else {
                        Util.hideKeyboard(getActivity(), mContext);
                        templateHeading = (LinearLayout) rootView.findViewById(R.id.templateHeading);
                        TextView tvtemplateHeading = (TextView) rootView.findViewById(R.id.tvtemplateHeading);
                        templateHeading.setVisibility(View.VISIBLE);
                        tvtemplateHeading.setText(templateName);
                        edittemplateName.setVisibility(View.GONE);
                        save_template_btn.setVisibility(View.GONE);
                        menuLabelsRight.setVisibility(View.VISIBLE);
                    }

/*
                if(templateName.isEmpty()){
                    Util.showToastmessage(mContext,"Please enter template name.");
                }else if(hashMap_order.size()!=0){
                    if(hashMap_single.size()!=0)
                        userModel.setLabel_single_data(hashMap_single);
                    if(hashMap_multiple.size()!=0)
                        userModel.setLabel_multiple_data(hashMap_multiple);

                    userModel.setLabel_order(hashMap_order);


                   // hashMapTemplate_data.put(templateName, userModel);
                    //userModel.setTemplate_data(hashMapTemplate_data);
                    hashMapTemplate_dataPrevious=new Util(mContext).getUserModel();
                    if(D)
                        Log.d(TAG,"before hashMapTemplate_dataPrevious=>"+hashMapTemplate_dataPrevious);
                    if(hashMapTemplate_dataPrevious!=null){ //Add previou data
                        hashMapTemplate_dataPrevious.put(templateName, userModel);
                        if(D)
                            Log.d(TAG,"aftre adding hashMapTemplate_dataPrevious=>"+hashMapTemplate_dataPrevious);

                        new Util(mContext).storeUserModel(hashMapTemplate_dataPrevious);
                    }else{
                        hashMapTemplate_data.put(templateName, userModel);
                        if(D)
                            Log.d(TAG,"hashMapTemplate_data null adding data=>"+hashMapTemplate_data);

                        new Util(mContext).storeUserModel(hashMapTemplate_data);
                    }


                    TemplateChildFragment templateChildFragment=new TemplateChildFragment();
                    ((MainActivity)mContext).switchContent(templateChildFragment, 1, true);


                }else
                    Util.showToastmessage(mContext, "Please create field using text/dropdown.");
*/


                } catch (Exception e) {
                    if (D)
                        Log.e(TAG, "save_template_btn Ex :" + e);

                }


            }
        });
        return rootView;
    }

    private void saveFields(String label_value) {
        order++;
        hashMap_single.put(label_value, type);
        hashMap_order.put(order, label_value);
        labelNameValue.setText("");
        menuLabelsRight.setVisibility(View.VISIBLE);
        labelName.setVisibility(View.GONE);
        labelNameValue.setVisibility(View.GONE);
        displayLayout.removeAllViews();
        Create_btn.setVisibility(View.GONE);
        Cancel_btn.setVisibility(View.GONE);

        //Save data
        templateHeading.setVisibility(View.VISIBLE);
        sizeOfMap = hashMap_order.size();
        if (sizeOfMap != 0) {
            if (hashMap_single.size() != 0)
                userModel.setLabel_single_data(hashMap_single);
            if (hashMap_multiple.size() != 0)
                userModel.setLabel_multiple_data(hashMap_multiple);

            userModel.setLabel_order(hashMap_order);


            // hashMapTemplate_data.put(templateName, userModel);
            //userModel.setTemplate_data(hashMapTemplate_data);
            hashMapTemplate_dataPrevious = new Util(mContext).getUserModel();
            if (D)
                Log.d(TAG, "before hashMapTemplate_dataPrevious=>" + hashMapTemplate_dataPrevious);
            if (hashMapTemplate_dataPrevious != null) { //Add previou data
                hashMapTemplate_dataPrevious.put(templateName, userModel);
                if (D)
                    Log.d(TAG, "aftre adding hashMapTemplate_dataPrevious=>" + hashMapTemplate_dataPrevious);

                new Util(mContext).storeUserModel(hashMapTemplate_dataPrevious);
            } else {
                hashMapTemplate_data.put(templateName, userModel);
                if (D)
                    Log.d(TAG, "hashMapTemplate_data null adding data=>" + hashMapTemplate_data);

                new Util(mContext).storeUserModel(hashMapTemplate_data);
            }


            createChildForm();
        }


    }

    private void createChildForm() {
        try {
            //margin attribute
            LinearLayout.LayoutParams layoutMargin = new LinearLayout.LayoutParams
                    (new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            layoutMargin.setMargins(0, 10, 0, 10);

            //drawable
            gradientDrawable = new GradientDrawable();
            gradientDrawable.setColor(getResources().getColor(R.color.white)); //background color
            gradientDrawable.setStroke(1, getResources().getColor(R.color.color_text_lablel_value)); //border color
            gradientDrawable.setCornerRadius(1.30f);


            if (D)
                Log.d(TAG, "size :" + sizeOfMap);
            if (D)
                Log.d(TAG, "hashMap_order :" + hashMap_order);

            if (D)
                Log.d(TAG, "hashMap_single :" + hashMap_single);
            if (D)
                Log.d(TAG, "hashMap_multiple :" + hashMap_multiple);
            displayLayout.removeAllViews();


            //View
            View view;
            for (int i = 0; i < sizeOfMap; i++) {
                String Key_value = hashMap_order.get((i + 1));
                if (D)
                    Log.d(TAG, "Key_value" + Key_value);
                String Key_type = hashMap_single.get(Key_value);
                if (D)
                    Log.d(TAG, "Key_type" + Key_type);
                //Heading as Label
                view = layoutInflater.inflate(R.layout.textview_template, displayLayout, false);
                textViews[i] = (TextView) view.findViewById(R.id.textTemplate);
                textViews[i].setText(Key_value);
                displayLayout.addView(textViews[i]);

                if (Key_type.equals("text")) { // label Value type="comment";
                    view = layoutInflater.inflate(R.layout.edit_text_template, displayLayout, false);
                    editTexts[i] = (CustomEditText) view.findViewById(R.id.editTextTemplate);
                    editTexts[i].setHint("Please enter your " + Key_value);
                    displayLayout.addView(editTexts[i]);

                } else if (Key_type.equals("comment")) { // label Value type="comment";
                    view = layoutInflater.inflate(R.layout.comment_template, displayLayout, false);
                    editTexts[i] = (CustomEditText) view.findViewById(R.id.commentTemplate);
                    editTexts[i].setHint("Please enter your " + Key_value);
                    displayLayout.addView(editTexts[i]);

                } else if (Key_type.equals("Multi-select(checkboxes)")) { //dropdown value MultipleChoice {"Single-select(radio-buttons)","Multi-select(checkboxes)"}
                    ArrayList<String> Optionlist = hashMap_multiple.get(Key_value);
                    for (int j = 0; j < Optionlist.size(); j++) {
                        view = layoutInflater.inflate(R.layout.checkbox_text_template, displayLayout, false);
                        checkBoxes[j] = (CheckBox) view.findViewById(R.id.checkBoxTemplate);
                        checkBoxes[j].setText(Optionlist.get(j));
                        displayLayout.addView(checkBoxes[j]);

                    }
                    labelName.setText("Enter label Name");
                    labelNameValue.setHint("Enter label Name");
                    spinnerselectionType.setVisibility(View.GONE);
                    selectionTypeLabel.setVisibility(View.GONE);


                } else if (Key_type.equals("Single-select(radio-buttons)")) {
                    ArrayList<String> Optionlist = hashMap_multiple.get(Key_value); //radioGroupTemplate
                    view = layoutInflater.inflate(R.layout.radiogroup_template, displayLayout, false);
                    RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radioGroupTemplate);


                    for (int j = 0; j < Optionlist.size(); j++) {
                        view = layoutInflater.inflate(R.layout.radio_text_template, displayLayout, false);
                        radioButtons[j] = (RadioButton) view.findViewById(R.id.radioTemplate);
                        radioButtons[j].setText(Optionlist.get(j));
                        radioGroup.addView(radioButtons[j]);

                    }
                    displayLayout.addView(radioGroup);
                    labelName.setText("Enter label Name");
                    labelNameValue.setHint("Enter label Name");
                    spinnerselectionType.setVisibility(View.GONE);
                    selectionTypeLabel.setVisibility(View.GONE);


                } else if (Key_type.equals("dropdown")) { //dropdown value MultipleChoice
                    view = layoutInflater.inflate(R.layout.spinner_template, displayLayout, false);
                    spinners[i] = (Spinner) view.findViewById(R.id.spinnerTemplate);
                    // HashMap<String, ArrayList<String>> dropdownMap = userModel.getLabel_multiple_data();
                    ArrayList<String> dropdownlist = hashMap_multiple.get(Key_value);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, R.layout.spinner_item, dropdownlist);
                    spinners[i].setAdapter(adapter);
                    adapter.setDropDownViewResource(R.layout.spinner_popup_item);
                    spinners[i].setAdapter(adapter);
                    displayLayout.addView(spinners[i]);

                }
            }

        } catch (Exception e) {
            if (D)
                Log.e(TAG, "createChildForm Ex :" + e);

        }

    }

    private void createDropDown(View rootView) {
        createRow(rootView);
        isDropDownCreated = true;

    }

    private void createRow(final View rootView) {
        editTexts[row] = new EditText(rootView.getContext());
        editTexts[row].setTextAppearance(rootView.getContext(), R.style.EditText_style);
        editTexts[row].setHint("Option" + (row + 1));
        editTexts[row].setTypeface(Typeface.create("", R.style.EditText_style));

        displayLayout.addView(editTexts[row]);
        editTexts[row].addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
               /* if (D)
                    Log.d(TAG, "beforeTextChanged count=>"+count+" start=>"+start+" after=?"+after);
          */
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (D)
                    Log.d(TAG, "onTextChanged count=>" + count + " start=>" + start + " before=" + before);
                if (count == 1) {
                    if (editTexts[row].getText().toString().trim().isEmpty()) {
                        Util.showToastmessage(mContext, "Please enter value for Option" + (row + 1));
                    } else {
                        row++;
                        if (row < Util.dropDownItemSize - 1)
                            createRow(rootView);
                        else
                            Util.showToastmessage(mContext, "You can't create Option more than" + (Util.dropDownItemSize - 1));
                    }

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
               /* if (D)
                    Log.d(TAG, "afterTextChanged "+s.getFilters());
           */
            }
        });

    }
}
