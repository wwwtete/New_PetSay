package com.petsay.activity.petalk.capture;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import com.petsay.vo.petalk.PetTagVo;

/**
 * Builder for crop Intents and utils for handling result
 * Created by sam on 14-10-16.
 */
public class Crop {

    public static final int REQUEST_CROP = 6709;
    private int mFromType=0;
    static interface Extra {
        String MAX_WIDTH = "max_width";
        String ERROR = "error";
    }

    private Intent cropIntent;

    /**
     * Create a crop Intent builder with source image
     *
     * @param source Source image URI
     */
    public Crop(Uri source,int fromType,int model,PetTagVo tagVo) {
    	mFromType=fromType;
        cropIntent = new Intent();
        cropIntent.putExtra("fromType", mFromType);
        cropIntent.putExtra("model",model);
        if(tagVo != null)
            cropIntent.putExtra("tag",tagVo);
        cropIntent.setData(source);
    }

    /**
     * Set output URI where the cropped image will be saved
     *
     * @param output Output image URI
     */
    public Crop output(Uri output) {
        cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, output);
        return this;
    }


    /**
     * Set maximum crop size
     *
     * @param width Max width
     */
    public Crop withWidth(int width) {
        cropIntent.putExtra(Extra.MAX_WIDTH, width);
        return this;
    }

    /**
     * Send the crop Intent!
     *
     * @param activity Activity that will receive result
     */
    public void start(Activity activity) {
        activity.startActivityForResult(getIntent(activity), REQUEST_CROP);
    }

    /**
     * Send the crop Intent!
     *
     * @param context Context
     * @param fragment Fragment that will receive result
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void start(Context context, Fragment fragment) {
        fragment.startActivityForResult(getIntent(context), REQUEST_CROP);
    }

    private Intent getIntent(Context context) {
        cropIntent.setClass(context, CropImageActivity.class);
        return cropIntent;
    }

    /**
     * Retrieve URI for cropped image, as set in the Intent builder
     *
     * @param result Output Image URI
     */
    public static Uri getOutput(Intent result) {
        return result.getParcelableExtra(MediaStore.EXTRA_OUTPUT);
    }

    /**
     * Retrieve error that caused crop to fail
     *
     * @param result Result Intent
     * @return Throwable handled in CropImageActivity
     */
    public static Throwable getError(Intent result) {
        return (Throwable) result.getSerializableExtra(Extra.ERROR);
    }

}
