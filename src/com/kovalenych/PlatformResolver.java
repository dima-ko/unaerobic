package com.kovalenych;


import android.content.Context;
import android.graphics.Typeface;

public class PlatformResolver {

    private Context _context;
    public static boolean isHVGA = false;
    public static boolean isQVGA = false;
    public static boolean isDONUT = false;
    public static boolean is720p = false;
    public static boolean isTabI = false;
    public static boolean is854 = false;


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
            return 720;
        else if (isTabI)
            return 600;
        else
            return 480;
    }

    static public int getTableLayout() {
        if (isHVGA)
            return R.layout.table_hvga;
        else
            return R.layout.table;
    }

    static public int getTableItemLayout() {
        if (isHVGA)
            return R.layout.table_item_hvga;
        else
            return R.layout.table_item;
    }

    static public int getClocksLayout() {
        if (isHVGA)
            return R.layout.clocks_hvga;
        else
            return R.layout.clocks;
    }

    static public int getMenuLayout() {
        if (isHVGA)
            return R.layout.tables_hvga;
        else
            return R.layout.tables;
    }

    static public int getNewCycleDialogLayout() {
        if (isHVGA)
            return R.layout.new_cycle_dialog_hvga;
        else
            return R.layout.new_cycle_dialog;
    }

    static public int getVoicesLayout() {
        if (isHVGA)
            return R.layout.voice_hvga;
        else
            return R.layout.voice;
    }

    static public int getFilterDialogLayout() {
        if (isHVGA)
            return R.layout.filter_dialog_hwga;
        else
            return R.layout.filter_dialog;
    }

    static public int getMenuBG() {
        if (is720p)
            return R.drawable.bg720p;
        if (isQVGA)
            return R.drawable.bg_qwga;
        if (isHVGA)
            return R.drawable.bg_hwga;
        else
            return R.drawable.bg;
    }

    static public int getRadius() {
        if (is720p)
            return 460;
        if (isQVGA)
            return 130;
        if (isHVGA)
            return 200;
        if (isTabI)
            return 375;
        else
            return 300;
    }

    static public int getCenterX() {
        if (isQVGA)
            return 210;
        if (isHVGA)
            return 280;
        if (is720p)
            return 700;
        if (isTabI)
            return 570;
        else
            return 425;
    }

    static public int getCenterY() {
        if (isQVGA)
            return 310;
        if (isHVGA)
            return 465;
        if (is720p)
            return 1200;
        if (isTabI)
            return 980;
        if (is854)
            return 799;
        else
            return 745;
    }

    static public int getCircleShiftX() {
        if (isQVGA)
            return -15;
        if (isHVGA)
            return -35;
        else
            return -35;
    }

    static public int getCircleShiftY() {
        if (isQVGA)
            return -43;
        if (isHVGA)
            return -55;
        else
            return -55;
    }


    static public int getRecordItemLayout() {
        if (isHVGA)
            return R.layout.record_item_hwga;
        else
            return R.layout.record_item;
    }


    static public int getCycleItemLayout() {
        if (isHVGA)
            return R.layout.cycle_item;
        else
            return R.layout.cycle_item;
    }


    public static int[] getLabelsX() {
        if (isTabI)
            return new int[]{315, 470, 35, 210, 315, 207};
        if (is720p)
            return new int[]{380, 580, 45, 260, 370, 230};
        if (isQVGA)
            return new int[]{95, 155, -15, 48, 85, 60};
        if (isHVGA)
            return new int[]{133, 225, -10, 80, 140, 75};
        else
            return new int[]{215, 360, 30, 160, 245, 142};

    }

    public static int[] getLabelsY() {
        if (isTabI)
            return new int[]{780, 690, 405, 305, 160, 945};
        if (is720p)
            return new int[]{950, 830, 500, 380, 180, 1150};
        if (isQVGA)
            return new int[]{255, 221, 123, 91, 38, 304};
        if (isHVGA)
            return new int[]{371, 320, 190, 145, 65, 460};
        if (is854)
            return new int[]{605 + 54, 540 + 54, 325, 255, 125, 735 + 54};
        else
            return new int[]{605, 540, 325, 255, 125, 735};

    }


    public static float getTextSize() {
        if (is720p)
            return 25;
        else
            return 16;
    }

    public static int getTextWidth() {
        if (is720p)
            return 130;
        else
            return 100;
    }
}
