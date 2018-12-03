package cn.leo.engine.cell.animation;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Picture;
import android.support.annotation.NonNull;

import cn.leo.engine.cell.BaseCell;
import cn.leo.engine.common.SystemTime;
import cn.leo.engine.listener.OnCellAnimListener;
import cn.leo.engine.screen.ScreenUtil;

/**
 * @author : Jarry Leo
 * @date : 2018/10/18 15:45
 * 动画元素
 */
public class AnimCell extends BaseCell<AnimCell> {
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

    /**
     * 当前动画图像
     */
    private Picture mAnimPicture;

    /**
     * 动画事件回调
     */
    private OnCellAnimListener mOnCellAnimListener;

    /**
     * 构造自身
     */
    public static AnimCell create() {
        return new AnimCell();
    }

    public AnimCell() {
    }

    public AnimCell(AnimClip animClip) {
        setAnimClip(animClip);
    }

    public AnimCell(AnimClip animClip, int anchorCorner) {
        setAnimClip(animClip);
        mAnchorCorner = anchorCorner;
    }

    @Override
    public AnimCell setWidth(int width) {
        super.setWidth(width);
        setScaleX(getWidthInPx() * 1f / mAnimPicture.getWidth());
        return this;
    }


    @Override
    public AnimCell setHeight(int height) {
        super.setHeight(height);
        setScaleY(getHeightInPx() * 1f / mAnimPicture.getHeight());
        return this;
    }

    /**
     * 设置宽度,是否等比设置
     *
     * @param width      宽度
     * @param equalRatio 是否等比
     */
    public AnimCell setWidth(int width, boolean equalRatio) {
        if (equalRatio) {
            float ratio = getHeightInPx() * 1f / getWidthInPx();
            setHeight((int) (width * ratio));
        }
        setWidth(width);
        return this;
    }

    /**
     * 设置高度,是否等比设置
     *
     * @param height     高度
     * @param equalRatio 是否等比
     */
    public AnimCell setHeight(int height, boolean equalRatio) {
        if (equalRatio) {
            float ratio = getHeightInPx() * 1f / getWidthInPx();
            setWidth((int) (height / ratio));
        }
        setHeight(height);
        return this;
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
        //播放结束并且不重复,动画元素隐藏
        if (mAnimPicture == null) {
            if (mOnCellAnimListener != null) {
                mOnCellAnimListener.onAnimFinished(this);
            }
            return;
        }
        canvas.drawPicture(mAnimPicture);
    }

    @Override
    protected void translate(@NonNull Canvas canvas) {
        //没有动画片段退出
        if (mAnimClip == null) {
            return;
        }
        if (mStartTime == 0) {
            //没有开始播放显示第一帧
            mAnimPicture = mAnimClip.getFrameBitmapFromTime(0);
        } else {
            //画面暂停,保持动画不变
            if (mIsPause) {
                mStartTime = SystemTime.now() - mPausePassedTime;
            }
            //开始播放
            mAnimPicture = mAnimClip.getFrameBitmapFromTime(SystemTime.now() - mStartTime);
        }
        //绘制当前动画帧图片,并且以不同的固定角作为锚点
        switch (mAnchorCorner) {
            case CORNER_TOP_LEFT:
                canvas.translate(getXInPx(), getYInPx());
                break;
            case CORNER_TOP_RIGHT:
                canvas.translate(getXInPx() +
                        (mAnimClip.getFrame(0).getPicture().getWidth()
                                - mAnimPicture.getWidth()), getYInPx());
                break;
            case CORNER_BOTTOM_LEFT:
                canvas.translate(getXInPx(),
                        getYInPx() + (mAnimClip.getFrame(0).getPicture().getHeight()
                                - mAnimPicture.getHeight()));
                break;
            case CORNER_BOTTOM_RIGHT:
                canvas.translate(getXInPx()
                                + (mAnimClip.getFrame(0).getPicture().getWidth() - mAnimPicture.getWidth()),
                        getYInPx()
                                + (mAnimClip.getFrame(0).getPicture().getHeight() - mAnimPicture.getHeight()));
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
        mStartTime = SystemTime.now();
        if (mOnCellAnimListener != null) {
            mOnCellAnimListener.onAnimStart(this);
        }
    }

    /**
     * 暂停
     */
    public void pause() {
        mIsPause = true;
        mPausePassedTime = SystemTime.now() - mStartTime;
        if (mOnCellAnimListener != null) {
            mOnCellAnimListener.onAnimPause(this);
        }

    }

    /**
     * 继续
     */
    public void resume() {
        mIsPause = false;
        mPausePassedTime = 0;
        if (mOnCellAnimListener != null) {
            mOnCellAnimListener.onAnimResume(this);
        }
    }

    /**
     * 停止播放并回归第一帧显示
     */
    public void stop() {
        mStartTime = 0;
        if (mOnCellAnimListener != null) {
            mOnCellAnimListener.onAnimFinished(this);
        }
    }

    /**
     * 修改锚点角
     *
     * @param anchorCorner 固定角
     */
    public AnimCell setAnchorCorner(int anchorCorner) {
        mAnchorCorner = anchorCorner;
        return this;
    }

    /**
     * 替换动画片段
     *
     * @param animClip  动画片段
     * @param autoStart 替换后自动开始播放
     */
    public AnimCell setAnimClip(AnimClip animClip, boolean autoStart) {
        setAnimClip(animClip);
        if (autoStart) {
            start();
        } else {
            mStartTime = 0;
        }
        return this;
    }

    /**
     * 替换动画片段并停止播放
     *
     * @param animClip 动画片段
     * @return 本对象
     */
    public AnimCell setAnimClip(AnimClip animClip) {
        mAnimClip = animClip;
        mAnimPicture = mAnimClip.getFrame(0).getPicture();
        if (getWidth() == 0) {
            setWidth(ScreenUtil.px2dp(mAnimPicture.getWidth()));
        }
        if (getHeight() == 0) {
            setHeight(ScreenUtil.px2dp(mAnimPicture.getHeight()));
        }
        mStartTime = 0;
        return this;
    }

    /**
     * 设置动画监听
     *
     * @param onCellAnimListener 动画监听器
     */
    public AnimCell setOnCellAnimListener(OnCellAnimListener onCellAnimListener) {
        mOnCellAnimListener = onCellAnimListener;
        return this;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAnimClip.onDestroy();
    }
}
