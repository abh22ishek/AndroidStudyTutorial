package com.example.androidstudytutorial;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidstudytutorial.listners.CallFragment;
import com.example.androidstudytutorial.main.DisplayFragment;
import com.example.androidstudytutorial.main.MainFragment;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.mine.mywallpaper.BuildConfig;
import com.mine.mywallpaper.R;

public class MainActivity extends FragmentActivity implements CallFragment {

    private final String TAG = MainActivity.class.getSimpleName();
    private AdView mAdView;
    private boolean mLoadRequest;
    private TextView mVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.status_color));
        }

        loadFragment();

        mVersion = findViewById(R.id.version);
        final String versionName = BuildConfig.VERSION_NAME;
        mVersion.setText("Version : "+" "+versionName);


        mLoadRequest = true;
       adsUnit();
       AdsThread adsThread =new AdsThread();
       adsThread.start();

    }

    private void adsUnit(){
        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
    }

    class AdsThread extends Thread {
        public void run() {
          if(mLoadRequest){
              AdRequest adRequest = new AdRequest.Builder().build();
              runOnUiThread(new Runnable() {
                  @Override
                  public void run() {
                      mAdView.loadAd(adRequest);
                      Log.d(TAG,"AdsThread() is running .. ");
                  }
              });

          }

        }
    }




    @Override
    public void showFragment(int drawable, String fileName , String TAG) {
        // Add the fragment to the activity, pushing this transaction
        // on to the back stack.
        DisplayFragment displayFragment =new DisplayFragment();
        Bundle bundle =new Bundle();
        bundle.putInt("IMAGE",drawable);
        bundle.putString("FILE_NAME",fileName);
        displayFragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.mainLayout, displayFragment,DisplayFragment.class.getSimpleName()).
                addToBackStack(null).commit();

    }


    private void loadFragment(){
        MainFragment mainFragment =new MainFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.mainLayout, mainFragment,MainFragment.class.getSimpleName()).
                addToBackStack(null).commit();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy() called ");
        mLoadRequest=false;
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        int entryCount = fragmentManager.getBackStackEntryCount();
        final String name = String.valueOf(fragmentManager.findFragmentByTag(MainFragment.class.getSimpleName()));
        Log.i(TAG,"Fragment Name ="+name +"entryCount = "+entryCount);
        if(entryCount ==1){
            finishAffinity();
        }else{
            super.onBackPressed();
        }




    }
}