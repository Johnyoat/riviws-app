package com.hipromarketing.riviws.ui;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hipromarketing.riviws.R;
import com.hipromarketing.riviws.adapters.ViewPagerAdapter;
import com.hipromarketing.riviws.bus.Message;
import com.hipromarketing.riviws.ui.authentication.AuthSplash;
import com.hipromarketing.riviws.ui.authentication.LogIn;
import com.hipromarketing.riviws.ui.authentication.SignUp;
import com.litetech.libs.nonswipeviewpager.ViewPager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;



public class AuthenticationPage extends Fragment {
    private EventBus eventBus = EventBus.getDefault();
    private ViewPager pager;
    List<Fragment> fragments;

    public static AuthenticationPage newInstance() {

        Bundle args = new Bundle();

        AuthenticationPage fragment = new AuthenticationPage();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.authentication, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pager = view.findViewById(R.id.pager);
        fragments = new ArrayList<>();

        fragments.add(AuthSplash.newInstance());
        fragments.add(LogIn.newInstance());
        fragments.add(SignUp.newInstance());

        assert getActivity() != null;
        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager(), fragments);

        pager.setAdapter(adapter);
    }

    @Subscribe
    @SuppressWarnings("all")
    public void moveToNext(@NonNull Message msg) {
        String key = msg.getMsg();

        switch (key) {
            case "0":
                pager.setCurrentItem(0);
                break;
            case "1":
                pager.setCurrentItem(1);
                break;
            case "2":
                pager.setCurrentItem(2, false);
                break;
            default:
                pager.setCurrentItem(0);
                break;
        }

    }


    @Override
    public void onStart() {
        super.onStart();
        eventBus.register(this);
    }


    @Override
    public void onStop() {
        super.onStop();

        eventBus.unregister(this);
    }
}
