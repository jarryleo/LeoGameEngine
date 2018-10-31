package cn.leo.engine.common;

/**
 * @author : Jarry Leo
 * @date : 2018/10/31 11:47
 * System.currentTimeMillis() 这个函数消耗巨大,比new100个对象还大
 * 为了避免绘制时多处调用,导致性能降低非常严重
 * 因此
 * 每一帧绘制前获取一次系统时间,一帧内的所有事件都用这个时间,还能起到统一作用
 */
public class SystemTime {
    private static long currentTimeMillis = System.currentTimeMillis();

    public static void setTime() {
        currentTimeMillis = System.currentTimeMillis();
    }

    public static long now() {
        return currentTimeMillis;
    }

}
