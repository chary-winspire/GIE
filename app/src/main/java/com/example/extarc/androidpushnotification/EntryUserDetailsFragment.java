package com.example.extarc.androidpushnotification;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;


/**
 * A simple {@link Fragment} subclass.
 */
public class EntryUserDetailsFragment extends Fragment implements DialogInterface.OnCancelListener, DialogInterface.OnDismissListener, View.OnClickListener {

    ImageView proimage;
    Spinner procategory, progender;
    EditText proname, proparent, proemail, promobile, proschool, proclass;
    Button prosubmit, proskip;

    private final String Default = "N/A";
    String gender;

    Boolean isSavedDetails = false;

    public EntryUserDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_enteruserdetails, container, false);

        proimage = view.findViewById(R.id.profileImage);
        procategory = view.findViewById(R.id.profileCategory);
        progender = view.findViewById(R.id.profileGender);
        proname = view.findViewById(R.id.profileName);
        proparent = view.findViewById(R.id.profileParent);
        proemail = view.findViewById(R.id.profileEmail);
        promobile = view.findViewById(R.id.profileMobile);
        proschool = view.findViewById(R.id.profileSchool);
        proclass = view.findViewById(R.id.profileClass);
        prosubmit = view.findViewById(R.id.profileSubmit);
        proskip = view.findViewById(R.id.profileSkip);

        prosubmit.setOnClickListener(this);
        proskip.setOnClickListener(this);

        procategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch ((position)) {
                    case 0: //student
                        proparent.setVisibility(View.VISIBLE);
                        proclass.setVisibility(View.VISIBLE);
                        break;

                    case 1:  //parent
                        proparent.setVisibility(View.VISIBLE);
                        proclass.setVisibility(View.VISIBLE);
                        break;

                    case 2:  // principal / headmaster
                        proparent.setVisibility(View.INVISIBLE);
                        proclass.setVisibility(View.INVISIBLE);
                        break;

                    case 3: // School
                        proparent.setVisibility(View.INVISIBLE);
                        proclass.setVisibility(View.INVISIBLE);
                        break;

                    case 4:  //Librarian
                        proparent.setVisibility(View.INVISIBLE);
                        proclass.setVisibility(View.INVISIBLE);
                        break;

                    case 5:  // Others
                        proparent.setVisibility(View.INVISIBLE);
                        proclass.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        return view;
    }


    @Override
    public void onCancel(DialogInterface dialog) {

    }

    @Override
    public void onDismiss(DialogInterface dialog) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.profileSubmit:
                SaveAttempt();
                break;
            case R.id.profileSkip:
                Intent intent = new Intent(getActivity(), MasterActivity.class);
                startActivity(intent);
                break;
        }
    }

    protected void SaveAttempt() {

        // Reset errors.
        proname.setError(null);
        proparent.setError(null);
        proemail.setError(null);
        promobile.setError(null);
        proschool.setError(null);
        proclass.setError(null);

        // Store values at the time of the login attempt.
        String Name = proname.getText().toString();
        String Parent = proparent.getText().toString();
        String Email = proemail.getText().toString();
        String Mobile = promobile.getText().toString();
        String School = proschool.getText().toString();
        String proClass = proclass.getText().toString();

        // Check for a valid Mobile, if the user entered one.
        if (TextUtils.isEmpty(Name)) {
            proname.setError(getString(R.string.error_mobile_field_required));
        } else if (!isNameValid(Name)) {
            proname.setError(getString(R.string.error_invalid_Name));
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(Email)) {
            proemail.setError(getString(R.string.error_emailid_field_required));
        } else if (!isEmailValid(Email)) {
            proemail.setError(getString(R.string.error_invalid_email));
        }

        if (TextUtils.isEmpty(Parent)) {
            proparent.setError(getString(R.string.error_field_required));
        } else if (!isParentValid(Parent)) {
            proparent.setError(getString(R.string.error_invalid_Name));
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(Mobile)) {
            promobile.setError(getString(R.string.error_field_required));
        } else if (!isMobileValid(Mobile)) {
            promobile.setError(getString(R.string.error_invalid_Mobile));
        }

        if (TextUtils.isEmpty(School)) {
            proschool.setError(getString(R.string.error_field_required));
        } else if (!isSchoolValid(School)) {
            proschool.setError(getString(R.string.error_invalid_School));
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(proClass)) {
            proclass.setError(getString(R.string.error_field_required));
        } else if (!isClassValid(proClass)) {
            proclass.setError(getString(R.string.error_invalid_Class));
        }
    }

    private boolean isEmailValid(String email) {

        return (email.contains("@") && email.contains(".com"));
    }

    private boolean isNameValid(String name) {

        return name.length() > 3 && name.length() < 15;
    }

    private boolean isMobileValid(String mobile) {

        return mobile.length() == 10;
    }

    private boolean isParentValid(String parent) {

        return parent.length() > 6 && parent.length() < 15;
    }

    private boolean isClassValid(String proClass) {

        return proClass.length() > 1 && proClass.length() < 10;
    }

    private boolean isSchoolValid(String school) {

        return school.length() > 10 && school.length() < 30;
    }

}
