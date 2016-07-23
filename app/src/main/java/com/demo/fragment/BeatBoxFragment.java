package com.demo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.demo.R;
import com.demo.base.BeatBox;
import com.demo.bean.Sound;

import java.util.List;

/**
 * Created by hc on 2016/7/23.
 */
public class BeatBoxFragment extends Fragment {
    private BeatBox mBeatBox;
    public static BeatBoxFragment newIntance(){
        return new BeatBoxFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBeatBox = new BeatBox(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_beat_box,container,false);
        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.id_fragment_beat_recyclerview);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));
        recyclerView.setAdapter(new SoundAdapter(mBeatBox.getSoundList()));
        return view;
    }

    private class SoundHolder extends RecyclerView.ViewHolder{
        private Button mButton;
        private Sound mSound;

        public SoundHolder(LayoutInflater inflater,ViewGroup container) {
            super(inflater.inflate(R.layout.view_beat_listitem_sound,container,false));
            mButton = (Button)itemView.findViewById(R.id.id_listitem_sound_btn);
        }

        public void bindSound(Sound sound){
            mSound = sound;
            mButton.setText(mSound.getName());
        }
    }

    private class SoundAdapter extends  RecyclerView.Adapter<SoundHolder>{
        private List<Sound> mSoundList;
        public SoundAdapter(List<Sound> soundList){
            mSoundList = soundList;
        }

        @Override
        public SoundHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new SoundHolder(inflater,parent);
        }

        @Override
        public void onBindViewHolder(SoundHolder holder, int position) {
            Sound sound = mSoundList.get(position);
            holder.bindSound(sound);
        }

        @Override
        public int getItemCount() {
            return mSoundList.size();
        }
    }
}
