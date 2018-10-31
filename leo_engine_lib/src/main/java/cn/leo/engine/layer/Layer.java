package cn.leo.engine.layer;

import android.graphics.Canvas;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import cn.leo.engine.cell.BaseCell;

/**
 * @author : Jarry Leo
 * @date : 2018/10/18 14:51
 * <p>
 * 图层类
 * 每个场景包含多个图层:
 * 比方说背景图层,怪物图层,人物图层
 * 每个图层包含多个元素,可以是图片元素,动画元素,文字元素
 * 最表层图层覆盖底层图层;
 */
public class Layer {

    private boolean mIsVisible = true;
    /**
     * 元素高度是否需要重新排序
     */
    private boolean mIsNeedReSort;

    private List<BaseCell> mCells = new ArrayList<>();

    public void addCell(@NonNull BaseCell cell) {
        mCells.add(cell);
        requestReSortZ();
    }

    public void removeCell(@NonNull BaseCell cell) {
        cell.setDestroy(true);
    }

    /**
     * 元素高度重排
     */
    private void reSort() {
        mIsNeedReSort = false;
        Collections.sort(mCells);
    }

    /**
     * 请求元素高度重排
     */
    public void requestReSortZ() {
        mIsNeedReSort = true;
    }

    /**
     * 给图层内的元素分发绘制事件
     *
     * @param canvas 画布
     */
    public void dispatchDraw(@NonNull Canvas canvas) {
        if (!mIsVisible) {
            return;
        }
        if (mIsNeedReSort) {
            reSort();
        }
        ListIterator<BaseCell> iterator = mCells.listIterator();
        while (iterator.hasNext()) {
            BaseCell cell = iterator.next();
            if (cell.isDestroy()) {
                iterator.remove();
                continue;
            }
            cell.dispatchDraw(canvas);
        }
    }

    public boolean isVisible() {
        return mIsVisible;
    }

    public void setVisible(boolean visible) {
        mIsVisible = visible;
    }

    /**
     * 销毁场景,回收资源
     */
    public void onDestroy() {
        for (BaseCell cell : mCells) {
            cell.onDestroy();
        }
        mCells.clear();
    }
}
