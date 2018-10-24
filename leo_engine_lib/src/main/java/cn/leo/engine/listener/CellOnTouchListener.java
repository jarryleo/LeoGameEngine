package cn.leo.engine.listener;

import android.view.MotionEvent;

import cn.leo.engine.cell.BaseCell;
import cn.leo.engine.screen.ScreenUtil;

/**
 * @author : Jarry Leo
 * @date : 2018/10/22 10:34
 */
public interface CellOnTouchListener {
    /**
     * 元素触摸事件
     *
     * @param cell  触摸元素
     * @param event 事件类型
     * @return 是否消费事件
     */
    boolean onTouch(BaseCell cell, CellMotionEvent event);

    /**
     * 触摸事件包装类,获取的坐标为dp
     */
    class CellMotionEvent {
        MotionEvent mMotionEvent;

        public CellMotionEvent() {
        }

        public CellMotionEvent(MotionEvent event) {
            mMotionEvent = event;
        }

        public void setMotionEvent(MotionEvent motionEvent) {
            mMotionEvent = motionEvent;
        }


        public int getX() {
            float x = mMotionEvent.getX();
            return ScreenUtil.px2dp(x);
        }

        public int getY() {
            float y = mMotionEvent.getY();
            return ScreenUtil.px2dp(y);
        }

        public int getAction() {
            return mMotionEvent.getAction();
        }
    }
}
