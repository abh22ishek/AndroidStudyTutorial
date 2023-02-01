package com.example.androidstudytutorial.main;

import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.work.impl.model.WorkProgressDao;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.androidstudytutorial.model.Images;
import com.example.androidstudytutorial.model.ListDescx;
import com.mine.mywallpaper.BuildConfig;
import com.mine.mywallpaper.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class DisplayFragment extends Fragment {

    private ImageView imageview;
    private final String TAG = DisplayFragment.class.getSimpleName();
    private ViewFlipper viewFlipper;
    private ImageView imagePrevious;
    private ImageView imageNext;

    private Button download;
    private Button share;
    private Button setWallpaper;


    private int position = 0;
    ProgressDialog progressDialog;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view= inflater.inflate(R.layout.fragment_layout, container,false);
        return view;
    }

    int mDrawable =0;
    String fileName= "";
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageview=view.findViewById(R.id.imageView);
        mDrawable  = getArguments().getInt("IMAGE");
        fileName  = getArguments().getString("FILE_NAME");

        setImageView(mDrawable);

        //imageview.setBackground(getResources().getDrawable(mDrawable,null));
        imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG ,"imageview onClick()");
            }
        });

        download =view.findViewById(R.id.btnDownload);
        share =view.findViewById(R.id.btnShare);
        setWallpaper =view.findViewById(R.id.btnSetWallPaper);
        setWallpaper.setEnabled(true);
        setWallpaper.setVisibility(View.VISIBLE);

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    new Thread(() -> {
                        downloadFile(getActivity(),getBitmapFromView(imageview), fileName);
                    }).start();
                    Toast.makeText(getActivity(),"File downloaded successfully  in Downloads Folder",Toast.LENGTH_LONG).show();
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getActivity(),"Sorry , Unable to download File ",Toast.LENGTH_SHORT).show();
                }
            }
        });


        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
                    String shareMessage= "\nHD wallpaper for your Phones and Tablets \n\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch(Exception e) {
                    e.toString();
                }
            }
        });



        setWallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             //need to change later
                position = viewFlipper.getDisplayedChild()-1;
                ListDescx listDescx = ListDescx.getInstance();
                List<Images>  list  = listDescx.getDescxList();
                mDrawable = list.get(position).getImage();
                Log.i(TAG ,"Position = "+position);

                setWallpaper.setEnabled(false);
                progressDialog =new ProgressDialog(getActivity());
                progressDialog.setMessage("Setting Wallpaper....");
                progressDialog.setTitle("Please Wait !");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setCancelable(false);
                progressDialog.show();

                ImageViewThread imageViewThread =new ImageViewThread();
                imageViewThread.start();

            }
        });


        imageNext = view.findViewById(R.id.imageNext);
        imagePrevious =view.findViewById(R.id.imagePrev);
        viewFlipper =view.findViewById(R.id.viewFlipper);
        if(getActivity()!=null){
           displayList(getActivity(),position);
        }

        imageNext.setOnClickListener(v -> {
            viewFlipper.setFlipInterval(1000);
            viewFlipper.showNext();

        });

           imagePrevious.setOnClickListener(v -> {
            viewFlipper.setFlipInterval(1000);
            viewFlipper.showPrevious();

           });

    }

    private void setImageView(int mDrawable){
        if(getActivity() !=null ){
            Glide.with(getActivity())
                    .load(getActivity().getDrawable(mDrawable))
                    .fitCenter()
                    .placeholder(R.color.gray)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(imageview);
        }
    }

    private void setSetWallpaper(Context context, int drawable)
    {
        try{
               final Bitmap bitmap = BitmapFactory.decodeResource(getResources(),drawable);
               final WallpaperManager wallpaperManager = WallpaperManager.getInstance(context);
               wallpaperManager.setBitmap(bitmap);
               requireActivity().runOnUiThread(() -> {
                   if(progressDialog.isShowing()){
                       progressDialog.dismiss();
                   }
                   Toast.makeText(context, "Wallpaper set!", Toast.LENGTH_LONG).show();
                   setWallpaper.setVisibility(View.GONE);


               });

            } catch (IOException e) {
                Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show();
            }
    }

    Bitmap getBitmapFromView(ImageView view) {
        try {
            Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            view.draw(canvas);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void displayList(Context context,int position){
        TypedArray images =  context.getResources().obtainTypedArray(R.array.wallpaper_array);
        // int [] images ={R.drawable.wall_paper1,R.drawable.wall_paper2,R.drawable.wall_paper3,R.drawable.wall_paper4, R.drawable.wall_3,R.drawable.wall_paper5};
        for(int k=0;k<images.length();k++){
          setFlipperImage(images.getResourceId(k,0),context,position);
        }
        images.recycle();

    }

    class ImageViewThread extends Thread {
        public void run() {
            setSetWallpaper(getActivity(),mDrawable);
        }

    }

    class LoadListThread extends Thread {
        public void run() {

        }

    }

    private void setFlipperImage(int res ,Context context ,int position) {
        Log.i("Set Flipper Called", res+"");
        ImageView image = new ImageView(context);
        image.setBackgroundResource(res);
        viewFlipper.addView(image);
    }

    private void downloadFile(Context context ,Bitmap bitmap,String image_name) {
        try{
            String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() + "/MyWallpaper";
            File myDir = new File(root);
            myDir.mkdirs();
            String fileName = image_name + ".png";

            File file = new File(myDir, fileName);
            Log.d(TAG,file.getAbsolutePath());
            if (file.exists())
                file.delete();

            Log.i("Load File ", root + fileName);
            try {
                FileOutputStream out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            MediaScannerConnection.scanFile(context, new String[]{file.getPath()}, new String[]{"image/png"}, null);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}


//