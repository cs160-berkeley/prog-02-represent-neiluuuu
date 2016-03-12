package com.cs160.neiluuu.electiontracker;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.wearable.view.CardFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by neiluuu on 3/1/16.
 */
public class CustomCardFragment extends CardFragment {

    public CustomCardFragment() {
    }

    public static CustomCardFragment create(CharSequence title, CharSequence description) {
        return create(title, description, "", "", 0, -1);
    }

    public static CustomCardFragment create(CharSequence title, CharSequence text, String zip, String party, int iconRes, int pageNumber) {
        CustomCardFragment fragment = new CustomCardFragment();
        Bundle args = new Bundle();
        if(title != null) {
            args.putCharSequence("CardFragment_title", title);
        }

        if(text != null) {
            args.putCharSequence("CardFragment_text", text);
        }

        if(zip != null) {
            args.putCharSequence("CardFragment_zip", zip);
        }

        if(party != null) {
            args.putCharSequence("CardFragment_party", party);
        }

        if(iconRes != 0) {
            args.putInt("CardFragment_icon", iconRes);
        }

        if(pageNumber != -1) {
            args.putInt("CardFragment_page", pageNumber);
        }

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(android.support.wearable.R.layout.watch_card_content, container, false);
        final Bundle args = this.getArguments();
        if (args != null) {
            TextView title = (TextView) view.findViewById(android.support.wearable.R.id.title);
            if (args.containsKey("CardFragment_title") && title != null) {
                title.setText(args.getCharSequence("CardFragment_title"));
                title.setTextColor(Color.BLACK);
            }

            if (args.containsKey("CardFragment_text")) {
                TextView text = (TextView) view.findViewById(android.support.wearable.R.id.text);
                if (text != null) {
                    text.setText(args.getCharSequence("CardFragment_text"));
                }
            }

            if (args.containsKey("CardFragment_icon") && title != null) {
                title.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, args.getInt("CardFragment_icon"), 0);
            }
            view.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // Perform action on click
                    Intent sendIntent = new Intent(getActivity().getBaseContext(), WatchToPhoneService.class);
                    System.out.println(args.getInt("CardFragment_page"));
                    sendIntent.putExtra("CARD", Integer.toString(args.getInt("CardFragment_page")));
                    sendIntent.putExtra("ZIP", args.getString("CardFragment_zip"));
                    getActivity().startService(sendIntent);
                }
            });
        }


        return view;
    }

}
