package org.akshara.callback;


import org.ekstep.genieservices.commons.IResponseHandler;
import org.ekstep.genieservices.commons.bean.GenieResponse;

import java.util.Map;

public class UserProfileCreateResponseHandler implements IResponseHandler<Map> {
    private IUserCreateProfile mIUserCreateProfile = null;


    public UserProfileCreateResponseHandler(IUserCreateProfile mIUserCreateProfile) {
        this.mIUserCreateProfile = mIUserCreateProfile;
    }

    @Override
    public void onSuccess(GenieResponse<Map> genieResponse) {
        if (mIUserCreateProfile != null) {
            mIUserCreateProfile.onSuccessUserProfile(genieResponse);
        }
    }

    @Override
    public void onError(GenieResponse<Map> genieResponse) {
        if (mIUserCreateProfile != null) {
            mIUserCreateProfile.onFailureUserProfile(genieResponse);
        }
    }
}

