package com.example.hw2.Utilities;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.example.hw2.Interface.Step_Callback;

public class Sensors {
    private final float LEFT = 5.0f;
    private final float RIGHT = -5.0f;
    private final float SPEED_UP = -5.0f;
    private final float SPEED_DOWN = 5.0f;
    private boolean isLeft;
    private boolean isRight;
    private boolean isSpeedUp;
    private boolean isSpeedDown;
    private SensorManager sensorManager;
    private Sensor sensor;
    private SensorEventListener sensorEventListener;
    private long timestamp = 0l;
    private Step_Callback stepCallback;


    public Sensors(Context context, Step_Callback stepCallback)
    {
        isLeft = false;
        isRight = false;
        isSpeedUp = false;
        isSpeedDown = false;
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.stepCallback = stepCallback;
        initEventListener();
    }

    private void initEventListener() {
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event)
            {
                float x = event.values[0];
                float y = event.values[1];
                calculateStep(x,y);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                //pass
            }
        };
    }

    private void calculateStep(float x, float y) {
        if(System.currentTimeMillis() - timestamp > 350){
            timestamp = System.currentTimeMillis();
            if(x>LEFT)
                if (stepCallback != null)
                    stepCallback.stepLeft();
            if(x<RIGHT)
                if (stepCallback != null)
                    stepCallback.stepRight();
            if(y > SPEED_DOWN)
                if(stepCallback != null)
                    stepCallback.stepSpeedDown();
            if(y < SPEED_UP)
                if(stepCallback != null)
                    stepCallback.stepSpeedUp();
        }
    }
    public void start(){
        sensorManager.registerListener(
                sensorEventListener,
                sensor,
                SensorManager.SENSOR_DELAY_NORMAL
        );
    }
    public void stop(){
        sensorManager.unregisterListener(
                sensorEventListener,
                sensor
        );
    }
}
