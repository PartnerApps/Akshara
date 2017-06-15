package org.akshara.callback;


public interface DelegateAction {

    public void onActionStart();

    public void onActionCompleted(Boolean result);

    public void onActionFailed(String failedResult);

}
