package com.mcdev.whap.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mcdev.whap.Adapters.ImageViewAdapter;
import com.mcdev.whap.Adapters.LibraryViewAdapter;
import com.mcdev.whap.Models.StatusModel;
import com.mcdev.whap.R;
import com.mcdev.whap.Utils.MyConstants;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LibraryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LibraryFragment extends Fragment {
    private String TAG = this.getClass().getSimpleName();
    private RecyclerView libraryRecyclerView;
    ArrayList<StatusModel> libraryModelArrayList;
    LibraryViewAdapter libraryViewAdapter;
    TextView noSavedStatusesTV;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LibraryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LibraryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LibraryFragment newInstance(String param1, String param2) {
        LibraryFragment fragment = new LibraryFragment();
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
        View view = inflater.inflate(R.layout.fragment_library, container, false);

        //init
        init(view);

        //init library arrayList
        libraryModelArrayList = new ArrayList<>();

        //init library recycler view
        libraryRecyclerView.setHasFixedSize(true);
        libraryRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        //get statuses
        getStatus();
        return view;

    }

    private void getStatus() {
        Log.d(TAG, "getStatus: method called");
        if (MyConstants.getSavedStatusesDir(requireContext()).exists()) {
            Log.d(TAG, "getStatus: Whap status directory located.");
            File[] statusFiles = MyConstants.getSavedStatusesDir(requireContext()).listFiles();

            /*check if the status files is not empty*/
            if (statusFiles != null && statusFiles.length > 0) {
                Log.d(TAG, "getStatus: statuses are not null");
                Arrays.sort(statusFiles);

                /*list all types in to recycler view*/
                for (final File statusFile : statusFiles) {
                    StatusModel statusModel = new StatusModel(statusFile, statusFile.getName(), statusFile.getAbsolutePath());

                    /*checking to exclude ".nomedia"*/
                    if (!statusModel.getTitle().equals(MyConstants.NOMEDIA)) {
                        libraryModelArrayList.add(statusModel);
                    }
                }
                /*pass to adapter*/
                libraryViewAdapter = new LibraryViewAdapter(libraryModelArrayList, this.getContext(), LibraryFragment.this);
                libraryRecyclerView.setAdapter(libraryViewAdapter);
                libraryViewAdapter.notifyDataSetChanged();

                /*check if there are any saved statuses*/
                if (libraryModelArrayList.size() < 1) {
                    noSavedStatusesTV.setVisibility(View.VISIBLE);
                } else {
                    noSavedStatusesTV.setVisibility(View.GONE);
                }
            }
        }
    }

    private void init(View view) {
        libraryRecyclerView = view.findViewById(R.id.library_recyclerview);
        noSavedStatusesTV = view.findViewById(R.id.no_saved_statuses_tv);
    }
}