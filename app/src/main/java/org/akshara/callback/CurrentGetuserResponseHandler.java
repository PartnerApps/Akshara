package org.akshara.callback;


import org.ekstep.genieservices.commons.IResponseHandler;
import org.ekstep.genieservices.commons.bean.GenieResponse;

public class CurrentGetuserResponseHandler implements IResponseHandler {
    private ICurrentGetUser mICurrentGetUser = null;

    public CurrentGetuserResponseHandler(ICurrentGetUser currentGetUser) {
        mICurrentGetUser = currentGetUser;
    }

    @Override
    public void onSuccess(GenieResponse genieResponse) {
        // Code to handle success scenario
        mICurrentGetUser.onSuccessCurrentGetUser(genieResponse);

    }

    @Override
    public void onError(GenieResponse genieResponse) {
        // Code to handle error scenario
        mICurrentGetUser.onFailureCurrentGetUser(genieResponse);
    }
}

