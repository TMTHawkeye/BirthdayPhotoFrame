package com.project.birthdayphotoframe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.project.birthdayphotoframe.databinding.ActivityBirthdayPhotoFramesBinding;
import com.project.birthdayphotoframe.ui.Adaptor.ViewPagerAdaptor;
import com.project.birthdayphotoframe.ui.FramesFragments.PhotoOnFrameFragment;

import java.util.Objects;

public class BirthdayPhotoFramesActivity extends AppCompatActivity {

    ActivityBirthdayPhotoFramesBinding binding;
    ViewPager viewPager;
    TabLayout tabLayout;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBirthdayPhotoFramesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        String path_name = getIntent().getStringExtra("path");

        viewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tabs);
        String ref = getIntent().getStringExtra("reference_add_stk");


//        String [] list= getIntent().getStringArrayListExtra("list").toArray(new String[0]);
//
//        if(list.length!=0)
//        {
//            binding.tabs.setVisibility(View.GONE);
//        }

        String val = "s";
        val = getIntent().getStringExtra("val");

        if (path_name.equals("frames_list")) {
            binding.tool1.titalOfToolbar.setText("Birthday Photo Frames");
            ViewPagerAdaptor adapter = new ViewPagerAdaptor(getSupportFragmentManager());
            adapter.addFragment(new PhotoOnFrameFragment(path_name,ref), "Photo Frames");
            if (!Objects.equals(val, "fromEdit")) {
                adapter.addFragment(new PhotoOnFrameFragment("cakes_list",ref), "Photo on Cakes");
            }
                viewPager.setAdapter(adapter);
            if (!Objects.equals(val, "fromEdit")) {
                tabLayout.setVisibility(View.VISIBLE);
                tabLayout.setupWithViewPager(viewPager);
            }
        } else if (path_name.equals("wishes_list")) {
            binding.tool1.titalOfToolbar.setText("Birthday Invitations & wishes");

            ViewPagerAdaptor adapter = new ViewPagerAdaptor(getSupportFragmentManager());
            adapter.addFragment(new PhotoOnFrameFragment(path_name,ref), "Birthday Invitations ");
            if (!Objects.equals(val, "fromEdit")) {
                adapter.addFragment(new PhotoOnFrameFragment("invitations_list",ref), "Birthday Wishes");
            }
            viewPager.setAdapter(adapter);
            if (!Objects.equals(val, "fromEdit")) {
                tabLayout.setVisibility(View.VISIBLE);
                tabLayout.setupWithViewPager(viewPager);
            }
            // for Quotations
        } else {
            binding.tool1.titalOfToolbar.setText("Birthday Stickers & Quotations");
            ViewPagerAdaptor adapter = new ViewPagerAdaptor(getSupportFragmentManager());
            adapter.addFragment(new PhotoOnFrameFragment(path_name,ref), "Birthday Stickers");
            if (!ref.equals("from_add_btn")) {
                adapter.addFragment(new PhotoOnFrameFragment("quotations_list",ref), "Birthday Quotations");
            }
            viewPager.setAdapter(adapter);
            if (!Objects.equals(val, "fromEdit")) {
                tabLayout.setVisibility(View.VISIBLE);
                if (ref.equals("from_add_btn")) {
                    tabLayout.setVisibility(View.GONE);

                }
                    tabLayout.setupWithViewPager(viewPager);
            }
        }


        binding.tool1.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}