package cn.leo.engine.cell;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;

/**
 * @author : Jarry Leo
 * @date : 2018/10/18 14:29
 */
public class ImageCell extends BaseCell {
    /**
     * bitmap图像
     */
    private Bitmap mBitmap;
    /**
     * 旋转角度
     */
    private int mRotate;

    @Override
    protected Paint initPaint() {
        return new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        canvas.drawBitmap(mBitmap, x, y, paint);
    }

    @Override
    public int getCellType() {
        return TYPE_IMAGE;
    }
}
