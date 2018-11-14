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
public class LogIn extends Fragment {
    private LoginButton fbBtn;
    private AuthHelper authHelper;
    private TextUtility utility = TextUtility.getInstance();
    private TextInputLayout email;
    private TextInputLayout password;

    public static LogIn newInstance() {

        Bundle args = new Bundle();

        LogIn fragment = new LogIn();
        fragment.setArguments(args);
        return fragment;
    }

    public LogIn() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.log_in, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AppCompatImageView bck = view.findViewById(R.id.back);
        AppCompatButton login = view.findViewById(R.id.logIn);
        AppCompatButton fbLogIn = view.findViewById(R.id.fbLogin);
        AppCompatButton gLogin = view.findViewById(R.id.gLogin);
        fbBtn = view.findViewById(R.id.fbButton);
        email = view.findViewById(R.id.email);
        password = view.findViewById(R.id.password);


        authHelper = AuthHelper.getInstance(getActivity());


        email.setErrorEnabled(true);
        password.setErrorEnabled(true);


        utility.checkTextInput(email, "Email");
        utility.checkTextInput(password, "Password");

        fbBtn.setFragment(this);


        fbLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authHelper.facebookSignIn(fbBtn);
            }
        });


        gLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                authHelper.setContext(getContext());
                authHelper.googleSignIn();

            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (utility.validEmail(email) & utility.validPassword(password)) {
                    authHelper.signInWithEmailAndPassword(utility.fieldToString(email), utility.fieldToString(password));
                }
            }
        });


        bck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new Message("0"));
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        authHelper.getCallbackManager().onActivityResult(requestCode, resultCode, data);

    }


}
