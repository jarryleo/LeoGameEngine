package cn.leo.engine.control;

import android.view.MotionEvent;

import cn.leo.engine.cell.BaseCell;

/**
 * @author : Jarry Leo
 * @date : 2018/10/22 10:34
 */
public interface CellOnTouchListener {
    void onTouch(BaseCell cell, MotionEvent event);
}
