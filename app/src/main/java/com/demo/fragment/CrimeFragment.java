package com.demo.fragment;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.demo.R;
import com.demo.bean.Crime;
import com.demo.data.CrimeLab;
import com.demo.util.PictureUtils;

import java.io.File;
import java.util.Date;
import java.util.UUID;


/**
 * Created by hao on 2016/7/15.
 */
public class CrimeFragment extends Fragment {
    private static final String ARG_CRIME_ID = "arg_crime_id";
    private static final String DIALOG_DATE = "DialogDate";
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_CONTACT = 1;
    private static final int REQUEST_PHOTO= 2;
    private Crime mCrime;
    private EditText mTitleEdit;
    private Button mDateBtn,mReportBtn,mSuspectBtn;
    private CheckBox mSolvedCheckBox;
    private ImageButton mPhotoBtn;
    private ImageView mPhotoView;
    private File mPhotoFile;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCrime = new Crime();
        UUID id = (UUID)getArguments().getSerializable(ARG_CRIME_ID);
        mCrime = CrimeLab.getInstance(getActivity()).getCrime(id);
        mPhotoFile = CrimeLab.getInstance(getActivity()).getPhotoFile(mCrime);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime_main,container,false);
        mTitleEdit = (EditText)v.findViewById(R.id.id_fragment_crime_edittext_title);
        mDateBtn = (Button)v.findViewById(R.id.id_fragment_crime_date_btn);
        mSolvedCheckBox = (CheckBox)v.findViewById(R.id.id_fragment_crime_solved_cbx);
        mReportBtn = (Button)v.findViewById(R.id.id_crime_report_btn);
        mSuspectBtn = (Button)v.findViewById(R.id.id_crime_suspect_btn);
        mPhotoBtn = (ImageButton)v.findViewById(R.id.id_crime_camera_imgbtn);
        mPhotoView = (ImageView)v.findViewById(R.id.id_crime_camera_imgview);

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
        updateDate();

        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mCrime.setSolved(b);
            }
        });
        mSolvedCheckBox.setChecked(mCrime.isSolved());

        //时间按钮的点击事件
        mDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialogFragment = DatePickerFragment.newInstance(mCrime.getDate());
                dialogFragment.show(manager,DIALOG_DATE);
                //设置目标的fragment
                dialogFragment.setTargetFragment(CrimeFragment.this,REQUEST_DATE);

            }
        });

        //发送report消息
        mReportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT,getCrimeReport());
                i.putExtra(Intent.EXTRA_SUBJECT,getString(R.string.crime_report_subject));
                //创建一个选择器显示响应隐式intent的全部activity
                i = Intent.createChooser(i,getString(R.string.send_report));
                startActivity(i);
            }
        });
        //添加怀疑人
        final Intent pickContact = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        //临时添加额外的类别给Intent，没什么用只是不让任何联系人应用和你的INTENT匹配
        //  pickContact.addCategory(Intent.CATEGORY_HOME);
        mSuspectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(pickContact,REQUEST_CONTACT);
            }
        });
        if(mCrime.getSuspect() != null){
            mSuspectBtn.setText(mCrime.getSuspect());
        }

        PackageManager packageManager = getActivity().getPackageManager();
        if(packageManager.resolveActivity(pickContact,PackageManager.MATCH_DEFAULT_ONLY) == null){
            mSuspectBtn.setEnabled(false);
        }

        //使用相机intent
        final Intent captureImageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        boolean canTakePhoto = mPhotoFile != null && captureImageIntent.resolveActivity(packageManager) != null;
        mPhotoBtn.setEnabled(canTakePhoto);
        if(canTakePhoto){
            Uri uri = Uri.fromFile(mPhotoFile);
            captureImageIntent.putExtra(MediaStore.EXTRA_OUTPUT,uri);
        }
        mPhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(captureImageIntent,REQUEST_PHOTO);
            }
        });

        //显示图片
        updatePhotoView();

        return v;
    }
    //创建crimeFragment对象
    public static CrimeFragment newInstace(UUID crimeId){
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID,crimeId);
        CrimeFragment cf = new CrimeFragment();
        cf.setArguments(args);
        return cf;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != Activity.RESULT_OK){
            return;
        }
        if(requestCode == REQUEST_DATE){
            //覆盖方法，从extra中获取日期数据，设置对应crime的记录日期，然后刷新日期按钮的显示
            Date date = (Date)data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setDate(date);
            updateDate();
        }
        else if(requestCode == REQUEST_CONTACT && data != null){
            //得到联系人传回的数据
            Uri uri = data.getData();
            String[] queryField = new String[]{ContactsContract.Contacts.DISPLAY_NAME};

            Cursor c = getActivity().getContentResolver().query(uri,queryField,null,null,null);
            try{
                if(c.getCount() == 0){
                    return;
                }
                c.moveToFirst();
                String suspect = c.getString(0);
                mCrime.setSuspect(suspect);
                mSuspectBtn.setText(suspect);
            }finally {
                c.close();
            }
        }
        else if(requestCode == REQUEST_PHOTO){
            updatePhotoView();
        }
    }

    private void updateDate() {
        mDateBtn.setText(mCrime.getDate().toString());
    }

    /**
     * 刷新lab中的crime数据
     */
    @Override
    public void onPause() {
        super.onPause();
        CrimeLab.getInstance(getActivity()).updateCrime(mCrime);
    }

    /**
     * 创建四段字符串信息，并返回拼接完整的消息
     */
    private String getCrimeReport(){
        String solvedString = null;
        if(mCrime.isSolved()){
            solvedString = getString(R.string.crime_report_solved);
        }
        else{
            solvedString = getString(R.string.crime_report_unsolved);
        }
        String dateFormat = "EEE, MMM dd";
        String dateString = DateFormat.format(dateFormat, mCrime.getDate()).toString();

        String suspect = mCrime.getSuspect();
        if(suspect == null){
            suspect = getString(R.string.crime_report_no_suspect);
        }
        else{
            suspect = getString(R.string.crime_report_suspect,suspect);
        }
        String report = getString(R.string.crime_report,mCrime.getTitle(),dateString,solvedString,suspect);

        return report;
    }

    /**
     * 更新photoview的图片
     */
    private void updatePhotoView(){
        if(mPhotoFile == null || !mPhotoFile.exists()){
            mPhotoView.setImageDrawable(null);
        }else{
            Bitmap bitmap = PictureUtils.getScaleBitmap(mPhotoFile.getPath(),getActivity());
            mPhotoView.setImageBitmap(bitmap);
        }
    }

}
