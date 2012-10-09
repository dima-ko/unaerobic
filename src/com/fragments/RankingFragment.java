package com.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.*;
import android.widget.*;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.kovalenych.MenuActivity;
import com.kovalenych.R;
import com.kovalenych.Utils;
import com.kovalenych.ranking.RankingManager;

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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View tables = inflater.inflate(R.layout.ranking, null);
        mPullRefreshListView = (PullToRefreshListView) tables.findViewById(R.id.ranking_list);
        rManager = new RankingManager(getActivity(), this, mPullRefreshListView);
        initFilterAndProgress(tables);
        tables.setId(1);
        return tables;
    }

    private void initFilterAndProgress(View tables) {       //todo: filter slide down

        filterView = (RelativeLayout) tables.findViewById(R.id.ranking_filter);
        recordsView = (RelativeLayout) tables.findViewById(R.id.ranking_records);

        progressBar = (ProgressBar) filterView.findViewById(R.id.sending_prog);

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
        Spinner s = (Spinner) filterView.findViewById(R.id.discipline_spinner);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(
                getActivity(), R.array.disciplines, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);
        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                rManager.chosenDisciplNumber = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        filterView.findViewById(R.id.sendButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rManager.asmFilter();
                rManager.getRecords();
            }
        });

        recordsView.findViewById(R.id.ranking_back).setOnClickListener(new View.OnClickListener() {
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
            recordsView.setVisibility(View.GONE);
        } else {
            filterView.setVisibility(View.GONE);
            recordsView.setVisibility(View.VISIBLE);
        }
    }

    class ProgressTask extends AsyncTask<Void, Integer, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            int incr = 0;
            while (!isCancelled()) {
                publishProgress(incr);
                incr += 2;
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
