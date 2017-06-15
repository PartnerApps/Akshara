package org.akshara.callback;


import java.util.ArrayList;

public interface DelegateExcelAction {

    public void onExcelActionStart();

    public void onExcelActionCompleted(ArrayList rowList);

    public void onExcelActionFailed(String failedResult);

}
