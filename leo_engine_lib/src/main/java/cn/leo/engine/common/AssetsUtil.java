package cn.leo.engine.common;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import java.io.IOException;

/**
 * @author : Jarry Leo
 * @date : 2018/10/22 18:19
 */
public class AssetsUtil {
    /**
     * 从 assets 文件夹获取图片
     *
     * @param context  上下文
     * @param fileName 文件名
     * @return bitmap
     */
    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    public static Bitmap getBitmapFromAsset(Context context, String fileName) {
        AssetManager assets = context.getAssets();
        Bitmap bitmap = null;
        try {
            AssetFileDescriptor fileDescriptor = assets.openFd(fileName);
            bitmap = BitmapFactory.decodeStream(fileDescriptor.createInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
