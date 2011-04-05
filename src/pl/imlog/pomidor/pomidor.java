package pl.imlog.pomidor;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.os.Handler;
import android.os.SystemClock;

public class pomidor extends Activity implements OnClickListener
{
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
        mStartTime = 1500;

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
                    int current = new Integer(pomodoros.getText().toString()).intValue();
                    pomodoros.setText(Integer.toString(current));
                }
            }
        };
}
