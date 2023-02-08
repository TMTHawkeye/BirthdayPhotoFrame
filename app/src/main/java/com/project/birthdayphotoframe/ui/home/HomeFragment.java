package com.project.birthdayphotoframe.ui.home;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.project.birthdayphotoframe.BirthdayPhotoFramesActivity;
import com.project.birthdayphotoframe.databinding.FragmentHomeBinding;
import com.project.birthdayphotoframe.ui.Adaptor.FragmentAdaptor;

import java.io.IOException;

import io.paperdb.Paper;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        Paper.init(getActivity());

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        AssetManager assetManager = getActivity().getAssets();
        String[] frames_list = new String[0];
        String[] invitations_list = new String[0];
        String[] stickers_list = new String[0];
        try {
            //for frames
            frames_list = assetManager.list("frames_list");
            Log.e("gggTAG", "onCreateView: " + frames_list[0]);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
            binding.recyclerViewFrames.setLayoutManager(linearLayoutManager);
            FragmentAdaptor fragmentAdaptor = new FragmentAdaptor(getActivity(), frames_list, "", "frames_list", "from_add_btn");
            binding.recyclerViewFrames.setAdapter(fragmentAdaptor);


            //for invitations
            invitations_list = assetManager.list("invitations_list");
            LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
            binding.recyclerViewInvitations.setLayoutManager(linearLayoutManager1);
            FragmentAdaptor fragmentAdaptor1 = new FragmentAdaptor(getActivity(), invitations_list, "", "invitations_list", "from_add_btn");
            binding.recyclerViewInvitations.setAdapter(fragmentAdaptor1);


            //for stickers
            stickers_list = assetManager.list("stickers_list");

            LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
            binding.recyclerViewStickers.setLayoutManager(linearLayoutManager2);
            FragmentAdaptor fragmentAdaptor2 = new FragmentAdaptor(getActivity(), stickers_list, "", "stickers_list", "not_from_add_btn");
            binding.recyclerViewStickers.setAdapter(fragmentAdaptor2);
        } catch (IOException e) {
            e.printStackTrace();
        }


        binding.moreFrames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), BirthdayPhotoFramesActivity.class).putExtra("path", "frames_list").putExtra("reference_add_stk", "frm"));
            }
        });

        binding.moreInvitations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), BirthdayPhotoFramesActivity.class).putExtra("path", "wishes_list").putExtra("reference_add_stk", "inv"));
            }
        });

        binding.moreStickers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), BirthdayPhotoFramesActivity.class).putExtra("path", "stickers_list").putExtra("reference_add_stk", "stk"));

            }
        });
        return root;
    }


}