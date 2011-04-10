package pl.imlog.pomidor;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;


import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

import android.widget.LinearLayout;

public class Pomidor extends Activity implements OnClickListener
{
    private static int donePomodoros;
    private static final int INTERVAL = 1500;

    private Handler handler;
    private TextView textView;
    private TextView pomodoros;
    private int mStartTime;

    private AdView adView;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest();
        adView.loadAd(adRequest);

        donePomodoros = getDonePomodorosCount();

        try {
            handler = new Handler();
            mStartTime = INTERVAL;

            View startButton = findViewById(R.id.start_button);
            startButton.setOnClickListener(this);
            View stopButton = findViewById(R.id.stop_button);
            stopButton.setOnClickListener(this);


            textView = (TextView) findViewById(R.id.timer_label);
            textView.setText(getTimeString(INTERVAL));

            pomodoros = (TextView) findViewById(R.id.pomodoros_count);
            pomodoros.setText(Integer.toString(getDonePomodorosCount()));

        } catch (Exception ex) {
            Log.e("onCreate", ex.getMessage());
        }
    }


    public void onClick(View v) {
        try {
            switch (v.getId()) {
            case R.id.start_button:
                handler.postDelayed(mUpdateTimeTask, 1000);
                break;
            case R.id.stop_button:
                handler.removeCallbacks(mUpdateTimeTask);
                break;
            }
        } catch (Exception ex) {
            Log.e("onClick", ex.getMessage());
        }
    }

    private Runnable mUpdateTimeTask = new Runnable() {
            public void run() {
                mStartTime--;
                textView.setText(getTimeString(mStartTime));
                if (mStartTime >= 1) {
                    handler.postDelayed(mUpdateTimeTask, 1000);
                } else {
                    textView.setText(getTimeString(INTERVAL));
                    donePomodoros++;
                    // Save the current value
                    getPreferences(MODE_PRIVATE).edit().putInt("pomodorod_done", donePomodoros).commit();
                    mStartTime = INTERVAL; //reset timer
                    pomodoros.setText(Integer.toString(getDonePomodorosCount()));
                }
            }
        };

    public int getDonePomodorosCount() {
        return PreferenceManager.getDefaultSharedPreferences(this)
            .getInt("pomodorosDone", donePomodoros);
    }

    private String getTimeString(int time) {
        int minutes = time / 60;
        int seconds = time % 60;
        final String separator = (seconds < 10) ? ":0" : ":";
        return minutes + separator + seconds;
    }

    public void onDestroy() {
        adView.stopLoading();
        super.onDestroy();
    }
}
