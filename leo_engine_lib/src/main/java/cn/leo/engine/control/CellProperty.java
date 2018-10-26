package cn.leo.engine.control;

import cn.leo.engine.cell.BaseCell;
import cn.leo.engine.cell.animation.AnimCell;
import cn.leo.engine.cell.animation.AnimClip;
import cn.leo.engine.listener.CellEventListener;
import cn.leo.engine.listener.CellOnClickListener;
import cn.leo.engine.listener.CellOnTouchListener;
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
     * 元素回调
     */
    private CellEventListener mCellEventListener;
    private CellOnClickListener mCellOnClickListener;
    private CellOnTouchListener mCellOnTouchListener;
    /**
     * 元素轨迹
     */
    private BasePath mBasePath;

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

    public void setBasePath(BasePath basePath) {
        basePath.setCell(mCell);
        mBasePath = basePath;
    }

    void cellMove() {
        if (mBasePath != null) {
            boolean moveSuccess = mBasePath.onFrame();
            if (!moveSuccess && mCellEventListener != null) {
                mCellEventListener.onCellMoveFinished(mCell);
            }
        } else {
            generalMove();
        }
    }

    private void generalMove() {
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
