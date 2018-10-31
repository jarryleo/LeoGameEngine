package cn.leo.engine.cell.animation;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;

import cn.leo.engine.cell.BaseCell;
import cn.leo.engine.common.SystemClock;
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
     * 缩放矩形
     */
    private Rect mSource;
    private Rect mTarget;

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
        mTarget = new Rect(0, 0, getWidthInPx(), getHeightInPx());
        return this;
    }

    @Override
    public AnimCell setHeight(int height) {
        super.setHeight(height);
        mTarget = new Rect(0, 0, getWidthInPx(), getHeightInPx());
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
        Bitmap animBitmap;
        if (mStartTime == 0) {
            //没有开始播放显示第一帧
            animBitmap = mAnimClip.getFrameBitmapFromTime(0);
        } else {
            //画面暂停,保持动画不变
            if (mIsPause) {
                mStartTime = SystemClock.now() - mPausePassedTime;
            }
            //开始播放
            animBitmap = mAnimClip.getFrameBitmapFromTime(SystemClock.now() - mStartTime);
        }
        //播放结束并且不重复,动画元素隐藏
        if (animBitmap == null) {
            setVisible(false);
            return;
        }
        canvas.save();
        canvas.rotate(getRotate(), getXInPx() + getWidthInPx() / 2, getYInPx() + getHeightInPx() / 2);
        //绘制当前动画帧图片,并且以不同的固定角作为锚点
        switch (mAnchorCorner) {
            case CORNER_TOP_LEFT:
                canvas.translate(getXInPx(), getYInPx());
                break;
            case CORNER_TOP_RIGHT:
                canvas.translate(getXInPx() +
                        (mAnimClip.getFrame(0).getBitmap().getWidth()
                                - animBitmap.getWidth()), getYInPx());
                break;
            case CORNER_BOTTOM_LEFT:
                canvas.translate(getXInPx(),
                        getYInPx() + (mAnimClip.getFrame(0).getBitmap().getHeight()
                                - animBitmap.getHeight()));
                break;
            case CORNER_BOTTOM_RIGHT:
                canvas.translate(getXInPx()
                                + (mAnimClip.getFrame(0).getBitmap().getWidth() - animBitmap.getWidth()),
                        getYInPx()
                                + (mAnimClip.getFrame(0).getBitmap().getHeight() - animBitmap.getHeight()));
                break;
            default:
                break;
        }
        canvas.drawBitmap(animBitmap, mSource, mTarget, getPaint());
        canvas.restore();
    }

    @Override
    public int getCellType() {
        return TYPE_ANIMATION;
    }

    /**
     * 开始动画
     */
    public void start() {
        mStartTime = SystemClock.now();
    }

    /**
     * 暂停
     */
    public void pause() {
        mIsPause = true;
        mPausePassedTime = SystemClock.now() - mStartTime;
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
        Bitmap bitmap = mAnimClip.getFrame(0).getBitmap();
        if (getWidth() == 0) {
            setWidth(ScreenUtil.px2dp(bitmap.getWidth()));
        }
        if (getHeight() == 0) {
            setHeight(ScreenUtil.px2dp(bitmap.getHeight()));
        }
        mSource = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        mStartTime = 0;
        return this;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mAnimClip.onDestroy();
    }
}
