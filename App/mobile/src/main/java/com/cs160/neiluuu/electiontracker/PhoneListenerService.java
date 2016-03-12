package com.cs160.neiluuu.electiontracker;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
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
    private final static String CONGRESS_API_KEY = "5246dfb27133470ca6d01000fdbaa611";
    public final static String CONGRESS_URL = "https://congress.api.sunlightfoundation.com/";
    public final static String LOCATE_URL = "legislators/locate?";

    public final static String REPUBLICAN = "Republican";
    public final static String DEMOCRAT = "Democrat";

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d("T", "in PhoneListenerService, got: " + messageEvent.getPath());
        String[] data = (new String(messageEvent.getData())).split(" ");
        String card_number = data[0];
        Log.d("T", "value is: " + card_number);
        if( messageEvent.getPath().equalsIgnoreCase(CARD) ) {
            int card_num = Integer.parseInt(card_number);
            if (card_num < 4 && card_num >= 0) {
                String zip = data[1];
                new RetrieveDetailed().execute(zip, Integer.toString(card_num));
//                Intent intent = new Intent(this, Detailed.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                String name = "";
//                String party = "";
//                String tag = "";
//                String year = "";
//                List<String> bills = new ArrayList<String>();
//                Bundle billsBundle = new Bundle();
//                Bundle committeesBundle = new Bundle();
//                if (card_num == 0) {
////                    intent.putExtra("IMG", R.drawable.blee);
////                    name = "Barbara Lee";
////                    party = "Democrat";
////                    tag = "Representative";
////                    year = "2017";
////                    bills = Arrays.asList("Bill 1bl", "Bill 2bl", "Bill 3bl");
////                    billsBundle.putSerializable("BILLSLIST", (Serializable) bills);
////                    List<String> committees = Arrays.asList("Committee 1bl", "Committee 2bl", "Committee 3bl");
////                    committeesBundle.putSerializable("COMMITTEESLIST", (Serializable) committees);
//                } else if (card_num == 1) {
//                    intent.putExtra("IMG", R.drawable.bboxer);
//                    name = "Barbara Boxer";
//                    party = "Democrat";
//                    tag = "Senator";
//                    year = "2017";
//                    bills = Arrays.asList("Bill 1bb", "Bill 2bb", "Bill 3bb");
//                    billsBundle.putSerializable("BILLSLIST", (Serializable) bills);
//                    List<String> committees = Arrays.asList("Committee 1bb", "Committee 2bb", "Committee 3bb");
//                    committeesBundle.putSerializable("COMMITTEESLIST", (Serializable) committees);
//                } else if (card_num == 2) {
//                    intent.putExtra("IMG", R.drawable.dfeinstein);
//                    name = "Diane Feinstein";
//                    party = "Democrat";
//                    tag = "Senator";
//                    year = "2019";
//                    bills = Arrays.asList("Bill 1d", "Bill 2d", "Bill 3d");
//                    billsBundle.putSerializable("BILLSLIST", (Serializable) bills);
//                    List<String> committees = Arrays.asList("Committee 1d", "Committee 2d", "Committee 3d");
//                    committeesBundle.putSerializable("COMMITTEESLIST", (Serializable) committees);
//                }
//                intent.putExtra("NAME", name);
//                intent.putExtra("PARTY", party);
//                intent.putExtra("TAG", tag);
//                intent.putExtra("YEAR", year);
//                intent.putExtra("BILLS", billsBundle);
//                intent.putExtra("COMMITTEES", committeesBundle);
//                startActivity(intent);
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

    public String getName(JSONObject cur) {
        try {
            if (!cur.getString("middle_name").contentEquals("null")) {
                return cur.getString("first_name") + " " + cur.getString("middle_name") + " " + cur.getString("last_name");
            } else {
                return cur.getString("first_name") + " " + cur.getString("last_name");
            }
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage(), e);
            return "";
        }
    }

    public String getParty(String party) {
        if (party.contentEquals("D")) {
            return DEMOCRAT;
        }
        else if (party.contentEquals("R")) {
            return REPUBLICAN;
        }
        else {
            return "Independent";
        }
    }

    class RetrieveDetailed extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... args) {
            String zip = args[0];
            int cardNumber = Integer.parseInt(args[1]);

            // Do some validation here

            try {
                URL url = new URL(CONGRESS_URL + LOCATE_URL + "zip=" + zip + "&apikey=" + CONGRESS_API_KEY);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    try {
                        JSONObject jObject = new JSONObject(stringBuilder.toString());
                        JSONArray resultsArray = jObject.getJSONArray("results");
                        if (cardNumber == 1) {
                            cardNumber = resultsArray.length() - 2;
                        } else if (cardNumber == 2) {
                            cardNumber = resultsArray.length() - 1;
                        } else if (cardNumber == 3) {
                            cardNumber = 1;
                        }
                        JSONObject cur = resultsArray.getJSONObject(cardNumber);
                        return cur.toString();
                    } catch (Exception e) {
                        Log.e("ERROR", e.getMessage(), e);
                        return null;
                    }
                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        protected void onPostExecute(String response) {
            if (response == null) {
                response = "THERE WAS AN ERROR";
            } else {
                try {
                    JSONObject cur = new JSONObject(response);
                    String name, party, pos, email, website, year, id, twitterid;
                    Intent intent = new Intent(getApplicationContext(), Detailed.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    name = getName(cur);
                    party = getParty(cur.getString("party"));
                    pos = cur.getString("title");
                    id = cur.getString("bioguide_id");
                    year = cur.getString("term_end");
                    twitterid = cur.getString("twitter_id");
                    intent.putExtra("NAME", name);
                    intent.putExtra("PARTY", party);
                    intent.putExtra("TAG", pos);
                    intent.putExtra("YEAR", year);
                    intent.putExtra("ID", id);
                    intent.putExtra("TWITTER", twitterid);
                    startActivity(intent);
                } catch (Exception e) {
                    Log.e("ERROR", e.getMessage(), e);
                }
            }
        }
    }
}
