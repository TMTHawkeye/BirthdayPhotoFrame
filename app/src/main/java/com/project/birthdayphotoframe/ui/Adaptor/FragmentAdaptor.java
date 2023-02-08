package com.project.birthdayphotoframe.ui.Adaptor;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.CalendarContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.project.birthdayphotoframe.BirthdayPhotoFramesActivity;
import com.project.birthdayphotoframe.EditPhotoFrameActivity;
import com.project.birthdayphotoframe.R;
import com.project.birthdayphotoframe.ui.AddTextInterface;
import com.project.birthdayphotoframe.ui.Saved.SavedImagesFragment;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import io.paperdb.Paper;

public class FragmentAdaptor extends RecyclerView.Adapter<FragmentAdaptor.viewHolder> implements Serializable {
    String[] list_fileNames;
    private final int limit = 5;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    Context context;
    String more_btn, ref;
    ArrayList<SavedImagesFragment.ImageModel> imagesModelList;

    String folder_from_asset;
    AddTextInterface addTextInterface;

    public FragmentAdaptor(Context activity, String[] d, String more, String list_item) {
        this.list_fileNames = d;
        context = activity;
        this.more_btn = more;
        this.folder_from_asset = list_item;
    }

    public FragmentAdaptor(Context context1, String[] fonts_list, AddTextInterface addTextInterface) {
        this.list_fileNames = fonts_list;
        context = context1;
        more_btn = "fonts";
        this.addTextInterface = addTextInterface;
    }

    public FragmentAdaptor(FragmentActivity activity, ArrayList<SavedImagesFragment.ImageModel> imageModelList) {
        context = activity;
        this.imagesModelList = imageModelList;
        this.more_btn = "model";
    }

