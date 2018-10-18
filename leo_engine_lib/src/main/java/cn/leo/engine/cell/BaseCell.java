package cn.leo.engine.cell;

import android.graphics.Canvas;
import android.graphics.Paint;


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
    protected int cellType;
    /**
     * 元素默认画笔
     */
    protected Paint paint = initPaint();

    /**
     * 元素可见状态
     */
    protected boolean visible = true;
    /**
     * 元素销毁状态
     */
    protected boolean destroy = false;
    /**
     * 元素 x 坐标
     */
    protected float x;
    /**
     * 元素 y 坐标
     */
    protected float y;
    /**
     * 元素图层高度，值越大越显示到前面，值相同随机；
     */
    protected float z;
    /**
     * 元素宽
     */
    protected float width;
    /**
     * 元素高
     */
    protected float height;
    /**
     * 元素id
     */
    protected int id;
    /**
     * 元素标记，附加数据
     */
    protected Object tag;

    /**
     * 元素在每帧画面执行的动作，不能有耗时操作和睡眠操作，否则会导致画面卡顿
     */
    public void event() {

    }

    /**
     * 子类初始化画笔
     */
    protected abstract Paint initPaint();

    /**
     * 引擎调用绘制方法
     */
    public abstract void draw(Canvas canvas);

    /**
     * 获取元素类型
     */
    public abstract int getCellType();

    /**
     * 元素绘制顺序排序,先绘制底层元素,再绘制表层元素
     */
    @Override
    public int compareTo(BaseCell o) {
        return Float.compare(z, o.getZ());
    }

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isDestroy() {
        return destroy;
    }

    public void setDestroy(boolean destroy) {
        this.destroy = destroy;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }
}
