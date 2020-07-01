package com.mcdev.whap.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mcdev.whap.Adapters.VideoViewAdapter;
import com.mcdev.whap.Models.StatusModel;
import com.mcdev.whap.R;
import com.mcdev.whap.Utils.MyConstants;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VideosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VideosFragment extends Fragment {
    private String TAG = this.getClass().getSimpleName();
    private RecyclerView videosRecyclerView;
    ArrayList<StatusModel> videoModelArrayList;
    VideoViewAdapter videoViewAdapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public VideosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VideosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VideosFragment newInstance(String param1, String param2) {
        VideosFragment fragment = new VideosFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_videos, container, false);

        //init
        init(view);

        //init video arrayList
        videoModelArrayList = new ArrayList<>();

        //init video recycler view
        videosRecyclerView.setHasFixedSize(true);
        videosRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        //get status
        getStatus();
        return view;
    }

    private void getStatus() {
        Log.d(TAG, "getStatus: method called");
        if (MyConstants.getWhatsAppStatusDir(requireContext()).exists()) {
            Log.d(TAG, "getStatus:  WhatsApp status directory located.");
            File[] statusFiles = MyConstants.getWhatsAppStatusDir(requireContext()).listFiles();

            /*check if the status files is not empty*/
            if (statusFiles != null && statusFiles.length > 0) {
                Log.d(TAG, "getStatus: statuses are not null");
                Arrays.sort(statusFiles);

                for (final File statusFile : statusFiles) {
                    StatusModel statusModel = new StatusModel(statusFile, statusFile.getName(), statusFile.getAbsolutePath());

                    /*checking if file is a video and also checking if file is not ".nomedia"*/
                    if (statusModel.isVideo() && !statusModel.getTitle().equals(MyConstants.NOMEDIA)) {
                        videoModelArrayList.add(statusModel);
                    }
                }
                /*pass to adapter*/
                videoViewAdapter = new VideoViewAdapter(videoModelArrayList, this.getContext(), VideosFragment.this);
                videosRecyclerView.setAdapter(videoViewAdapter);
                videoViewAdapter.notifyDataSetChanged();
            }
        }
    }

    private void init(View view) {
        videosRecyclerView = view.findViewById(R.id.videos_recyclerview);
    }
}