package cn.leo.engine.cell;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.util.SparseArray;

/**
 * @author : Jarry Leo
 * @date : 2018/10/18 15:45
 * 动画元素
 */
public class AnimCell extends BaseCell {
    /**
     * 动画固定角左上
     */
    public static final int CORNER_TOP_LEFT = 0;
    /**
     * 动画固定角右上
     */
    public static final int CORNER_TOP_RIGHT = 1;
    /**
     * 动画固定角左下
     */
    public static final int CORNER_BOTTOM_LEFT = 2;
    /**
     * 动画固定角右下
     */
    public static final int CORNER_BOTTOM_RIGHT = 3;

    /**
     * 动画固定角
     */
    private int mAnchorCorner;
    /**
     * 动画片段
     */
    private AnimClip mAnimClip;
    /**
     * 动画开始时间戳
     */
    private long mStartTime;

    /**
     * 动画是否暂停
     */
    private boolean mIsPause;
    /**
     * 暂停的时候已经执行完的动画时间
     */
    private long mPausePassedTime;

    public AnimCell() {
    }

    public AnimCell(AnimClip animClip) {
        mAnimClip = animClip;
    }

    public AnimCell(AnimClip animClip, int anchorCorner) {
        mAnimClip = animClip;
        mAnchorCorner = anchorCorner;
    }

    @Override
    protected Paint initPaint() {
        return new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        //没有动画片段退出
        if (mAnimClip == null) {
            return;
        }
        Bitmap animBitmap;
        if (mStartTime == 0) {
            //没有开始播放显示第一帧
            animBitmap = mAnimClip.getFrameBitmapFromTime(0);
        } else {
            //画面暂停,保持动画不变
            if (mIsPause) {
                mStartTime = System.currentTimeMillis() - mPausePassedTime;
            }
            //开始播放
            animBitmap = mAnimClip.getFrameBitmapFromTime(System.currentTimeMillis() - mStartTime);
        }
        //播放结束并且不重复,动画元素隐藏
        if (animBitmap == null) {
            setVisible(false);
            return;
        }
        //绘制当前动画帧图片,并且以不同的固定角作为锚点
        switch (mAnchorCorner) {
            case CORNER_TOP_LEFT:
                canvas.drawBitmap(animBitmap, getX(), getY(), getPaint());
                break;
            case CORNER_TOP_RIGHT:
                canvas.drawBitmap(animBitmap, getX() +
                        (mAnimClip.getFrame(0).getBitmap().getWidth()
                                - animBitmap.getWidth()), getY(), getPaint());
                break;
            case CORNER_BOTTOM_LEFT:
                canvas.drawBitmap(animBitmap, getX(),
                        getY() + (mAnimClip.getFrame(0).getBitmap().getHeight()
                                - animBitmap.getHeight()), getPaint());
                break;
            case CORNER_BOTTOM_RIGHT:
                canvas.drawBitmap(animBitmap,
                        getX() + (mAnimClip.getFrame(0).getBitmap().getWidth()
                                - animBitmap.getWidth()),
                        getY() + (mAnimClip.getFrame(0).getBitmap().getHeight()
                                - animBitmap.getHeight()), getPaint());
                break;
            default:
                break;
        }
    }

    @Override
    public int getCellType() {
        return TYPE_ANIMATION;
    }

    /**
     * 开始动画
     */
    public void start() {
        mStartTime = System.currentTimeMillis();
    }

    /**
     * 暂停
     */
    public void pause() {
        mIsPause = true;
        mPausePassedTime = System.currentTimeMillis() - mStartTime;
    }

    /**
     * 继续
     */
    public void resume() {
        mIsPause = false;
        mPausePassedTime = 0;
    }

    /**
     * 停止播放并回归第一帧显示
     */
    public void stop() {
        mStartTime = 0;
    }

    /**
     * 修改锚点角
     *
     * @param anchorCorner 固定角
     */
    public void setAnchorCorner(int anchorCorner) {
        mAnchorCorner = anchorCorner;
    }

    /**
     * 替换动画片段
     *
     * @param animClip  动画片段
     * @param autoStart 替换后自动开始播放
     */
    public void setAnimClip(AnimClip animClip, boolean autoStart) {
        mAnimClip = animClip;
        if (autoStart) {
            start();
        } else {
            mStartTime = 0;
        }
    }


    /**
     * 动画帧
     */
    public static class AnimFrame {
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

        public Bitmap getBitmap() {
            return mBitmap;
        }

        public int getDuration() {
            return mDuration;
        }
    }

    /**
     * 动画片段
     */
    public static class AnimClip {
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

        public void addFrame(@NonNull AnimFrame frame) {
            mFrames.append(mTotalTime, frame);
            mTotalTime += frame.mDuration;
        }

        public void removeFrame(int index) {
            mFrames.remove(index);
        }

        public AnimFrame getFrame(int index) {
            return mFrames.get(mFrames.keyAt(index));
        }

        /**
         * 根据时间获取帧图像
         *
         * @param timeMills 动画执行时间
         * @return 图像
         */
        public Bitmap getFrameBitmapFromTime(long timeMills) {
            if (mLoop) {
                if (timeMills > mTotalTime) {
                    timeMills %= mTotalTime;
                }
            } else if (timeMills > mTotalTime) {
                if (mFillAfter) {
                    return getFrame(getFrameCount() - 1).getBitmap();
                } else {
                    return null;
                }
            }
            for (int i = mFrames.size(); i > 0; i--) {
                int timestamp = mFrames.keyAt(i);
                if (timestamp <= timeMills) {
                    return mFrames.get(timestamp).getBitmap();
                }
            }
            return getFrame(getFrameCount() - 1).getBitmap();
        }

        public boolean isLoop() {
            return mLoop;
        }

        public void setLoop(boolean loop) {
            this.mLoop = loop;
        }

        public void setFillAfter(boolean fillAfter) {
            mFillAfter = fillAfter;
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
    }
}
