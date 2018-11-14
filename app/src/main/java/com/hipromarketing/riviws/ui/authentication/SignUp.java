package com.hipromarketing.riviws.ui.authentication;


import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputLayout;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.login.widget.LoginButton;
import com.hipromarketing.riviws.R;
import com.hipromarketing.riviws.bus.Message;
import com.hipromarketing.riviws.utils.AuthHelper;
import com.hipromarketing.riviws.utils.TextUtility;

import org.greenrobot.eventbus.EventBus;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUp extends Fragment {
    private AuthHelper authHelper;
    AppCompatImageView bck;
    AppCompatButton signUp;
    AppCompatButton gSignUp;
    AppCompatButton fbSignUp;
    TextInputLayout fnameLayout;
    TextInputLayout lnameLayout;
    TextInputLayout email;
    TextInputLayout passwordLayout;
    TextInputLayout confirmPassword;
    TextUtility textUtility;
    LoginButton fbSignUpBtn;

    public static SignUp newInstance() {

        Bundle args = new Bundle();

        SignUp fragment = new SignUp();
        fragment.setArguments(args);
        return fragment;
    }

    public SignUp() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.sign_up, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bck = view.findViewById(R.id.back);
        signUp = view.findViewById(R.id.signUp);
        fbSignUp = view.findViewById(R.id.fbSignUp);
        fnameLayout = view.findViewById(R.id.fnameLayout);
        lnameLayout = view.findViewById(R.id.lnameLayout);
        email = view.findViewById(R.id.emailLayout);
        passwordLayout = view.findViewById(R.id.passwordLayout);
        confirmPassword = view.findViewById(R.id.confirmPasswordLayout);
        authHelper = AuthHelper.getInstance(getActivity());
        fbSignUpBtn = view.findViewById(R.id.fbButton);
        gSignUp = view.findViewById(R.id.gSignUp);

        fbSignUpBtn.setFragment(this);

        fbSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authHelper.facebookSignIn(fbSignUpBtn);
            }
        });

        gSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authHelper.googleSignIn();
            }
        });




        textUtility = TextUtility.getInstance(getActivity());

        textUtility.addTextChangeListener(fnameLayout, lnameLayout, email, passwordLayout);

        bck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new Message("0"));
            }
        });


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (textUtility.isEmpty(fnameLayout, lnameLayout, email, passwordLayout)) {
                    String name = textUtility.fieldToString(fnameLayout) + " " + textUtility.fieldToString(lnameLayout);
                    String emailAddress = textUtility.fieldToString(email);
                    String pass = textUtility.fieldToString(passwordLayout);
                    authHelper.signUp(name, emailAddress, pass);

                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        authHelper.getCallbackManager().onActivityResult(requestCode, resultCode, data);
    }
}
