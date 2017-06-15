package org.akshara.callback;


import org.ekstep.genieservices.commons.IResponseHandler;
import org.ekstep.genieservices.commons.bean.GenieResponse;

import java.util.Map;

public class CurrentGetUserResponseHandler implements IResponseHandler<Map> {
    private ICurrentGetUser mICurrentGetUser = null;

    public CurrentGetUserResponseHandler(ICurrentGetUser currentGetUser) {
        mICurrentGetUser = currentGetUser;
    }

    @Override
    public void onSuccess(GenieResponse<Map> genieResponse) {
        // Code to handle success scenario
        mICurrentGetUser.onSuccessCurrentGetUser(genieResponse);

    }

    @Override
    public void onError(GenieResponse<Map> genieResponse) {
        // Code to handle error scenario
        mICurrentGetUser.onFailureCurrentGetUser(genieResponse);
    }
}

