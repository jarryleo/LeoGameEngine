package cn.leo.engine.control;

import cn.leo.engine.cell.BaseCell;
import cn.leo.engine.cell.animation.AnimCell;
import cn.leo.engine.cell.animation.AnimClip;
import cn.leo.engine.common.SystemTime;
import cn.leo.engine.listener.OnCellAnimListener;
import cn.leo.engine.listener.OnCellStateChangeListener;
import cn.leo.engine.path.BasePath;

/**
 * @author : Jarry Leo
 * @date : 2018/10/26 15:52
 * 元素属性包装类
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
     * 元素状态回调
     */
    private OnCellStateChangeListener mCellEventListener;
    /**
     * 元素轨迹
     */
    private BasePath mBasePath;

    CellProperty(BaseCell cell) {
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

    public void setBasePath(BasePath basePath) {
        basePath.setCell(mCell);
        mBasePath = basePath;
    }

    void cellMove() {
        if (mBasePath != null) {
            pathMove();
        } else {
            generalMove();
        }
    }

    /**
     * 轨迹移动
     */
    private void pathMove() {
        float lastX = mCell.getX();
        float lastY = mCell.getY();
        float lastRotate = mCell.getRotate();
        boolean moveSuccess = mBasePath.onFrame();
        if (!moveSuccess && mCellEventListener != null) {
            mCellEventListener.onCellMoveFinished(mCell);
        } else if (moveSuccess) {
            if (mCellEventListener != null) {
                mCellEventListener.onCellMove(mCell, lastX, mCell.getX(),
                        lastY, mCell.getY(), lastRotate, mCell.getRotate());
            }
        }
    }

    /**
     * 常规移动
     */
    private void generalMove() {
        long timeMillis = SystemTime.now();
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

    public void setCellStateChangeListener(OnCellStateChangeListener cellEventListener) {
        mCellEventListener = cellEventListener;
    }

    public void setCellAnimListener(OnCellAnimListener cellEventListener) {
        if (mCell instanceof AnimCell) {
            ((AnimCell) mCell).setOnCellAnimListener(cellEventListener);
        }
    }

    public void hideCell() {
        mCell.setVisible(false);
        if (mCellEventListener != null) {
            mCellEventListener.onCellHide(mCell);
        }
    }

    public void showCell() {
        mCell.setVisible(true);
        if (mCellEventListener != null) {
            mCellEventListener.onCellShow(mCell);
        }
    }

    public void playAnim() {
        if (mCell instanceof AnimCell) {
            AnimCell cell = (AnimCell) mCell;
            cell.start();
        }
    }

    public void playAnim(AnimClip animClip) {
        if (mCell instanceof AnimCell) {
            AnimCell cell = (AnimCell) mCell;
            cell.setAnimClip(animClip, true);
            cell.start();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CellProperty) {
            CellProperty cellProperty = (CellProperty) obj;
            return cellProperty.getCell().equals(getCell());
        } else {
            return false;
        }
    }
}
