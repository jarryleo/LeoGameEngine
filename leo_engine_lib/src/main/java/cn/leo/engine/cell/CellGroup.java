package cn.leo.engine.cell;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : Jarry Leo
 * @date : 2018/10/24 14:09
 * 元素分组,统一操作
 */
public class CellGroup {
    /**
     * 组名
     */
    private String mGroupName;

    private List<BaseCell> mCells = new ArrayList<>();

    public void add(BaseCell cell) {
        mCells.add(cell);
    }

    public void remove(BaseCell cell) {
        mCells.remove(cell);
    }

    public void clear() {
        mCells.clear();
    }
}
