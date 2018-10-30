package cn.leo.engine.control;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.view.GestureDetector;
import android.view.MotionEvent;

import java.util.concurrent.ConcurrentHashMap;

import cn.leo.engine.cell.BaseCell;
import cn.leo.engine.listener.CellOnClickListener;
import cn.leo.engine.listener.CellOnTouchListener;

/**
 * @author : Jarry Leo
 * @date : 2018/10/22 10:54
 * <p>
 * 触摸事件和点击事件等和用户交互事件控制器
 */
public class TouchControlImpl implements TouchControl{
    private Context mContext;

    private CellOnTouchListener mOnTouchListener;

    public TouchControlImpl(Context context) {
        mContext = context;
    }

    /**
     * 元素点击事件集合
     */
    private ConcurrentHashMap<BaseCell, CellOnClickListener> mCellOnClickListeners
            = new ConcurrentHashMap<>();
    /**
     * 元素触摸事件集合
     */
    private ConcurrentHashMap<BaseCell, CellOnTouchListener> mCellOnTouchListeners
            = new ConcurrentHashMap<>();

    /**
     * 设置元素点击事件
     *
     * @param cell            元素
     * @param onClickListener 点击事件
     */
    @Override
    public void setCellOnClick(BaseCell cell, CellOnClickListener onClickListener) {
        mCellOnClickListeners.put(cell, onClickListener);
    }

    /**
     * 设置元素触摸事件
     *
     * @param cell            元素
     * @param onTouchListener 事件
     */
    @Override
    public void setCellOnTouch(BaseCell cell, CellOnTouchListener onTouchListener) {
        mCellOnTouchListeners.put(cell, onTouchListener);
    }

    /**
     * 全局触摸事件
     *
     * @param onTouchListener 触摸回调
     */
    @Override
    public void setOnTouchListener(CellOnTouchListener onTouchListener) {
        mOnTouchListener = onTouchListener;
    }

    /**
     * 触摸事件
     *
     * @param event 事件
     */
    public void onTouchEvent(MotionEvent event) {
        mGestureDetectorCompat.onTouchEvent(event);
        mCellMotionEvent.setMotionEvent(event);
        float x = mCellMotionEvent.getX();
        float y = mCellMotionEvent.getY();
        int action = mCellMotionEvent.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            for (BaseCell cell : mCellOnTouchListeners.keySet()) {
                if (cell.getRect().contains((int) x, (int) y)) {

                    boolean touch = mCellOnTouchListeners.get(cell).onTouch(cell, mCellMotionEvent);
                    if (touch) {
                        mTouchCell = cell;
                        break;
                    }
                }
            }
        } else if (action == MotionEvent.ACTION_UP) {
            mTouchCell = null;
        } else if (mTouchCell != null) {
            mCellOnTouchListeners.get(mTouchCell).onTouch(mTouchCell, mCellMotionEvent);
        }
        if (mOnTouchListener != null) {
            mOnTouchListener.onTouch(mTouchCell, mCellMotionEvent);
        }
    }

    /**
     * 触摸事件down时选中的cell
     */
    private BaseCell mTouchCell;
    /**
     * 触摸事件包装类
     */
    CellOnTouchListener.CellMotionEvent mCellMotionEvent
            = new CellOnTouchListener.CellMotionEvent();

    /**
     * 触摸手势辅助类
     */
    private GestureDetectorCompat mGestureDetectorCompat
            = new GestureDetectorCompat(mContext, new GameGestureListener());

    class GameGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            mCellMotionEvent.setMotionEvent(e);
            float x = mCellMotionEvent.getX();
            float y = mCellMotionEvent.getY();
            for (BaseCell cell : mCellOnClickListeners.keySet()) {
                if (cell.getRect().contains((int) x, (int) y)) {
                    mCellOnClickListeners.get(cell).onClick(cell);
                }
            }
            return true;
        }
    }

    @Override
    public void onFrame() {

    }

    /**
     * 销毁场景,回收资源
     */
    @Override
    public void onDestroy() {
        mCellOnClickListeners.clear();
        mCellOnTouchListeners.clear();
    }
}
