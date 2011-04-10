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

public class Pomidor extends Activity implements OnClickListener
{
    private static int donePomodoros = 0;
    private static final int INTERVAL = 62;

    private Handler handler;
    private TextView textView;
    private TextView pomodoros;
    private int mStartTime;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        try {
            handler = new Handler();
            //mStartTime = 1500;
            mStartTime = INTERVAL;

            View startButton = findViewById(R.id.start_button);
            startButton.setOnClickListener(this);
            View stopButton = findViewById(R.id.stop_button);
            stopButton.setOnClickListener(this);

            textView = (TextView) findViewById(R.id.timer_label);
            pomodoros = (TextView) findViewById(R.id.pomodoros_count);

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
                int minutes = mStartTime / 60;
                int seconds = mStartTime % 60;

                String separator = (seconds < 10) ? ": 0" : ":";

                textView.setText(minutes + separator + seconds);
                if (mStartTime >= 1) {
                    handler.postDelayed(mUpdateTimeTask, 1000);
                } else {
                    textView.setText(Integer.toString(INTERVAL));
                    donePomodoros++;

                    // Save the current value
                    getPreferences(MODE_PRIVATE).edit().putInt("pomodorod_done", donePomodoros).commit();
                    mStartTime = INTERVAL;
                    pomodoros.setText(Integer.toString(getDonePomodorosCount()));
                }
            }
        };

    public int getDonePomodorosCount() {
        int curr = PreferenceManager.getDefaultSharedPreferences(this)
            .getInt("pomodorosDone", donePomodoros);
        Log.i("ccc", "Current count is : " + curr);
        return curr;
    }
}
