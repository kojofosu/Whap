package com.mcdev.whap.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mcdev.whap.Adapters.ImageViewAdapter;
import com.mcdev.whap.Models.StatusModel;
import com.mcdev.whap.R;
import com.mcdev.whap.Utils.MyConstants;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;


public class ImagesFragment extends Fragment {
    private String TAG = this.getClass().getSimpleName();
    private RecyclerView imagesRecyclerView;
    ArrayList<StatusModel> imageModelArrayList;
    ImageViewAdapter imageViewAdapter;
    TextView noImagesTV;
    SwipeRefreshLayout swipeRefreshLayout;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ImagesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ImagesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ImagesFragment newInstance(String param1, String param2) {
        ImagesFragment fragment = new ImagesFragment();
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
        View view = inflater.inflate(R.layout.fragment_images, container, false);

        //init
        init(view);

        //init image arrayList
        imageModelArrayList = new ArrayList<>();

        //init image recycler view
        imagesRecyclerView.setHasFixedSize(true);
        imagesRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        //swipe to refresh listeners
        swipeToRefresh();

        //get Status
        getStatus();
        return view;
    }

    private void swipeToRefresh() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                imageModelArrayList.clear(); //clearing the arrayList first before getting the status to avoid duplication
                //get status
                getStatus();
            }
        });
    }

    private void getStatus() {
        Log.d(TAG, "getStatus: method called");
        if (MyConstants.getWhatsAppStatusDir(requireActivity()).exists()) {
            Log.d(TAG, "getStatus: WhatsApp status directory located.");
            File[] statusFiles = MyConstants.getWhatsAppStatusDir(requireActivity()).listFiles(); // it will list all the files and store them in this array

            /*check if the status files is not empty*/
            if (statusFiles != null && statusFiles.length > 0) {
                Log.d(TAG, "getStatus: statuses are not null");
                Arrays.sort(statusFiles);

                for (final File statusFile : statusFiles) {
                    StatusModel statusModel = new StatusModel(statusFile, statusFile.getName(), statusFile.getAbsolutePath());


                    /*checking if file is not a video and also checking if file is not ".nomedia"*/
                    if (!statusModel.isVideo() && !statusModel.getTitle().equals(MyConstants.NOMEDIA)) {
                        imageModelArrayList.add(statusModel);
                    }
                }
                //pass to adapter
                imageViewAdapter = new ImageViewAdapter(imageModelArrayList, this.getContext(), ImagesFragment.this);
                imagesRecyclerView.setAdapter(imageViewAdapter);
                imageViewAdapter.notifyDataSetChanged();

                /*checking if there are images to display*/
                if (imageModelArrayList.size() < 1) {
                    noImagesTV.setVisibility(View.VISIBLE);
                    //stop refresh layout if it is active
                    if (swipeRefreshLayout.isRefreshing()) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                } else {
                    noImagesTV.setVisibility(View.GONE);
                    //stop refresh layout if it is active
                    if (swipeRefreshLayout.isRefreshing()) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
            }
        }
    }


    private void init(View view) {
        imagesRecyclerView = view.findViewById(R.id.images_recyclerview);
        noImagesTV = view.findViewById(R.id.no_images_tv);
        swipeRefreshLayout = view.findViewById(R.id.images_swipe_to_refresh_layout);
    }
}