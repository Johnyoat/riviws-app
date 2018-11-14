package com.hipromarketing.riviws.ui;


import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.hipromarketing.riviws.R;
import com.hipromarketing.riviws.bus.Message;
import com.hipromarketing.riviws.services.NotificationService;
import com.hipromarketing.riviws.ui.welcome.WelcomeHost;
import com.hipromarketing.riviws.utils.UICreator;
import com.litetech.libs.statemanager.StateManger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import io.realm.Realm;

import static com.hipromarketing.riviws.constants.Constants.NOTIFICATION;
import static com.hipromarketing.riviws.constants.Constants.getUser;

/**
 * A simple {@link Fragment} subclass.
 */
public class BaseHome extends AppCompatActivity implements PermissionListener, GoogleApiClient.OnConnectionFailedListener {
    private AppCompatImageView logo;
    private UICreator uiCreator;
    BottomNavigationView bnv;
    GoogleApiClient mGoogleApiClient;
    Glide glide;
    View foobar;
    EventBus eventBus = EventBus.getDefault();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startService(new Intent(this, NotificationService.class));
        Realm.init(this);
        getUser();

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        setContentView(R.layout.base_home);

        StateManger.init(this);

        foobar = findViewById(R.id.foobar);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        checkPermission();
        logo = findViewById(R.id.logo);
        uiCreator = UICreator.getInstance(this);
        uiCreator.setFRAG(R.id.homeContainer);
//        uiCreator.createFragment(Home.newInstance(), "home");
        uiCreator.createFragment(WelcomeHost.newInstance(), "home");

        bnv = findViewById(R.id.btnNav);
        bnv.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
//      Notifications.getInstance(this).createNotification("Riviws","Some one liked your post");


        showWelcomeScreen();
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.home:
                    uiCreator.createFragment(Home.newInstance(), "home");
                    blueLogo();
                    return true;
                case R.id.trending:
                    uiCreator.createFragment(Trending.newInstance("default"), "trending");
                    blueLogo();
                    return true;
                case R.id.post:
                    uiCreator.createFragment(Post.newInstance(), "post");
                    whiteLogo();
                    return true;
                case R.id.videos:
//                    uiCreator.createFragment(Videos.newInstance(), "videos");
                    uiCreator.createFragment(SuggestionForm.newInstance(), "videos");
                    blueLogo();
                    return true;
                case R.id.account:
                    uiCreator.createFragment(Account.newInstance(), "account");
                    blueLogo();
                    return true;

            }
            return false;
        }
    };


    private void checkPermission() {
        TedPermission.with(this)
                .setPermissionListener(this)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();
    }

    private void blueLogo() {
//        logo.setImageDrawable(getDrawable(R.drawable.blue_logo));
        Glide.with(getApplicationContext()).load(R.drawable.blue_logo).into(logo);
    }

    private void whiteLogo() {
//        logo.setImageDrawable(getDrawable(R.drawable.white_logo));
        Glide.with(getApplicationContext()).load(R.drawable.white_logo).into(logo);

    }

    @Override
    public void onPermissionGranted() {
    }

    @Override
    public void onPermissionDenied(ArrayList<String> deniedPermissions) {
        Toast.makeText(this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            final boolean fromNotification = extras.getBoolean(NOTIFICATION);
            if (fromNotification) {
                UICreator.getInstance(this).createDialog(NotificationPage.newInstance(), "notification");
//                bnv.setSelectedItemId(R.id.account);
//                EventBus.getDefault().post(new Message(NOTIFICATION));
            }

        }
    }

    private void showWelcomeScreen() {

        Object obj = StateManger.getInstance().getObject("trained", Boolean.class);

        if (obj != null) {
            boolean isUserDoneWithOnBoarding = (Boolean) obj;

            if (isUserDoneWithOnBoarding) {
                createHomeFragment();
            } else {
                bnv.setVisibility(View.GONE);
                foobar.setVisibility(View.GONE);
                uiCreator.createFragment(WelcomeHost.newInstance(), "welcome");
            }
        } else {
            bnv.setVisibility(View.GONE);
            foobar.setVisibility(View.GONE);
            uiCreator.createFragment(WelcomeHost.newInstance(), "welcome");
        }
    }

    private void createHomeFragment() {
        uiCreator.createFragment(Home.newInstance(), "home");
        bnv.setVisibility(View.VISIBLE);
        foobar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @SuppressWarnings("all")
    @Subscribe
    public void callHome(Message message){
        if (message != null && message.getMsg().equalsIgnoreCase("callHome")){
            createHomeFragment();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        eventBus.register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        eventBus.unregister(this);
    }
}
