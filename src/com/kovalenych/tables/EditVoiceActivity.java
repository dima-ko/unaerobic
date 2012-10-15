package com.kovalenych.tables;

import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.*;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.*;
import com.kovalenych.Const;
import com.kovalenych.R;
import com.kovalenych.Table;
import com.kovalenych.Utils;
import group.pals.android.lib.ui.filechooser.FileChooserActivity;
import group.pals.android.lib.ui.filechooser.services.IFileProvider;

import java.io.*;
import java.util.ArrayList;
import java.util.TreeMap;

public class EditVoiceActivity extends Activity implements Soundable, Const {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, FileChooserActivity.class);
        /*
        * by default, if not specified, default rootpath is sdcard,
        * if sdcard is not available, "/" will be used
        */
//                        intent.putExtra(FileChooserActivity._RegexFilenameFilter, "(?si).*\\.(zip|7z)$");
        startActivityForResult(intent, _ReqChooseFile);
    }

    private static final int _ReqChooseFile = 600;

    private static final String LOG_TAG = "CO2 Editing";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(LOG_TAG, "onActivityResult" + resultCode);


        switch (requestCode) {
            case _ReqChooseFile:
                if (resultCode == RESULT_OK) {
                    /*
                    * you can use two flags included in data
                    */
                    IFileProvider.FilterMode filterMode = (IFileProvider.FilterMode)
                            data.getSerializableExtra(FileChooserActivity._FilterMode);
                    boolean saveDialog = data.getBooleanExtra(FileChooserActivity._SaveDialog, false);

                    /*
                    * a list of files will always return,
                    * if selection mode is single, the list contains one file
                    */
//                    List<LocalFile> files = (List<LocalFile>)
//                            data.getSerializableExtra(FileChooserActivity._Results);
//                    for (File f : files)
//                    ...
                }
                break;
        }
    }

}