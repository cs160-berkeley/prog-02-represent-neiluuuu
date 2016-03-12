package com.cs160.neiluuu.electiontracker;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.cs160.neiluuu.electiontracker.R;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.AppSession;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;
import com.twitter.sdk.android.tweetui.CompactTweetView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import io.fabric.sdk.android.Fabric;

public class Detailed extends Activity {

    private ScrollView scrollView;
    private ImageView img;
    private TextView name;
    private TextView tag;
    private TextView party;
    private TextView term;
    private TextView committees;
    private TextView bills;
    private TwitterApiClient twitterApiClient;
    private Bitmap bitmap;
    private final static String CONGRESS_API_KEY = "5246dfb27133470ca6d01000fdbaa611";
    public final static String CONGRESS_URL = "https://congress.api.sunlightfoundation.com/";
    public final static String BILLS_URL = "bills?";
    public final static String COMMITTEES_URL = "committees?";

    private static final String TWITTER_KEY = "mhzYoQy95oS48P26xsk3wXbk0";
    private static final String TWITTER_SECRET = "YecMHRAaVf1xsAvwbgc8rs7MpfyWXhqburA8dkJ3udA2HQhB9q";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_detailed);

        final View main = findViewById(R.id.scrollView);
        main.setVisibility(View.INVISIBLE);

        final Intent intent = getIntent();
        final Bundle extras = intent.getExtras();
        final String id = intent.getStringExtra("ID");
        final String nameVal = intent.getStringExtra("NAME");
        final String tagVal = intent.getStringExtra("TAG");
        final String partyVal = intent.getStringExtra("PARTY");
        final String yearVal = intent.getStringExtra("YEAR");
        final String twitterid = intent.getStringExtra("TWITTER");

        int imgId = extras.getInt("IMG");

        final TextView name = (TextView) findViewById(R.id.name);
        final TextView tag = (TextView) findViewById(R.id.tag);
        final TextView party = (TextView) findViewById(R.id.party);
        final TextView term = (TextView) findViewById(R.id.term);
        final TextView committees = (TextView) findViewById(R.id.committees);
        final TextView bills = (TextView) findViewById(R.id.bills);
        final ImageView img = (ImageView) findViewById(R.id.img);
        TwitterCore.getInstance().logInGuest(new Callback<AppSession>() {
            @Override
            public void success(com.twitter.sdk.android.core.Result<AppSession> result) {
                AppSession session = result.data;
                TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient(session);
                StatusesService service = twitterApiClient.getStatusesService();
                service.userTimeline(null, twitterid, null, null, null, null, null, null, null, new Callback<List<Tweet>>() {


                    @Override
                    public void success(com.twitter.sdk.android.core.Result<List<Tweet>> result) {
                        if (twitterid.contentEquals("null")) {
                            Toast.makeText(Detailed.this, "Image Does Not exist or Network Error", Toast.LENGTH_SHORT).show();
                            name.setText(nameVal);
                            tag.setText(tagVal);
                            setPartyLetter(partyVal, party);
                            term.setText(term.getText() + " " + yearVal.substring(0, 4));
                            new LoadCommittees().execute(id);
                            new LoadBills().execute(id);
                        } else {
                            String imageURL = result.data.get(0).user.profileImageUrl;
                            imageURL = imageURL.replace("_normal", "");
                            new LoadImage().execute(imageURL);
                            name.setText(nameVal);
                            tag.setText(tagVal);
                            setPartyLetter(partyVal, party);
                            term.setText(term.getText() + " " + yearVal.substring(0, 4));
                            new LoadCommittees().execute(id);
                            new LoadBills().execute(id);
                        }
                        main.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void failure(TwitterException e) {

                        Log.d("TwitterKit", "Load Tweet failure", e);
                    }
                });
            }

            @Override
            public void failure(TwitterException e) {
                Log.d("TwitterKit", "Load Tweet failure", e);
            }
        });

    }

    void setPartyLetter(String party, TextView view) {
        if (party.equals(MainActivity.REPUBLICAN)) {
            view.setBackgroundResource(R.drawable.rep);
            view.setText("R");
        }
        else if (party.equals(MainActivity.DEMOCRAT)) {
            view.setBackgroundResource(R.drawable.dem);
            view.setText("D");
        }
        else {
            view.setText("?");
            view.setTextColor(Color.BLACK);
            view.setBackgroundColor(Color.WHITE);
        }
    }

    private class LoadBills extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
        protected String doInBackground(String... args) {
            String id = args[0];
            try {
                URL url = new URL(CONGRESS_URL + BILLS_URL + "sponsor_id=" + id + "&apikey=" + CONGRESS_API_KEY);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                }
                finally{
                    urlConnection.disconnect();
                }
            }
            catch(Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        protected void onPostExecute(String response) {
            try {
                JSONObject jObject = new JSONObject(response);
                JSONArray resultsArray = jObject.getJSONArray("results");
                String title, year;
                TextView bills = (TextView) findViewById(R.id.bills);
                int length = Math.min(5, resultsArray.length());
                for (int i = 0; i < length; i++) {
                    JSONObject cur = resultsArray.getJSONObject(i);
                    title = cur.getString("short_title");
                    year = cur.getString("introduced_on").substring(0,4);
                    if (!title.contentEquals("null")) {
                        bills.setText(bills.getText() + "\n     " + title + " (" + year + ")");
                    }
                }

            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);
            }
        }
    }
    private class LoadCommittees extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
        protected String doInBackground(String... args) {
            String id = args[0];
            try {
                URL url = new URL(CONGRESS_URL + COMMITTEES_URL + "member_ids=" + id + "&apikey=" + CONGRESS_API_KEY);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                }
                finally{
                    urlConnection.disconnect();
                }
            }
            catch(Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        protected void onPostExecute(String response) {
            try {
                JSONObject jObject = new JSONObject(response);
                JSONArray resultsArray = jObject.getJSONArray("results");
                String name;
                TextView committees = (TextView) findViewById(R.id.committees);
                int length = Math.min(5, resultsArray.length());
                for (int i = 0; i < length; i++) {
                    JSONObject cur = resultsArray.getJSONObject(i);
                    name = cur.getString("name");
                    if (!name.contentEquals("null")) {
                        committees.setText(committees.getText() + "\n     " + name);
                    }
                }

            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);
            }
        }
    }

    private class LoadImage extends AsyncTask<String, String, Bitmap> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
        protected Bitmap doInBackground(String... args) {
            try {
                bitmap = BitmapFactory.decodeStream((InputStream)new URL(args[0]).getContent());

            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap image) {

            if(image != null){
                ImageView img = (ImageView) findViewById(R.id.img);
                img.setImageBitmap(image);

            }else{
                Toast.makeText(Detailed.this, "Image Does Not exist or Network Error", Toast.LENGTH_SHORT).show();

            }
        }
    }

}
