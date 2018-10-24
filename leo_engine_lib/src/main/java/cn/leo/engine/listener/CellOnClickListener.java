package cn.leo.engine.listener;

import cn.leo.engine.cell.BaseCell;

/**
 * @author : Jarry Leo
 * @date : 2018/10/22 10:34
 */
public interface CellOnClickListener {
    /**
     * 点击事件
     * @param cell 元素
     */
    void onClick(BaseCell cell);
}
