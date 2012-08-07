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

//    private Dialog progressDialog;

//    LinearLayout sendingRequestView;
    RankingManager rManager;

    public static RankingFragment newInstance() {

        return  new RankingFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View tables = inflater.inflate(R.layout.ranking, null);
        mPullRefreshListView = (PullToRefreshListView) tables.findViewById(R.id.ranking_list);
        rManager = new RankingManager(getActivity(), mPullRefreshListView);
        initFilterAndProgress();
        return tables;
    }


    private void initFilterAndProgress() {
//        progressDialog = new Dialog(context);
//        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        progressDialog.setCancelable(true);
//
//        sendingRequestView = new LinearLayout(this);
//        sendingRequestView.setBackgroundColor(Color.BLACK);
//        TextView sendText = new TextView(this);
//        sendText.setGravity(Gravity.CENTER);
//        sendingRequestView.addView(sendText, new LinearLayout.LayoutParams(220, 100));
//
//        sendText.setText(getString(R.string.sendingRequest));
//        progressDialog.setContentView(sendingRequestView);
//        (findViewById(R.id.disc_info)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.aida-international.org/aspportal1/code/page.asp?ObjectID=39&CountryID=4&actID=3")));
//            }
//        });
//        Spinner s = (Spinner) findViewById(R.id.discipline_spinner);
//        ArrayAdapter adapter = ArrayAdapter.createFromResource(
//                getActivity(), R.array.disciplines, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        s.setAdapter(adapter);
//        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                rManager.chosenDisciplNumber = i;
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//            }
//        });
//
//        findViewById(R.id.sendButton).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                rManager.asmFilter();
//                rManager.getRecords();
//            }
//        });
    }


    public void onBackPressed() {
//        rManager.cancelTask();
//        rManager.packSavedTables();
//        if (rManager.recodsDBHelper != null)
//            rManager.recodsDBHelper.close();
//
//        if (rManager.requestsDBHelper != null)
//            rManager.requestsDBHelper.close();
//
//        RankingActivity.this.finish();
    }


    public void showProgressDialog(boolean show) {
//        if (show)
//            progressDialog.show();
//        else
//            progressDialog.dismiss();
    }


    public boolean haveInternet() {
        NetworkInfo info = ((ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info == null || !info.isConnected()) {
            return false;
        }
        if (info.isRoaming()) {
            // here is the roaming option you can change it if you want to disable internet while roaming, just return false
            return true;
        }
        return true;
    }


}
