package cn.leo.engine.cell.animation;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

import cn.leo.engine.common.AssetsUtil;
import cn.leo.engine.scene.BaseScene;

/**
 * @author : Jarry Leo
 * @date : 2018/10/25 9:42
 * <p>
 * 动画帧
 */
public class AnimFrame {
    /**
     * 帧图像
     */
    private Bitmap mBitmap;
    /**
     * 帧时长
     */
    private int mDuration;

    public AnimFrame(@NonNull Bitmap bitmap, @IntRange(from = 1) int duration) {
        mBitmap = bitmap;
        mDuration = duration;
    }

    private AnimFrame(Context context, @NonNull String bitmapFile, @IntRange(from = 1) int duration) {
        mBitmap = AssetsUtil.getBitmapFromAsset(context, bitmapFile);
        mDuration = duration;
    }

    public AnimFrame(BaseScene baseScene, @NonNull String bitmapFile, @IntRange(from = 1) int duration) {
        this(baseScene.getContext(), bitmapFile, duration);
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public int getDuration() {
        return mDuration;
    }

    public void onDestroy() {
        mBitmap.recycle();
        mBitmap = null;
    }
}
