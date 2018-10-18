package cn.leo.engine.cell;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;

import java.util.Arrays;

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
     * 元素类型
     */
    private int mCellType;
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
     * 引擎调用绘制方法
     *
     * @param canvas 画布
     */
    public abstract void draw(@NonNull Canvas canvas);

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

    public float getX() {
        return mX;
    }

    public void setX(float x) {
        this.mX = x;
    }

    public float getY() {
        return mY;
    }

    public void setY(float y) {
        this.mY = y;
    }

    public float getZ() {
        return mZ;
    }

    public void setZ(float z) {
        this.mZ = z;
    }

    public int getWidth() {
        return mWidth;
    }

    public void setWidth(int width) {
        this.mWidth = ScreenUtil.dp2px(width);
    }

    public int getHeight() {
        return mHeight;
    }

    public void setHeight(int height) {
        this.mHeight = ScreenUtil.dp2px(height);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BaseCell baseCell = (BaseCell) o;
        return mCellType == baseCell.mCellType &&
                mVisible == baseCell.mVisible &&
                mDestroy == baseCell.mDestroy &&
                Float.compare(baseCell.mX, mX) == 0 &&
                Float.compare(baseCell.mY, mY) == 0 &&
                Float.compare(baseCell.mZ, mZ) == 0 &&
                Float.compare(baseCell.mWidth, mWidth) == 0 &&
                Float.compare(baseCell.mHeight, mHeight) == 0 &&
                mId == baseCell.mId;
    }

    @Override
    public int hashCode() {
        Object[] o = new Object[]{mCellType, mPaint, mVisible, mDestroy, mX, mY, mZ, mWidth, mHeight, mId};
        return Arrays.hashCode(o);
    }
}
