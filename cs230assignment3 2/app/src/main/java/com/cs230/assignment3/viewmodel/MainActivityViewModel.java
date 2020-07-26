package com.cs230.assignment3.viewmodel;

import androidx.lifecycle.ViewModel;


public class MainActivityViewModel extends ViewModel {

    private boolean mIsSigningIn;

    public MainActivityViewModel() {
        mIsSigningIn = false;
    }

    public boolean getIsSigningIn() {
        return mIsSigningIn;
    }

    public void setIsSigningIn(boolean mIsSigningIn) {
        this.mIsSigningIn = mIsSigningIn;
    }

}
