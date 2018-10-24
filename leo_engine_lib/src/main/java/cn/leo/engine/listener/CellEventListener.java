package cn.leo.engine.listener;

import cn.leo.engine.cell.BaseCell;

/**
 * @author : Jarry Leo
 * @date : 2018/10/24 10:33
 */
public class CellEventListener<T extends BaseCell> {
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
     * 元素执行动画
     *
     * @param cell 元素
     */
    public void onCellPlayAnim(T cell) {

    }

    /**
     * 元素执行移动
     *
     * @param cell 元素
     */
    public void onCellMove(T cell, float lastX, float newX, float lastY, float newY, float lastRotation, float newRotation) {

    }
}
