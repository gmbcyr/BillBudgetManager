package com.gmb.bbm2.viewmodel;

import android.arch.lifecycle.ViewModel;

import com.gmb.bbm2.tools.utils.Filters;


/**
 *
 */

public class ListingViewModel extends ViewModel {

    private boolean mIsSigningIn;
    private Filters mFilters;

    public ListingViewModel() {
        mIsSigningIn = false;
        mFilters = Filters.getDefault();
    }

    public boolean getIsSigningIn() {
        return mIsSigningIn;
    }

    public void setIsSigningIn(boolean mIsSigningIn) {
        this.mIsSigningIn = mIsSigningIn;
    }

    public Filters getFilters() {
        return mFilters;
    }

    public void setFilters(Filters mFilters) {
        this.mFilters = mFilters;
    }
}
