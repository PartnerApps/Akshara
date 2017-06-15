package org.akshara.callback;


import org.ekstep.genieservices.commons.IResponseHandler;
import org.ekstep.genieservices.commons.bean.GenieResponse;

public class RegisterResponseHandler implements IResponseHandler {
    private IRegister mRegister = null;

    public RegisterResponseHandler(IRegister register) {
        mRegister = register;
    }

    @Override
    public void onSuccess(GenieResponse genieResponse) {
        mRegister.onSuccess(genieResponse);
    }

    @Override
    public void onError(GenieResponse genieResponse) {
        mRegister.onFailure(genieResponse);
    }
}

