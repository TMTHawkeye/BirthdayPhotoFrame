package com.project.birthdayphotoframe;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.project.birthdayphotoframe.databinding.ActivityEditPhotoFrameBinding;
import com.project.birthdayphotoframe.ui.Adaptor.FragmentAdaptor;
import com.project.birthdayphotoframe.ui.AddTextInterface;
import com.project.birthdayphotoframe.ui.Saved.SavedImagesFragment;
import com.project.birthdayphotoframe.ui.home.HomeFragment;
import com.xiaopo.flying.sticker.DrawableSticker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EditPhotoFrameActivity extends AppCompatActivity implements AddTextInterface {

    ActivityEditPhotoFrameBinding binding;
    int SELECT_PICTURE = 200;
    String pathName = "";
    String folderName = "";

    public static AddTextInterface addTextInterface1;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityEditPhotoFrameBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        addTextInterface1 = this;


        binding.tool1.saveBtnId.setVisibility(View.VISIBLE);
        InputStream inputStream = null;

        folderName = getIntent().getStringExtra("folderName");
        pathName = getIntent().getStringExtra("pathName");


        if (!folderName.equals("from_SavedFile")) {
            try {
                inputStream = getApplicationContext().getAssets().open(folderName + pathName);
                Drawable d = Drawable.createFromStream(inputStream, null);
                binding.imgFrame.setImageDrawable(d);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            String pathOfFile = getIntent().getStringExtra("model_path");
            binding.imgFrame.setImageURI(Uri.parse(pathOfFile));
            binding.linearBottom.setVisibility(View.GONE);
            binding.tool1.saveBtnId.setVisibility(View.GONE);
            binding.layoutShareAndDltBottom.setVisibility(View.VISIBLE);


            binding.shareSavedImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    Uri screenshotUri = Uri.parse(pathOfFile);
                    try {
                        InputStream stream = getApplicationContext().getContentResolver().openInputStream(screenshotUri);
                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    sharingIntent.setType("image/jpeg");
                    sharingIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
                    startActivity(Intent.createChooser(sharingIntent, "Share image using"));
                }
            });

            binding.dltSavedImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                    new File(Uri.parse(pathOfFile).toString()).delete();
                    SavedImagesFragment.minterface.deleteCall("");
                }
            });


        }

        binding.tool1.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.tool1.saveBtnId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.imageViewForFrames.setShowBorder(false);
                binding.imageViewForFrames.setShowIcons(false);
                binding.imageView.setShowIcons(false);
                binding.imageView.setShowBorder(false);
//                RelativeLayout layout = (RelativeLayout)findViewById(R.id.rel);
                binding.l1.setDrawingCacheEnabled(true);
                Bitmap bm = Bitmap.createBitmap(binding.l1.getDrawingCache());
                Drawable d = new BitmapDrawable(bm);
                CustomDialogueSaveImage c = new CustomDialogueSaveImage(EditPhotoFrameActivity.this, d);
                c.show();
            }
        });

        binding.layoutAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChooser();
            }
        });

        binding.layoutAddFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] list = new String[0];
                String fN;
                fN = folderName.substring(0, folderName.length() - 1);
                startActivity(new Intent(getApplicationContext(), BirthdayPhotoFramesActivity.class).putExtra("path", fN).putExtra("val", "fromEdit").putExtra("reference_add_stk", "not_from_add_btn"));
            }
        });

        binding.layoutAddText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.imageViewForFrames.setVisibility(View.VISIBLE);
                binding.imageView.setVisibility(View.VISIBLE);
                CustomAdaptorDialogueAddText customAdaptorDialogueAddText = new CustomAdaptorDialogueAddText(EditPhotoFrameActivity.this, binding.imageViewForFrames, R.style.DialogTheme);

                customAdaptorDialogueAddText.show();
            }
        });

        binding.layoutAddSticker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), BirthdayPhotoFramesActivity.class).putExtra("path", "stickers_list").putExtra("reference_add_stk", "from_add_btn"));

            }
        });


        if (!folderName.equals("frames_list/")) {
            binding.layoutShareFromEdit.setVisibility(View.VISIBLE);

            binding.layoutShareFromEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    shareImage(folderName, pathName);
                }
            });
        }
    }

    private void shareImage(String folderName, String path) {

        InputStream inputStream = null;
        try {
            inputStream = getApplicationContext().getAssets().open(folderName + path);
            Log.d("TAG sticker path", "onClick: " + folderName + path);

            Drawable mDrawable = Drawable.createFromStream(inputStream, null);
            Bitmap mBitmap = ((BitmapDrawable) mDrawable).getBitmap();

            Bitmap icon = mBitmap;
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("image/jpeg");

            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, "title");
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            Uri uri = getApplicationContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    values);
            OutputStream outstream;
            try {
                outstream = getApplicationContext().getContentResolver().openOutputStream(uri);
                icon.compress(Bitmap.CompressFormat.JPEG, 100, outstream);
                outstream.close();
            } catch (Exception e) {
                System.err.println(e.toString());
            }

            share.putExtra(Intent.EXTRA_STREAM, uri);
            startActivity(Intent.createChooser(share, "Share Image"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    void imageChooser() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }

    @SuppressLint("ClickableViewAccessibility")
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == SELECT_PICTURE) {

                Uri selectedImageUri = data.getData();

                if (selectedImageUri != null) {
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                        Drawable d = new BitmapDrawable(bitmap);

                        if (folderName.equals("frames_list/") || folderName.equals("cakes_list/")) {
                            binding.imageView.addSticker(new DrawableSticker(d));

                        } else if (folderName.equals("invitations_list/") || folderName.equals("wishes_list/")) {
                            binding.imageViewForFrames.addSticker(new DrawableSticker(d));
                        } else {
                            Toast.makeText(this, "None selected", Toast.LENGTH_SHORT).show();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(this, "Data not set", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }

    @Override
    public void addFont(Typeface typeface) {
    }

    @Override
    public void addStickers(String p_name) {
        binding.imageView.setVisibility(View.VISIBLE);
        InputStream inputStream = null;
        try {
            inputStream = getApplicationContext().getAssets().open(p_name);
            Drawable d = Drawable.createFromStream(inputStream, null);
            binding.imageViewForFrames.addSticker(new DrawableSticker(d));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void changeFrame(String path) {
        InputStream inputStream = null;
        try {
            inputStream = getApplicationContext().getAssets().open(path);
            Drawable d = Drawable.createFromStream(inputStream, null);
            binding.imgFrame.setImageDrawable(d);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}