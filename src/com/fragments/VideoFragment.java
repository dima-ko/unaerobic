package com.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.kovalenych.MenuActivity;
import com.kovalenych.R;
import com.kovalenych.media.Article;
import com.kovalenych.media.ArticleViewBinder;
import com.kovalenych.media.Video;
import com.nostra13.universalimageloader.imageloader.DisplayImageOptions;
import com.nostra13.universalimageloader.imageloader.ImageLoader;
import com.nostra13.universalimageloader.imageloader.ImageLoadingListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class VideoFragment extends Fragment {

    private ArrayList<Video> videoList;

    public static VideoFragment newInstance() {

        return  new VideoFragment();
    }

    AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            if (((MenuActivity) getActivity()).haveInternet()) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoList.get(i).getUri()));
                startActivity(intent);
            } else {
                Toast.makeText(getActivity(), getActivity().getString(R.string.noConnectRank), Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        videoList = new ArrayList<Video>();
        fillList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View tables = inflater.inflate(R.layout.videos, null);
        ((ListView) tables.findViewById(R.id.video_list)).setAdapter(new ItemAdapter());
        ((ListView) tables.findViewById(R.id.video_list)).setOnItemClickListener(listener);

        return tables;
    }



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

    public ImageLoader imageLoader = ImageLoader.getInstance();

    @Override
    public void onDestroy() {
        imageLoader.stop();
        super.onDestroy();
    }

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
                view = getActivity().getLayoutInflater().inflate(R.layout.video_item, null);
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
                    holder.text.setText("No connection");
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
