package com.hipromarketing.riviws.ui.welcome;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hipromarketing.riviws.R;
import com.hipromarketing.riviws.adapters.PageAdpter;
import com.hipromarketing.riviws.bus.Message;
import com.litetech.libs.statemanager.StateManger;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class WelcomeHost extends Fragment {
    private PageAdpter adapter;
    private ViewPager pager;
    public static WelcomeHost newInstance() {
        
        Bundle args = new Bundle();
        
        WelcomeHost fragment = new WelcomeHost();
        fragment.setArguments(args);
        return fragment;
    }


    public WelcomeHost() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.welocme, container, false);
    }


    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final List<Fragment> fragments = new ArrayList<>();
        pager = view.findViewById(R.id.welcomePager);
        final AppCompatTextView next = view.findViewById(R.id.next);
        AppCompatTextView skipTut = view.findViewById(R.id.skipTut);

        assert getActivity() != null;

        fragments.add(Page.newInstance("Love the product/service/place?\nShare your experience.",""));
        fragments.add(Page.newInstance("Make decisions easily by reading other people's experiences.",""));
        fragments.add(Page.newInstance("The more stars the better.\nFollow the stars.",""));

        adapter = new PageAdpter(getChildFragmentManager(),fragments);

        pager.setAdapter(adapter);
        adapter.notifyDataSetChanged();


        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == fragments.size()-1){
                    next.setText("Finish");
                    StateManger.getInstance().saveObject("trained", true);
                }else {
                    next.setText("Next");
                    StateManger.getInstance().saveObject("trained", false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (next.getText().toString().equalsIgnoreCase("Finish")){
                    EventBus.getDefault().post(new Message("callHome"));
                }else {
                    pager.setCurrentItem((pager.getCurrentItem()+1));
                }
            }
        });


        skipTut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StateManger.getInstance().saveObject("trained", true);
                EventBus.getDefault().post(new Message("callHome"));
            }
        });
    }




    public static class Page extends Fragment {


        public static Page newInstance(String message,String extraData) {

            Bundle args = new Bundle();

            Page fragment = new Page();
            fragment.setArguments(args);
            args.putString("message",message);
            args.putString("extraData",extraData);
            return fragment;
        }


        public Page() {
            // Required empty public constructor
        }


        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            return inflater.inflate(R.layout.page_one, container, false);
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            AppCompatTextView text = view.findViewById(R.id.text);

            text.setText(getArguments().getString("message"));
        }
    }



}
