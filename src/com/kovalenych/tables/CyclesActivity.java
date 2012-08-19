package com.kovalenych.tables;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.*;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.kovalenych.Fonts;
import com.kovalenych.R;
import com.kovalenych.Table;
import com.kovalenych.Utils;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CyclesActivity extends Activity implements Soundable {

    ListView lv;

    Table curTable;
    int curCycleId;

    private static final String LOG_TAG = "CyclesActivity";
    String name;
    Button add_button, ok_button, melody;
    Dialog newDialog;
    Activity ptr;
    EditText holdEdit, breathEdit;
    Dialog voiceDialog;
    int chosenTable;
    Dialog delDialog;
    private Button del_button;
    private SharedPreferences _preferedSettings;
    boolean isvibro;
    private Button stopButton;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ptr = this;
        Bundle bun = getIntent().getExtras();
        name = bun.getString("name");


        unPackTable();
        setContentView(R.layout.cycles);

        initViews();

        _preferedSettings = getSharedPreferences("sharedSettings", MODE_PRIVATE);

        isvibro = _preferedSettings.getBoolean("vibro", true);

    }

    public void initViews() {
        TextView id = (TextView) findViewById(R.id.chosen_table_name);
        id.setText(name);

        delDialog = new Dialog(ptr);
        delDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        delDialog.setCancelable(true);
        delDialog.setContentView(R.layout.delete_dialog);

        del_button = (Button) delDialog.findViewById(R.id.delete_button);


        newDialog = new Dialog(ptr);
        newDialog.setTitle(getResources().getString(R.string.new_cycle));
        newDialog.setCancelable(true);
        newDialog.setContentView(R.layout.new_cycle_dialog);

        holdEdit = (EditText) newDialog.findViewById(R.id.hold_edit);
        breathEdit = (EditText) newDialog.findViewById(R.id.breath_edit);
        breathEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    (ptr).getWindow().setSoftInputMode(
                            WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
            }
        });
        ok_button = (Button) newDialog.findViewById(R.id.new_cycle_ok);
        ok_button.setTypeface(Fonts.BELIGERENT);


        invalidateList();

        add_button = (Button) findViewById(R.id.add_cycle);
        melody = (Button) findViewById(R.id.melody);

        ((TextView) findViewById(R.id.holdtime)).setTypeface(Fonts.BELIGERENT);
        ((TextView) findViewById(R.id.breathtime)).setTypeface(Fonts.BELIGERENT);
        id.setTypeface(Fonts.BELIGERENT);

        setListeners();
    }


    @Override
    protected void onResume() {
        super.onResume();
        stopButton = (Button) findViewById(R.id.stop_button);
        if (Utils.isMyServiceRunning(this))
            stopButton.setVisibility(View.VISIBLE);
        else
            stopButton.setVisibility(View.GONE);
        lv = (ListView) findViewById(R.id.cycles_list);

    }

    private void invalidateList() {

        CyclesArrayAdapter adapter = new CyclesArrayAdapter(this, curTable.getCycles());
        lv.setAdapter(adapter);
        lv.setVisibility(View.VISIBLE);
    }


    @Override
    protected void onPause() {
        try {
            ContextWrapper cw = new ContextWrapper(this);
            File tablesDir = cw.getDir("tables", Context.MODE_PRIVATE);

            File f = new File(tablesDir, name);
            FileOutputStream fos = new FileOutputStream(f);
            ObjectOutputStream obj_out = new ObjectOutputStream(fos);
            obj_out.writeObject(curTable);
            obj_out.close();                                                                //TODO: deleted cycles must be deleted!!!
            fos.close();

        } catch (IOException ex) {
            Log.d(LOG_TAG, "IOException");

        }

        super.onPause();
    }

    void unPackTable() {

        try {
            ContextWrapper cw = new ContextWrapper(this);
            File tablesDir = cw.getDir("tables", Context.MODE_PRIVATE);
            String[] sl = tablesDir.list();
            int chosenNum = -1;
            for (int numInList = 0; numInList < sl.length; numInList++)
                if (sl[numInList].equals(name))
                    chosenNum = numInList;
            File[] fl = tablesDir.listFiles();
            if (chosenNum == -1) throw new FileNotFoundException();
            FileInputStream fis = new FileInputStream(fl[chosenNum]);
            ObjectInputStream obj_in = new ObjectInputStream(fis);
            Object obj = obj_in.readObject();
            if (obj instanceof Table)
                curTable = (Table) obj;
            obj_in.close();
            fis.close();

        } catch (FileNotFoundException ex) {
            if (name.equals("O2 Table"))
                curTable = new Table(fill_O2());
            else if (name.equals("CO2 Table"))
                curTable = new Table(fill_CO2());
            else {
                curTable = new Table();
                Log.d(LOG_TAG, "FileNotFoundException ");
            }
        } catch (IOException
                ex) {
            Log.d(LOG_TAG, "Error parsing file");
        } catch (ClassNotFoundException
                ex) {
            Log.d(LOG_TAG, "Error class not found");
        }

    }

    public void setListeners() {

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                stopService(new Intent(ptr, ClockService.class));

                curCycleId = position;
                Intent intent = new Intent(lv.getContext(), ClockActivity.class);
                Bundle bun = new Bundle();


                bun.putInt("tablesize", curTable.getCycles().size());

                for (int i = 0; i < curTable.getCycles().size(); i++) {
                    bun.putInt("breathe" + Integer.toString(i), curTable.getCycles().get(i).breathe);
                    bun.putInt("hold" + Integer.toString(i), curTable.getCycles().get(i).hold);
                }

                bun.putIntegerArrayList("voices", curTable.getVoices());
                bun.putInt("number", position);
                bun.putBoolean("vibro", isvibro);
                intent.putExtras(bun);
                startActivity(intent);

            }
        });

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("onLongClick", "zzz");
                chosenTable = i;
                delDialog.show();
                return false;
            }
        });

        del_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                curTable.getCycles().remove(chosenTable);
                delDialog.dismiss();
                invalidateList();
            }
        });


        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newDialog.show();

            }
        });

        melody.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                voiceDialog = new Dialog(ptr);
                voiceDialog.setCancelable(true);
                voiceDialog.setTitle(getResources().getString(R.string.voices));
                LayoutInflater inf = getLayoutInflater();
                ScrollView view1 = (ScrollView) inf.inflate(R.layout.voice, null);
                voiceDialog.setContentView(view1, new RelativeLayout.LayoutParams(Utils.smallerDim - 30, ViewGroup.LayoutParams.FILL_PARENT));
