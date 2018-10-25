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
            draw(canvas);
        }
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

    /**
     * 销毁场景,回收资源
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
