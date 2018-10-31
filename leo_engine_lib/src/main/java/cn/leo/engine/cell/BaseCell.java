package cn.leo.engine.cell;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;

import cn.leo.engine.screen.ScreenUtil;


/**
 * @author : Leo
 * @date : 2018/10/18 14:20
 * <p>
 * 元素类
 * 元素作为每个图层的基本单元,用来显示图片,文字,动画等!
 */
public abstract class BaseCell<T extends BaseCell> implements Comparable<BaseCell>, Cloneable {
    /**
     * 图片元素
     */
    public static final int TYPE_IMAGE = 0;
    /**
     * 字符串元素
     */
    public static final int TYPE_STRING = 1;
    /**
     * 动画元素
     */
    public static final int TYPE_ANIMATION = 2;
    /**
     * 自绘元素
     */
    public static final int TYPE_CUSTOM = 3;
    /**
     * 元素默认画笔
     */
    private Paint mPaint = initPaint();

    /**
     * 元素可见状态
     */
    private boolean mVisible = true;
    /**
     * 元素销毁状态
     */
    private boolean mDestroy = false;
    /**
     * 元素 x 坐标
     */
    private float mX;
    /**
     * 元素 y 坐标
     */
    private float mY;
    /**
     * 元素图层高度，值越大越显示到前面，值相同随机；
     */
    private float mZ;
    /**
     * 元素宽
     */
    private int mWidth;
    /**
     * 元素高
     */
    private int mHeight;
    /**
     * 旋转角度
     */
    private float mRotate;
    /**
     * X方向缩放比例
     */
    private float mScaleX = 1f;
    /**
     * Y方向缩放比例
     */
    private float mScaleY = 1f;
    /**
     * x方向翻转
     */
    private boolean mMirrorX = false;
    /**
     * y方向翻转
     */
    private boolean mMirrorY = false;
    /**
     * x方向倾斜
     */
    private float mSkewX;
    /**
     * y方向倾斜
     */
    private float mSkewY;

    /**
     * 元素区域
     */
    private Rect mRect = new Rect();
    /**
     * 元素id
     */
    private int mId;
    /**
     * 元素标记，附加数据
     */
    private Object mTag;
    /**
     * 标记元素是否可回收
     */
    private boolean mRecycle;

    /**
     * 子类初始化画笔
     *
     * @return 画笔
     */
    protected abstract Paint initPaint();

    /**
     * 子类调用绘制方法
     *
     * @param canvas 画布
     */
    protected abstract void draw(@NonNull Canvas canvas);

    /**
     * 引擎调用绘制方法
     *
     * @param canvas 画布
     */

    public void dispatchDraw(@NonNull Canvas canvas) {
        if (isVisible() && !isDestroy()) {
            canvas.save();
            translate(canvas);
            mirror(canvas);
            skew(canvas);
            rotate(canvas);
            scale(canvas);
            draw(canvas);
            canvas.restore();
        }
    }

    /**
     * 元素平移
     */
    protected void translate(@NonNull Canvas canvas) {
        canvas.translate(getXInPx(), getYInPx());
    }

    /**
     * 元素缩放
     */
    protected void scale(@NonNull Canvas canvas) {
        canvas.scale(mScaleX, mScaleY);
    }

    /**
     * 元素旋转
     */
    protected void rotate(@NonNull Canvas canvas) {
        canvas.rotate(getRotate(), getWidthInPx() / 2, getHeightInPx() / 2);
    }

    /**
     * 元素镜像
     */
    protected void mirror(@NonNull Canvas canvas) {
        if (mMirrorX) {
            canvas.scale(-1, 1, getWidthInPx() / 2, getHeightInPx() / 2);
        }
        if (mMirrorY) {
            canvas.scale(1, -1, getWidthInPx() / 2, getHeightInPx() / 2);
        }
    }

    /**
     * 元素倾斜
     */
    protected void skew(@NonNull Canvas canvas) {
        canvas.skew(mSkewX, mSkewY);
    }

    /**
     * 移动到指定坐标 单位dp
     *
     * @param x 坐标
     * @param y 坐标
     */
    public void moveTo(float x, float y) {
        setX(x);
        setY(y);
    }

    /**
     * 移动到指定坐标 单位px
     *
     * @param x 坐标
     * @param y 坐标
     */
    void moveToPx(float x, float y) {
        this.mX = ScreenUtil.px2dp(x);
        this.mY = ScreenUtil.px2dp(y);
        setRect();
    }

    /**
     * 中心点移动到指定坐标 单位dp
     *
     * @param x 坐标
     * @param y 坐标
     */
    public void centerMoveTo(float x, float y) {
        setX(x - getWidth() / 2);
        setY(y - getHeight() / 2);
    }

    /**
     * 中心点移动到指定坐标 单位px
     *
     * @param x 坐标
     * @param y 坐标
     */
    void centerMoveToPx(float x, float y) {
        this.mX = ScreenUtil.px2dp(x) - (getWidth() / 2);
        this.mY = ScreenUtil.px2dp(y) - (getHeight() / 2);
        setRect();
    }

    /**
     * 移动相对坐标 单位dp
     *
     * @param x 坐标
     * @param y 坐标
     */
    public void moveBy(float x, float y) {
        mX += x;
        mY += y;
        setRect();
    }

