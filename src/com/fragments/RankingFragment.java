package com.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.*;
import android.view.animation.TranslateAnimation;
import android.widget.*;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.kovalenych.MenuActivity;
import com.kovalenych.R;
import com.kovalenych.Utils;
import com.kovalenych.ranking.RankingManager;

import java.util.ArrayList;

public final class RankingFragment extends Fragment {

    PullToRefreshListView mPullRefreshListView;
    RelativeLayout filterView;
    RelativeLayout recordsView;
    ProgressBar progressBar;
    RankingManager rManager;

    public static RankingFragment newInstance() {

        return new RankingFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        discArray = getResources().getStringArray(R.array.disciplines);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View tables = inflater.inflate(R.layout.ranking, null);
        mPullRefreshListView = (PullToRefreshListView) tables.findViewById(R.id.ranking_list);
        rManager = new RankingManager(getActivity(), this, mPullRefreshListView);
        initFilterAndProgress(tables);
        return tables;
    }

    String[] discArray;

    private void initFilterAndProgress(View tables) {

        filterView = (RelativeLayout) tables.findViewById(R.id.ranking_filter);
        recordsView = (RelativeLayout) tables.findViewById(R.id.ranking_records);

        progressBar = (ProgressBar) filterView.findViewById(R.id.sending_prog);
        progressBar.setProgressDrawable(getResources().getDrawable(R.drawable.red_progress));

        (filterView.findViewById(R.id.disc_info)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (((MenuActivity) getActivity()).haveInternet()) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.aida-international.org/aspportal1/code/page.asp?ObjectID=39&CountryID=4&actID=3")));
                } else {
                    Toast.makeText(getActivity(), getActivity().getString(R.string.noConnectArt), Toast.LENGTH_SHORT).show();
                }
            }
        });

       final Button s = (Button) filterView.findViewById(R.id.discipline_button);
        final Spinner spinner = (Spinner) filterView.findViewById(R.id.discipline_spinner);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(
                getActivity(), R.array.disciplines, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                rManager.chosenDisciplNumber = i;
                s.setText(discArray[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        s.setText("STA");
        s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner.performClick();
            }
        });

        filterView.findViewById(R.id.sendButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rManager.asmFilter();
                rManager.getRecords();

            }
        });

        tables.findViewById(R.id.filter_triangle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFilter(true);
            }
        });
    }

    @Override
    public void onDestroyView() {
        rManager.cancelTask();
        rManager.packSavedTables();
        rManager.closeDBHelpers();

        super.onDestroyView();
    }

    ProgressTask task;

    public void showProgressDialog(boolean show) {
        if (show) {
            progressBar.setVisibility(View.VISIBLE);
            task = new ProgressTask();
            task.execute();
        } else {
            task.cancel(true);
            progressBar.setVisibility(View.GONE);
        }
    }

    public void showFilter(boolean show) {
        if (show) {
            filterView.setVisibility(View.VISIBLE);
            TranslateAnimation animation = new TranslateAnimation(0, 0, Utils.height, 0);
            animation.setDuration(600);
            filterView.startAnimation(animation);
        } else {
            TranslateAnimation animation = new TranslateAnimation(0, 0, 0, Utils.height);
            animation.setDuration(600);
            filterView.startAnimation(animation);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    filterView.setVisibility(View.GONE);
                }
            }, 650);

        }
    }

    class ProgressTask extends AsyncTask<Void, Integer, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            int incr = 0;
            while (!isCancelled()) {
                publishProgress(incr);
                incr += 4*(200-incr)/200;
                try {
                    Thread.sleep(101);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setProgress(values[0]);
        }
    }


}
