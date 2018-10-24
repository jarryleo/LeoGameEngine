package cn.leo.engine.listener;

import android.view.MotionEvent;

import cn.leo.engine.cell.BaseCell;

/**
 * @author : Jarry Leo
 * @date : 2018/10/22 10:34
 */
public interface CellOnTouchListener {
    /**
     * 元素触摸事件
     *
     * @param cell  触摸元素
     * @param event 事件类型
     */
    void onTouch(BaseCell cell, MotionEvent event);
}
