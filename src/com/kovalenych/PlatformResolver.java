package com.kovalenych;


import android.content.Context;
import android.graphics.Typeface;

public class PlatformResolver {

    private Context _context;
    public static boolean isHVGA = false;
    public static boolean isHVGA400 = false;
    public static boolean isDONUT = false;


    public PlatformResolver(Context context) {
        _context = context;

        Utils.setFonts(Typeface.createFromAsset(context.getAssets(),
                "fonts/belligerent.ttf"));

    }

    static public int getTableLayout() {
        if (isHVGA)
            return R.layout.table_hvga;
        if (isHVGA400)
            return R.layout.table_400;
        else
            return R.layout.table;
    }

    static public int getTableItemLayout() {
        if (isHVGA)
            return R.layout.table_item_hvga;
        if (isHVGA400)
            return R.layout.table_item_400;
        else
            return R.layout.table_item;
    }

    static public int getClocksLayout() {
        if (isHVGA)
            return R.layout.clocks_hvga;
        if (isHVGA400)
            return R.layout.clocks_400;
        else
            return R.layout.clocks;
    }

    static public int getMenuLayout() {
        if (isHVGA)
            return R.layout.menu_hvga;
        if (isHVGA400)
            return R.layout.menu_400;
        else
            return R.layout.menu;
    }

    static public int getNewCycleDialogLayout() {
        if (isHVGA)
            return R.layout.new_cycle_dialog_hvga;
        if (isHVGA400)
            return R.layout.new_cycle_dialog_400;
        else
            return R.layout.new_cycle_dialog;
    }

    static public int getVoicesLayout() {
        if (isHVGA)
            return R.layout.voice_hvga;
        if (isHVGA400)
            return R.layout.voice_400;
        else
            return R.layout.voice;
    }

    static public int getFilterDialogLayout() {
        if (isHVGA)
            return R.layout.filter_dialog_hwga;
//        if (isHVGA400)
//            return R.layout.filter_dialog_hwga;
        else
            return R.layout.filter_dialog;
    }


    static public int getCycleItemLayout() {
        if (isHVGA)
            return R.layout.cycle_item;
        if (isHVGA400)
            return R.layout.cycle_item_400;
        else
            return R.layout.cycle_item;
    }


}
