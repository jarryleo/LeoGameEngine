package cn.leo.engine.cell;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;

/**
 * @author : Jarry Leo
 * @date : 2018/10/18 14:29
 * 图片元素
 */
public class ImageCell extends BaseCell {
    /**
     * bitmap图像
     */
    private Bitmap mBitmap;


    /**
     * 旋转角度
     */
    private float mRotate;
    private Rect mSource;
    private Rect mTarget;

    @Override
    protected Paint initPaint() {
        return new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        canvas.save();
        canvas.rotate(mRotate);
        if (getWidth() == 0 || getHeight() == 0) {
            canvas.drawBitmap(mBitmap, getX(), getY(), getPaint());
        } else {
            canvas.translate(getX(), getY());
            canvas.drawBitmap(mBitmap, mSource, mTarget, getPaint());
        }
        canvas.restore();
    }

    @Override
    public void setWidth(int width) {
        super.setWidth(width);
        mTarget = new Rect(0, 0, getWidth(), getHeight());
    }

    @Override
    public void setHeight(int height) {
        super.setHeight(height);
        mTarget = new Rect(0, 0, getWidth(), getHeight());
    }

    @Override
    public int getCellType() {
        return TYPE_IMAGE;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
        mSource = new Rect(0, 0, mBitmap.getWidth(), mBitmap.getHeight());
    }

    public float getRotate() {
        return mRotate;
    }

    public void setRotate(float rotate) {
        mRotate = rotate;
    }
}
