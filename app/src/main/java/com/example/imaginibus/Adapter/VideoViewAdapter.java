package com.example.imaginibus.Adapter;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import com.example.imaginibus.Fragment.MyFragment;
import com.example.imaginibus.Model.VideoModel;
import com.example.imaginibus.R;

import java.util.ArrayList;

public class VideoViewAdapter extends SmartFragmentStatePagerAdapter{
    private ArrayList<VideoModel> modelArrayList;

    public VideoViewAdapter(FragmentManager fragmentManager, ArrayList<VideoModel> list) {
        super(fragmentManager);
        modelArrayList = list;
    }

    @Override
    public int getCount() {
        return modelArrayList.size();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return MyFragment.newInstance(modelArrayList.get(position).getPath());
    }
}
