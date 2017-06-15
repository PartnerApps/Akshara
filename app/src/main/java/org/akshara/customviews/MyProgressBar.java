package org.akshara.customviews;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import org.akshara.R;


/**
 * Progress bar that implements the  Progress by rotating the PNG image
 */
public class MyProgressBar extends ProgressBar {

    public MyProgressBar(Context context) {
        super(context);
        myDrawable(context);
    }

    public MyProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        myDrawable(context);
    }

    public MyProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        myDrawable(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MyProgressBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        myDrawable(context);
    }

    private void myDrawable(Context context) {
        setIndeterminate(true);
        setIndeterminateDrawable(context.getResources().getDrawable(R.drawable.progressbar));
//        setProgressDrawable(context.getResources().getDrawable(R.drawable.loading_v4));
    }


}
