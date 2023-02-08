package com.project.birthdayphotoframe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.res.FontResourcesParserCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Layout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.github.dhaval2404.colorpicker.ColorPickerDialog;
import com.github.dhaval2404.colorpicker.listener.ColorListener;
import com.github.dhaval2404.colorpicker.listener.DismissListener;
import com.github.dhaval2404.colorpicker.model.ColorShape;
import com.github.dhaval2404.colorpicker.util.ColorUtil;
import com.project.birthdayphotoframe.databinding.ActivityCustomDialogueAddBinding;
import com.project.birthdayphotoframe.ui.Adaptor.FragmentAdaptor;
import com.project.birthdayphotoframe.ui.AddTextInterface;
import com.xiaopo.flying.sticker.StickerView;
import com.xiaopo.flying.sticker.TextSticker;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class CustomAdaptorDialogueAddText extends Dialog implements AddTextInterface {

    ActivityCustomDialogueAddBinding binding;
    TextSticker textSticker;
    Activity c;
    StickerView imageViewForFrames;


    public CustomAdaptorDialogueAddText(Activity a, StickerView imageViewForFrames, int dialogTheme) {
        super(a,dialogTheme);
        this.c = a;
        this.imageViewForFrames=imageViewForFrames;


    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCustomDialogueAddBinding.inflate(LayoutInflater.from(c),null,false);
        setContentView(binding.getRoot());
        String[] fonts_list = new String[0];
        String[] colors_list = new String[0];

        Window window =getWindow();
        window.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);



        AssetManager assetManager = c.getApplication().getAssets();
        try {
            fonts_list = assetManager.list("font");
        } catch (IOException e) {
            e.printStackTrace();
        }




        binding.fontsRecyclerView.setLayoutManager(new LinearLayoutManager(c.getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
        FragmentAdaptor fragmentAdaptor=new FragmentAdaptor(c,fonts_list,this);
        binding.fontsRecyclerView.setAdapter(fragmentAdaptor);

        binding.addTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textAdd(binding.writtenText11.getText().toString(),binding.writtenText11.getTypeface(), binding.writtenText11.getCurrentTextColor());

            }
        });

//        pathName=getIntent().getStringExtra("pathName");
//        folderName=getIntent().getStringExtra("folderName");
//
//
//        binding.toolbarAddText.titalOfToolbar.setText("Add Text");
//
        binding.toolbarAddText.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        binding.chooseColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                binding.colorPickerView.setVisibility(View.VISIBLE);
//                binding.chooseColor.setVisibility(View.GONE);
//                binding.addTextBtn.setVisibility(View.GONE);

                new ColorPickerDialog.Builder(getContext())
                        .setColorShape(ColorShape.SQAURE)
                        .setDefaultColor(R.color.blue)
                        .setColorListener(new ColorListener() {
                            @Override
                            public void onColorSelected(int color, @NotNull String colorHex) {
//                                mColor = color;
                                binding.colorPickerView.setColor(color);
                                setButtonBackground((AppCompatButton) binding.chooseColor, color);
                            }
                        })
                        .setDismissListener(new DismissListener() {
                            @Override
                            public void onDismiss() {
                                binding.colorPickerView.setVisibility(View.GONE);
                                binding.chooseColor.setVisibility(View.VISIBLE);
                                binding.addTextBtn.setVisibility(View.VISIBLE);
                                Log.d("ColorPickerDialog", "Handle dismiss event");
                            }
                        })
                        .show();
            }
        });


    }

    private void setButtonBackground(AppCompatButton btn, int color) {
        if (ColorUtil.isDarkColor(color)) {
            btn.setTextColor(Color.WHITE);
        } else {
            btn.setTextColor(Color.WHITE);
        }
//        btn.setBackgroundTintList(ColorStateList.valueOf(color));
        binding.writtenText11.setTextColor(color);
    }

    public void textAdd(String text, Typeface typeface, int color) {
        if (text.equals(null) || text.equals("")) {
            Toast.makeText(c.getApplicationContext(), "Enter Text to Proceed!", Toast.LENGTH_SHORT).show();
        } else {
            textSticker = new TextSticker(c.getApplicationContext());
            textSticker.setText(text);
            textSticker.setTextColor(color);
            try {
                textSticker.setTypeface(typeface);
            } catch (NullPointerException e){
                e.printStackTrace();
            }
            textSticker.setTextAlign(Layout.Alignment.ALIGN_CENTER);
            textSticker.resizeText();
            
            Log.e("text",textSticker.getText());
//            binding.addSticker(textSticker);
            imageViewForFrames.addSticker(textSticker);
            dismiss();

        }
    }

    @Override
    public void addFont(Typeface typeface) {
        binding.writtenText11.setTypeface(typeface);
    }

    @Override
    public void addStickers(String path) {

    }

    @Override
    public void changeFrame(String path) {

    }

}



