package com.example.extarc.androidpushnotification.Services;

/**
 * Created by Lenovo on 05-02-2016.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import com.example.extarc.androidpushnotification.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageLoader {

    MemoryCache memoryCache=new MemoryCache();
    final String TAG = "ImageLoader";
    FileCache fileCache;
    private Map<ImageView, String> imageViews= Collections.synchronizedMap(new WeakHashMap<ImageView, String>());
    ExecutorService executorService;
    private int maxWidth = 0;
    private int maxHeight = 0;

    public ImageLoader(Context context){
        fileCache=new FileCache(context);
        executorService= Executors.newFixedThreadPool(5);
    }
    public ImageLoader(Context context, int maxWidth, int maxHeight){
        fileCache=new FileCache(context);
        executorService= Executors.newFixedThreadPool(5);
        this.maxHeight = maxHeight;
        this.maxWidth = maxWidth;
    }

    int stub_id = R.drawable.winspire_logo;
    public void DisplayImage(String url, int loader, ImageView imageView)
    {
        stub_id = loader;
        imageViews.put(imageView, url);
        Bitmap bitmap=memoryCache.get(url);
        if(bitmap!=null)
            imageView.setImageBitmap(bitmap);
        else
        {
            queuePhoto(url, imageView);
            imageView.setImageResource(loader);
        }
    }
    private void queuePhoto(String url, ImageView imageView)
    {
        PhotoToLoad p=new PhotoToLoad(url, imageView);
        executorService.submit(new PhotosLoader(p));
    }

    public Bitmap getBitmap(String url)
    {
        File f = fileCache.getFile(url);

        //from SD cache
        Bitmap b = decodeFile(f);
        if(b!=null)
            return b;

        //from web
        try {
            Bitmap bitmap=null;
            URL imageUrl = new URL(url);
            Log.i(TAG, "image Url In ImageLoader: " +  imageUrl.toString());
            HttpURLConnection conn = (HttpURLConnection)imageUrl.openConnection();
            conn.setInstanceFollowRedirects(true);
            InputStream is=conn.getInputStream();
            OutputStream os = new FileOutputStream(f);
            Utils.CopyStream(is, os);
            os.close();
            bitmap = decodeFile(f);
            return bitmap;
        } catch(Exception ex){
            Log.e("Exception ", ex.toString());
            //ex.printStackTrace();
            return null;
        }
    }

    //decodes image and scales it to reduce memory consumption
    public Bitmap decodeFile(File f){
        Bitmap bitmap = null;
        try {
            //decode image size

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, options);

            //Find the correct scale value. It should be the power of 2.
            int REQUIRED_WIDTH = (maxWidth == 0)? 90: maxWidth;
            int REQUIRED_HEIGHT = (maxHeight == 0)? 90: maxHeight;
            int width_tmp = options.outWidth;
            int height_tmp = options.outHeight;
            Log.i(TAG, "Image Original Dimensions: (WxH): " + options.outWidth + "x" + options.outHeight);
            int scale=1;
            while(true){
                if(width_tmp/2 < REQUIRED_WIDTH || height_tmp/2 < REQUIRED_HEIGHT)
                    break;
                width_tmp = width_tmp/2;
                height_tmp = height_tmp/2;
                scale*=2;
            }
            Log.i(TAG, "Image Optimal Dimensions: (WxH): " + width_tmp + "x" + height_tmp);
            //decode with inSampleSize
            options.inSampleSize=scale;
            options.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeStream(new FileInputStream(f), null, options);
            if(bitmap == null){
                Log.e(TAG, "Image Bitmap is null after size optimization");
            } else {
                Log.i(TAG, "Image Dimensions after Optimization : (WxH): " + bitmap.getWidth() + "x" + bitmap.getHeight());
            }
        } catch (FileNotFoundException e) {
            Log.e("Decodefile",e.toString());
            return null;
        }
        return bitmap;
    }

    //Task for the queue
    private class PhotoToLoad
    {
        public String url;
        public ImageView imageView;
        public PhotoToLoad(String u, ImageView i){
            url=u;
            imageView=i;
        }
    }

    class PhotosLoader implements Runnable {
        PhotoToLoad photoToLoad;
        PhotosLoader(PhotoToLoad photoToLoad){
            this.photoToLoad=photoToLoad;
        }

        @Override
        public void run() {
            if(imageViewReused(photoToLoad))
                return;
            Bitmap bmp=getBitmap(photoToLoad.url);
            memoryCache.put(photoToLoad.url, bmp);
            if(imageViewReused(photoToLoad))
                return;
            BitmapDisplayer bd=new BitmapDisplayer(bmp, photoToLoad);
            Activity a=(Activity)photoToLoad.imageView.getContext();
            a.runOnUiThread(bd);
        }
    }

    boolean imageViewReused(PhotoToLoad photoToLoad){
        String tag=imageViews.get(photoToLoad.imageView);
        if(tag==null || !tag.equals(photoToLoad.url))
            return true;
        return false;
    }

    //Used to display bitmap in the UI thread
    class BitmapDisplayer implements Runnable
    {
        Bitmap bitmap;
        PhotoToLoad photoToLoad;
        public BitmapDisplayer(Bitmap b, PhotoToLoad p){bitmap=b;photoToLoad=p;}
        public void run()
        {
            if(imageViewReused(photoToLoad))
                return;
            if(bitmap!=null)
                photoToLoad.imageView.setImageBitmap(bitmap);
            else
                photoToLoad.imageView.setImageResource(stub_id);
        }
    }

    public void clearCache() {
        memoryCache.clear();
        fileCache.clear();
    }

}