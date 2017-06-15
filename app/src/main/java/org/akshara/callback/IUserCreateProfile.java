package org.akshara.callback;


import org.ekstep.genieservices.commons.bean.GenieResponse;

import java.util.Map;

/**
 * Callback for User Profile Created events
 *
 * @author vinayagasundar
 */

public interface IUserCreateProfile {

    void onSuccessUserProfile(GenieResponse<Map> genieResponse);

    void onFailureUserProfile(GenieResponse<Map> genieResponse);
}
