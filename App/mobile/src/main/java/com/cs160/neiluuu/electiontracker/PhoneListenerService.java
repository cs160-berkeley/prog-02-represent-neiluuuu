package com.cs160.neiluuu.electiontracker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by joleary and noon on 2/19/16 at very late in the night. (early in the morning?)
 */
public class PhoneListenerService extends WearableListenerService {

//   WearableListenerServices don't need an iBinder or an onStartCommand: they just need an onMessageReceieved.
    private static final String CARD = "/card";
    private static final String ZIPCODE = "/zipcode";

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d("T", "in PhoneListenerService, got: " + messageEvent.getPath());
        String card_number = new String(messageEvent.getData());
        Log.d("T", "value is: " + card_number);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.activity_main, null);
        if( messageEvent.getPath().equalsIgnoreCase(CARD) ) {
            int card_num = Integer.parseInt(card_number);
            if (card_num < 3 && card_num >= 0) {
                Intent intent = new Intent(this, Detailed.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                String name = "";
                String party = "";
                String tag = "";
                String year = "";
                List<String> bills = new ArrayList<String>();
                Bundle billsBundle = new Bundle();
                Bundle committeesBundle = new Bundle();
                if (card_num == 0) {
                    intent.putExtra("IMG", R.drawable.blee);
                    name = "Barbara Lee";
                    party = "Democrat";
                    tag = "Representative";
                    year = "2017";
                    bills = Arrays.asList("Bill 1bl", "Bill 2bl", "Bill 3bl");
                    billsBundle.putSerializable("BILLSLIST", (Serializable) bills);
                    List<String> committees = Arrays.asList("Committee 1bl", "Committee 2bl", "Committee 3bl");
                    committeesBundle.putSerializable("COMMITTEESLIST", (Serializable) committees);
                } else if (card_num == 1) {
                    intent.putExtra("IMG", R.drawable.bboxer);
                    name = "Barbara Boxer";
                    party = "Democrat";
                    tag = "Senator";
                    year = "2017";
                    bills = Arrays.asList("Bill 1bb", "Bill 2bb", "Bill 3bb");
                    billsBundle.putSerializable("BILLSLIST", (Serializable) bills);
                    List<String> committees = Arrays.asList("Committee 1bb", "Committee 2bb", "Committee 3bb");
                    committeesBundle.putSerializable("COMMITTEESLIST", (Serializable) committees);
                } else if (card_num == 2) {
                    intent.putExtra("IMG", R.drawable.dfeinstein);
                    name = "Diane Feinstein";
                    party = "Democrat";
                    tag = "Senator";
                    year = "2019";
                    bills = Arrays.asList("Bill 1d", "Bill 2d", "Bill 3d");
                    billsBundle.putSerializable("BILLSLIST", (Serializable) bills);
                    List<String> committees = Arrays.asList("Committee 1d", "Committee 2d", "Committee 3d");
                    committeesBundle.putSerializable("COMMITTEESLIST", (Serializable) committees);
                }
                intent.putExtra("NAME", name);
                intent.putExtra("PARTY", party);
                intent.putExtra("TAG", tag);
                intent.putExtra("YEAR", year);
                intent.putExtra("BILLS", billsBundle);
                intent.putExtra("COMMITTEES", committeesBundle);
                startActivity(intent);
            }

            // Value contains the String we sent over in WatchToPhoneService, "good job"

//            Intent intent = new Intent(this, Detailed.class);
//            String name;
//            String party;
//            String tag;
//            String year;
//            List<String> bills = new ArrayList<String>();
//            Bundle billsBundle = new Bundle();
//            Bundle committeesBundle = new Bundle();
//
//            intent.putExtra("IMG", R.drawable.blee);
//            name = (String) person;
//            party = "Democrat";
//            tag = "Representative";
//            year = "2017";
//            bills = Arrays.asList("Bill 1bl", "Bill 2bl", "Bill 3bl");
//            billsBundle.putSerializable("BILLSLIST", (Serializable) bills);
//            List<String> committees = Arrays.asList("Committee 1bl", "Committee 2bl", "Committee 3bl");
//            committeesBundle.putSerializable("COMMITTEESLIST", (Serializable) committees);



//            intent.putExtra("PERSON", person);
//            startActivity(intent);
        }
        else if (messageEvent.getPath().equalsIgnoreCase(ZIPCODE)) {
            Intent sendIntent = new Intent(getBaseContext(), MainActivity.class);
            sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            sendIntent.putExtra("ZIPCODE", card_number);
            startActivity(sendIntent);

        }
        else {
            super.onMessageReceived( messageEvent );
        }

    }
}
