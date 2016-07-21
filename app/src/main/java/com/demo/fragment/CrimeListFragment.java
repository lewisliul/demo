package com.demo.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.demo.R;
import com.demo.data.CrimeLab;
import com.demo.bean.Crime;
import com.demo.ui.CrimePagerActivity;

import java.util.List;

/**
 * Created by hc on 2016/7/16.
 */
public class CrimeListFragment extends Fragment {
    private static final String TAG = "CrimeListFragment";
    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";
    private RecyclerView mRecyclerView;
    private CrimeAdapter mAdapter;
    private boolean mSubtitleVisible;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crimelist_main, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.id_fragment_crimelist_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //adapter绑定view
        updateUi();
        if(savedInstanceState != null){
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }
        return view;
    }

    private void updateUi() {
        CrimeLab lab = CrimeLab.getInstance(getActivity());
        List<Crime> crimeList = lab.getCrimeList();
        if(mAdapter == null){
            mAdapter = new CrimeAdapter(crimeList);
            mRecyclerView.setAdapter(mAdapter);
        }
        else{
            mAdapter.setCrimeList(crimeList);
            mAdapter.notifyDataSetChanged();
        }
        updateSubtitle();

        Log.d(TAG, "updateUi: ----------" + crimeList.size());
    }

    /**
     * holder类
     */
    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mTitleTextView;
        private TextView mDateTextView;
        private CheckBox mSolvedCheckbox;
        private Crime mCrime;

        public CrimeHolder(View itemView) {
            super(itemView);
            mTitleTextView = (TextView) itemView.findViewById(R.id.id_view_crimelistitem_title_txt);
            mDateTextView = (TextView) itemView.findViewById(R.id.id_view_crimelistitem_date_txt);
            mSolvedCheckbox = (CheckBox) itemView.findViewById(R.id.id_view_crimelistitem_cbx);
            itemView.setOnClickListener(this);
        }

        public void bindCrime(Crime crime) {
            mCrime = crime;
            mTitleTextView.setText(mCrime.getTitle());
            mDateTextView.setText(mCrime.getDate().toString());
            mSolvedCheckbox.setChecked(mCrime.isSolved());
        }

        @Override
        public void onClick(View view) {
            Intent intent = CrimePagerActivity.newIntent(getActivity(),mCrime.getId());
            startActivity(intent);
        }
    }

    /**
     * 适配器adapter
     */
    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {
        private List<Crime> crimeList;

        public CrimeAdapter(List<Crime> list) {
            crimeList = list;
        }

        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.view_fragment_crimelist_recycleview_item, parent, false);
            return new CrimeHolder(view);
        }

        @Override
        public void onBindViewHolder(CrimeHolder holder, int position) {
            Crime crime = crimeList.get(position);
            holder.bindCrime(crime);
        }

        @Override
        public int getItemCount() {
            return crimeList.size();
        }

        public void setCrimeList(List<Crime> crimeLists){
            crimeList = crimeLists;
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        updateUi();
    }

    /**
     * 创建菜单
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list,menu);
        MenuItem item = menu.findItem(R.id.menu_item_show_subtitle);
        if(mSubtitleVisible){
            item.setTitle(R.string.hide_subtitle);
        }
        else{
            item.setTitle(R.string.show_subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_new_crime:
                Crime c = new Crime();
                CrimeLab.getInstance(getActivity()).addCrime(c);
                Intent i = CrimePagerActivity.newIntent(getActivity(),c.getId());
                startActivity(i);
                return true;
            case R.id.menu_item_show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            case R.id.menu_item_delete_crime:
                CrimeLab.getInstance(getActivity()).deleteCrime();
                updateUi();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    /**
     * 添加子menu
     */
    private void updateSubtitle(){
        CrimeLab lab = CrimeLab.getInstance(getActivity());
        int count = lab.getCrimeList().size();
        String s = getResources().getQuantityString(R.plurals.subtitle_plural,count,count);
        if(!mSubtitleVisible){
            s = null;
        }
        AppCompatActivity activity = (AppCompatActivity)getActivity();
        activity.getSupportActionBar().setSubtitle(s);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE,mSubtitleVisible);
    }
}
