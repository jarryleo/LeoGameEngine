package cn.leo.engine.screen;

/**
 * @author : Jarry Leo
 * @date : 2018/10/18 18:02
 */
public class ScreenUtil {
    /**
     * px转dp
     */
    public static int px2dp(float pxValue) {
        return (int) (pxValue / ScreenAdapter.density + 0.5f);
    }

    /**
     * dp转px
     */
    public static int dp2px(float dipValue) {
        return (int) (dipValue * ScreenAdapter.density + 0.5f);
    }

    /**
     * 获取屏幕高度的dp值
     */
    public static int getScreenHeightInDp() {
        return ScreenAdapter.screenHeightInDp;
    }

    /**
     * 获取屏幕宽度的dp值
     */
    public static int getScreenWidthIndp() {
        return ScreenAdapter.screenWidthInDp;
    }
}
