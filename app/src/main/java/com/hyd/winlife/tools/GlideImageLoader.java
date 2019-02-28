package com.hyd.winlife.tools;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.hyd.winlife.R;


public class GlideImageLoader{

    private static final int RES_DEFAULT_HEAD  = R.color.transparent_00000000;//img_default_avatar
    private static final int RES_ERROR_HEAD    = R.color.transparent_00000000;
    private static final int RES_DEFAULT_PIC   = R.color.transparent_00000000;
    private static final int RES_ERROR_PIC     = R.color.transparent_00000000;
    private static final int RES_DEFAULT_COLOR = 0x888888;


    private static GlideImageLoader imageLoader;

    public static synchronized GlideImageLoader getImageLoader(){
        if (imageLoader == null){
            imageLoader = new GlideImageLoader();
        }
        return imageLoader;
    }


    public void loadImage(Object object, ImageView imageView, String imgUrl) {
        loadImage(object, imageView, imgUrl, RES_DEFAULT_PIC, RES_ERROR_PIC);
    }

    public void loadImage(Object object, ImageView imageView, String imgUrl, OnLoadImageFinishListener onLoadImageFinishListener) {
        loadImage(object, imageView, imgUrl, RES_DEFAULT_PIC, RES_ERROR_PIC, onLoadImageFinishListener);
    }

    public void loadImage(Object object, ImageView imageView, String imgUrl, int resDefault, int resError) {
        loadImage(object, imageView, imgUrl, resDefault, resError, null);
    }

    public void loadImage(Object object, ImageView imageView, String imgUrl, int resDefault, int resError, final OnLoadImageFinishListener onLoadImageFinishListener) {
        RequestManager requestManager = getRequestManager(object);
        if (checkParams(requestManager,imageView,imgUrl)){

            requestManager.load(imgUrl)
//                    .fitCenter()
//                    .placeholder(resDefault)
//                    .error(resError)
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            if (onLoadImageFinishListener != null){
                                onLoadImageFinishListener.onLoadFinish(false);
                            }
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            if (onLoadImageFinishListener != null){
                                onLoadImageFinishListener.onLoadFinish(true);
                            }
                            return false;
                        }
                    })
                    .into(imageView);
        }else if(imageView != null){
            imageView.setImageResource(resError);
        }
    }



    public void loadRoundImage(Object object, ImageView imageView, String imgUrl, int radius) {
        loadRoundImage(object, imageView, imgUrl, radius, RES_DEFAULT_COLOR, RES_ERROR_PIC);
    }

    public void loadRoundImage(Object object, ImageView imageView, String imgUrl, int radius, int resDefault, int resError) {
        RequestManager requestManager = getRequestManager(object);
        if (checkParams(requestManager,imageView,imgUrl)){
            requestManager.load(imgUrl)
//                    .centerCrop()
//                    .placeholder(resDefault)
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .error(resError)
//                    .transition(new RoundedCornersTransformation(imageView.getContext(), radius, 0))
                    .into(imageView);
        }else if(imageView != null){
            imageView.setImageResource(resError);
        }
    }


    public boolean checkParams(Object object, ImageView imageView, String imgUrl) {
        return (imageView != null && !TextUtils.isEmpty(imgUrl) && object != null);
    }

    private RequestManager getRequestManager(Object object){

        RequestManager requestManager = null;

        if (object instanceof FragmentActivity){
            requestManager = Glide.with((Activity)object);
        }else if (object instanceof Fragment){
            requestManager = Glide.with((Fragment)object);
        }else if (object instanceof android.app.Fragment){
            requestManager = Glide.with((android.app.Fragment)object);
        }else if (object instanceof Activity){
            requestManager = Glide.with((Activity)object);
        }else if (object instanceof Context){
            requestManager = Glide.with((Context)object);
        }
        return requestManager;
    }


    public interface OnLoadImageFinishListener {
        void onLoadFinish(boolean isSuccess);
    }
}
