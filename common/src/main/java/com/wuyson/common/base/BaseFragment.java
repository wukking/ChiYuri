package com.wuyson.common.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wang.avi.indicators.BallSpinFadeLoaderIndicator;
import com.wuyson.common.widget.ProgressDialog;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment extends Fragment {
    protected View rootView;
    private Unbinder butterKnife;
    protected Context mContext;
    protected Activity mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = getContext();
        mActivity = getActivity();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(getLayoutId(), container, false);
        }
        butterKnife = ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    protected abstract int getLayoutId();
    protected abstract void initView();

    protected void showDefaultDialog(Activity activity){
        ProgressDialog.showDefaultLoading(activity);
    }

    protected void showProgressDialog(Activity activity,String msg){
        ProgressDialog.showDialogForLoading(activity,msg , new BallSpinFadeLoaderIndicator());
    }

    protected void closeProgressDialog(){
        ProgressDialog.dismissProgressDialog();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        butterKnife.unbind();
    }

}
