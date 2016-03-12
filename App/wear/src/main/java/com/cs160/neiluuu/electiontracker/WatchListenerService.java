package com.cs160.neiluuu.electiontracker;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Created by joleary and noon on 2/19/16 at very late in the night. (early in the morning?)
 */
public class WatchListenerService extends WearableListenerService {
    // In PhoneToWatchService, we passed in a path, either "/FRED" or "/LEXY"
    // These paths serve to differentiate different phone-to-watch messages

    private final static String CONGRESS_API_KEY = "5246dfb27133470ca6d01000fdbaa611";
    public final static String CONGRESS_URL = "https://congress.api.sunlightfoundation.com/";
    public final static String LOCATE_URL = "legislators/locate?";
    public final static String REPUBLICAN = "Republican";
    public final static String DEMOCRAT = "Democrat";

    public String json;

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d("T", "in WatchListenerService, got: " + messageEvent.getPath());
        //use the 'path' field in sendmessage to differentiate use cases
        //(here, fred vs lexy)

        json = new String(messageEvent.getData(), StandardCharsets.UTF_8);

        //you need to add this flag since you're starting a new activity from a service

        try {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            JSONObject cur = new JSONObject(json);
            // int[] indicesToPull = {0};
            intent.putExtra("PERSON1", cur.getString("person1name"));
            intent.putExtra("PERSON1TITLE", cur.getString("person1title"));
            intent.putExtra("PERSON1PARTY", cur.getString("person1party"));
            intent.putExtra("PERSON2", cur.getString("person2name"));
            intent.putExtra("PERSON2TITLE", cur.getString("person2title"));
            intent.putExtra("PERSON2PARTY", cur.getString("person2party"));
            intent.putExtra("PERSON3", cur.getString("person3name"));
            intent.putExtra("PERSON3TITLE", cur.getString("person3title"));
            intent.putExtra("PERSON3PARTY", cur.getString("person3party"));
            if (cur.has("person4name")) {
                intent.putExtra("PERSON4", cur.getString("person4name"));
                intent.putExtra("PERSON4TITLE", cur.getString("person4title"));
                intent.putExtra("PERSON4PARTY", cur.getString("person4party"));
            }
            intent.putExtra("ZIPCODE", cur.getString("zip"));
            intent.putExtra("OBAMA", cur.getString("obama"));
            intent.putExtra("ROMNEY", cur.getString("romney"));
            intent.putExtra("COUNTY", cur.getString("county"));
            intent.putExtra("STATE", cur.getString("state"));

            startActivity(intent);
            Log.d("STARTED", "activity started");
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage(), e);
        }

        //Log.d("T", "about to start watch MainActivity with CAT_NAME: Fred");
        //startActivity(intent);


    }
}