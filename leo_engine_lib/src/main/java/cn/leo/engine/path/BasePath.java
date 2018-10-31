package cn.leo.engine.path;

import cn.leo.engine.cell.BaseCell;
import cn.leo.engine.common.SystemTime;

/**
 * @author : Jarry Leo
 * @date : 2018/10/26 9:22
 * 元素运动轨迹
 */
public abstract class BasePath {
    /**
     * 起始左边x
     */
    private float mStartX;
    /**
     * 起始坐标y
     */
    private float mStartY;
    /**
     * 起始坐标z
     */
    private float mStartZ;
    /**
     * 起始旋转角度
     */
    private float mStartRotate;
    /**
     * 移动目标点x
     */
    private float mTargetX;
    /**
     * 移动目标点y
     */
    private float mTargetY;
    /**
     * 目标z
     */
    private float mTargetZ;
    /**
     * 目标角度
     */
    private float mTargetRotate;

    /**
     * 执行的元素
     */
    private BaseCell mBaseCell;

    /**
     * 移动时间
     */
    private float mInterval;
    /**
     * 计算插值器
     */
    private CellInterpolator mCellInterpolator;
    /**
     * 开始移动时间
     */
    private long mStartTime;

    /**
     * 是否移动结束
     */
    private boolean mIsMoveFinished;


    public BasePath() {
    }

    private void resetStartAttr() {
        mStartTime = SystemTime.now();
        mIsMoveFinished = false;
        if (mBaseCell != null) {
            mStartX = mBaseCell.getX();
            mStartY = mBaseCell.getY();
            mStartZ = mBaseCell.getZ();
            mStartRotate = mBaseCell.getRotate();
        }
        if (mTargetRotate == 0) {
            mTargetRotate = mStartRotate;
        }
    }

    /**
     * 实时移动
     */
    public boolean onFrame() {
        if (mIsMoveFinished) {
            return true;
        }
        //计算移动
        long l = SystemTime.now();
        long passTime = l - mStartTime;
        //执行完毕
        if (passTime > mInterval) {
            mIsMoveFinished = true;
            return false;
        }
        //已过时间百分比
        float v = passTime / mInterval;
        //移动百分比
        float xRange = (mTargetX - mStartX) * v;
        float yRange = (mTargetY - mStartY) * v;
        float zRange = (mTargetZ - mStartZ) * v;
        float rRange = (mTargetRotate - mStartRotate) * v;

        if (mCellInterpolator != null) {
            xRange = mCellInterpolator.getX(xRange);
            yRange = mCellInterpolator.getY(yRange);
            zRange = mCellInterpolator.getZ(zRange);
            rRange = mCellInterpolator.getRotate(rRange);
        }
        if (mBaseCell != null) {
            mBaseCell.setZ(mStartZ + zRange);
            mBaseCell.moveTo(mStartX + xRange, mStartY + yRange);
            mBaseCell.setRotate(mStartRotate + rRange);
        }
        return true;
    }

    public BaseCell getCell() {
        return mBaseCell;
    }

    public void setCell(BaseCell baseCell) {
        mBaseCell = baseCell;
        resetStartAttr();
    }

    public void setTargetX(float targetX) {
        mTargetX = targetX;
        resetStartAttr();
    }

    public void setTargetY(float targetY) {
        mTargetY = targetY;
        resetStartAttr();
    }

    public void setTargetZ(float targetZ) {
        mTargetZ = targetZ;
        resetStartAttr();
    }

    public void setTargetRotate(float targetRotate) {
        mTargetRotate = targetRotate;
        resetStartAttr();
    }

    public void setInterval(float interval) {
        mInterval = interval;
        resetStartAttr();
    }

    public void setCellInterpolator(CellInterpolator cellInterpolator) {
        mCellInterpolator = cellInterpolator;
    }
}
