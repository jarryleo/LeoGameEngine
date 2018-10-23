package cn.leo.engine.cell;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;

import cn.leo.engine.screen.ScreenUtil;


/**
 * @author : Leo
 * @date : 2018/10/18 14:20
 * <p>
 * 元素类
 * 元素作为每个图层的基本单元,用来显示图片,文字,动画等!
 */
public abstract class BaseCell implements Comparable<BaseCell> {
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
     * 元素id
     */
    private int mId;
    /**
     * 元素标记，附加数据
     */
    private Object mTag;

    /**
     * 元素在每帧画面执行的动作，不能有耗时操作和睡眠操作，否则会导致画面卡顿
     */
    public void event() {

    }

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
     * 移动相对坐标 单位dp
     *
     * @param x 坐标
     * @param y 坐标
     */
    public void moveBy(float x, float y) {
        mX += ScreenUtil.dp2px(x);
        mY += ScreenUtil.dp2px(y);
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

    public void setPaint(Paint paint) {
        this.mPaint = paint;
    }

    public boolean isVisible() {
        return mVisible;
    }

    public void setVisible(boolean visible) {
        this.mVisible = visible;
    }

    public boolean isDestroy() {
        return mDestroy;
    }

    public void setDestroy(boolean destroy) {
        this.mDestroy = destroy;
    }

    public float getXInPx() {
        return mX;
    }

    public void setX(float x) {
        this.mX = ScreenUtil.dp2px(x);
    }

    public float getYInPx() {
        return mY;
    }

    public void setY(float y) {
        this.mY = ScreenUtil.dp2px(y);
    }

    public float getZ() {
        return mZ;
    }

    public void setZ(float z) {
        this.mZ = z;
    }

    public int getWidthInDp() {
        return mWidth;
    }

    public int getWidthInPx() {
        return ScreenUtil.dp2px(mWidth);
    }

    public void setWidth(int width) {
        this.mWidth = width;
    }

    public int getHeightInDp() {
        return mHeight;
    }

    public int getHeightInPx() {
        return ScreenUtil.dp2px(mHeight);
    }

    public void setHeight(int height) {
        this.mHeight = height;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public Object getTag() {
        return mTag;
    }

    public void setTag(Object tag) {
        this.mTag = tag;
    }

    /**
     * 销毁场景,回收资源
     */
    public void onDestroy() {
        setDestroy(true);
    }
}
