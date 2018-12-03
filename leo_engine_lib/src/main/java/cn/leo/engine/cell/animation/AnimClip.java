package cn.leo.engine.cell.animation;

import android.graphics.Paint;
import android.graphics.Picture;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.util.SparseArray;

import cn.leo.engine.scene.Scene;

/**
 * @author : Jarry Leo
 * @date : 2018/10/25 9:42
 * <p>
 * 动画片段
 */
public class AnimClip {

    /**
     * 片段所有帧
     */
    private SparseArray<AnimFrame> mFrames = new SparseArray<>();
    /**
     * 是否重复播放
     */
    private boolean mLoop;
    /**
     * 片段总时长
     */
    private int mTotalTime;
    /**
     * 播放完是否保持最后一张画面
     */
    private boolean mFillAfter;
    /**
     * 场景
     */
    private final Scene mScene;
    /**
     * 画笔
     */
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public static AnimClip create(Scene scene) {
        return new AnimClip(scene);
    }

    public AnimClip(Scene scene) {
        mScene = scene;
    }

    /**
     * 添加帧
     *
     * @param frame 动画帧
     * @return 本对象
     */
    public AnimClip addFrame(@NonNull AnimFrame frame) {
        mFrames.append(mTotalTime, frame);
        mTotalTime += frame.getDuration();
        return this;
    }

    /**
     * 添加帧
     *
     * @param bitmapFile 图片路径
     * @param duration   帧延时
     * @return 本对象
     */
    public AnimClip addFrame(@NonNull String bitmapFile, @IntRange(from = 1) int duration) {
        AnimFrame frame = new AnimFrame(mScene.getContext(), bitmapFile, duration, mPaint);
        mFrames.append(mTotalTime, frame);
        mTotalTime += frame.getDuration();
        return this;
    }

    /**
     * 移除帧
     *
     * @param index 序号
     * @return 本对象
     */
    public AnimClip removeFrame(int index) {
        mFrames.remove(index);
        return this;
    }

    /**
     * 获取帧
     *
     * @param index 序号
     * @return 帧
     */
    public AnimFrame getFrame(int index) {
        return mFrames.get(mFrames.keyAt(index));
    }

    /**
     * 根据时间获取帧图像
     *
     * @param timeMills 动画执行时间
     * @return 图像
     */
    public Picture getFrameBitmapFromTime(long timeMills) {
        if (mLoop) {
            if (timeMills > mTotalTime) {
                timeMills %= mTotalTime;
            }
        } else if (timeMills > mTotalTime) {
            if (mFillAfter) {

                return getFrame(getFrameCount() - 1).getPicture();
            } else {
                return null;
            }
        }
        for (int i = mFrames.size() - 1; i >= 0; i--) {
            int timestamp = mFrames.keyAt(i);
            if (timestamp <= timeMills) {
                return mFrames.get(timestamp).getPicture();
            }
        }
        return getFrame(getFrameCount() - 1).getPicture();
    }

    public boolean isLoop() {
        return mLoop;
    }

    /**
     * 设置动画是否循环播放
     *
     * @param loop 是否循环
     */
    public AnimClip setLoop(boolean loop) {
        this.mLoop = loop;
        return this;
    }

    /**
     * 设置动画播放完后显示最后一帧 (循环播放时无效)
     *
     * @param fillAfter 是否显示最后一帧
     */

    public AnimClip setFillAfter(boolean fillAfter) {
        mFillAfter = fillAfter;
        return this;
    }

    /**
     * 获取片段总时长
     *
     * @return 时长
     */
    public int getTotalTime() {
        return mTotalTime;
    }

    /**
     * 获取总帧数
     *
     * @return 帧数
     */
    public int getFrameCount() {
        return mFrames.size();
    }

    /**
     * 释放资源
     */
    public void onDestroy() {
        for (int i = mFrames.size(); i > 0; i--) {
            mFrames.get(i).onDestroy();
        }
    }
}