    public FragmentAdaptor(Context activity, String[] images, String more, String path_name, String ref) {
        this.list_fileNames = images;
        context = activity;
        this.more_btn = more;
        this.folder_from_asset = path_name;
        this.ref = ref;
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //for home page
        if (!more_btn.equals("more") && !more_btn.equals("quotes") && !more_btn.equals("fonts") && !more_btn.equals("model")) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_for_home_layout, parent, false);
            viewHolder vH = new viewHolder(v);
            return vH;
            //for quotes
        } else if (more_btn.equals("quotes")) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_quotes_layout, parent, false);
            viewHolder vH = new viewHolder(v);
            return vH;
            //for fonts
        } else if (more_btn.equals("fonts")) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_font_layout, parent, false);
            viewHolder vH = new viewHolder(v);
            return vH;

        }
        //for saved images
        else if (more_btn.equals("model")) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_for_saved_images_layout, parent, false);
            viewHolder vH = new viewHolder(v);
            return vH;
        }
        //for more button elements
        else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout_for_fragments, parent, false);
            viewHolder vH = new viewHolder(v);
            return vH;
        }
    }

    @Override
    public void onBindViewHolder(viewHolder holder, @SuppressLint("RecyclerView") int position) {

        //Case : Quotations

        if (more_btn.equals("quotes")) {
            Log.d("TAG quotes", "onBindViewHolder: " + list_fileNames.length);
            holder.text_Quote.setText(list_fileNames[position]);

            holder.copy_quote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("label_quote", holder.text_Quote.getText().toString());
                    Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show();
                    clipboard.setPrimaryClip(clip);
                }
            });

            holder.share_quote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                    String shareBody = holder.text_Quote.getText().toString();
                    intent.setType("text/plain");
                    intent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                    context.startActivity(Intent.createChooser(intent, shareBody));
                }
            });

            //Case : Saved Fragment

        } else if (more_btn.equals("model")) {

            Glide.with(context).load(imagesModelList.get(position).getUri().getPath()).into(holder.img_saved);
            holder.name_File.setText(imagesModelList.get(position).getFile());

            holder.share_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    Uri screenshotUri = imagesModelList.get(position).getUri();
                    try {
                        InputStream stream = context.getContentResolver().openInputStream(screenshotUri);
                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    sharingIntent.setType("image/jpeg");
                    sharingIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
                    context.startActivity(Intent.createChooser(sharingIntent, "Share image using"));
                }
            });
            holder.saved_rel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, EditPhotoFrameActivity.class);
                    intent.putExtra("folderName", "from_SavedFile");
                    intent.putExtra("model_path", imagesModelList.get(position).getUri().getPath());
                    context.startActivity(intent);
                }
            });

            holder.delete_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new File(imagesModelList.get(position).getUri().toString()).delete();
                    imagesModelList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, imagesModelList.size());
                }
            });

            //Case : Fonts

        } else if (more_btn.equals("fonts")) {
            String font = "font/";
            Typeface typeface1 = Typeface.createFromAsset(context.getAssets(), font + list_fileNames[position]);
            holder.textFont.setTypeface(typeface1);

            holder.card_font.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("font path is : ", "onClick: " + font + list_fileNames[position]);
                    Typeface typeface = Typeface.createFromAsset(context.getAssets(), font + list_fileNames[position]);
                    if (typeface != null) {
                        Log.d("font path is : ", "onClick: " + typeface);
                        addTextInterface.addFont(typeface);
                    } else {
                        Log.d("font path is : ", "onClick: " + typeface);

                    }
                }
            });

            //Case : Frames/Invitations/Stickers

        } else {
            String path = list_fileNames[position];
            String folderName = folder_from_asset + "/";

            //Case : Stickers

            if (folder_from_asset.equals("stickers_list")) {
                holder.share_id.setColorFilter(ContextCompat.getColor(context.getApplicationContext(), R.color.white));
                holder.share_id.setImageResource(R.drawable.share_quote_svg);
                holder.share_id.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String path = list_fileNames[position];
                        shareImage(folderName, path);
                    }
                });
                holder.img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String path = list_fileNames[position];
                        shareImage(folderName, path);
                    }
                });

                //Case : More button pressed

                if (more_btn.equals("more")) {
                    if (!ref.equals("from_add_btn")) {
                        holder.img.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String path = list_fileNames[position];
                                shareImage(folderName, path);
                            }
                        });
                    } else {
                        holder.share_id.setVisibility(View.GONE);
                        holder.rel_to_be_hide.setVisibility(View.GONE);

                        holder.img.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
//                                Toast.makeText(context, "Sticker Added", Toast.LENGTH_SHORT).show();
                                EditPhotoFrameActivity.addTextInterface1.addStickers(folderName + path);
                                ((BirthdayPhotoFramesActivity) context).finish();
                            }
                        });
                    }
                }

                //Case : Frames/Invitations

            } else if (!folder_from_asset.equals("stickers_list")) {

                holder.img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (ref.equals("not_from_add_btn")) {
                            EditPhotoFrameActivity.addTextInterface1.changeFrame(folderName + path);
                            ((BirthdayPhotoFramesActivity) context).finish();

                        } else {
                            Intent intent = new Intent(context, EditPhotoFrameActivity.class);
                            intent.putExtra("folderName", folderName);
                            intent.putExtra("pathName", path);
                            context.startActivity(intent);
                        }

                    }
                });

                holder.share_id.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        if (holder.share_id.getDrawable().getConstantState() == context.getResources().getDrawable(R.drawable.favourite_logo_svg).getConstantState()) {
                            deleteFavData(folderName + path);
                            holder.share_id.setImageResource(R.drawable.border_fav);

                        } else {
                            addfavData(folderName + path);
                            holder.share_id.setImageResource(R.drawable.favourite_logo_svg);
                        }
                    }
                });

            }

            try {
                ArrayList<String> pathDB = getFavforList();
                String pos_of_item = list_fileNames[position];
                if (pathDB != null) {
                    if (pathDB.contains(folderName + pos_of_item)) {
                        holder.share_id.setImageResource(R.drawable.favourite_logo_svg);
                    }
                }
                InputStream inputStream = context.getAssets().open(folderName + path);
                Drawable d = Drawable.createFromStream(inputStream, null);
                holder.img.setImageDrawable(d);
                inputStream.close();


            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void shareImage(String folderName, String path) {

        InputStream inputStream = null;
        try {
            inputStream = context.getAssets().open(folderName + path);
            Log.d("TAG sticker path", "onClick: " + folderName + path);

            Drawable mDrawable = Drawable.createFromStream(inputStream, null);
            Bitmap mBitmap = ((BitmapDrawable) mDrawable).getBitmap();

            Bitmap icon = mBitmap;
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("image/*");

            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, "title");
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
//            checkPermission();
            Uri uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    values);
            OutputStream outstream;
            try {
                outstream = context.getContentResolver().openOutputStream(uri);
                icon.compress(Bitmap.CompressFormat.JPEG, 100, outstream);
                outstream.close();
            } catch (Exception e) {
                System.err.println(e.toString());
            }

            share.putExtra(Intent.EXTRA_STREAM, uri);
            context.startActivity(Intent.createChooser(share, "Share Image"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void showDialog(final String msg, final Context context,
                           final String permission) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Permission necessary");
        alertBuilder.setMessage(msg + " permission is necessary");
        alertBuilder.setPositiveButton(android.R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions((Activity) context,
                                new String[] { permission },
                                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }



    @Override
    public int getItemCount() {
        if (!more_btn.equals("more") && !more_btn.equals("fonts") && !more_btn.equals("model") && !more_btn.equals("quotes")) {
            if (list_fileNames.length > limit) {
                return limit;
            } else {
                return list_fileNames.length;
            }

        } else if (more_btn.equals("model")) {
            return imagesModelList.size();
        } else {
            return list_fileNames.length;
        }

    }


    class viewHolder extends RecyclerView.ViewHolder {
        ImageView img, img_saved, copy_quote, share_quote, share_image, delete_image, share_id, fav_icon;
        TextView text_Quote, name_File, noItemTextView;
        EditText text_written11;
        TextView textFont;
        CardView card_font;
        Context context1;
        RelativeLayout relative_for_share, saved_rel,rel_to_be_hide;


        public viewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img_id);
            noItemTextView = itemView.findViewById(R.id.noItem_textview);
            name_File = itemView.findViewById(R.id.fileName_id);
            saved_rel = itemView.findViewById(R.id.rel_saved);
            img_saved = itemView.findViewById(R.id.img_saved);
            text_Quote = itemView.findViewById(R.id.text_quote);
            share_quote = itemView.findViewById(R.id.share_quote);
            copy_quote = itemView.findViewById(R.id.copy_id);
            textFont = itemView.findViewById(R.id.text_font);
            card_font = itemView.findViewById(R.id.card_font);
            text_written11 = itemView.findViewById(R.id.writtenText11);
            context1 = itemView.getContext();
            relative_for_share = itemView.findViewById(R.id.rel_share);
            share_image = itemView.findViewById(R.id.share_image_id);
            delete_image = itemView.findViewById(R.id.delete_image_id);
            share_id = itemView.findViewById(R.id.share_id);
            fav_icon = itemView.findViewById(R.id.share_id);
            rel_to_be_hide = itemView.findViewById(R.id.rel_to_be_hide);

        }
    }

    public void addfavData(String path) {
        ArrayList<String> pathDB = Paper.book().read("fav_db_list", new ArrayList<>());
        ArrayList<String> duplicateDB = new ArrayList<>();

        if (pathDB == null) {
            duplicateDB.add(path);
            Paper.book().write("fav_db_list", duplicateDB);
        } else {
            if (pathDB.contains(path)) {
                pathDB.remove(path);
            }
            pathDB.add(path);
            Paper.book().write("fav_db_list", pathDB);
        }
    }

    public void deleteFavData(String path) {
        ArrayList<String> fav_list = Paper.book().read("fav_db_list", new ArrayList<>());
        if (fav_list.contains(path)) {
            fav_list.remove(path);
        }
        Paper.book().write("fav_db_list", fav_list);
    }

    public ArrayList<String> getFavforList() {
        ArrayList<String> db_list = Paper.book().read("fav_db_list");
        return db_list;
    }


}
