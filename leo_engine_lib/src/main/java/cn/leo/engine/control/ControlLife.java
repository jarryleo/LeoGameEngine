package cn.leo.engine.control;

/**
 * @author : Jarry Leo
 * @date : 2018/10/30 13:55
 * 控制器生命周期
 */
public interface ControlLife {
    /**
     * 帧事件
     */
    void onFrame();

    /**
     * 销毁场景,回收资源
     */
    void onDestroy();
}
