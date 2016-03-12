package com.cs160.neiluuu.electiontracker;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by joleary on 2/19/16.
 */
public class PhoneToWatchService extends Service {

    private GoogleApiClient mApiClient;

    @Override
    public void onCreate() {
        super.onCreate();
        //initialize the googleAPIClient for message passing
        mApiClient = new GoogleApiClient.Builder( this )
                .addApi( Wearable.API )
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle connectionHint) {
                    }

                    @Override
                    public void onConnectionSuspended(int cause) {
                    }
                })
                .build();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mApiClient.disconnect();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Which cat do we want to feed? Grab this info from INTENT
        // which was passed over when we called startService
        if (intent != null) {
            Bundle extras = intent.getExtras();
            final String zip = extras.getString("ZIPCODE");
            final String obama = extras.getString("OBAMA");
            final String romney = extras.getString("ROMNEY");
            final String county = extras.getString("COUNTY");
            final String state = extras.getString("STATE");
            final String person1name = extras.getString("PERSON1");
            final String person1title = extras.getString("PERSON1TITLE");
            final String person1party = extras.getString("PERSON1PARTY");
            final String person2name = extras.getString("PERSON2");
            final String person2title = extras.getString("PERSON2TITLE");
            final String person2party = extras.getString("PERSON2PARTY");
            final String person3name = extras.getString("PERSON3");
            final String person3title = extras.getString("PERSON3TITLE");
            final String person3party = extras.getString("PERSON3PARTY");
            final String person4name = extras.getString("PERSON4");
            final String person4title = extras.getString("PERSON4TITLE");
            final String person4party = extras.getString("PERSON4PARTY");

            final JSONObject obj = new JSONObject();
            try {
                obj.put("zip", zip);
                obj.put("person1name", person1name);
                obj.put("person1title", person1title);
                obj.put("person1party", person1party);
                obj.put("person2name", person2name);
                obj.put("person2title", person2title);
                obj.put("person2party", person2party);
                obj.put("person3name", person3name);
                obj.put("person3title", person3title);
                obj.put("person3party", person3party);
                obj.put("person4name", person4name);
                obj.put("person4title", person4title);
                obj.put("person4party", person4party);
                obj.put("obama", obama);
                obj.put("romney", romney);
                obj.put("county", county);
                obj.put("state", state);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            final String JSONobj = obj.toString();

            // Send the message with the cat name
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //first, connect to the apiclient
                    mApiClient.connect();
                    //now that you're connected, send a massage with the cat name
                    sendMessage("/json", JSONobj);
                }
            }).start();
        }
        return START_STICKY;
    }

    @Override //remember, all services need to implement an IBiner
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void sendMessage( final String path, final String text ) {
        //one way to send message: start a new thread and call .await()
        //see watchtophoneservice for another way to send a message
        new Thread( new Runnable() {
            @Override
            public void run() {
                NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes( mApiClient ).await();
                for(Node node : nodes.getNodes()) {
                    //we find 'nodes', which are nearby bluetooth devices (aka emulators)
                    //send a message for each of these nodes (just one, for an emulator)
                    MessageApi.SendMessageResult result = Wearable.MessageApi.sendMessage(
                            mApiClient, node.getId(), path, text.getBytes() ).await();
                    //4 arguments: api client, the node ID, the path (for the listener to parse),
                    //and the message itself (you need to convert it to bytes.)
                }
            }
        }).start();
    }

}
