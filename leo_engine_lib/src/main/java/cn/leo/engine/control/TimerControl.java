package cn.leo.engine.control;

/**
 * @author : Jarry Leo
 * @date : 2018/10/30 14:24
 */
public interface TimerControl extends ControlLife {
    /**
     * 事件重复次数无限
     */
    int REPEAT_FOREVER = -1;

    /**
     * 订阅事件
     *
     * @param scheduler 触发执行的事件
     * @param interval  事件间隔 毫秒
     * @param repeat    事件重复次数 -1 无限次
     * @param delay     订阅后多久开始执行 毫秒
     */
    void subscribe(Scheduler scheduler, long interval, int repeat, long delay);

    /**
     * 订阅执行一次
     *
     * @param scheduler 执行事件
     * @param delay     订阅后延迟多久执行
     */
    void subscribeOnce(Scheduler scheduler, long delay);

    /**
     * 取消订阅
     *
     * @param scheduler 事件
     */
    void unsubscribe(Scheduler scheduler);
}
