package com.rex.rchat.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.rex.common.common.app.Activity;
import com.rex.factory.net.SendThread;
import com.rex.factory.net.SocketManager;
import com.rex.rchat.R;
import com.rex.rchat.SocketService;

import butterknife.BindView;
import butterknife.OnClick;
import icepick.State;

public class MainActivity extends Activity {
    @BindView(R.id.testText)
    TextView text;

    @BindView(R.id.testButton)
    Button b;

    @State
    int mCodeData=0;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }


    @Override
    protected void initBefore() {
        Intent intent = new Intent(this, SocketService.class);
        Log.d("MainActivity","before start service");
        startService(intent);
        Log.d("MainActivity","after start service");

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        text.setText(mCodeData+"");

    }

    @OnClick(R.id.testButton)
    public void onClick(View view) {
//        mCodeData++;
//        text.setText(mCodeData+"");
        for(int i = 99; i >90; i--){
            Log.d("MainActivity","before append msg: " + String.valueOf(i));
            SocketManager.getInstance().sendMessage("here is the data " +String.valueOf(i)+"\n");
            Log.d("MainActivity","after append msg: " + String.valueOf(i));

        }
    }


}
