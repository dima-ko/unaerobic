package com.kovalenych;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import com.nostra13.universalimageloader.imageloader.DisplayImageOptions;
import com.nostra13.universalimageloader.imageloader.ImageLoader;
import com.nostra13.universalimageloader.imageloader.ImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

public class NostraVideoActivity extends Activity {

    private ArrayList<Video> videoList;

    AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoList.get(i).getUri()));
            startActivity(intent);
        }
    };

    //    private List<? extends Map<String, ?>> createCyclesList() {
//
//        List<Map<String, ?>> items = new ArrayList<Map<String, ?>>();
//
//        for (int i = 0; i < videoList.size(); i++) {
//            Map<String, Object> map = new HashMap<String, Object>();
//            map.put("title", videoList.get(i).getTitle());
//            map.put("picture", videoList.get(i).getPictureUri());
//            items.add(map);
//        }
//
//        return items;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        videoList = new ArrayList<Video>();
        context = this;
        fillList();

        setContentView(R.layout.videos);

        ((ListView) findViewById(R.id.video_list)).setAdapter(new ItemAdapter());
        ((ListView) findViewById(R.id.video_list)).setOnItemClickListener(listener);
    }

    Context context;


    private void fillList() {
        videoList.add(new Video("WEIGHTLESS - Emotional Freediving", "http://www.youtube.com/watch?v=jOM75l225Qg&feature=player_embedded"));
        videoList.add(new Video("TANYA STREETER: Shark Therapy", "http://www.youtube.com/watch?feature=player_embedded&v=T1knyXu4QuQ"));
        videoList.add(new Video("FreeDive.mov", "http://www.youtube.com/watch?feature=player_embedded&v=wuVgrY4FjY0"));
        videoList.add(new Video("Sea Bed Hunting On One Breath", "http://www.youtube.com/watch?feature=player_embedded&v=MgRpwESWPLM"));
        videoList.add(new Video("TEDxVienna - Herbert Nitsch - Breathless", "http://www.youtube.com/watch?v=INqG2YtgU08"));
        videoList.add(new Video("Intervew with Herbert 83m CNF", "http://www.youtube.com/watch?v=2exs67Npnas"));
        videoList.add(new Video("Freediver Blackout", "http://www.youtube.com/watch?v=PBnEIMTrgFk"));
        videoList.add(new Video("Carlos Coste Black Out WC Italy 2011", "http://www.youtube.com/watch?v=rS3wYUOBxgo&feature=related"));
        videoList.add(new Video("Goran Colak - Freediving World Record 273 m", "http://www.youtube.com/watch?v=9Th8Zt-HMCg"));
        videoList.add(new Video("Guillaume Nery base jumping at Dean's Blue Hole, filmed on breath hold by Julie Gautier", "http://www.youtube.com/watch?v=uQITWbAaDx0"));
        videoList.add(new Video("YogaDa! Freediving - In space and back without scuba", "http://www.youtube.com/watch?v=89HXF4PkAw4"));
        videoList.add(new Video("Breathe Teaser Trailer #1", "http://www.youtube.com/watch?v=2osGJLA18lk&feature=related"));
        videoList.add(new Video("Dave 265M Dynamic Apnea with fin World Record 25 Sept 2010.MOD", "http://www.youtube.com/watch?v=0WFDWYNs4Ac&feature=related"));
        videoList.add(new Video("William Trubridge 101 CNF Record", "http://www.youtube.com/watch?v=UKLo5j53h10&feature=related"));
        videoList.add(new Video("Record mundial de apnea \"No Limits\" - Herbert Nitsch -214 m", "http://www.youtube.com/watch?v=WBNaGscqcyc"));
    }

//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menushka, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection

        switch (item.getItemId()) {
            case R.id.articles:
                Intent intent = new Intent(context, ArticlesActivity.class);
                startActivity(intent);
                return true;
            case R.id.videos:
                Intent intent2 = new Intent(context, NostraVideoActivity.class);
                startActivity(intent2);
                return true;
            case R.id.ranking:
                Intent intent3 = new Intent(context, RankingActivity.class);
                startActivity(intent3);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public ImageLoader imageLoader = ImageLoader.getInstance();

    @Override
    protected void onDestroy() {
        imageLoader.stop();
        super.onDestroy();
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        Intent intent = new Intent(context, MenuActivity.class);
//        startActivity(intent);
//    }

    class ItemAdapter extends BaseAdapter {

        private List<String> imageUrls;

        private ItemAdapter() {

            imageUrls = new ArrayList<String>(videoList.size());
            for (Video video : videoList)
                imageUrls.add(video.getPictureUri());
        }

        public int getCount() {
            return imageUrls.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public class ViewHolder {
            public TextView text;
            public ImageView image;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = convertView;
            final ViewHolder holder;
            if (convertView == null) {
                view = getLayoutInflater().inflate(R.layout.video_item, null);
                holder = new ViewHolder();
                holder.text = (TextView) view.findViewById(R.id.video_text);
                holder.image = (ImageView) view.findViewById(R.id.video_image);
                view.setTag(holder);
            } else
                holder = (ViewHolder) view.getTag();

            holder.text.setText(videoList.get(position).getTitle());

            // Full "displayImage" method using.
            // You can use simple call:
            //  imageLoader.displayImage(imageUrls.get(position), holder.image);
            // instead of.
            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .showStubImage(R.drawable.stub_image)
                    .cacheInMemory()
                    .cacheOnDisc()
                    .build();
            imageLoader.displayImage(imageUrls.get(position), holder.image, options, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted() {
                    holder.text.setText("...loading...");
                }

                @Override
                public void onLoadingFailed() {
                    holder.text.setText("Error!");
                }

                @Override
                public void onLoadingComplete() {
                    holder.text.setText(videoList.get(position).getTitle());
                }
            });

            return view;
        }
    }
}
