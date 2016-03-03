package com.cs160.neiluuu.electiontracker;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.cs160.neiluuu.electiontracker.R;

import java.util.ArrayList;

public class Detailed extends Activity {

    private ScrollView scrollView;
    private ImageView img;
    private TextView name;
    private TextView tag;
    private TextView party;
    private TextView term;
    private TextView committees;
    private TextView bills;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String nameVal = intent.getStringExtra("NAME");
        String tagVal = intent.getStringExtra("TAG");
        String partyVal = intent.getStringExtra("PARTY");
        String yearVal = intent.getStringExtra("YEAR");
        Bundle billsBundle = intent.getBundleExtra("BILLS");
        ArrayList<String> billsVal = (ArrayList<String>) billsBundle.getSerializable("BILLSLIST");
        Bundle committeesBundle = intent.getBundleExtra("COMMITTEES");
        ArrayList<String> committeesVal = (ArrayList<String>) committeesBundle.getSerializable("COMMITTEESLIST");
        int imgId = extras.getInt("IMG");

        TextView name = (TextView) findViewById(R.id.name);
        TextView tag = (TextView) findViewById(R.id.tag);
        TextView party = (TextView) findViewById(R.id.party);
        TextView term = (TextView) findViewById(R.id.term);
        TextView committees = (TextView) findViewById(R.id.committees);
        TextView bills = (TextView) findViewById(R.id.bills);
        ImageView img = (ImageView) findViewById(R.id.img);

        name.setText(nameVal);
        tag.setText(tagVal);
        setPartyLetter(partyVal, party);
        term.setText(term.getText()+ " " + yearVal);
        for (int i = 0; i < committeesVal.size(); i++) {
            committees.setText(committees.getText() + "\n" + committeesVal.get(i));
        }
        for (int i = 0; i < billsVal.size(); i++) {
            bills.setText(bills.getText() + "\n" + billsVal.get(i));
        }
        img.setImageResource(imgId);

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

}
