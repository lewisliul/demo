package com.demo.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.demo.R;
import com.demo.base.CrimeLab;
import com.demo.bean.Crime;

import java.util.UUID;


/**
 * Created by hao on 2016/7/15.
 */
public class CrimeFragment extends Fragment {
    private static final String ARG_CRIME_ID = "arg_crime_id";
    private Crime mCrime;
    private EditText mTitleEdit;
    private Button mDateBtn;
    private CheckBox mSolvedCheckBox;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCrime = new Crime();
        UUID id = (UUID)getArguments().getSerializable(ARG_CRIME_ID);
        mCrime = CrimeLab.getInstance(getActivity()).getCrime(id);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime_main,container,false);
        mTitleEdit = (EditText)v.findViewById(R.id.id_fragment_crime_edittext_title);
        mDateBtn = (Button)v.findViewById(R.id.id_fragment_crime_date_btn);
        mSolvedCheckBox = (CheckBox)v.findViewById(R.id.id_fragment_crime_solved_cbx);
        mTitleEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCrime.setTitle(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mTitleEdit.setText(mCrime.getTitle());
        mDateBtn.setText(mCrime.getDate().toString());
        mDateBtn.setEnabled(false);
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mCrime.setSolved(b);
            }
        });
        mSolvedCheckBox.setChecked(mCrime.isSolved());

        return v;
    }

    public static CrimeFragment newInstace(UUID crimeId){
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID,crimeId);
        CrimeFragment cf = new CrimeFragment();
        cf.setArguments(args);
        return cf;
    }
}
