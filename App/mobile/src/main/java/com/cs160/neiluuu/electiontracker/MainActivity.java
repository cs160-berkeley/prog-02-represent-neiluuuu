package com.cs160.neiluuu.electiontracker;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends Activity {
    //there's not much interesting happening. when the buttons are pressed, they start
    //the PhoneToWatchService with the cat name passed in.

    public final static String REPUBLICAN = "Republican";
    public final static String DEMOCRAT = "Democrat";
    public final static String REP = "Representative";
    public final static String SENATOR = "Senator";

    public final static String REPCOLOR = "#E91D0E";
    public final static String DEMCOLOR = "#2F80ED";

    private ScrollView scrollReps;
    private Button goButton;
    private Button curLoc;
    private EditText zipCodeText;
    private ImageView repImg1;
    private TextView repName1;
    private TextView repPartyTag1;
    private TextView repEmail1;
    private TextView repWebsite1;
    private TextView repTweet1;
    private ImageView repImg2;
    private TextView repName2;
    private TextView repPartyTag2;
    private TextView repEmail2;
    private TextView repWebsite2;
    private TextView repTweet2;
    private ImageView repImg3;
    private TextView repName3;
    private TextView repPartyTag3;
    private TextView repEmail3;
    private TextView repWebsite3;
    private TextView repTweet3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scrollReps = (ScrollView) findViewById(R.id.scrollReps);
        scrollReps.setVisibility(View.INVISIBLE);

        goButton = (Button) findViewById(R.id.go);
        curLoc = (Button) findViewById(R.id.curLoc);
        zipCodeText = (EditText) findViewById(R.id.zipCode);
        repImg1 = (ImageView) findViewById(R.id.repImg1);
        repName1 = (TextView) findViewById(R.id.repName1);
        repPartyTag1 = (TextView) findViewById(R.id.repPartyTag1);
        repEmail1 = (TextView) findViewById(R.id.repEmail1);
        repWebsite1 = (TextView) findViewById(R.id.repWebsite1);
        repTweet1 = (TextView) findViewById(R.id.repTweet1);
        repImg2 = (ImageView) findViewById(R.id.repImg2);
        repName2 = (TextView) findViewById(R.id.repName2);
        repPartyTag2 = (TextView) findViewById(R.id.repPartyTag2);
        repEmail2 = (TextView) findViewById(R.id.repEmail2);
        repWebsite2 = (TextView) findViewById(R.id.repWebsite2);
        repTweet2 = (TextView) findViewById(R.id.repTweet2);
        repImg3 = (ImageView) findViewById(R.id.repImg3);
        repName3 = (TextView) findViewById(R.id.repName3);
        repPartyTag3 = (TextView) findViewById(R.id.repPartyTag3);
        repEmail3 = (TextView) findViewById(R.id.repEmail3);
        repWebsite3 = (TextView) findViewById(R.id.repWebsite3);
        repTweet3 = (TextView) findViewById(R.id.repTweet3);

        Intent intent = getIntent();
        if (intent.getStringExtra("ZIPCODE") != null) {
            zipCodeText.setText(intent.getStringExtra("ZIPCODE"), TextView.BufferType.EDITABLE);
            zipCodeText.setSelection(zipCodeText.getText().toString().length());
            String party, pos;
            repImg1.setImageResource(R.drawable.lzeldin);
            repName1.setText("Lee Zeldin");
            party = REPUBLICAN;
            pos = REP;
            repPartyTag1.setText(party + " " + pos);
            getPartyColor(repPartyTag1, party);
            repEmail1.setText("email@website.com");
            repWebsite1.setText("zeldin.house.gov");
            repTweet1.setText("Latest Tweet: Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque interdum rutrum sodales. Nullam mattis fermentum libero, non volutpat.");
            repImg2.setImageResource(R.drawable.cschumer);
            repName2.setText("Chuck Schumer");
            party = DEMOCRAT;
            pos = SENATOR;
            repPartyTag2.setText(party + " " + pos);
            getPartyColor(repPartyTag2, party);
            repEmail2.setText("email@website.com");
            repWebsite2.setText("schumer.senate.gov");
            repTweet2.setText("Latest Tweet: Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque interdum rutrum sodales. Nullam mattis fermentum libero, non volutpat.");
            repImg3.setImageResource(R.drawable.kgillibrand);
            repName3.setText("Kirsten Gillibrand");
            party = DEMOCRAT;
            pos = SENATOR;
            repPartyTag3.setText(party + " " + pos);
            getPartyColor(repPartyTag3, party);
            repEmail3.setText("email@website.com");
            repWebsite3.setText("gillibrand.senate.gov");
            repTweet3.setText("Latest Tweet: Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque interdum rutrum sodales. Nullam mattis fermentum libero, non volutpat.");
            scrollReps.setVisibility(View.VISIBLE);
            zipCodeText.setSelected(false);
        }

        View.OnClickListener myhandler = new View.OnClickListener() {
            public void onClick(View view) {
                if (view.getId() == R.id.curLoc) {
                    zipCodeText.setText("94704", TextView.BufferType.EDITABLE);
                    zipCodeText.setSelection(zipCodeText.getText().toString().length());
                }
                String party, pos;
                repImg1.setImageResource(R.drawable.blee);
                repName1.setText("Barbara Lee");
                party = DEMOCRAT;
                pos = REP;
                repPartyTag1.setText(party + " " + pos);
                getPartyColor(repPartyTag1, party);
                repEmail1.setText("email@website.com");
                repWebsite1.setText("lee.house.gov");
                repTweet1.setText("Latest Tweet: Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque interdum rutrum sodales. Nullam mattis fermentum libero, non volutpat.");
                repImg2.setImageResource(R.drawable.bboxer);
                repName2.setText("Barbara Boxer");
                party = DEMOCRAT;
                pos = SENATOR;
                repPartyTag2.setText(party + " " + pos);
                getPartyColor(repPartyTag2, party);
                repEmail2.setText("email@website.com");
                repWebsite2.setText("boxer.senate.gov");
                repTweet2.setText("Latest Tweet: Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque interdum rutrum sodales. Nullam mattis fermentum libero, non volutpat.");
                repImg3.setImageResource(R.drawable.dfeinstein);
                repName3.setText("Diane Feinstein");
                party = DEMOCRAT;
                pos = SENATOR;
                repPartyTag3.setText(party + " " + pos);
                getPartyColor(repPartyTag3, party);
                repEmail3.setText("email@website.com");
                repWebsite3.setText("feinstein.senate.gov");
                repTweet3.setText("Latest Tweet: Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque interdum rutrum sodales. Nullam mattis fermentum libero, non volutpat.");
                scrollReps.setVisibility(View.VISIBLE);
                zipCodeText.setSelected(false);

                Intent sendIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
                sendIntent.putExtra("ZIPCODE", zipCodeText.getText().toString());
                startService(sendIntent);
            }
        };

        goButton.setOnClickListener(myhandler);
        curLoc.setOnClickListener(myhandler);

//        mLexyButton = (Button) findViewById(R.id.lexy_btn);
//
//        mFredButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent sendIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
//                sendIntent.putExtra("CAT_NAME", "Fred");
//                startService(sendIntent);
//            }
//        });
//
//        mLexyButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent sendIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
//                sendIntent.putExtra("CAT_NAME", "Lexy");
//                startService(sendIntent);
//            }
//        });

    }

    public void sendRep(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, Detailed.class);
        String name;
        String party;
        String tag;
        String year;
        String[] partyTag;
        List<String> bills = new ArrayList<String>();
        Bundle billsBundle = new Bundle();
        Bundle committeesBundle = new Bundle();
        // Get data for intent
        switch (view.getId()) {
            case (R.id.rep1):
                intent.putExtra("IMG", R.drawable.blee);
                repName1 = (TextView) findViewById(R.id.repName1);
                name = (String) repName1.getText();
                repPartyTag1 = (TextView) findViewById(R.id.repPartyTag1);
                partyTag = ((String) repPartyTag1.getText()).split(" ");
                party = partyTag[0];
                tag = partyTag[1];
                year = "2017";
                bills = Arrays.asList("Bill 1bl", "Bill 2bl", "Bill 3bl");
                billsBundle.putSerializable("BILLSLIST", (Serializable) bills);
                List<String> committees = Arrays.asList("Committee 1bl", "Committee 2bl", "Committee 3bl");
                committeesBundle.putSerializable("COMMITTEESLIST", (Serializable) committees);
                break;
            case (R.id.rep2):
                intent.putExtra("IMG", R.drawable.bboxer);
                repName2 = (TextView) findViewById(R.id.repName2);
                name = (String) repName2.getText();
                repPartyTag2 = (TextView) findViewById(R.id.repPartyTag2);
                partyTag = ((String)repPartyTag2.getText()).split(" ");
                party = partyTag[0];
                tag = partyTag[1];
                year = "2017";
                bills = Arrays.asList("Bill 1bb", "Bill 2bb", "Bill 3bb");
                billsBundle.putSerializable("BILLSLIST",(Serializable)bills);
                committees = Arrays.asList("Committee 1bb", "Committee 2bb", "Committee 3bb");
                committeesBundle.putSerializable("COMMITTEESLIST",(Serializable)committees);
                break;
            case (R.id.rep3):
                intent.putExtra("IMG", R.drawable.dfeinstein);
                repName3 = (TextView) findViewById(R.id.repName3);
                name = (String) repName3.getText();
                repPartyTag3 = (TextView) findViewById(R.id.repPartyTag3);
                partyTag = ((String)repPartyTag3.getText()).split(" ");
                party = partyTag[0];
                tag = partyTag[1];
                year = "2019";
                bills = Arrays.asList("Bill 1d", "Bill 2d", "Bill 3d");
                billsBundle.putSerializable("BILLSLIST",(Serializable)bills);
                committees = Arrays.asList("Committee 1d", "Committee 2d", "Committee 3d");
                committeesBundle.putSerializable("COMMITTEESLIST",(Serializable)committees);
                break;
            default:
                name = "??????";
                party = "??????";
                tag = "??????";
                year = "??????";
                billsBundle.putSerializable("BILLSLIST", (Serializable) Arrays.asList("??????"));
                committeesBundle.putSerializable("COMMITTEESLIST", (Serializable) Arrays.asList("??????"));
        }

        // Package the intent
        intent.putExtra("NAME", name);
        intent.putExtra("PARTY", party);
        intent.putExtra("TAG", tag);
        intent.putExtra("YEAR", year);
        intent.putExtra("BILLS", billsBundle);
        intent.putExtra("COMMITTEES", committeesBundle);
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

    public void getPartyColor(TextView tag, String party) {
        if (party == DEMOCRAT) {
            tag.setTextColor(Color.parseColor(DEMCOLOR));
        }
        else if (party == REPUBLICAN) {
            tag.setTextColor(Color.parseColor(REPCOLOR));
        }
    }
}
