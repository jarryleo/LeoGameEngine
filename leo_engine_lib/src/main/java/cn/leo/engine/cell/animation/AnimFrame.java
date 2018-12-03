package cn.leo.engine.cell.animation;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Picture;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

import cn.leo.engine.common.PicturePool;

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
    private String mFileName;
    /**
     * 帧时长
     */
    private int mDuration;


    public AnimFrame(Context context, @NonNull String assetsFileName, @IntRange(from = 1) int duration, Paint paint) {
        mFileName = assetsFileName;
        mDuration = duration;
        PicturePool.put(context, assetsFileName, paint);
    }

    public Picture getPicture() {
        return PicturePool.getPicture(mFileName);
    }

    public int getDuration() {
        return mDuration;
    }

    public void onDestroy() {
        PicturePool.destroy(mFileName);
    }
}
