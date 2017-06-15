package org.akshara.callback;


import org.ekstep.genieservices.commons.IResponseHandler;
import org.ekstep.genieservices.commons.bean.GenieResponse;

public class EndSessionResponseHandler implements IResponseHandler {
    private IEndSession mIEndSession = null;

    public EndSessionResponseHandler(IEndSession endSession) {
        mIEndSession = endSession;
    }

    @Override
    public void onSuccess(GenieResponse response) {
        // Code to handle success scenario
        mIEndSession.onSuccessEndSession(response);
    }

    @Override
    public void onError(GenieResponse response) {
        // Code to handle error scenario
        mIEndSession.onFailureEndSession(response);
    }

}

