package com.project.birthdayphotoframe;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.project.birthdayphotoframe.databinding.ActivityCustomDialogueSaveImageBinding;
import com.project.birthdayphotoframe.ui.Saved.SavedImagesFragment;
import com.xiaopo.flying.sticker.StickerView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CustomDialogueSaveImage extends Dialog {

    public Activity c;
    public Dialog d;
    public Drawable imageDrawable;
    //    StickerView imageView,imageViewForFrames;
    String nameOfFile;
    ActivityCustomDialogueSaveImageBinding binding;

    public CustomDialogueSaveImage(Activity a, Drawable drawable) {
        super(a);
        this.c = a;
        this.imageDrawable = drawable;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCustomDialogueSaveImageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        final Handler handler = new Handler(Looper.getMainLooper());
        Window window = getWindow();
        window.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);

        binding.saveImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.textToSaveImg.getText().toString() != null) {
                    nameOfFile=binding.textToSaveImg.getText().toString();
                } else {
                    nameOfFile = "default";
                }

                if (TextUtils.isEmpty(binding.textToSaveImg.getText().toString())) {
                    binding.textToSaveImg.setError("File name can not be Empty!");
                    return;
                }
                if (binding.textToSaveImg.getText().length() <= 8) {
                    binding.progressSave.setVisibility(View.VISIBLE);
                    binding.saveImgBtn.setVisibility(View.GONE);
                    executorService.execute(() -> {
                        BitmapDrawable bitmapDrawable = (BitmapDrawable) imageDrawable;
                        if (bitmapDrawable.getBitmap() != null) {
                            Bitmap bt = bitmapDrawable.getBitmap();
                            String savedImageName = saveToInternalStorage(bt, nameOfFile);
                        } else {
                            Toast.makeText(c, "Unable to save the file", Toast.LENGTH_SHORT).show();
                        }

                        handler.post(() -> {
                            Toast.makeText(c, "Image have been saved successfully", Toast.LENGTH_SHORT).show();
                            binding.progressSave.setVisibility(View.GONE);

                            c.finish();
                            dismiss();
                        });
                    });

                } else {
                    Toast.makeText(c, "Please enter name with max length of 8", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    private String saveToInternalStorage(Bitmap bitmapImage, String nameOfFile) {
        ContextWrapper cw = new ContextWrapper(c.getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir

        File directory = new File(c.getExternalFilesDir("Invitation Cards"), "");
        // Create imageDir
        File mypath = new File(directory, nameOfFile + ".png");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }


}