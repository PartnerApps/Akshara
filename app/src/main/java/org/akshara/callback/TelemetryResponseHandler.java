package org.akshara.callback;

import org.ekstep.genieservices.commons.IResponseHandler;
import org.ekstep.genieservices.commons.bean.GenieResponse;

import java.util.Map;

public class TelemetryResponseHandler implements IResponseHandler<Map> {
    private ITelemetryData mtelemetryData = null;

    public TelemetryResponseHandler(ITelemetryData telemetryData) {
        mtelemetryData = telemetryData;
    }

    @Override
    public void onSuccess(GenieResponse response) {
        // Code to handle success scenario
        mtelemetryData.onSuccessTelemetry(response);


    }

    @Override
    public void onError(GenieResponse response) {
        // Code to handle error scenario
        mtelemetryData.onFailureTelemetry(response);

    }

}

