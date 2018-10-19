package cn.leo.engine.cell;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;

/**
 * @author : Jarry Leo
 * @date : 2018/10/18 15:53
 */
public class CustomCell extends BaseCell {
    @Override
    protected Paint initPaint() {
        return new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {

    }

    @Override
    public int getCellType() {
        return TYPE_CUSTOM;
    }
}
