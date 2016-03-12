package com.cs160.neiluuu.electiontracker;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.location.LocationServices;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.*;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;
import com.twitter.sdk.android.tweetui.*;
import io.fabric.sdk.android.Fabric;

import io.fabric.sdk.android.Fabric;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MainActivity extends Activity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    //there's not much interesting happening. when the buttons are pressed, they start
    //the PhoneToWatchService with the cat name passed in.

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "mhzYoQy95oS48P26xsk3wXbk0";
    private static final String TWITTER_SECRET = "YecMHRAaVf1xsAvwbgc8rs7MpfyWXhqburA8dkJ3udA2HQhB9q";
    private final static String CONGRESS_API_KEY = "5246dfb27133470ca6d01000fdbaa611";

    public final static String REPUBLICAN = "Republican";
    public final static String DEMOCRAT = "Democrat";
    public final static String REP = "Representative";
    public final static String SENATOR = "Senator";

    public final static String REPCOLOR = "#E91D0E";
    public final static String DEMCOLOR = "#2F80ED";
    public final static String CONGRESS_URL = "https://congress.api.sunlightfoundation.com/";
    public final static String LOCATE_URL = "legislators/locate?";

    private ScrollView scrollReps;
    private Button goButton;
    private Button curLoc;
    private EditText zipCodeText;
    private ImageView repImg1;
    private TextView repName1;
    private TextView repPartyTag1;
    private TextView repEmail1;
    private TextView repWebsite1;
    private ImageView repImg2;
    private TextView repName2;
    private TextView repPartyTag2;
    private TextView repEmail2;
    private TextView repWebsite2;
    private ImageView repImg3;
    private TextView repName3;
    private TextView repPartyTag3;
    private TextView repEmail3;
    private TextView repWebsite3;
    private ImageView repImg4;
    private TextView repName4;
    private TextView repPartyTag4;
    private TextView repEmail4;
    private TextView repWebsite4;
    private GoogleApiClient mGoogleApiClient;
    private TwitterApiClient twitterApiClient;
    private Location mLastLocation;
    private TwitterLoginButton loginButton;
    private String rep1twitter_id;
    private String rep2twitter_id;
    private String rep3twitter_id;
    private String rep4twitter_id;
    private Bitmap bitmap1;
    private Bitmap bitmap2;
    private Bitmap bitmap3;
    private Bitmap bitmap4;
    private String[] people;
    private String[] years;
    private String zip;
    private String county;
    private String state;
    private String romney;
    private String obama;
    private String jsonString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_main);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();

        TwitterCore.getInstance().logInGuest(new Callback<AppSession>() {
            @Override
            public void success(com.twitter.sdk.android.core.Result<AppSession> result) {
                AppSession session = result.data;
                twitterApiClient = TwitterCore.getInstance().getApiClient(session);
            }

            @Override
            public void failure(TwitterException e) {
                Log.d("TwitterKit", "Login with Twitter failure", e);
            }
        });


        scrollReps = (ScrollView) findViewById(R.id.scrollReps);
        scrollReps.setVisibility(View.INVISIBLE);

        people = new String[4];
        years = new String[4];

        goButton = (Button) findViewById(R.id.go);
        curLoc = (Button) findViewById(R.id.curLoc);
        zipCodeText = (EditText) findViewById(R.id.zipCode);
        repImg1 = (ImageView) findViewById(R.id.repImg1);
        repName1 = (TextView) findViewById(R.id.repName1);
        repPartyTag1 = (TextView) findViewById(R.id.repPartyTag1);
        repEmail1 = (TextView) findViewById(R.id.repEmail1);
        repWebsite1 = (TextView) findViewById(R.id.repWebsite1);
        repImg2 = (ImageView) findViewById(R.id.repImg2);
        repName2 = (TextView) findViewById(R.id.repName2);
        repPartyTag2 = (TextView) findViewById(R.id.repPartyTag2);
        repEmail2 = (TextView) findViewById(R.id.repEmail2);
        repWebsite2 = (TextView) findViewById(R.id.repWebsite2);
        repImg3 = (ImageView) findViewById(R.id.repImg3);
        repName3 = (TextView) findViewById(R.id.repName3);
        repPartyTag3 = (TextView) findViewById(R.id.repPartyTag3);
        repEmail3 = (TextView) findViewById(R.id.repEmail3);
        repWebsite3 = (TextView) findViewById(R.id.repWebsite3);
        repImg4 = (ImageView) findViewById(R.id.repImg4);
        repName4 = (TextView) findViewById(R.id.repName4);
        repPartyTag4 = (TextView) findViewById(R.id.repPartyTag4);
        repEmail4 = (TextView) findViewById(R.id.repEmail4);
        repWebsite4 = (TextView) findViewById(R.id.repWebsite4);

        zip = "";
        obama = "69.0";
        romney = "29.6";
        county = "";
        state = "";

        Intent intent = getIntent();
        if (intent.getStringExtra("ZIPCODE") != null) {
            String zip = intent.getStringExtra("ZIPCODE");
            zipCodeText.setText(zip, TextView.BufferType.EDITABLE);
            zipCodeText.setSelection(zipCodeText.getText().toString().length());
            scrollReps.setVisibility(View.INVISIBLE);
            new RetrieveCongressmen().execute(zip);
            zipCodeText.setSelected(false);
        }

        View.OnClickListener myhandler = new View.OnClickListener() {
            public void onClick(View view) {
                String county = "";
                if (view.getId() == R.id.curLoc) {
                    Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                    if (mLastLocation == null) {
                        Toast.makeText(MainActivity.this, "Make sure location services are on", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        double lat = mLastLocation.getLatitude();
                        double lng = mLastLocation.getLongitude();
                        try {
                            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
                            Address address = addresses.get(0);
                            zip = address.getPostalCode();
                            zipCodeText.setText(zip, TextView.BufferType.EDITABLE);
                            zipCodeText.setSelection(zipCodeText.getText().toString().length());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                } else {
                    zip = zipCodeText.getText().toString();
                }

                new RetrieveCongressmen().execute(zip);
            }
        };

        InputStream inputStream = getResources().openRawResource(R.raw.newelectioncounty2012);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            try {
                Reader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                int n;
                while ((n = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);
            } finally {
                inputStream.close();
            }
        } catch(Exception e) {
            Log.e("ERROR", e.getMessage(), e);
        }

        jsonString = writer.toString();

        goButton.setOnClickListener(myhandler);
        curLoc.setOnClickListener(myhandler);


    }

    public void sendRep(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, Detailed.class);
        String name;
        String party;
        String tag;
        String year;
        String[] partyTag;
        String id;
        String twitterid;
        // Get data for intent
        switch (view.getId()) {
            case (R.id.rep1):
                repName1 = (TextView) findViewById(R.id.repName1);
                name = (String) repName1.getText();
                repPartyTag1 = (TextView) findViewById(R.id.repPartyTag1);
                partyTag = ((String) repPartyTag1.getText()).split(" ");
                party = partyTag[0];
                tag = partyTag[1];
                year = years[0];
                id = people[0];
                twitterid = rep1twitter_id;
                break;
            case (R.id.rep2):
                repName2 = (TextView) findViewById(R.id.repName2);
                name = (String) repName2.getText();
                repPartyTag2 = (TextView) findViewById(R.id.repPartyTag2);
                partyTag = ((String) repPartyTag2.getText()).split(" ");
                party = partyTag[0];
                tag = partyTag[1];
                year = years[1];
                id = people[1];
                twitterid = rep2twitter_id;
                break;
            case (R.id.rep3):
                repName3 = (TextView) findViewById(R.id.repName3);
                name = (String) repName3.getText();
                repPartyTag3 = (TextView) findViewById(R.id.repPartyTag3);
                partyTag = ((String) repPartyTag3.getText()).split(" ");
                party = partyTag[0];
                tag = partyTag[1];
                year = years[2];
                id = people[2];
                twitterid = rep3twitter_id;
                break;
            case (R.id.rep4):
                repName4 = (TextView) findViewById(R.id.repName4);
                name = (String) repName4.getText();
                repPartyTag4 = (TextView) findViewById(R.id.repPartyTag4);
                partyTag = ((String) repPartyTag4.getText()).split(" ");
                party = partyTag[0];
                tag = partyTag[1];
                year = years[3];
                id = people[3];
                twitterid = rep4twitter_id;
                break;
            default:
                name = "??????";
                party = "??????";
                tag = "??????";
                year = "??????";
                twitterid = "???????";
                id = null;
        }

        // Package the intent
        intent.putExtra("NAME", name);
        intent.putExtra("PARTY", party);
        intent.putExtra("TAG", tag);
        intent.putExtra("YEAR", year);
        intent.putExtra("ID", id);
        intent.putExtra("TWITTER", twitterid);
        startActivity(intent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Make sure that the loginButton hears the result from any
        // Activity that it triggered.
        loginButton.onActivityResult(requestCode, resultCode, data);
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

    public void getPartyColor(TextView tag, String party) {
        if (party == DEMOCRAT) {
            tag.setTextColor(Color.parseColor(DEMCOLOR));
        } else if (party == REPUBLICAN) {
            tag.setTextColor(Color.parseColor(REPCOLOR));
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            Log.d("Connected", mLastLocation.toString());
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        System.out.println("CONNECTION FAILED: " + connectionResult);
    }

    class RetrieveCongressmen extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... args) {
            String zip = args[0];
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
            if(response == null) {
                response = "THERE WAS AN ERROR";
            }
            else {
                try {
                    JSONObject jObject = new JSONObject(response);
                    // int[] indicesToPull = {0};

                    final JSONArray resultsArray = jObject.getJSONArray("results");
                    new GetCounty().execute("").get();
                    if (resultsArray.length() <= 0) {
                        Toast.makeText(MainActivity.this, "Invalid Zip Code or API Down", Toast.LENGTH_SHORT).show();
                    } else {

                        // TODO: Base this Tweet ID on some data from elsewhere in your app

                        TwitterCore.getInstance().logInGuest(new Callback<AppSession>() {
                            @Override
                            public void success(com.twitter.sdk.android.core.Result<AppSession> result) {
                                AppSession session = result.data;
                                TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient(session);
                                try {
                                    String name, party, pos, email, website;
                                    JSONObject cur = resultsArray.getJSONObject(0);
                                    rep1twitter_id = cur.getString("twitter_id");
                                    name = getName(cur);
                                    party = getParty(cur.getString("party"));
                                    pos = cur.getString("title");
                                    email = cur.getString("oc_email");
                                    website = cur.getString("website");
                                    people[0] = cur.getString("bioguide_id");
                                    years[0] = cur.getString("term_end");
                                    //TODO: Use a more specific parent
                                    final ViewGroup reps = (ViewGroup) findViewById(R.id.reps);
                                    StatusesService service = twitterApiClient.getStatusesService();
                                    service.userTimeline(null, rep1twitter_id, null, null, null, null, null, null, null, new Callback<List<Tweet>>() {


                                        @Override
                                        public void success(com.twitter.sdk.android.core.Result<List<Tweet>> result) {
                                            if (rep1twitter_id.contentEquals("null")) {
                                                TextView tweetView = new TextView(getApplicationContext());
                                                tweetView.setText("Politician has no twitter");
                                                tweetView.setTextColor(Color.parseColor("#4099FF"));
                                                tweetView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                                reps.removeViewAt(1);
                                                reps.addView(tweetView, 1);
                                            } else {
                                                String imageURL = result.data.get(0).user.profileImageUrl;
                                                new LoadImage1().execute(imageURL);
                                                CompactTweetView tweetView = new CompactTweetView(MainActivity.this, result.data.get(0));
                                                reps.removeViewAt(1);
                                                reps.addView(tweetView, 1);
                                            }
                                        }

                                        @Override
                                        public void failure(TwitterException e) {

                                            Log.d("TwitterKit", "Load Tweet failure", e);
                                        }
                                    });


                                    repImg1.setImageResource(R.drawable.basic);
                                    repName1.setText(name);
                                    repPartyTag1.setText(party + " " + pos);
                                    getPartyColor(repPartyTag1, party);
                                    repEmail1.setText(email);
                                    repWebsite1.setText(website);

                                    cur = resultsArray.getJSONObject(resultsArray.length() - 2);
                                    rep2twitter_id = cur.getString("twitter_id");
                                    name = getName(cur);
                                    party = getParty(cur.getString("party"));
                                    pos = cur.getString("title");
                                    email = cur.getString("oc_email");
                                    website = cur.getString("website");
                                    people[1] = cur.getString("bioguide_id");
                                    years[1] = cur.getString("term_end");
                                    repImg2.setImageResource(R.drawable.basic);
                                    repName2.setText(name);
                                    repPartyTag2.setText(party + " " + pos);
                                    getPartyColor(repPartyTag2, party);
                                    repEmail2.setText(email);
                                    repWebsite2.setText(website);


                                    // TODO: Base this Tweet ID on some data from elsewhere in your app
                                    service.userTimeline(null, rep2twitter_id, null, null, null, null, null, null, null, new Callback<List<Tweet>>() {


                                        @Override
                                        public void success(com.twitter.sdk.android.core.Result<List<Tweet>> result) {
                                            if (rep2twitter_id.contentEquals("null")) {
                                                TextView tweetView = new TextView(getApplicationContext());
                                                tweetView.setText("Politician has no twitter");
                                                tweetView.setTextColor(Color.parseColor("#4099FF"));
                                                tweetView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                                reps.removeViewAt(3);
                                                reps.addView(tweetView, 3);
                                            } else {
                                                try {
                                                    String imageURL = result.data.get(0).user.profileImageUrl;
                                                    new LoadImage2().execute(imageURL);
                                                    CompactTweetView tweetView = new CompactTweetView(MainActivity.this, result.data.get(0));
                                                    reps.removeViewAt(3);
                                                    reps.addView(tweetView, 3);
                                                } catch (Exception e) {
                                                    Log.e("ERROR", e.getMessage(), e);
                                                }
                                            }
                                        }

                                        @Override
                                        public void failure(TwitterException e) {
                                            Log.d("TwitterKit", "Load Tweet failure", e);
                                        }
                                    });

                                    cur = resultsArray.getJSONObject(resultsArray.length() - 1);
                                    rep3twitter_id = cur.getString("twitter_id");
                                    name = getName(cur);
                                    party = getParty(cur.getString("party"));
                                    pos = cur.getString("title");
                                    email = cur.getString("oc_email");
                                    website = cur.getString("website");
                                    people[2] = cur.getString("bioguide_id");
                                    years[2] = cur.getString("term_end");
                                    repImg3.setImageResource(R.drawable.basic);
                                    repName3.setText(name);
                                    repPartyTag3.setText(party + " " + pos);
                                    getPartyColor(repPartyTag3, party);
                                    repEmail3.setText(email);
                                    repWebsite3.setText(website);
                                    // TODO: Base this Tweet ID on some data from elsewhere in your app
                                    service.userTimeline(null, rep3twitter_id, null, null, null, null, null, null, null, new Callback<List<Tweet>>() {


                                        @Override
                                        public void success(com.twitter.sdk.android.core.Result<List<Tweet>> result) {
                                            if (rep3twitter_id.contentEquals("null")) {
                                                TextView tweetView = new TextView(getApplicationContext());
                                                tweetView.setText("Politician has no twitter");
                                                tweetView.setTextColor(Color.parseColor("#4099FF"));
                                                tweetView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                                reps.removeViewAt(5);
                                                reps.addView(tweetView, 5);
                                            } else {
                                                String imageURL = result.data.get(0).user.profileImageUrl;
                                                new LoadImage3().execute(imageURL);
                                                CompactTweetView tweetView = new CompactTweetView(MainActivity.this, result.data.get(0));
                                                reps.removeViewAt(5);
                                                reps.addView(tweetView, 5);
                                            }
                                        }

                                        @Override
                                        public void failure(TwitterException e) {
                                            Log.d("TwitterKit", "Load Tweet failure", e);
                                        }
                                    });
                                    if (resultsArray.length() > 3) {
                                        cur = resultsArray.getJSONObject(1);
                                        rep4twitter_id = cur.getString("twitter_id");
                                        name = getName(cur);
                                        party = getParty(cur.getString("party"));
                                        pos = cur.getString("title");
                                        email = cur.getString("oc_email");
                                        website = cur.getString("website");
                                        people[3] = cur.getString("bioguide_id");
                                        years[3] = cur.getString("term_end");
                                        repImg4.setImageResource(R.drawable.basic);
                                        repName4.setText(name);
                                        repPartyTag4.setText(party + " " + pos);
                                        getPartyColor(repPartyTag4, party);
                                        repEmail4.setText(email);
                                        repWebsite4.setText(website);
                                        reps.getChildAt(7).setVisibility(View.VISIBLE);
                                        reps.getChildAt(6).setVisibility(View.VISIBLE);
                                        // TODO: Base this Tweet ID on some data from elsewhere in your app
                                        service.userTimeline(null, rep4twitter_id, null, null, null, null, null, null, null, new Callback<List<Tweet>>() {


                                            @Override
                                            public void success(com.twitter.sdk.android.core.Result<List<Tweet>> result) {
                                                if (rep4twitter_id.contentEquals("null")) {
                                                    TextView tweetView = new TextView(getApplicationContext());
                                                    tweetView.setText("Politician has no twitter");
                                                    tweetView.setTextColor(Color.parseColor("#4099FF"));
                                                    tweetView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                                    reps.removeViewAt(7);
                                                    reps.addView(tweetView, 7);
                                                } else {
                                                    String imageURL = result.data.get(0).user.profileImageUrl;
                                                    new LoadImage4().execute(imageURL);
                                                    CompactTweetView tweetView = new CompactTweetView(MainActivity.this, result.data.get(0));
                                                    reps.removeViewAt(7);
                                                    reps.addView(tweetView, 7);
                                                }
                                            }

                                            @Override
                                            public void failure(TwitterException e) {
                                                Log.d("TwitterKit", "Load Tweet failure", e);
                                            }
                                        });
                                    } else {
                                        reps.getChildAt(7).setVisibility(View.GONE);
                                        reps.getChildAt(6).setVisibility(View.GONE);
                                    }

                                    scrollReps.setVisibility(View.VISIBLE);
                                    zipCodeText.setSelected(false);

                                    String[] partyTag;
                                    String tag;
                                    Intent sendIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
                                    sendIntent.putExtra("ZIPCODE", zipCodeText.getText().toString());
                                    sendIntent.putExtra("OBAMA", obama);
                                    sendIntent.putExtra("ROMNEY", romney);
                                    sendIntent.putExtra("COUNTY", county);
                                    sendIntent.putExtra("STATE", state);
                                    sendIntent.putExtra("PERSON1", repName1.getText().toString());
                                    partyTag = ((String) repPartyTag1.getText()).split(" ");
                                    party = partyTag[0];
                                    tag = partyTag[1];
                                    sendIntent.putExtra("PERSON1TITLE", tag);
                                    sendIntent.putExtra("PERSON1PARTY", party);
                                    sendIntent.putExtra("PERSON1TWITTER", rep1twitter_id);
                                    sendIntent.putExtra("PERSON1YEAR", years[0]);
                                    sendIntent.putExtra("PERSON2", repName2.getText().toString());
                                    partyTag = ((String) repPartyTag2.getText()).split(" ");
                                    party = partyTag[0];
                                    tag = partyTag[1];
                                    sendIntent.putExtra("PERSON2TITLE", tag);
                                    sendIntent.putExtra("PERSON2PARTY", party);
                                    sendIntent.putExtra("PERSON2TWITTER", rep2twitter_id);
                                    sendIntent.putExtra("PERSON2YEAR", years[1]);
                                    sendIntent.putExtra("PERSON3", repName3.getText().toString());
                                    partyTag = ((String) repPartyTag3.getText()).split(" ");
                                    party = partyTag[0];
                                    tag = partyTag[1];
                                    sendIntent.putExtra("PERSON3TITLE", tag);
                                    sendIntent.putExtra("PERSON3PARTY", party);
                                    sendIntent.putExtra("PERSON3TWITTER", rep3twitter_id);
                                    sendIntent.putExtra("PERSON3YEAR", years[2]);
                                    if (reps.getChildAt(6).getVisibility() != View.GONE) {
                                        sendIntent.putExtra("PERSON4", repName4.getText().toString());
                                        partyTag = ((String) repPartyTag4.getText()).split(" ");
                                        party = partyTag[0];
                                        tag = partyTag[1];
                                        sendIntent.putExtra("PERSON4TITLE", tag);
                                        sendIntent.putExtra("PERSON4PARTY", party);
                                        sendIntent.putExtra("PERSON4TWITTER", rep4twitter_id);
                                        sendIntent.putExtra("PERSON4YEAR", years[3]);
                                    }
                                    startService(sendIntent);
                                    scrollReps.setVisibility(View.VISIBLE);
                                } catch (Exception e) {
                                    Log.e("ERROR", e.getMessage(), e);
                                }
                            }

                            @Override
                            public void failure(TwitterException e) {
                                e.printStackTrace();
                            }
                        });

                    }
                }
                catch(Exception e) {
                    Log.e("ERROR", e.getMessage(), e);
                }
            }
        }
    }
    private class LoadImage1 extends AsyncTask<String, String, Bitmap> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
        protected Bitmap doInBackground(String... args) {
            try {
                bitmap1 = BitmapFactory.decodeStream((InputStream)new URL(args[0]).getContent());

            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap1;
        }

        protected void onPostExecute(Bitmap image) {

            if(image != null){
                repImg1.setImageBitmap(image);

            }else{
                Toast.makeText(MainActivity.this, "Image Does Not exist or Network Error", Toast.LENGTH_SHORT).show();

            }
        }
    }
    private class LoadImage2 extends AsyncTask<String, String, Bitmap> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
        protected Bitmap doInBackground(String... args) {
            try {
                bitmap2 = BitmapFactory.decodeStream((InputStream)new URL(args[0]).getContent());

            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap2;
        }

        protected void onPostExecute(Bitmap image) {

            if(image != null){
                repImg2.setImageBitmap(image);

            }else{
                Toast.makeText(MainActivity.this, "Image Does Not exist or Network Error", Toast.LENGTH_SHORT).show();

            }
        }
    }
    private class LoadImage3 extends AsyncTask<String, String, Bitmap> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
        protected Bitmap doInBackground(String... args) {
            try {
                bitmap3 = BitmapFactory.decodeStream((InputStream)new URL(args[0]).getContent());

            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap3;
        }

        protected void onPostExecute(Bitmap image) {

            if(image != null){
                repImg3.setImageBitmap(image);

            }else{
                Toast.makeText(MainActivity.this, "Image Does Not exist or Network Error", Toast.LENGTH_SHORT).show();

            }
        }
    }
    private class LoadImage4 extends AsyncTask<String, String, Bitmap> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
        protected Bitmap doInBackground(String... args) {
            try {
                bitmap4 = BitmapFactory.decodeStream((InputStream)new URL(args[0]).getContent());

            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap4;
        }

        protected void onPostExecute(Bitmap image) {

            if(image != null){
                repImg4.setImageBitmap(image);

            }else{
                Toast.makeText(MainActivity.this, "Image Does Not exist or Network Error", Toast.LENGTH_SHORT).show();

            }
        }
    }

    private class GetCounty extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
        protected String doInBackground(String... args) {
            // Do some validation here

            try {
                URL url = new URL("http://maps.googleapis.com/maps/api/geocode/json?address=" + zip + "&region=us");
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
                JSONObject jObject2 = resultsArray.getJSONObject(0);
                JSONArray addressComponents = jObject2.getJSONArray("address_components");
                int countyIndex = -1;
                int stateIndex = -1;
                for (int i = 0; i < addressComponents.length(); i++) {
                    JSONObject addressComponent = addressComponents.getJSONObject(i);
                    JSONArray types = addressComponent.getJSONArray("types");
                    for (int j = 0; j < types.length(); j++) {
                        String type = types.getString(j);
                        if (type.contentEquals("administrative_area_level_2")) {
                            countyIndex = i;
                        }
                        if (type.contentEquals("administrative_area_level_1")) {
                            stateIndex = i;
                        }
                    }
                }
                if (stateIndex != -1) {
                    county = addressComponents.getJSONObject(countyIndex).getString("short_name");
                    state = addressComponents.getJSONObject(stateIndex).getString("short_name");
                    JSONObject info = new JSONObject(jsonString);
                    String countyString = county+", "+state;
                    if (info.has(countyString)) {
                        JSONObject countyResults = info.getJSONObject(countyString);
                        obama = countyResults.getString("obama");
                        romney = countyResults.getString("romney");
                    } else {
                        Toast.makeText(MainActivity.this,"No info for: " +countyString, Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);
            }
        }
    }
}
