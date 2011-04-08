package pl.imlog.pomidor;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class Pomidor extends Activity implements OnClickListener
{
    private static int donePomodoros = 0;

    private Handler handler;
    private TextView textView;
    private TextView pomodoros;
    private int mStartTime;

    @Override
        public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        handler = new Handler();
        //mStartTime = 1500;
        mStartTime = 63;

        View startButton = findViewById(R.id.start_button);
        startButton.setOnClickListener(this);
        View stopButton = findViewById(R.id.stop_button);
        stopButton.setOnClickListener(this);

        textView = (TextView) findViewById(R.id.timer_label);
        pomodoros = (TextView) findViewById(R.id.pomodoros_count);
    }


    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.start_button:
            handler.postDelayed(mUpdateTimeTask, 1000);
            break;
        case R.id.stop_button:
            handler.removeCallbacks(mUpdateTimeTask);
            break;
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
                    textView.setText("25:00");
                    donePomodoros++;

                    // Save the current value
                    getPreferences(MODE_PRIVATE).edit().putInt("pomodorod_done", donePomodoros).commit();

                    pomodoros.setText(Integer.toString(Pomidor.getDonePomodoros(getContext())));
                }
            }
        };

    public static int getDonePomodorosCount(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
            .getInt("pomodorosDone", donePomodoros);
    }
}