//                voiceDialog.setContentView(R.layout.voice);
                setVoiceRadios();
                voiceDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        getVoiceRadios();
                    }
                });
                voiceDialog.show();
            }
        });

        ok_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int b = Integer.parseInt(breathEdit.getText().toString());
                int h = Integer.parseInt(holdEdit.getText().toString());
                if (b < 3600 && h < 3600) {
                    curTable.getCycles().add(new Cycle(b, h));
                    invalidateList();
                    newDialog.dismiss();
                }

            }
        });

    }

    private void setVoiceRadios() {

        if (curTable.getVoices().contains(TO_START_2_MIN))
            ((CheckBox) voiceDialog.findViewById(R.id.voice2to)).setChecked(true);
        if (curTable.getVoices().contains(TO_START_1_MIN))
            ((CheckBox) voiceDialog.findViewById(R.id.voice1to)).setChecked(true);
        if (curTable.getVoices().contains(TO_START_30_SEC))
            ((CheckBox) voiceDialog.findViewById(R.id.voice30to)).setChecked(true);
        if (curTable.getVoices().contains(TO_START_10_SEC))
            ((CheckBox) voiceDialog.findViewById(R.id.voice10to)).setChecked(true);
        if (curTable.getVoices().contains(TO_START_5_SEC))
            ((CheckBox) voiceDialog.findViewById(R.id.voice5to)).setChecked(true);
        if (curTable.getVoices().contains(START))
            ((CheckBox) voiceDialog.findViewById(R.id.voicestart)).setChecked(true);
        if (curTable.getVoices().contains(AFTER_START_1))
            ((CheckBox) voiceDialog.findViewById(R.id.voice1after)).setChecked(true);
        if (curTable.getVoices().contains(AFTER_START_2))
            ((CheckBox) voiceDialog.findViewById(R.id.voice2after)).setChecked(true);
        if (curTable.getVoices().contains(AFTER_START_3))
            ((CheckBox) voiceDialog.findViewById(R.id.voice3after)).setChecked(true);
        if (curTable.getVoices().contains(AFTER_START_4))
            ((CheckBox) voiceDialog.findViewById(R.id.voice4after)).setChecked(true);
        if (curTable.getVoices().contains(AFTER_START_5))
            ((CheckBox) voiceDialog.findViewById(R.id.voice5after)).setChecked(true);
        if (curTable.getVoices().contains(BREATHE))
            ((CheckBox) voiceDialog.findViewById(R.id.voicebreathe)).setChecked(true);
        if (isvibro)
            ((CheckBox) voiceDialog.findViewById(R.id.vibro)).setChecked(true);
    }

    private void getVoiceRadios() {
        curTable.getVoices().clear();
        if (((CheckBox) voiceDialog.findViewById(R.id.voice2to)).isChecked())
            curTable.getVoices().add(TO_START_2_MIN);
        if (((CheckBox) voiceDialog.findViewById(R.id.voice1to)).isChecked())
            curTable.getVoices().add(TO_START_1_MIN);
        if (((CheckBox) voiceDialog.findViewById(R.id.voice30to)).isChecked())
            curTable.getVoices().add(TO_START_30_SEC);
        if (((CheckBox) voiceDialog.findViewById(R.id.voice10to)).isChecked())
            curTable.getVoices().add(TO_START_10_SEC);
        if (((CheckBox) voiceDialog.findViewById(R.id.voice5to)).isChecked())
            curTable.getVoices().add(TO_START_5_SEC);
        if (((CheckBox) voiceDialog.findViewById(R.id.voicestart)).isChecked())
            curTable.getVoices().add(START);
        if (((CheckBox) voiceDialog.findViewById(R.id.voice1after)).isChecked())
            curTable.getVoices().add(AFTER_START_1);
        if (((CheckBox) voiceDialog.findViewById(R.id.voice2after)).isChecked())
            curTable.getVoices().add(AFTER_START_2);
        if (((CheckBox) voiceDialog.findViewById(R.id.voice3after)).isChecked())
            curTable.getVoices().add(AFTER_START_3);
        if (((CheckBox) voiceDialog.findViewById(R.id.voice4after)).isChecked())
            curTable.getVoices().add(AFTER_START_4);
        if (((CheckBox) voiceDialog.findViewById(R.id.voice5after)).isChecked())
            curTable.getVoices().add(AFTER_START_5);
        if (((CheckBox) voiceDialog.findViewById(R.id.voicebreathe)).isChecked())
            curTable.getVoices().add(BREATHE);

        SharedPreferences.Editor editor = _preferedSettings.edit();
        isvibro = ((CheckBox) voiceDialog.findViewById(R.id.vibro)).isChecked();
        editor.putBoolean("vibro", ((CheckBox) voiceDialog.findViewById(R.id.vibro)).isChecked());
        editor.commit();
    }

    public ArrayList<Cycle> fill_O2() {
        ArrayList<Cycle> O2 = new ArrayList<Cycle>();
        O2.add(new Cycle(60, 120));
        O2.add(new Cycle(90, 120));
        O2.add(new Cycle(120, 180));
        O2.add(new Cycle(180, 240));
        O2.add(new Cycle(240, 300));
        O2.add(new Cycle(300, 360));
        return O2;

    }

    public ArrayList<Cycle> fill_CO2() {
        ArrayList<Cycle> CO2 = new ArrayList<Cycle>();
        CO2.add(new Cycle(60, 120));
        CO2.add(new Cycle(150, 120));
        CO2.add(new Cycle(135, 120));
        CO2.add(new Cycle(120, 120));
        CO2.add(new Cycle(105, 120));
        CO2.add(new Cycle(90, 120));
        CO2.add(new Cycle(75, 120));
        CO2.add(new Cycle(60, 120));
        CO2.add(new Cycle(60, 120));
        return CO2;
    }

}

