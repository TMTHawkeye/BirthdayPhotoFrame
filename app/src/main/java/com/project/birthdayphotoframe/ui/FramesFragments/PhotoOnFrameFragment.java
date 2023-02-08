package com.project.birthdayphotoframe.ui.FramesFragments;

import android.content.res.AssetManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.birthdayphotoframe.R;
import com.project.birthdayphotoframe.ui.Adaptor.FragmentAdaptor;
import com.project.birthdayphotoframe.ui.AddTextInterface;

import java.io.IOException;

public class PhotoOnFrameFragment extends Fragment {

    RecyclerView recyclerView;

    String path_name;
    String ref;

    public PhotoOnFrameFragment(String path_name, String ref) {
        this.path_name = path_name;
        this.ref = ref;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_photo_on_frame, container, false);
//        TextView textView=v.findViewById(R.id.text);
//        textView.setText("First Fragment");


        recyclerView = v.findViewById(R.id.recyclerview_frame_photos);

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));


        if (!path_name.equals("quotations_list")) {
            AssetManager assetManager = getActivity().getAssets();
            String[] images = new String[0];
            try {
                images = assetManager.list(path_name);
                if (!ref.equals("from_add_btn")) {
                    FragmentAdaptor fragmentAdaptor = new FragmentAdaptor(getActivity(), images, "more", path_name, ref);
                    recyclerView.setAdapter(fragmentAdaptor);
                } else {
                    FragmentAdaptor fragmentAdaptor = new FragmentAdaptor(getActivity(), images, "more", path_name, ref);
                    recyclerView.setAdapter(fragmentAdaptor);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            String[] myString = new String[18];
            myString[0] = getActivity().getString(R.string.quote_1);
            myString[1] = getActivity().getString(R.string.quote_2);
            myString[2] = getActivity().getString(R.string.quote_3);
            myString[3] = getActivity().getString(R.string.quote_4);
            myString[4] = getActivity().getString(R.string.quote_5);
            myString[5] = getActivity().getString(R.string.quote_6);
            myString[6] = getActivity().getString(R.string.quote_7);
            myString[7] = getActivity().getString(R.string.quote_8);
            myString[8] = getActivity().getString(R.string.quote_9);
            myString[9] = getActivity().getString(R.string.quote_10);
            myString[10] = getActivity().getString(R.string.quote_11);
            myString[11] = getActivity().getString(R.string.quote_12);
            myString[12] = getActivity().getString(R.string.quote_13);
            myString[13] = getActivity().getString(R.string.quote_14);
            myString[14] = getActivity().getString(R.string.quote_15);
            myString[15] = getActivity().getString(R.string.quote_16);
            myString[16] = getActivity().getString(R.string.quote_17);
            myString[17] = getActivity().getString(R.string.quote_18);

            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            FragmentAdaptor fragmentAdaptorQuote = new FragmentAdaptor(getActivity(), myString, "quotes", "");
            recyclerView.setAdapter(fragmentAdaptorQuote);
        }


        return v;
    }
}