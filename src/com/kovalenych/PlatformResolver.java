package com.kovalenych;


import android.content.Context;
import android.graphics.Typeface;

public class PlatformResolver {

    private Context _context;
    public static boolean isHVGA = false;
    public static boolean isDONUT = false;


    public PlatformResolver(Context context) {
        _context = context;

        Utils.setFonts(Typeface.createFromAsset(context.getAssets(),
                "fonts/belligerent.ttf"));

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
            return R.layout.menu_hvga;
        else
            return R.layout.menu;
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
        if (isHVGA )
            return R.drawable.bg_hwga;
        else
            return R.drawable.bg;
    }

    static public int getRadius() {
        if (isHVGA )
            return 200;
        else
            return 300;
    }

    static public int getCenterX() {
        if (isHVGA )
            return 280;
        else
            return 425;
    }

    static public int getCenterY() {
        if (isHVGA )
            return 465;
        else
            return 745;
    }

    static public int getCircleShiftX() {
        if (isHVGA )
            return -35;
        else
            return -35;
    }

    static public int getCircleShiftY() {
        if (isHVGA )
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
        if (isHVGA )
            return new int[]{158, 250, 15, 105, 165, 100};
        else
            return new int[]{215, 360, 30, 160, 245, 142};

    }

    public static int[] getLabelsY() {
        if (isHVGA )
        return new int[]{371, 320, 190, 145, 65, 460};
        else
            return new int[]{605, 540, 325, 255, 125, 735};

    }


}
