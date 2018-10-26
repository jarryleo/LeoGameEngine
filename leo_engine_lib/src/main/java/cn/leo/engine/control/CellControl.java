package cn.leo.engine.control;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import cn.leo.engine.cell.BaseCell;
import cn.leo.engine.listener.CellEventListener;
import cn.leo.engine.path.BasePath;

/**
 * @author : Jarry Leo
 * @date : 2018/10/22 10:54
 * <p>
 * 元素行为控制器
 * <p>
 * NPC 自主移动,场景事件等
 */

public class CellControl {

    private ConcurrentHashMap<String, List<CellProperty>> mCellProperties = new ConcurrentHashMap<>();

    /**
     * 元素属性包装,并交给控制器统一管理
     *
     * @param cellName 元素名称
     * @param cell     元素对象
     * @return 包装后的元素
     */
    public CellProperty addCell(String cellName, BaseCell cell) {
        List<CellProperty> properties = mCellProperties.get(cellName);
        if (properties == null) {
            properties = new ArrayList<>();
            mCellProperties.put(cellName, properties);
        }
        CellProperty property = new CellProperty(cell);
        properties.add(property);
        return property;
    }

    public void removeCell(BaseCell cell) {

    }

    public List<CellProperty> getCellProperty(String cellName) {
        return mCellProperties.get(cellName);
    }

    /**
     * 对同一类cell 设置事件监听
     *
     * @param cellName          cell关键字
     * @param cellEventListener 事件监听
     */
    public void setCellEventListener(String cellName, CellEventListener cellEventListener) {
        List<CellProperty> cellProperty = getCellProperty(cellName);
        if (cellProperty == null) {
            return;
        }
        for (CellProperty property : cellProperty) {
            property.setCellEventListener(cellEventListener);
        }

    }

    /**
     * 统一给元素分组设置速度
     *
     * @param cellName 元素名
     * @param xSpeed   速度
     */
    public void setXSpeed(String cellName, float xSpeed) {
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
    public void setYSpeed(String cellName, float ySpeed) {
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
    public void setPath(String cellName, BasePath path) {
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
    public void onDestroy() {
        mCellProperties.clear();
    }

}
