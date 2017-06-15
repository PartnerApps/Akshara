package org.akshara.callback;

import org.ekstep.genieservices.commons.IResponseHandler;
import org.ekstep.genieservices.commons.bean.GenieResponse;

import java.util.Map;

public class PartnerDataResponseHandler implements IResponseHandler<Map> {
    private IPartnerData mIPartnerData = null;

    public PartnerDataResponseHandler(IPartnerData partnerData) {
        mIPartnerData = partnerData;
    }

    @Override
    public void onSuccess(GenieResponse response) {
        // Code to handle success scenario
        mIPartnerData.onSuccessPartner(response);

    }

    @Override
    public void onError(GenieResponse response) {
        // Code to handle error scenario
        mIPartnerData.onFailurePartner(response);
    }

}

