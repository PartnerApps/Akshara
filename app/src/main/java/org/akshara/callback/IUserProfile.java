package org.akshara.callback;


import org.ekstep.genieservices.commons.bean.GenieResponse;

/**
 * Created by Jaya on 10/5/2015.
 */
public interface IUserProfile {

    void onSuccessUserProfile(GenieResponse genieResponse);

    void onFailureUserprofile(GenieResponse genieResponse);
}
