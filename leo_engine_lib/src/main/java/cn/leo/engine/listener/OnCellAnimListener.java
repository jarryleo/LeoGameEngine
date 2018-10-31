package cn.leo.engine.listener;

import cn.leo.engine.cell.animation.AnimCell;

/**
 * @author : Jarry Leo
 * @date : 2018/10/31 15:05
 * 动画元素动画事件
 */
public abstract class OnCellAnimListener {
    /**
     * 动画开始
     *
     * @param animCell 播放动画的元素
     */
    public void onAnimStart(AnimCell animCell) {
    }


    /**
     * 动画暂停
     *
     * @param animCell 播放动画的元素
     */
    public void onAnimPause(AnimCell animCell) {
    }


    /**
     * 动画继续
     *
     * @param animCell 播放动画的元素
     */
    public void onAnimResume(AnimCell animCell) {
    }


    /**
     * 动画结束
     *
     * @param animCell 播放动画的元素
     */
    public void onAnimFinished(AnimCell animCell) {
    }
}
