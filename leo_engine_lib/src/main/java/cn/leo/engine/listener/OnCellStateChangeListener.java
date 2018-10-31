package cn.leo.engine.listener;

import cn.leo.engine.cell.BaseCell;

/**
 * @author : Jarry Leo
 * @date : 2018/10/31 15:13
 * 元素状态变化事件监听
 */
public abstract class OnCellStateChangeListener<T extends BaseCell> {
    /**
     * 元素出现
     *
     * @param cell 元素
     */
    public void onCellShow(T cell) {

    }

    /**
     * 元素隐藏
     *
     * @param cell 元素
     */
    public void onCellHide(T cell) {

    }

    /**
     * 元素执行移动
     *
     * @param cell 元素
     */
    public void onCellMove(T cell, float lastX, float newX, float lastY, float newY, float lastRotation, float newRotation) {

    }

    /**
     * 元素移动结束
     *
     * @param cell 元素
     */
    public void onCellMoveFinished(T cell) {

    }
}
