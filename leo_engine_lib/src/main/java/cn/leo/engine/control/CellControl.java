package cn.leo.engine.control;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import cn.leo.engine.cell.BaseCell;
import cn.leo.engine.listener.CellEventListener;

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
     * 元素属性
     */
    public class CellProperty {
        BaseCell mCell;
        /**
         * 移动速度,每秒移动距离dp
         */
        private float mXSpeed;
        private float mYSpeed;
        /**
         * 元素上次绘制时间
         */
        private long lastDrawTime;
        private CellEventListener mCellEventListener;

        public CellProperty(BaseCell cell) {
            mCell = cell;
        }

        public float getXSpeed() {
            return mXSpeed;
        }

        public void setXSpeed(float XSpeed) {
            mXSpeed = XSpeed;
        }

        public float getYSpeed() {
            return mYSpeed;
        }

        public void setYSpeed(float YSpeed) {
            mYSpeed = YSpeed;
        }

        public void cellMove() {
            long timeMillis = System.currentTimeMillis();
            if (lastDrawTime > 0) {
                long l = timeMillis - lastDrawTime;
                float xDistance = mXSpeed * l / 1000;
                float yDistance = mYSpeed * l / 1000;
                mCell.moveBy(xDistance, yDistance);
            }
            lastDrawTime = timeMillis;
        }

        public void setCellEventListener(CellEventListener cellEventListener) {
            mCellEventListener = cellEventListener;
        }
    }

    public void addCell(String key, BaseCell cell) {
        List<CellProperty> properties = mCellProperties.get(key);
        if (properties == null) {
            properties = new ArrayList<>();
            mCellProperties.put(key, properties);
        }
        properties.add(new CellProperty(cell));
    }

    public List<CellProperty> getCellProperty(String key) {
        return mCellProperties.get(key);
    }

    /**
     * 对同一类cell 设置事件监听
     *
     * @param key               cell关键字
     * @param cellEventListener 事件监听
     */
    public void setCellEventListener(String key, CellEventListener cellEventListener) {
        List<CellProperty> cellProperty = getCellProperty(key);
        for (CellProperty property : cellProperty) {
            property.setCellEventListener(cellEventListener);
        }

    }

    public void onFrame() {
        for (List<CellProperty> properties : mCellProperties.values()) {
            for (CellProperty cellProperty : properties) {
                cellProperty.cellMove();
            }
        }
    }

    /**
     * 销毁场景,回收资源
     */
    public void onDestroy() {

    }

}
