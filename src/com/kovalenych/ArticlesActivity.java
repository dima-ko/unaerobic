package com.kovalenych;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArticlesActivity extends Activity {

    ListView lv;
    ArrayList<Article> artList;
    Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        artList = new ArrayList<Article>();
        context = this;
        fillList();
        // http://www.aidainternational.org/freediving/history
        setContentView(R.layout.articles);
//        setContentView(R.layout.ranking);

        lv = (ListView) findViewById(R.id.articles_list);
        lv.setOnItemClickListener(listener);

        invalidateList();
    }

    AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(artList.get(i).getUri()));
//                            Uri.parse("http://www.scubadivingplanet.com/articles/templates/general.asp?articleid=4&zoneid=1"));
            startActivity(intent);
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

        SimpleAdapter adapter = new SimpleAdapter(this, createCyclesList(), R.layout.article_item,
                new String[]{"name", "author", "domain"},
                new int[]{R.id.art_name, R.id.art_author, R.id.art_domain});
        adapter.setViewBinder(new ArticleViewBinder());
        lv.setAdapter(adapter);
        lv.setVisibility(View.VISIBLE);
    }

    private List<? extends Map<String, ?>> createCyclesList() {

        List<Map<String, ?>> items = new ArrayList<Map<String, ?>>();

        for (int i = 0; i < artList.size(); i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("name", artList.get(i).getName());
            map.put("author", artList.get(i).getAuthor());
            map.put("domain", artList.get(i).getDomain());
            items.add(map);
        }

        return items;
    }



}
