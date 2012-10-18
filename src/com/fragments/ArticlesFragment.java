package com.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import com.kovalenych.MenuActivity;
import com.kovalenych.UnaeroApplication;
import com.kovalenych.media.Article;
import com.kovalenych.R;
import com.kovalenych.media.ArticleArrayAdapter;
import com.kovalenych.media.Video;

import java.util.*;

public final class ArticlesFragment extends Fragment {

    ListView lv;
    ArrayList<Article> artList;

    public static ArticlesFragment newInstance() {

        return new ArticlesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        artList = new ArrayList<Article>();
        fillList();
        // http://www.aidainternational.org/freediving/history

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ArrayList<Article> articles = ((UnaeroApplication) getActivity().getApplication()).getArticles();

        for (Article article : articles) {
            if (!artList.contains(article))
                artList.add(0, article);
            Log.d("ArtFrag new article ", "" + article.getName() + "    uri " + article.getUri());
            //the end
        }


        View tables = inflater.inflate(R.layout.articles, null);
        lv = (ListView) tables.findViewById(R.id.articles_list);
        lv.setOnItemClickListener(listener);
        invalidateList();

        return tables;
    }

    AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            if (((MenuActivity) getActivity()).haveInternet()) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(artList.get(i).getUri()));
//                            Uri.parse("http://www.scubadivingplanet.com/articles/templates/general.asp?articleid=4&zoneid=1"));
                startActivity(intent);
            } else {
                Toast.makeText(getActivity(), getActivity().getString(R.string.noConnectArt), Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void fillList() {
        artList.add(new Article("breath-hold divers (Ama)", "Tamaki H & co", "http://www.ncbi.nlm.nih.gov/pubmed/20737928?dopt=Abstract"));
        artList.add(new Article("Apnea.cz", "", "http://www.apnea.cz"));
        artList.add(new Article("Breath-holding on pure O2", "by Sina Schieweck", "http://www.freediveinternational.com/allarticles/breath-holdiingonpureO2.htm"));
        artList.add(new Article("Hydrodinamics in finning and gliding", "by Jeremy Meyer", "http://www.freediveinternational.com/allarticles/Hydrodynamic.htm"));
        artList.add(new Article("Static Tables", "by anonymous", "http://freedivingexplained.blogspot.com/2008/03/freediving-training-static-tables.html"));
        artList.add(new Article("Alkaline diet for freedivers", "by William Trubridge", "http://www.anneliepompe.com/articles/alkaline_diet.htm"));
        artList.add(new Article("Patrick Musimu and Herbert Nitsch", "By Jimmy Muzzone", "http://www.patrykkruk.com/2011/02/interviews-with-patrick-musimu-and.html"));

    }

    private void invalidateList() {

        ArticleArrayAdapter adapter = new ArticleArrayAdapter(getActivity(), artList);
        lv.setAdapter(adapter);
        lv.setVisibility(View.VISIBLE);
    }


}
