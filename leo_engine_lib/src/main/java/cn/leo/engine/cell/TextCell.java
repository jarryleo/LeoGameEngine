package cn.leo.engine.cell;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * @author : Jarry Leo
 * @date : 2018/10/18 15:46
 * 文字元素
 */
public class TextCell extends BaseCell {
    @Override
    protected Paint initPaint() {
        return null;
    }

    @Override
    public void draw(Canvas canvas) {

    }

    @Override
    public int getCellType() {
        return 0;
    }
}
