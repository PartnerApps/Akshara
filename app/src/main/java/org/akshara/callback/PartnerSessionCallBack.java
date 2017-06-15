package org.akshara.callback;

public interface PartnerSessionCallBack {
    void onPartnerEventReceived(boolean status);

    void onPartnerEventReceived(boolean status, String event);
}
