package com.hipromarketing.riviws.ui;


import android.app.Fragment;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.hipromarketing.riviws.R;

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * A simple {@link Fragment} subclass.
 */
public class SuggestionForm extends DialogFragment {
    LinearLayout nameLayout;
    LinearLayout contactLayout;
    LinearLayout locationLayout;
    LinearLayout businessLayout;
    AppCompatEditText name;
    AppCompatEditText contact;
    AppCompatEditText location;
    AppCompatEditText business;
    AppCompatButton submit;
    AppCompatCheckBox owner;
    AppCompatButton suggest;
    AppCompatTextView homeText;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    public static SuggestionForm newInstance() {

        Bundle args = new Bundle();

        SuggestionForm fragment = new SuggestionForm();
        fragment.setArguments(args);
        return fragment;
    }


    public SuggestionForm() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        Objects.requireNonNull(getDialog().getWindow()).setBackgroundDrawableResource(R.drawable.dialog_rounded_bg);
        return inflater.inflate(R.layout.suggestion_form, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nameLayout = view.findViewById(R.id.nameLayout);
        contactLayout = view.findViewById(R.id.contactLayout);
        businessLayout = view.findViewById(R.id.businessLayout);
        locationLayout = view.findViewById(R.id.locationLayout);
        name = view.findViewById(R.id.name);
        contact = view.findViewById(R.id.contact);
        business = view.findViewById(R.id.business);
        location = view.findViewById(R.id.location);
        submit = view.findViewById(R.id.submit);
        owner = view.findViewById(R.id.owner);
        suggest = view.findViewById(R.id.suggest);
        homeText = view.findViewById(R.id.homeText);


        suggest.setVisibility(View.GONE);

        homeText.setText(R.string.suggest_text);

        validateTextWhilesTyping(name, nameLayout);
        validateTextWhilesTyping(contact, contactLayout);
        validateTextWhilesTyping(location, locationLayout);
        validateTextWhilesTyping(business, businessLayout);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateText(name, nameLayout) & validateText(contact, contactLayout) & validateText(location, locationLayout) & validateText(business, businessLayout)) {
                    HashMap<String, Object> sug = new HashMap<>();
                    sug.put("name", name.getText().toString());
                    sug.put("location", location.getText().toString());
                    sug.put("business", business.getText().toString());
                    sug.put("contact", contact.getText().toString());
                    if (owner.isChecked()) {
                        sug.put("owner", true);
                    }

                    db.collection("suggestions").add(sug);
                    Toast.makeText(getContext(), "Thank you", Toast.LENGTH_SHORT).show();
                    dismissAllowingStateLoss();
                }
            }
        });


    }

    void validateTextWhilesTyping(AppCompatEditText editText, final View view) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() < 2) {
                    view.setBackground(getActivity().getDrawable(R.drawable.error_bg));
                } else {
                    view.setBackground(getActivity().getDrawable(R.drawable.curvy_edit_text));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    boolean validateText(AppCompatEditText editText, View view) {
        boolean valid = true;
        if (editText.getText().toString().isEmpty()) {
            view.setBackground(getActivity().getDrawable(R.drawable.error_bg));
            valid = false;
        }

        return valid;
    }
}
