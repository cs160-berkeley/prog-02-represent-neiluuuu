package com.cs160.neiluuu.electiontracker;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.wearable.view.CardFragment;
import android.support.wearable.view.FragmentGridPagerAdapter;
import com.cs160.neiluuu.electiontracker.CustomCardFragment;
import android.view.Gravity;

import java.util.List;

/**
 * Created by neiluuu on 3/1/16.
 */
public class GridPagerAdapter extends FragmentGridPagerAdapter {

    private final Context mContext;
    private List mRows;
    private CustomCardFragment[][] PAGES;

    public GridPagerAdapter(Context ctx, FragmentManager fm, CustomCardFragment[][] pages) {
        super(fm);
        mContext = ctx;
        PAGES = pages;
    }

    static final int[] BG_IMAGES = new int[] {
    };

    // A simple container for static data in each page
    private static class Page {
        // static resources
        int titleRes;
        int textRes;
        int iconRes;
    };

    // Create a static set of pages in a 2D array
//    private final CustomCardFragment[][] PAGES = {
//            {
//                    CustomCardFragment.create("Barbara Lee", "Representative\nDemocrat"),
//                    CustomCardFragment.create("Barbara Boxer", "Senator\nDemocrat"),
//                    CustomCardFragment.create("Diane Feinstein", "Senator\nDemocrat")
//            },
//            {
//                    CustomCardFragment.create("2012 Election Results", "California\nBerkeley\nObama: 50%\nRomney: 50%")
//            }
//    };
    // Obtain the UI fragment at the specified position
    @Override
    public Fragment getFragment(int row, int col) {
        CustomCardFragment page = PAGES[row][col];
        return page;
    }

    @Override
    public Drawable getBackgroundForPage(int row, int column) {
        return GridPagerAdapter.BACKGROUND_NONE;
    }

    @Override
    public int getRowCount() {
        return PAGES.length;
    }

    @Override
    public int getColumnCount(int i) {
        return PAGES[i].length;
    }
}
