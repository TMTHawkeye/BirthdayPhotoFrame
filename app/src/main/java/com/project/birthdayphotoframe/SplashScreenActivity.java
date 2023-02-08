package com.project.birthdayphotoframe;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.window.SplashScreen;

import com.project.birthdayphotoframe.databinding.ActivitySplashScreenBinding;

public class SplashScreenActivity extends AppCompatActivity {

    ActivitySplashScreenBinding binding;
    private static int SPLASH_SCREEN = 3000;

    Animation top_animation
    Animation bottom_animation;
    Animation rotate_animation;
    Animation bounce_animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        top_animation = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottom_animation = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
        rotate_animation = AnimationUtils.loadAnimation(this, R.anim.rotate_animation);
        bounce_animation = AnimationUtils.loadAnimation(this, R.anim.bounce_animation);
//
//        rel_logo=findViewById(R.id.rel_logo);
//        image=findViewById(R.id.main_icon);
//        logo=findViewById(R.id.logo);

//        image.setAnimation(bounce_animation);
        binding.mainIcon.setAnimation(bounce_animation);
        binding.relLogo.setAnimation(bounce_animation);


        new Handler().postDelayed(() -> {
            Intent intent = new Intent(this, MainActivity.class);
            Pair[] pairs = new Pair[2];
            pairs[0] = new Pair<View, String>(binding.relLogo, "logo_text");
            pairs[1] = new Pair<View, String>(binding.mainIcon, "logo_image");

            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SplashScreenActivity.this, pairs);
            startActivity(intent, options.toBundle());
            finish();
        }, SPLASH_SCREEN);
    }
}