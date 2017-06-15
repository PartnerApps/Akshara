package org.akshara.callback;


import org.ekstep.genieservices.commons.IResponseHandler;
import org.ekstep.genieservices.commons.bean.GenieResponse;

public class UserProfileResponseHandler implements IResponseHandler {
    private IUserProfile mIUserProfile = null;

    public UserProfileResponseHandler(IUserProfile userProfile) {
        mIUserProfile = userProfile;
    }

    @Override
    public void onSuccess(GenieResponse genieResponse) {
        // Code to handle success scenario
        mIUserProfile.onSuccessUserProfile(genieResponse);
    }

    @Override
    public void onError(GenieResponse genieResponse) {
        // Code to handle error scenario
        mIUserProfile.onFailureUserprofile(genieResponse);
    }
}

