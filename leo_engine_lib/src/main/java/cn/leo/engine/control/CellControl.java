package cn.leo.engine.control;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import cn.leo.engine.cell.BaseCell;
import cn.leo.engine.cell.animation.AnimCell;
import cn.leo.engine.cell.animation.AnimClip;
import cn.leo.engine.listener.CellEventListener;
import cn.leo.engine.listener.CellOnClickListener;
import cn.leo.engine.listener.CellOnTouchListener;
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
     * 元素属性
     */
    public class CellProperty {
        /**
         * 对应元素
         */
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
        /**
         * 元素回调
         */
        private CellEventListener mCellEventListener;
        private CellOnClickListener mCellOnClickListener;
        private CellOnTouchListener mCellOnTouchListener;


        public CellProperty(BaseCell cell) {
            mCell = cell;
        }

        public BaseCell getCell() {
            return mCell;
        }

        public float getXSpeed() {
            return mXSpeed;
        }

        public void setXSpeed(float xSpeed) {
            mXSpeed = xSpeed;
        }

        public float getYSpeed() {
            return mYSpeed;
        }

        public void setYSpeed(float ySpeed) {
            mYSpeed = ySpeed;
        }

        void cellMove() {
            long timeMillis = System.currentTimeMillis();
            float lastX = mCell.getX();
            float lastY = mCell.getY();
            float lastRotate = mCell.getRotate();
            if (lastDrawTime > 0) {
                long l = timeMillis - lastDrawTime;
                float xDistance = mXSpeed * l / 1000;
                float yDistance = mYSpeed * l / 1000;
                if (mCellEventListener != null) {
                    mCellEventListener.onCellMove(mCell, lastX, mCell.getX(),
                            lastY, mCell.getY(), lastRotate, mCell.getRotate());
                }
                mCell.moveBy(xDistance, yDistance);
            }
            lastDrawTime = timeMillis;
        }

        public void setCellEventListener(CellEventListener cellEventListener) {
            mCellEventListener = cellEventListener;
        }

        public void setCellOnClickListener(CellOnClickListener cellOnClickListener) {
            mCellOnClickListener = cellOnClickListener;
        }

        public void setCellOnTouchListener(CellOnTouchListener cellOnTouchListener) {
            mCellOnTouchListener = cellOnTouchListener;
        }

        void hideCell() {
            mCell.setVisible(false);
            if (mCellEventListener != null) {
                mCellEventListener.onCellHide(mCell);
            }
        }

        void showCell() {
            mCell.setVisible(true);
            if (mCellEventListener != null) {
                mCellEventListener.onCellShow(mCell);
            }
        }

        void playAnim() {
            if (mCell instanceof AnimCell) {
                AnimCell cell = (AnimCell) mCell;
                cell.start();
                if (mCellEventListener != null) {
                    mCellEventListener.onCellPlayAnim(mCell);
                }
            }
        }

        void playAnim(AnimClip animClip) {
            if (mCell instanceof AnimCell) {
                AnimCell cell = (AnimCell) mCell;
                cell.setAnimClip(animClip, true);
                cell.start();
                if (mCellEventListener != null) {
                    mCellEventListener.onCellPlayAnim(mCell);
                }
            }
        }
    }

    public void addCell(String cellName, BaseCell cell) {
        List<CellProperty> properties = mCellProperties.get(cellName);
        if (properties == null) {
            properties = new ArrayList<>();
            mCellProperties.put(cellName, properties);
        }
        properties.add(new CellProperty(cell));
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
        for (CellProperty property : cellProperty) {

        }
    }

    /**
     * 帧事件
     */
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
        mCellProperties.clear();
    }

}
