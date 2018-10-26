package cn.leo.engine.path;

import cn.leo.engine.cell.BaseCell;

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

    private long mStartTime;

    public BasePath(BaseCell baseCell) {
        mBaseCell = baseCell;
        resetStartAttr();
    }

    private void resetStartAttr() {
        mStartTime = System.currentTimeMillis();
        mStartX = mBaseCell.getX();
        mStartY = mBaseCell.getY();
        mStartZ = mBaseCell.getZ();
        mStartRotate = mBaseCell.getRotate();
    }

    /**
     * 实时移动
     */
    public void onFrame() {
        long l = System.currentTimeMillis();
        long passTime = l - mStartTime;
        //已过时间百分比
        float v = passTime / mInterval;
        //移动百分比
        float xRange = mTargetX - mStartX;
        float yRange = mTargetY - mStartY;
        float zRange = mTargetZ - mStartZ;
        float rRange = mTargetRotate - mStartRotate;

        if (mCellInterpolator != null) {
            xRange = mCellInterpolator.getX(xRange);
            yRange = mCellInterpolator.getY(yRange);
            zRange = mCellInterpolator.getZ(zRange);
            rRange = mCellInterpolator.getRotate(rRange);
        }
        mBaseCell.zChangeBy(zRange);
        mBaseCell.moveBy(xRange, yRange);
        mBaseCell.rotateBy(rRange);
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
