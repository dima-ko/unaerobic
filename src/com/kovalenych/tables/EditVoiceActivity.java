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
import group.pals.android.lib.ui.filechooser.io.localfile.LocalFile;
import group.pals.android.lib.ui.filechooser.services.IFileProvider;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class EditVoiceActivity extends Activity implements Soundable, Const {

    ListView lv;

    ArrayList<Sound> sounds;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sounds_edit);
        sounds = new ArrayList<Sound>();

        sounds.add(new Sound("2 min to start", "to2min.mp3"));
        sounds.add(new Sound("1 min to start", "to1min.mp3"));
        sounds.add(new Sound("30 sec to start", "to30sec.mp3"));
        sounds.add(new Sound("10 sec to start", "to10sec.mp3"));
        sounds.add(new Sound("5 sec to start", "to5sec.mp3"));
        sounds.add(new Sound("start", "start.mp3"));
        sounds.add(new Sound("1 min after start", "after1min.mp3"));
        sounds.add(new Sound("2 min after start", "after2min.mp3"));
        sounds.add(new Sound("3 min after start", "after3min.mp3"));
        sounds.add(new Sound("4 min after start", "after4min.mp3"));
        sounds.add(new Sound("5 min after start", "after5min.mp3"));
        sounds.add(new Sound("breathe", "breathe.mp3"));

        Toast.makeText(this, "click to edit", Toast.LENGTH_SHORT).show();

        lv = (ListView) findViewById(R.id.sounds_edit_list);
        invalidateList();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(EditVoiceActivity.this, FileChooserActivity.class);
                /*
                * by default, if not specified, default rootpath is sdcard,
                * if sdcard is not available, "/" will be used
                */
                intent.putExtra(FileChooserActivity._RegexFilenameFilter, "[A-Za-z0-9_+-]+.(mp3|ogg)");
                startActivityForResult(intent, position);
            }
        });
    }

    public void invalidateList() {
        SoundsArrayAdapter adapter = new SoundsArrayAdapter(this, sounds);
        lv.setAdapter(adapter);
    }

    private static final String LOG_TAG = "CO2 Editing";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(LOG_TAG, "onActivityResult" + resultCode);


//        switch (requestCode) {
//            case _ReqChooseFile:
        if (resultCode == RESULT_OK) {  //todo: long click selection blue
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
            List<LocalFile> files = (List<LocalFile>) data.getSerializableExtra(FileChooserActivity._Results);

            sounds.get(requestCode).path = files.get(0).getAbsolutePath();
            invalidateList();
        }
//                break;
//        }
    }

}