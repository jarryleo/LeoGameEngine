package cn.leo.engine.control;

import cn.leo.engine.cell.BaseCell;
import cn.leo.engine.listener.CellOnClickListener;
import cn.leo.engine.listener.CellOnTouchListener;

/**
 * @author : Jarry Leo
 * @date : 2018/10/30 14:31
 */
public interface TouchControl extends ControlLife {
    /**
     * 设置元素点击事件
     *
     * @param cell            元素
     * @param onClickListener 点击事件
     */
    void setCellOnClick(BaseCell cell, CellOnClickListener onClickListener);

    /**
     * 设置元素触摸事件
     *
     * @param cell            元素
     * @param onTouchListener 事件
     */
    void setCellOnTouch(BaseCell cell, CellOnTouchListener onTouchListener);

    /**
     * 全局触摸事件
     *
     * @param onTouchListener 触摸回调
     */
    void setOnTouchListener(CellOnTouchListener onTouchListener);
}
