package com.project.birthdayphotoframe.ui.Saved;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.project.birthdayphotoframe.ui.CallBack;
import com.project.birthdayphotoframe.databinding.FragmentSavedimagesBinding;
import com.project.birthdayphotoframe.ui.Adaptor.FragmentAdaptor;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SavedImagesFragment extends Fragment implements CallBack {

    private FragmentSavedimagesBinding binding;

    ExecutorService executorService;
    final Handler handler = new Handler(Looper.getMainLooper());
    public static CallBack minterface;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

//        Paper.init(getContext());


        binding = FragmentSavedimagesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        minterface = this;
        executorService = Executors.newSingleThreadExecutor();

        binding.toolbarSavedScreen.titalOfToolbar.setText("Saved Images");

        binding.toolbarSavedScreen.back.setVisibility(View.GONE);


        executorService.execute(() -> {
            File directory = new File(getActivity().getExternalFilesDir("Invitation Cards"), "");
            File[] imagep = directory.listFiles();

            ArrayList<ImageModel> imageModelList = new ArrayList<>();
            for (int i = 0; i < imagep.length; i++) {
                imageModelList.add(new ImageModel(imagep[i].getName(), Uri.parse(imagep[i].getAbsolutePath())));
            }
            if (imageModelList.size() == 0) {
                binding.noItemTextview.setVisibility(View.VISIBLE);
                binding.progressLoading.setVisibility(View.GONE);
                binding.recyclerViewSavedImages.setVisibility(View.GONE);
            }

            handler.post(() -> {
                binding.progressLoading.setVisibility(View.GONE);
                binding.recyclerViewSavedImages.setLayoutManager(new GridLayoutManager(getActivity(), 3));
                FragmentAdaptor fragmentAdaptor = new FragmentAdaptor(getActivity(), imageModelList);
                binding.recyclerViewSavedImages.setAdapter(fragmentAdaptor);
            });

        });


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void deleteCall(String string) {
        executorService.execute(() -> {
            File directory = new File(getActivity().getExternalFilesDir("Invitation Cards"), "");
            File[] imagep = directory.listFiles();

            ArrayList<ImageModel> imageModelList = new ArrayList<>();
            for (int i = 0; i < imagep.length; i++) {
                imageModelList.add(new ImageModel(imagep[i].getName(), Uri.parse(imagep[i].getAbsolutePath())));
            }

            handler.post(() -> {
                binding.progressLoading.setVisibility(View.GONE);
                binding.recyclerViewSavedImages.setLayoutManager(new GridLayoutManager(getActivity(), 3));
                FragmentAdaptor fragmentAdaptor = new FragmentAdaptor(getActivity(), imageModelList);
                binding.recyclerViewSavedImages.setAdapter(fragmentAdaptor);
            });

        });
    }

    public class ImageModel {

        String file;
        Uri uri;

        ImageModel(String file1, Uri uri1) {
            this.file = file1;
            this.uri = uri1;
        }

        public String getFile() {
            return file;
        }

        public Uri getUri() {
            return uri;
        }
    }

}