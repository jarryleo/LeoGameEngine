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
public class TouchControl {
    private Context mContext;

    private CellOnTouchListener mOnTouchListener;

    public TouchControl(Context context) {
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
    public void setCellOnClick(BaseCell cell, CellOnClickListener onClickListener) {
        mCellOnClickListeners.put(cell, onClickListener);
    }

    /**
     * 设置元素触摸事件
     *
     * @param cell            元素
     * @param onTouchListener 事件
     */
    public void setCellOnTouch(BaseCell cell, CellOnTouchListener onTouchListener) {
        mCellOnTouchListeners.put(cell, onTouchListener);
    }

    /**
     * 全局触摸事件
     *
     * @param onTouchListener 触摸回调
     */
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
        float x = event.getX();
        float y = event.getY();
        BaseCell touchCell = null;
        for (BaseCell cell : mCellOnTouchListeners.keySet()) {
            if (cell.getRect().contains((int) x, (int) y)) {
                mCellOnTouchListeners.get(cell).onTouch(cell, event);
                touchCell = cell;
            }
        }
        if (mOnTouchListener != null) {
            mOnTouchListener.onTouch(touchCell, event);
        }
    }

    private GestureDetectorCompat mGestureDetectorCompat
            = new GestureDetectorCompat(mContext, new GameGestureListener());

    class GameGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            float x = e.getX();
            float y = e.getY();
            for (BaseCell cell : mCellOnClickListeners.keySet()) {
                if (cell.getRect().contains((int) x, (int) y)) {
                    mCellOnClickListeners.get(cell).onClick(cell);
                }
            }
            return true;
        }
    }

    /**
     * 销毁场景,回收资源
     */
    public void onDestroy() {
        mCellOnClickListeners.clear();
        mCellOnTouchListeners.clear();
    }
}
