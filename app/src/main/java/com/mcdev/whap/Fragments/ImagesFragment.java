package com.mcdev.whap.Fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mcdev.whap.Adapters.ImageViewAdapter;
import com.mcdev.whap.Models.StatusModel;
import com.mcdev.whap.R;
import com.mcdev.whap.Utils.MyConstants;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;


public class ImagesFragment extends Fragment {
    private String TAG = this.getClass().getSimpleName();
    private RecyclerView imagesRecyclerView;
    ArrayList<StatusModel> imageModelArrayList;
    ImageViewAdapter imageViewAdapter;

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

        //get Status
        getStatus();
        return view;
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
            }
        }
    }

    private Bitmap getThumbnail(StatusModel statusModel) {

        if (statusModel.isVideo()) {
            return ThumbnailUtils.createVideoThumbnail(statusModel.getFile().getAbsolutePath(),
                    MediaStore.Video.Thumbnails.MICRO_KIND);
        } else {
            return ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(statusModel.getFile().getAbsolutePath()),
                    MyConstants.THUMBSIZE,
                    MyConstants.THUMBSIZE);
        }
    }


    /*download whatsApp statuses*/
    public void downloadImage(StatusModel statusModel) throws IOException {

        if (!MyConstants.getSavedStatusesDir(requireContext()).exists()) {
            MyConstants.getSavedStatusesDir(requireContext()).mkdirs();
        }

        //creating the file
        File destinationFile = new File(MyConstants.getSavedStatusesDir(requireContext()) + File.separator + statusModel.getTitle());
        //if user tries to save the same file twice, the old one will be deleted
        if (destinationFile.exists()) {
            destinationFile.delete();
        }

        //copying file from whatsApp directory to our directory
        copyFile(statusModel.getFile(), destinationFile);

        /*refreshing the gallery*/
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(destinationFile));
        Objects.requireNonNull(getActivity()).sendBroadcast(intent);

    }

    private void copyFile(File file, File destinationFile) throws IOException {
        //checking if destination file's parent exits
        if (!Objects.requireNonNull(destinationFile.getParentFile()).exists()) {
            destinationFile.getParentFile().mkdirs();
        }

        /*checking if destination file exists*/
        if (!destinationFile.exists()) {
            destinationFile.createNewFile();
        }

        FileChannel source = new FileInputStream(file).getChannel();
        FileChannel destination = new FileOutputStream(destinationFile).getChannel();
        destination.transferFrom(source, 0, source.size());     //start copying

        /*close channels*/
        source.close();
        destination.close();

        if (!source.isOpen() && !destination.isOpen()) {
            Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show();
        }
    }

    private void init(View view) {
        imagesRecyclerView = view.findViewById(R.id.images_recyclerview);
    }
}