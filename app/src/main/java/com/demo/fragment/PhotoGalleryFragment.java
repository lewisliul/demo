package com.demo.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.demo.R;
import com.demo.http.FlickrFetchr;

/**
 * Created by hc on 2016/7/24.
 */
public class PhotoGalleryFragment extends Fragment {
    private static final String TAG = "PhotoGalleryFragment";
    private RecyclerView mRecyclerView;

    public static PhotoGalleryFragment newInstance(){
        return new PhotoGalleryFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        new FetchItemTask().execute();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_photo_gallery,container,false);
        mRecyclerView = (RecyclerView)v.findViewById(R.id.id_fragment_photogallery_recyclerview);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));
        return v;
    }

    private class FetchItemTask extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            try{
                String result = new FlickrFetchr().getUrlString("https://www.bignerdranch.com");
                Log.d(TAG, "doInBackground: " + result);
            }catch (Exception e){
                Log.e(TAG, "doInBackground: ", e );
            }
            return null;
        }
    }
}
