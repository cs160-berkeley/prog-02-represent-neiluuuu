package com.cs160.neiluuu.electiontracker;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
    private String[] randomZipcodes = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
    private int lastTime = 0;

    public final static String REPUBLICAN = "Republican";
    public final static String DEMOCRAT = "Democrat";
    String person1 = "", person1title = "", person1party = "", person2 = "", person2title = "", person2party = "", person3 = "", person3title = "", person3party = "", person4 = "", person4title = "", person4party = "";
    String zip = "", romney = "0", state="", obama = "0", county= "";
    CustomCardFragment[][] cards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

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
            person4 = extras.getString("PERSON4");
            person4title = extras.getString("PERSON4TITLE");
            person4party = extras.getString("PERSON4PARTY");
            zip = extras.getString("ZIPCODE");
            obama = extras.getString("OBAMA");
            romney = extras.getString("ROMNEY");
            county = extras.getString("COUNTY");
            state = extras.getString("STATE");
        }
        if (person4 == null) {
            cards = new CustomCardFragment[][]{
                    {
                            CustomCardFragment.create(person1, person1title + "\n" + person1party, zip, person1party, 0, 0),
                            CustomCardFragment.create(person2, person2title + "\n" + person2party, zip, person2party, 0, 1),
                            CustomCardFragment.create(person3, person3title + "\n" + person3party, zip, person3party, 0, 2)
                    },
                    {
                            CustomCardFragment.create("2012 Election", county+", "+state +"\nObama: "+ obama +"%\nRomney: " + romney + "%", zip, getParty(obama, romney), 0, 3)
                    }
            };
        }else {
            cards = new CustomCardFragment[][]{
                    {
                            CustomCardFragment.create(person1, person1title + "\n" + person1party, zip, person1party, 0, 0),
                            CustomCardFragment.create(person2, person2title + "\n" + person2party, zip, person2party, 0, 1),
                            CustomCardFragment.create(person3, person3title + "\n" + person3party, zip, person3party, 0, 2),
                            CustomCardFragment.create(person4, person4title + "\n" + person4party, zip, person4party, 0, 3)
                    },
                    {
                            CustomCardFragment.create("2012 Election", county + ", " + state + "\nObama: " + obama + "%\nRomney: " + romney + "%", zip, getParty(obama, romney), 0, 3)
                    }
            };
        }

        final GridViewPager pager = (GridViewPager) findViewById(R.id.pager);
        pager.setAdapter(new GridPagerAdapter(this, getFragmentManager(), cards));

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;

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
//                you need to add this flag since you're starting a new activity from a service
                String rnd1 = randomZipcodes[new Random().nextInt(randomZipcodes.length)];
                String rnd2 = randomZipcodes[new Random().nextInt(randomZipcodes.length)];
                String rnd3 = randomZipcodes[new Random().nextInt(randomZipcodes.length)];
                String rnd4 = randomZipcodes[new Random().nextInt(randomZipcodes.length)];
                String rnd5 = randomZipcodes[new Random().nextInt(randomZipcodes.length)];
                String rnd = rnd1+rnd2+rnd3+rnd4+rnd5;


                Intent intent = new Intent(getApplicationContext(), WatchToPhoneService.class );
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("ZIPCODE", rnd);
                Toast toast = Toast.makeText(getApplicationContext(), "New Zip: " + rnd, Toast.LENGTH_SHORT);
                toast.show();
                startService(intent);
            }
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    public String getParty(String obama, String romney) {
        double obamaNum = Double.parseDouble(obama);
        double romneyNum = Double.parseDouble(romney);
        if (obamaNum > romneyNum) {
            return DEMOCRAT;
        } else if (obamaNum < romneyNum){
            return REPUBLICAN;
        }
        else {
            return "";
        }
    }

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
