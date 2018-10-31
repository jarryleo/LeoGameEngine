package cn.leo.engine.control;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import cn.leo.engine.cell.BaseCell;
import cn.leo.engine.listener.OnCellStateChangeListener;
import cn.leo.engine.path.BasePath;

/**
 * @author : Jarry Leo
 * @date : 2018/10/22 10:54
 * <p>
 * 元素行为控制器
 * <p>
 * NPC 自主移动,场景事件等
 */

public class CellControlImpl implements CellControl {

    private ConcurrentHashMap<String, List<CellProperty>> mCellProperties = new ConcurrentHashMap<>();

    /**
     * 元素属性包装,并交给控制器统一管理
     *
     * @param cellName 元素名称
     * @param cell     元素对象
     * @return 包装后的元素
     */
    @Override
    public CellProperty addCellToControl(String cellName, BaseCell cell) {
        List<CellProperty> properties = mCellProperties.get(cellName);
        if (properties == null) {
            properties = new ArrayList<>();
            mCellProperties.put(cellName, properties);
        }
        CellProperty property = new CellProperty(cell);
        properties.add(property);
        return property;
    }

    /**
     * 从控制器移除元素
     *
     * @param cellName 元素名称
     * @param cell     元素对象
     */
    @Override
    public void removeCellFromControl(String cellName, BaseCell cell) {
        List<CellProperty> cellProperties = mCellProperties.get(cellName);
        cellProperties.remove(new CellProperty(cell));

    }

    /**
     * 根据关键字获取元素包装属性集合
     *
     * @param cellName 元素名称
     * @return 元素包装属性集合
     */
    @Override
    public List<CellProperty> getCellProperty(String cellName) {
        return mCellProperties.get(cellName);
    }

    /**
     * 对同一类cell 设置事件监听
     *
     * @param cellName                cell关键字
     * @param cellStateChangeListener 事件监听
     */
    @Override
    public void setCellEventListener(String cellName, OnCellStateChangeListener cellStateChangeListener) {
        List<CellProperty> cellProperty = getCellProperty(cellName);
        if (cellProperty == null) {
            return;
        }
        for (CellProperty property : cellProperty) {
            property.setCellStateChangeListener(cellStateChangeListener);
        }

    }

    /**
     * 统一给元素分组设置速度
     *
     * @param cellName 元素名
     * @param xSpeed   速度
     */
    @Override
    public void setCellXSpeed(String cellName, float xSpeed) {
        List<CellProperty> cellProperty = getCellProperty(cellName);
        if (cellProperty == null) {
            return;
        }
        for (CellProperty property : cellProperty) {
            property.setXSpeed(xSpeed);
        }
    }

    /**
     * 统一给元素分组设置速度
     *
     * @param cellName 元素名
     * @param ySpeed   速度
     */
    @Override
    public void setCellYSpeed(String cellName, float ySpeed) {
        List<CellProperty> cellProperty = getCellProperty(cellName);
        if (cellProperty == null) {
            return;
        }
        for (CellProperty property : cellProperty) {
            property.setYSpeed(ySpeed);
        }
    }

    /**
     * 给元素设置路径
     *
     * @param cellName 元素名称
     * @param path     路径
     */
    @Override
    public void setCellPath(String cellName, BasePath path) {
        List<CellProperty> cellProperty = getCellProperty(cellName);
        if (cellProperty == null) {
            return;
        }
        for (CellProperty property : cellProperty) {
            property.setBasePath(path);
        }
    }

    /**
     * 帧事件
     */
    @Override
    public void onFrame() {
        for (List<CellProperty> properties : mCellProperties.values()) {
            Iterator<CellProperty> iterator = properties.iterator();
            while (iterator.hasNext()) {
                CellProperty next = iterator.next();
                boolean destroy = next.getCell().isDestroy();
                if (destroy) {
                    iterator.remove();
                } else {
                    next.cellMove();
                }
            }
        }
    }

    /**
     * 销毁场景,回收资源
     */
    @Override
    public void onDestroy() {
        mCellProperties.clear();
    }

}
