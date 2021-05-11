package com.example.imaginibus.Fragment;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.fragment.app.Fragment;

import com.example.imaginibus.R;

public class MyFragment extends Fragment {
    // Store instance variables
    private String path;
    private VideoView vid;
    private boolean is_first = true;

    // newInstance constructor for creating fragment with arguments
    public static MyFragment newInstance( String path ) {
        MyFragment fragment = new MyFragment();
        Bundle args = new Bundle();
        args.putString("Video_Path", path);
        fragment.setArguments(args);
        return fragment;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        path = getArguments().getString("Video_Path");
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_video, container, false);
        vid = view.findViewById(R.id.view_video);

        if(getUserVisibleHint()){
            MediaController mediaController = new MediaController(getContext());
            mediaController.setAnchorView(vid);
            vid.setMediaController(mediaController);
            vid.setVideoPath(path);
            vid.requestFocus();
            vid.start();
        }
        
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (vid == null) return;

        if (getView() != null && isVisibleToUser) {
            MediaController mediaController = new MediaController(getContext());
            mediaController.setAnchorView(vid);

            vid.setMediaController(mediaController);
            vid.setVideoPath(path);
            vid.requestFocus();
            vid.start();
        } else {
            vid.setVideoPath(path);
            vid.requestFocus();
            vid.pause();
        }
    }
}
