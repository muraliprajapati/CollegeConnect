package com.sophomoreventure.collegeconnect;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Vikas Kumar on 02-02-2016.
 */
public class ImageHandler {

    Context context;

    public ImageHandler(Context context) {
        this.context = context;
    }

    public void saveBitmap(Bitmap bmp, String name) {

        String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                "/CollegeConnect/Images";
        File dir = new File(file_path);
        if (dir == null)
            dir.mkdirs();
        File file = new File(dir, name);
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 85, fOut);
            fOut.flush();
            fOut.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static File getImage(String imagename) {

        File mediaImage = null;
        try {
            String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                    "/CollegeConnect/Images";
            File myDir = new File(file_path);
            if (!myDir.exists())
                return null;
            mediaImage = new File(myDir.getPath() + imagename);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return mediaImage;
    }

    public void save(Bitmap bmp, String name) {
        FileOutputStream fOut = null;

        try {
            fOut = context.openFileOutput(name, Context.MODE_PRIVATE);
            bmp.compress(Bitmap.CompressFormat.PNG, 85, fOut);
            fOut.flush();
            Log.i("vikas kumar", context.getFilesDir()+"");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.i("vikas kumar", "error");
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("vikas kumar", "error");
        } finally {
            if (fOut != null) {
                try {
                    fOut.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Bitmap load(String name) {

        FileInputStream fIn = null;
        Bitmap bmp = null;
        try {
            fIn = context.openFileInput(name);
             bmp = BitmapFactory.decodeStream(fIn);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if(fIn !=null){
                try {
                    fIn.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return bmp;
    }


    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        return dir.delete();
    }


    public void clearApplicationData() {
        File cache = context.getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (!s.equals("lib")) {
                    deleteDir(new File(appDir, s));
                }
            }
        }
    }

}
