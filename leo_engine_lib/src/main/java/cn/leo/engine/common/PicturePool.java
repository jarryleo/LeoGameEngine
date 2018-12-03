package cn.leo.engine.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Picture;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author : Jarry Leo
 * @date : 2018/12/3 14:43
 */
public class PicturePool {

    private static ConcurrentHashMap<String, Picture> mPicturePool = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, Integer> mReference = new ConcurrentHashMap<>();

    public static void put(Context context, String fileName, Paint paint) {
        if (mPicturePool.containsKey(fileName)) {
            Integer num = mReference.get(fileName);
            mReference.put(fileName, ++num);
        } else {
            Bitmap bitmapFromAsset = AssetsUtil.getBitmapFromAsset(context, fileName);
            if (bitmapFromAsset == null) {
                throw new IllegalArgumentException("\"" + fileName + "\" are not exist in assets folder");
            }
            //录制picture
            Picture picture = new Picture();
            Canvas recodingCanvas = picture.beginRecording(bitmapFromAsset.getWidth(), bitmapFromAsset.getHeight());
            recodingCanvas.drawBitmap(bitmapFromAsset, 0, 0, paint);
            picture.endRecording();
            bitmapFromAsset.recycle();
            mPicturePool.put(fileName, picture);
            mReference.put(fileName, 1);
        }
    }

    public static Picture getPicture(String fileName) {
        return mPicturePool.get(fileName);
    }

    public static void destroy(String fileName) {
        Integer num = mReference.get(fileName);
        if (num <= 1) {
            mPicturePool.remove(fileName);
            mReference.remove(fileName);
        } else {
            mReference.put(fileName, --num);
        }
    }
}
