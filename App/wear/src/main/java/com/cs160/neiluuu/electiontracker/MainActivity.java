package com.cs160.neiluuu.electiontracker;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.wearable.view.CardFragment;
import android.support.wearable.view.GridViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Random;

public class MainActivity extends Activity {

//    private TextView mTextView;
//    private Button mFeedBtn;

    /* put this into your activity class */
    private SensorManager mSensorManager;
    private float mAccel; // acceleration apart from gravity
    private float mAccelCurrent; // current acceleration including gravity
    private float mAccelLast; // last acceleration including gravity
    private int[] randomZipcodes = {94704, 92679, 92691, 92692, 94102, 95113};
    private int lastTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String person1 = "", person1title = "", person1party = "", person2 = "", person2title = "", person2party = "", person3 = "", person3title = "", person3party = "";
        if (extras != null) {
            person1 = extras.getString("PERSON1");
            person1title = extras.getString("PERSON1TITLE");
            person1party = extras.getString("PERSON1PARTY");
            person2 = extras.getString("PERSON2");
            person2title = extras.getString("PERSON2TITLE");
            person2party = extras.getString("PERSON2PARTY");
            person3 = extras.getString("PERSON3");
            person3title = extras.getString("PERSON3TITLE");
            person3party = extras.getString("PERSON3PARTY");
        }
        final GridViewPager pager = (GridViewPager) findViewById(R.id.pager);
        pager.setAdapter(new GridPagerAdapter(this, getFragmentManager(), new CustomCardFragment[][]{
                {
                        CustomCardFragment.create(person1, person1title + "\n" + person1party, 0, 0),
                        CustomCardFragment.create(person2, person2title + "\n" + person2party, 0, 1),
                        CustomCardFragment.create(person3, person3title + "\n" + person3party, 0, 2)
                },
                {
                        CustomCardFragment.create("2012 Election Results", "California\nBerkeley\nObama: 90.90%\nRomney: 9.10%", 0, 3)
                }
        }));

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;




//        mFeedBtn = (Button) findViewById(R.id.feed_btn);
//
//        Intent intent = getIntent();
//        Bundle extras = intent.getExtras();
//
//        if (extras != null) {
//            String catName = extras.getString("CAT_NAME");
//            mFeedBtn.setText("Feed " + catName);
//        }
//
//        mFeedBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent sendIntent = new Intent(getBaseContext(), WatchToPhoneService.class);
//                startService(sendIntent);
//            }
//        });
    }
    private final SensorEventListener mSensorListener = new SensorEventListener() {

        public void onSensorChanged(SensorEvent se) {
            float x = se.values[0];
            float y = se.values[1];
            float z = se.values[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt((double) (x*x + y*y + z*z));
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta; // perform low-cut filter
            Calendar c = Calendar.getInstance();
            int seconds = c.get(Calendar.SECOND);

            if (mAccel > 12 && (seconds - lastTime > 2)) {
                lastTime = seconds;
                //you need to add this flag since you're starting a new activity from a service
                int rnd = randomZipcodes[new Random().nextInt(randomZipcodes.length)];
                final GridViewPager pager = (GridViewPager) findViewById(R.id.pager);
                pager.setAdapter(new GridPagerAdapter(getApplicationContext(), getFragmentManager(), new CustomCardFragment[][]{
                        {
                                CustomCardFragment.create("Lee Zeldin", "Representative" + "\n" + "Republican", 0, 0),
                                CustomCardFragment.create("Chuck Schumer", "Senator" + "\n" + "Democrat", 0, 1),
                                CustomCardFragment.create("Kirsten Gillibrand", "Senator" + "\n" + "Democrat", 0, 2)
                        },
                        {
                                CustomCardFragment.create("2012 Election Results", "New York\nNew York City\nObama: 83.75%\nRomney: 14.92%", 0, 3)
                        }
                }));
                Intent intent = new Intent(getApplicationContext(), WatchToPhoneService.class );
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.putExtra("PERSON1", "Lee Zeldin");
//                intent.putExtra("PERSON1TITLE", "Representative");
//                intent.putExtra("PERSON1PARTY", "Republican");
//                intent.putExtra("PERSON2", "Chuck Schumer");
//                intent.putExtra("PERSON2TITLE", "Senator");
//                intent.putExtra("PERSON2PARTY", "Democrat");
//                intent.putExtra("PERSON3", "Kirsten Gillibrand");
//                intent.putExtra("PERSON3TITLE", "Senator");
//                intent.putExtra("PERSON3PARTY", "Democrat");
                intent.putExtra("ZIPCODE", Integer.toString(rnd));
                Toast toast = Toast.makeText(getApplicationContext(), "New Zip: " + rnd, Toast.LENGTH_SHORT);
                toast.show();
                startService(intent);
            }
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }
}
