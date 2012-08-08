package com.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.kovalenych.R;
import com.kovalenych.media.Article;
import com.kovalenych.media.ArticleViewBinder;
import com.kovalenych.ranking.RankingManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class RankingFragment extends Fragment {

    PullToRefreshListView mPullRefreshListView;
    LinearLayout filterView;
    RelativeLayout recordsView;
    private Dialog progressDialog;

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
        return tables;
    }

    private void initFilterAndProgress(View tables) {
        progressDialog = new Dialog(getActivity());
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setCancelable(true);

        LinearLayout sendingRequestView = new LinearLayout(getActivity());
        sendingRequestView.setBackgroundColor(Color.BLACK);
        TextView sendText = new TextView(getActivity());
        sendText.setGravity(Gravity.CENTER);
        sendingRequestView.addView(sendText, new LinearLayout.LayoutParams(220, 100));

        sendText.setText(getString(R.string.sendingRequest));
        progressDialog.setContentView(sendingRequestView);

        filterView = (LinearLayout) tables.findViewById(R.id.ranking_filter);
        recordsView = (RelativeLayout) tables.findViewById(R.id.ranking_records);

        (filterView.findViewById(R.id.disc_info)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.aida-international.org/aspportal1/code/page.asp?ObjectID=39&CountryID=4&actID=3")));
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
    public void onDestroy() {
        rManager.cancelTask();
        rManager.packSavedTables();
        rManager.closeDBHelpers();

        super.onDestroy();
    }

    public void showProgressDialog(boolean show) {
        if (show)
            progressDialog.show();
        else
            progressDialog.dismiss();
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




}
