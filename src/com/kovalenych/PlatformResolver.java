package com.kovalenych;


import android.content.Context;
import android.graphics.Typeface;

public class PlatformResolver {

    private Context _context;
    public static boolean isHVGA = false;
    public static boolean isQVGA = false;
    public static boolean is720p = false;
    public static boolean isTabI = false;


    public PlatformResolver(Context context) {
        _context = context;

        Fonts.setFonts(Typeface.createFromAsset(context.getAssets(),
                "fonts/belligerent.ttf"));

    }

    static public int getWidth() {
        if (isHVGA)
            return 320;
        else if (isQVGA)
            return 240;
        else if (is720p)
            return 800;
        else if (isTabI)
            return 600;
        else
            return 480;
    }

    static public int getRank() {
            return R.layout.ranking;
    }

    static public int getTableLayout() {
            return R.layout.table;
    }

    static public int getTableItemLayout() {
            return R.layout.table_item;
    }

    static public int getClocksLayout() {
            return R.layout.clocks;
    }

    static public int getMenuLayout() {
            return R.layout.tables;
    }

    static public int getNewCycleDialogLayout() {
            return R.layout.new_cycle_dialog;
    }

    static public int getVoicesLayout() {
            return R.layout.voice;
    }

    static public int getRecordItemLayout() {
            return R.layout.record_item;
    }

}
