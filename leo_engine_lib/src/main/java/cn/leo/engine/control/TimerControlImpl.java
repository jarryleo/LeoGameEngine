package cn.leo.engine.control;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import cn.leo.engine.common.SystemTime;

/**
 * @author : Jarry Leo
 * @date : 2018/10/25 17:56
 * 向场景订阅事件机制
 */
public class TimerControlImpl implements TimerControl {


    private ConcurrentHashMap<Scheduler, SchedulerInfo> mSchedulers = new ConcurrentHashMap<>();

    /**
     * 订阅事件
     *
     * @param scheduler 触发执行的事件
     * @param interval  事件间隔 毫秒
     * @param repeat    事件重复次数 -1 无限次
     * @param delay     订阅后多久开始执行 毫秒
     */
    @Override
    public void subscribe(Scheduler scheduler, long interval, int repeat, long delay) {
        mSchedulers.put(scheduler, new SchedulerInfo(scheduler, interval, repeat, delay));
    }

    /**
     * 订阅执行一次
     *
     * @param scheduler 执行事件
     * @param delay     订阅后延迟多久执行
     */
    @Override
    public void subscribeOnce(Scheduler scheduler, long delay) {
        mSchedulers.put(scheduler, new SchedulerInfo(scheduler, 0, 1, delay));
    }

    /**
     * 取消订阅
     *
     * @param scheduler 事件
     */
    @Override
    public void unsubscribe(Scheduler scheduler) {
        mSchedulers.remove(scheduler);
    }

    /**
     * 场景执行
     */
    @Override
    public void onFrame() {
        Iterator<Map.Entry<Scheduler, SchedulerInfo>> iterator = mSchedulers.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Scheduler, SchedulerInfo> entry = iterator.next();
            SchedulerInfo value = entry.getValue();
            boolean scheduleResult = value.schedule();
            if (!scheduleResult) {
                iterator.remove();
            }
        }
    }

    @Override
    public void onDestroy() {
        mSchedulers.clear();
    }

    private static class SchedulerInfo {

        /**
         * 订阅的事件
         */
        private Scheduler mScheduler;
        /**
         * 订阅时间
         */
        private long mSubscribeTime;
        /**
         * 重复间隔
         */
        private long mInterval;
        /**
         * 重复次数
         */
        private long mRepeat;
        /**
         * 延迟执行事件
         */
        private long mDelay;
        /**
         * 上次执行时间
         */
        private long lastScheduledTime;


        private boolean schedule() {
            long timeMillis = SystemTime.now();
            //已达到延迟时间
            if (timeMillis - mSubscribeTime > mDelay) {
                //执行次数
                if (mRepeat > 0 || mRepeat == REPEAT_FOREVER) {
                    //上次执行时间到现在大于执行间隔
                    if (timeMillis - lastScheduledTime > mInterval) {
                        //执行订阅时间
                        mScheduler.event();
                        //记录上次执行时间
                        lastScheduledTime = timeMillis;
                        if (mRepeat > 0) {
                            //剩余次数-1
                            mRepeat--;
                        }
                    }
                } else {
                    return false;
                }
            }
            return true;
        }

        private SchedulerInfo(Scheduler scheduler, long interval, long repeat, long delay) {
            mScheduler = scheduler;
            mSubscribeTime = SystemTime.now();
            mInterval = interval;
            mRepeat = repeat;
            mDelay = delay;
        }

    }
}
