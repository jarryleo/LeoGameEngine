package cn.leo.engine.cell;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;

/**
 * @author : Jarry Leo
 * @date : 2018/10/18 15:45
 * 动画元素
 */
public class AnimCell extends BaseCell {
    /**动画固定角左上*/
    public static final int CORNER_TOP_LEFT = 0;
    /**动画固定角右上*/
    public static final int CORNER_TOP_RIGHT = 1;
    /**动画固定角左下*/
    public static final int CORNER_BOTTOM_LEFT = 2;
    /**动画固定角右下*/
    public static final int CORNER_BOTTOM_RIGHT = 3;



    @Override
    protected Paint initPaint() {
        return null;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {

    }

    @Override
    public int getCellType() {
        return TYPE_ANIMATION;
    }
}
