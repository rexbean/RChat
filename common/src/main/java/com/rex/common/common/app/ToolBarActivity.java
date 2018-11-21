package com.rex.common.common.app;

import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import com.rex.common.R;

public abstract class ToolBarActivity extends Activity {
    protected Toolbar mToolbar;

    @Override
    protected void initWidgets() {
        super.initWidgets();
        initToolbar((Toolbar)findViewById(R.id.toolbar));
    }

    public void initToolbar(Toolbar toolbar){
        mToolbar = toolbar;
        if(toolbar != null){
            setSupportActionBar(toolbar);
        }

        initBackButton();
    }

    protected void initBackButton(){
        ActionBar actionBar = getSupportActionBar();

        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
    }

}
