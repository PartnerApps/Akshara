package org.akshara.callback;


import org.ekstep.genieservices.commons.IResponseHandler;
import org.ekstep.genieservices.commons.bean.GenieResponse;

import java.util.Map;

public class LanguageResponseHandler implements IResponseHandler<Map> {
    private ILanguage mILanguage = null;

    public LanguageResponseHandler(ILanguage language) {
        mILanguage = language;
    }

    @Override
    public void onSuccess(GenieResponse genieListResponse) {
        // Code to handle success scenario
        mILanguage.onSuccessLanguage(genieListResponse);
    }

    @Override
    public void onError(GenieResponse genieListResponse) {
        // Code to handle error scenario
        mILanguage.onFailureLanguage(genieListResponse);
    }

}

