package org.akshara.callback;


import org.ekstep.genieservices.commons.IResponseHandler;
import org.ekstep.genieservices.commons.bean.GenieResponse;

import java.util.Map;

public class CurrentUserResponseHandler implements IResponseHandler<Map> {
    private ICurrentUser mICurrentUser = null;

    public CurrentUserResponseHandler(ICurrentUser currentUser) {
        mICurrentUser = currentUser;
    }

    @Override
    public void onSuccess(GenieResponse<Map> genieResponse) {
        // Code to handle success scenario
        mICurrentUser.onSuccessCurrentUser(genieResponse);
    }

    @Override
    public void onError(GenieResponse<Map> genieResponse) {
        // Code to handle error scenario
        mICurrentUser.onFailureCurrentUser(genieResponse);
    }
}

