package com.rex.common.common.app;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class Fragment extends android.support.v4.app.Fragment {
    protected View mRoot;
    protected boolean mIsFirstTimeInit = true;
    protected Unbinder mRootUnBinder;

    // what is the placeholderView?



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // init some arguments
        initArgs(getArguments());
    }

    /**
     * init the fields form the arguments from outside
     * @param arguments arguments
     */
    private void initArgs(Bundle arguments) {

    }

    /**
     *  get the layout Id
     * @return the layout Id
     */
    protected abstract int getContentLayoutId();


    /**
     * init the widget in the fragment
     * @param root the root view of the fragment
     */
    protected void initWidget(View root){
        mRootUnBinder = ButterKnife.bind(this, root);
    }


    /**
     * init the data in the fragment
     */
    protected void initData(){

    }

    /**
     * start presenter here
     */
    protected void onFirstInit(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // if fragment has the root then remove from its parent and return
        // otherwise create a view from the layout but not to add to the container
        if(mRoot == null){
            int layoutId = getContentLayoutId();
            View root = inflater.inflate(layoutId, container, false);
            initWidget(root);
            mRoot = root;
        } else {
            if(mRoot.getParent() != null){
                ((ViewGroup)mRoot.getParent()).removeView(mRoot);
            }
        }
        return mRoot;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // judge whether this is the first time creating the view
        // if yes, then start presenter
        if(mIsFirstTimeInit){
            onFirstInit();
            mIsFirstTimeInit = false;
        }
        initData();
    }

    public boolean onBackPressed(){
        return false;
    }

}
