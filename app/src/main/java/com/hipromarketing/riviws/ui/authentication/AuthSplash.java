package com.hipromarketing.riviws.ui.authentication;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hipromarketing.riviws.R;
import com.hipromarketing.riviws.bus.Message;

import org.greenrobot.eventbus.EventBus;

/**
 * A simple {@link Fragment} subclass.
 */
public class AuthSplash extends Fragment {

    public static AuthSplash newInstance() {

        Bundle args = new Bundle();

        AuthSplash fragment = new AuthSplash();
        fragment.setArguments(args);
        return fragment;
    }

    public AuthSplash() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.auth_splash, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        AppCompatButton logIn = view.findViewById(R.id.logIn);
        AppCompatTextView signUp = view.findViewById(R.id.signUp);

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendNavigationBus("1");
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendNavigationBus("2");
            }
        });
    }


    private void sendNavigationBus(String msg){
        EventBus.getDefault().post(new Message(msg));
    }
}
