package com.rex.common.common.app;

import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v7.app.AppCompatActivity;

import java.util.List;

import butterknife.ButterKnife;
import icepick.Icepick;

public abstract class Activity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initWindows();

        if(intiArgs(getIntent().getExtras())){
            int layoutId = getContentLayoutId();
            setContentView(layoutId);
            initBefore();
            initWidgets();
            initData(savedInstanceState);
        } else {
            finish();
        }
    }

    protected void initWindows(){

    }

    /**
     * start a presenter here
     */
    protected  void initBefore(){

    }

    /**
     * get the arguments from the previous activity
     * @param bundle arguments
     * @return initialize successfully or not
     */
    protected boolean intiArgs(Bundle bundle){
        return true;
    }

    /**
     * get the layout id
     * @return layout id
     */
    protected abstract int getContentLayoutId();

    /**
     * init the widget in the activity
     */
    protected void initWidgets(){
        ButterKnife.bind(this);
    }

    /**
     * init the data int the activity
     */
    protected void initData(Bundle savedInstanceState){
        // restore the data from last forced exit
        Icepick.restoreInstanceState(this, savedInstanceState);

    }


    /**
     * when the activity is forced to exit, the instance state will be saved in IcePick
     * @param outState the state of the app
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Icepick.saveInstanceState(this, outState);
        super.onSaveInstanceState(outState);
    }


    /**
     * the event when touch the navigate up
     * @return successfully or not
     */
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }


    /**
     * the event when press the back button
     * if some fragment cannot back then stay on that fragment
     */
    @Override
    public void onBackPressed() {
        List<android.support.v4.app.Fragment> fragments = getSupportFragmentManager().getFragments();
        if(fragments.size() > 0){
            for (android.support.v4.app.Fragment fragment : fragments) {
                if(fragment instanceof Fragment){
                    if(((Fragment) fragment).onBackPressed()){
                        return;
                    }
                }
            }
        }
        super.onBackPressed();
        finish();
    }
}
