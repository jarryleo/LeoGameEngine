package cn.leo.engine.physics;

import android.graphics.Rect;

import cn.leo.engine.cell.BaseCell;

/**
 * @author : Jarry Leo
 * @date : 2018/10/24 10:17
 * 碰撞检测
 */
public class CollisionDetection {
    private static Rect rect1 = new Rect();
    private static Rect rect2 = new Rect();

    /**
     * 碰撞检测，传入2个单元
     *
     * @param a
     * @param b
     * @param overlap //重叠部分，修正误差
     * @return
     */
    public static boolean isCollision(BaseCell a, BaseCell b, int overlap) {
        rect1.set((int) a.getXInPx() - overlap,
                (int) a.getYInPx() - overlap,
                (int) (a.getXInPx() + a.getWidthInPx() + overlap),
                (int) (a.getYInPx() + a.getHeightInPx() + overlap));
        rect2.set((int) b.getXInPx() - overlap,
                (int) b.getYInPx() - overlap,
                (int) (b.getXInPx() + b.getWidthInPx() + overlap),
                (int) (b.getYInPx() + b.getHeightInPx() + overlap));
        return rect1.intersect(rect2);
    }
}
