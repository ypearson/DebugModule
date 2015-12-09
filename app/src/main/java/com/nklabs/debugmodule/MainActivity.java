package com.nklabs.debugmodule;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private TextView textView;
    private Lm75bDriver lm75bDriver;
    TemperatureThread temperatureThread;
    private Thread thread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textView);
        textView.setText("0");

        lm75bDriver = new Lm75bDriver((byte) 0);
        temperatureThread = new TemperatureThread(lm75bDriver);
        thread = new Thread(temperatureThread);
        thread.start();


    }

    class TemperatureThread implements Runnable {

        private Lm75bDriver lm75bDriver;
        private int temperature;
        private boolean loop = true;

        TemperatureThread(Lm75bDriver lm75bDriver) {
            this.lm75bDriver = lm75bDriver;
        }
        public void stop() {
            loop = false;
        }

        @Override
        public void run() {

            while (loop) {
                Log.d(TAG, "get temp...");
                temperature = lm75bDriver.getTemperature();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText(Integer.toString(temperature));
                    }
                });

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
            Log.d(TAG, "Thread stopped...");
        }}

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop()");
    }

    @Override
    protected void onDestroy() {
        temperatureThread.stop();
        while(thread.isAlive());
        super.onDestroy();
        Log.d(TAG, "onDestroy()");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
}


