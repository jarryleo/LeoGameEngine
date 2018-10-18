package cn.leo.engine.screen;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.DisplayMetrics;

/**
 * @author : Jarry Leo
 * @date : 2018/10/18 15:01
 * <p>
 * 屏幕适配类
 * <p>
 * 用于统一不同尺寸不同分辨率的屏幕适配,保证画面效果一致
 */
public class ScreenAdapter {
    /**
     * 默认屏幕宽度 dp
     */
    static int screenWidthInDp = 360;
    /**
     * 屏幕高度dp值
     */
    static int screenHeightInDp = 360;
    /**
     * dp和px 比值
     */
    static float density = 1;

    /**
     * Reference from: https://mp.weixin.qq.com/s/d9QCoBP6kV9VSWvVldVVwA
     */
    @RequiresApi(api = Build.VERSION_CODES.DONUT)
    public static void adaptScreen(final Activity activity, int designWidthInDp) {
        if (activity == null) {
            return;
        }
        if (designWidthInDp <= 0) {
            designWidthInDp = screenWidthInDp;
        }
        final DisplayMetrics systemDm = Resources.getSystem().getDisplayMetrics();
        final DisplayMetrics appDm = activity.getApplication().getResources().getDisplayMetrics();
        final DisplayMetrics activityDm = activity.getResources().getDisplayMetrics();
        boolean isVerticalSlide = (activity.getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_PORTRAIT);
        if (isVerticalSlide) {
            activityDm.density = activityDm.widthPixels / (float) designWidthInDp;
            screenHeightInDp = (int) (activityDm.heightPixels / activityDm.density + 0.5f);
        } else {
            activityDm.density = activityDm.heightPixels / (float) designWidthInDp;
            screenHeightInDp = (int) (activityDm.widthPixels / activityDm.density + 0.5f);
        }
        density = activityDm.density;
        activityDm.scaledDensity = activityDm.density * (systemDm.scaledDensity / systemDm.density);
        activityDm.densityDpi = (int) (160 * activityDm.density + 0.5);
        appDm.density = activityDm.density;
        appDm.scaledDensity = activityDm.scaledDensity;
        appDm.densityDpi = activityDm.densityDpi;
    }

    /**
     * Cancel adapt the screen.
     *
     * @param activity The activity.
     */
    @RequiresApi(api = Build.VERSION_CODES.DONUT)
    static void cancelAdaptScreen(final Activity activity) {
        if (activity == null) {
            return;
        }
        final DisplayMetrics systemDm = Resources.getSystem().getDisplayMetrics();
        final DisplayMetrics appDm = activity.getApplication().getResources().getDisplayMetrics();
        final DisplayMetrics activityDm = activity.getResources().getDisplayMetrics();
        activityDm.density = systemDm.density;
        activityDm.scaledDensity = systemDm.scaledDensity;
        activityDm.densityDpi = systemDm.densityDpi;
        appDm.density = systemDm.density;
        appDm.scaledDensity = systemDm.scaledDensity;
        appDm.densityDpi = systemDm.densityDpi;
    }

    /**
     * Return whether adapt screen.
     *
     * @return {@code true}: yes<br>{@code false}: no
     */
    static boolean isAdaptScreen(Activity activity) {
        if (activity == null) {
            return false;
        }
        final DisplayMetrics systemDm = Resources.getSystem().getDisplayMetrics();
        final DisplayMetrics appDm = activity.getApplication().getResources().getDisplayMetrics();
        return systemDm.density != appDm.density;
    }
}
