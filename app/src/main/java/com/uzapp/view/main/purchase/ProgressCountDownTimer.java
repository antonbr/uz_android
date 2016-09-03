package com.uzapp.view.main.purchase;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.uzapp.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Vladimir on 31.08.2016.
 */
public class ProgressCountDownTimer extends CountDownTimer {

    private SimpleDateFormat timeFormat = new SimpleDateFormat("mm:ss");

    private Context context;
    private ProgressBar progressTime;
    private TextView txtTimerPurchase;

    public ProgressCountDownTimer(Context context, long millisInFuture, long countDownInterval,
                                  ProgressBar progressTime, TextView txtTimerPurchase) {
        super(millisInFuture, countDownInterval);
        this.context = context;
        this.progressTime = progressTime;
        this.txtTimerPurchase = txtTimerPurchase;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        updateProgress(millisUntilFinished);
    }

    @Override
    public void onFinish() {
        txtTimerPurchase.setText(timeFormat.format(0));
        progressTime.setProgress(0);
        Toast.makeText(context, "Срок резервации истек", Toast.LENGTH_SHORT).show();
    }

    public void updateProgress(long progress) {
        Date time = new Date(progress);
        if (progress < 300000) {
            progressTime.setProgressDrawable(ContextCompat.getDrawable(context, R.drawable.progress_bar_style_red));
        }
        if (progressTime != null && txtTimerPurchase != null) {
            progressTime.setProgress((int) (progress / 1000));
            txtTimerPurchase.setText(timeFormat.format(time));
        }
    }
}
