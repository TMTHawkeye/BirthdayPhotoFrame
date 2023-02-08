package com.project.birthdayphotoframe.ui.Favourites;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.project.birthdayphotoframe.BirthdayPhotoFramesActivity;
import com.project.birthdayphotoframe.databinding.FragmentFavouritesBinding;
import com.project.birthdayphotoframe.ui.Adaptor.FavouritesAdaptor;
import com.project.birthdayphotoframe.ui.Adaptor.FragmentAdaptor;

import java.io.IOException;
import java.util.ArrayList;

import io.paperdb.Paper;

public class FavouritesFragment extends Fragment {

    private FragmentFavouritesBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentFavouritesBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();

        binding.toolbarRefFav.titalOfToolbar.setText("Favourite Frames and cards");


        ArrayList<String> pathDB = new ArrayList<>();
        pathDB = Paper.book().read("fav_db_list");

        if(pathDB.size()==0)
        {
            binding.noFavouriteTextView.setVisibility(View.VISIBLE);
        }


        if (pathDB != null) {
            binding.recyclerViewFavourites.setLayoutManager(new GridLayoutManager(requireActivity(), 3));
            FavouritesAdaptor favouritesAdaptor = new FavouritesAdaptor(requireContext(), pathDB);
            binding.recyclerViewFavourites.setAdapter(favouritesAdaptor);
        }

        binding.toolbarRefFav.back.setVisibility(View.GONE);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}