package com.hipromarketing.riviws.utils;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;

import com.hipromarketing.riviws.R;

public class UICreator {
    private AppCompatActivity activity;
    private int FRAG = R.id.container;

    private UICreator(AppCompatActivity activity) {
        this.activity = activity;
    }

    public static UICreator getInstance(AppCompatActivity activity) {
        return new UICreator(activity);
    }

    public void setAnimation() {

    }


    public void createFragment(Fragment fragment, String tag) {
        FragmentManager fm = activity.getSupportFragmentManager();
        fm.beginTransaction().replace(FRAG, fragment, tag).commit();

    }


    public void setFRAG(int FRAG) {
        this.FRAG = FRAG;
    }

    public void createFragmentWithSlide(Fragment fragment, String tag) {

        FragmentManager fm = activity.getSupportFragmentManager();

        fm.beginTransaction()
                .setCustomAnimations(R.anim.exit, R.anim.entrance)
                .replace(FRAG, fragment, tag).commit();

    }


    public  void createDialog(DialogFragment fragment,String tag){
        fragment.setStyle(DialogFragment.STYLE_NORMAL,R.style.NoActionBar);
        fragment.show(activity.getSupportFragmentManager(),tag);
    }


    public  void createMinDialog(DialogFragment fragment,String tag){
//        fragment.setStyle(DialogFragment.STYLE_NORMAL,R.style.NoActionBar);
        fragment.show(activity.getSupportFragmentManager(),tag);
    }
}
