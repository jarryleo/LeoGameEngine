package cn.leo.engine.layer;

import android.support.annotation.NonNull;

import java.util.Set;
import java.util.TreeSet;

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
public class BaseLayer {
    private Set<BaseCell> mCells = new TreeSet<>();

    public void addCell(@NonNull BaseCell cell) {
        mCells.add(cell);

    }

    public void removeCell(@NonNull BaseCell cell) {
        mCells.remove(cell);
    }

    public void reSort(){
        
    }
}
