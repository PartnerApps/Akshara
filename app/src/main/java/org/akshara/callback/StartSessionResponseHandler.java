package org.akshara.callback;


import org.ekstep.genieservices.commons.IResponseHandler;
import org.ekstep.genieservices.commons.bean.GenieResponse;

public class StartSessionResponseHandler implements IResponseHandler {
    private IStartSession mIStartSession = null;

    public StartSessionResponseHandler(IStartSession startSession) {
        mIStartSession = startSession;
    }

    @Override
    public void onSuccess(GenieResponse response) {
        // Code to handle success scenario
        mIStartSession.onSuccessSession(response);

    }

    @Override
    public void onError(GenieResponse response) {
        // Code to handle error scenario
        mIStartSession.onFailureSession(response);
    }

}

