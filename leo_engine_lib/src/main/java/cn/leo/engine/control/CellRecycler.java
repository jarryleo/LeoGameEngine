package cn.leo.engine.control;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import cn.leo.engine.cell.BaseCell;

/**
 * @author : Jarry Leo
 * @date : 2018/10/26 13:36
 * 元素复用池
 */
public abstract class CellRecycler<T extends BaseCell> {
    /**
     * 正在使用的元素池
     */
    private List<T> mCellPool = new ArrayList<>();
    /**
     * 被复用的元素池
     */
    private List<T> mRecyclePool = new ArrayList<>();

    /**
     * 从复用池获取元素,如果没有则新建
     *
     * @return 元素
     */
    public T getCell() {
        //1.先从复用元素池找最先可复用元素
        if (mRecyclePool.size() > 0) {
            T remove = mRecyclePool.remove(0);
            remove.setRecycle(false);
            remove.setVisible(true);
            return remove;
        }
        //2.复用元素池里面没有,从现在使用的元素池里面找
        T lastCell = null;
        if (mCellPool.size() > 0) {
            ListIterator<T> iterator = mCellPool.listIterator();
            while (iterator.hasNext()) {
                T cell = iterator.next();
                //3.找到可复用的元素
                if (cell.isRecycle()) {
                    //4.把上一个找到的放到复用池
                    if (lastCell != null) {
                        mRecyclePool.add(lastCell);
                    }
                    //5.把找到的元素赋值给上一个变量并从元素池移除
                    lastCell = cell;
                    iterator.remove();
                }
                //剔除销毁的元素
                if (cell.isDestroy()) {
                    iterator.remove();
                }
            }
        }
        //6.最后一个找到的可复用元素,没有放入复用池,直接拿去复用
        if (lastCell != null) {
            lastCell.setRecycle(false);
            lastCell.setVisible(true);
            return lastCell;
        }
        //7.都没有可复用的元素,新建一个
        T newCell = buildCell();
        mCellPool.add(newCell);
        return newCell;
    }

    /**
     * 新建元素
     *
     * @return 新元素
     */
    public abstract T buildCell();

    /**
     * 回收元素
     *
     * @param cell 元素
     */
    public void recycleCell(T cell) {
        mCellPool.add(cell);
    }

    /**
     * 销毁元素复用池
     */
    public void onDestroy() {
        mRecyclePool.clear();
        mCellPool.clear();
    }
}
