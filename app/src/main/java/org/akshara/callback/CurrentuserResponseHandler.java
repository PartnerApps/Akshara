package org.akshara.callback;


import org.ekstep.genieservices.commons.IResponseHandler;
import org.ekstep.genieservices.commons.bean.GenieResponse;

public class CurrentuserResponseHandler implements IResponseHandler {
    private ICurrentUser mICurrentUser = null;

    public CurrentuserResponseHandler(ICurrentUser currentUser) {
        mICurrentUser = currentUser;
    }

    @Override
    public void onSuccess(GenieResponse genieResponse) {
        // Code to handle success scenario
        mICurrentUser.onSuccessCurrentUser(genieResponse);
    }

    @Override
    public void onError(GenieResponse genieResponse) {
        // Code to handle error scenario
        mICurrentUser.onFailureCurrentUser(genieResponse);
    }
}