    /**
     * 移动相对坐标 单位xp
     *
     * @param x 坐标
     * @param y 坐标
     */
    void moveByPx(float x, float y) {
        mX += ScreenUtil.px2dp(x);
        mY += ScreenUtil.px2dp(y);
        setRect();
    }

    /**
     * 获取元素类型
     *
     * @return 元素类型
     */
    public abstract int getCellType();

    /**
     * 元素绘制顺序排序,先绘制底层元素,再绘制表层元素
     */
    @Override
    public int compareTo(BaseCell o) {
        return Float.compare(mZ, o.getZ());
    }

    public Paint getPaint() {
        return mPaint;
    }

    public T setPaint(Paint paint) {
        this.mPaint = paint;
        return (T) this;
    }

    public boolean isVisible() {
        return mVisible;
    }

    public T setVisible(boolean visible) {
        this.mVisible = visible;
        return (T) this;
    }

    public boolean isDestroy() {
        return mDestroy;
    }

    public void setDestroy(boolean destroy) {
        this.mDestroy = destroy;
    }

    protected float getXInPx() {
        return ScreenUtil.dp2px(mX);
    }

    public float getX() {
        return mX;
    }

    /**
     * 获取中心点x坐标
     */
    public float getCenterX() {
        return mX + getWidth() / 2;
    }


    public T setX(float x) {
        this.mX = x;
        setRect();
        return (T) this;
    }

    /**
     * 元素中心点x方向移动到坐标
     */
    public T setCenterToX(float centerX) {
        this.mX = centerX - getWidth() / 2;
        setRect();
        return (T) this;
    }

    protected float getYInPx() {
        return ScreenUtil.dp2px(mY);
    }

    public float getY() {
        return mY;
    }

    /**
     * 获取中心点y坐标
     *
     * @return
     */
    public float getCenterY() {
        return mY + getHeight() / 2;
    }

    public T setY(float y) {
        this.mY = y;
        setRect();
        return (T) this;
    }

    /**
     * 元素中心点y方向移动到坐标
     */
    public T setCenterToY(float centerY) {
        this.mY = centerY - getHeight() / 2;
        setRect();
        return (T) this;
    }

    public float getZ() {
        return mZ;
    }

    public T setZ(float z) {
        this.mZ = z;
        return (T) this;
    }

    /**
     * 高度变化指定值
     *
     * @param value 要变化的值
     * @return
     */
    public T zChangeBy(float value) {
        this.mZ += value;
        return (T) this;
    }

    private void setRect() {
        mRect.set((int) getX(),
                (int) getY(),
                (int) getX() + getWidth(),
                (int) getY() + getHeight());
    }

    public Rect getRect() {
        return mRect;
    }

    public float getRotate() {
        return mRotate;
    }

    /**
     * 设置旋转角度
     *
     * @param rotate 0~360°为一圈
     */
    public T setRotate(float rotate) {
        mRotate = rotate;
        return (T) this;
    }

    /**
     * 旋转指定角度
     *
     * @param rotate 要旋转的角度
     */
    public T rotateBy(float rotate) {
        mRotate += rotate;
        return (T) this;
    }

    public int getWidth() {
        return mWidth;
    }

    protected int getWidthInPx() {
        return ScreenUtil.dp2px(mWidth);
    }

    public T setWidth(int width) {
        this.mWidth = width;
        setRect();
        return (T) this;
    }

    public int getHeight() {
        return mHeight;
    }

    protected int getHeightInPx() {
        return ScreenUtil.dp2px(mHeight);
    }

    public T setHeight(int height) {
        this.mHeight = height;
        setRect();
        return (T) this;
    }

    public float getScaleX() {
        return mScaleX;
    }

    public T setScaleX(float scaleX) {
        mScaleX = scaleX;
        return (T) this;
    }

    public float getScaleY() {
        return mScaleY;
    }

    public T setScaleY(float scaleY) {
        mScaleY = scaleY;
        return (T) this;
    }

    public boolean isMirrorX() {
        return mMirrorX;
    }

    public T setMirrorX(boolean mirrorX) {
        mMirrorX = mirrorX;
        return (T) this;
    }

    public boolean isMirrorY() {
        return mMirrorY;
    }

    public T setMirrorY(boolean mirrorY) {
        mMirrorY = mirrorY;
        return (T) this;
    }

    public float getSkewX() {
        return mSkewX;
    }

    public T setSkewX(float skewX) {
        this.mSkewX = skewX;
        return (T) this;
    }

    public float getSkewY() {
        return mSkewY;
    }

    public T setSkewY(float skewY) {
        this.mSkewY = skewY;
        return (T) this;
    }

    public int getId() {
        return mId;
    }

    public T setId(int id) {
        this.mId = id;
        return (T) this;
    }

    public Object getTag() {
        return mTag;
    }

    public T setTag(Object tag) {
        this.mTag = tag;
        return (T) this;
    }

    public boolean isRecycle() {
        return mRecycle;
    }

    public void setRecycle(boolean recycle) {
        mRecycle = recycle;
    }

    /**
     * 元素回收
     */
    public void recycle() {
        mRecycle = true;
        mVisible = false;
    }

    /**
     * 销毁元素
     */
    public void onDestroy() {
        setDestroy(true);
    }

    @Override
    public T clone() {
        try {
            return (T) super.clone();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
