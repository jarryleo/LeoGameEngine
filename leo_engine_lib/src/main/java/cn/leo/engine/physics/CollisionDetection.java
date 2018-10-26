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
     * @param overlap 允许重叠部分，修正误差 dp
     * @return
     */
    public static boolean isCollision(BaseCell a, BaseCell b, int overlap) {
        rect1.set((int) a.getX() + overlap,
                (int) a.getY() + overlap,
                (int) (a.getX() + a.getWidth() - overlap),
                (int) (a.getY() + a.getHeight() - overlap));
        rect2.set((int) b.getX() + overlap,
                (int) b.getY() + overlap,
                (int) (b.getX() + b.getWidth() - overlap),
                (int) (b.getY() + b.getHeight() - overlap));
        return rect1.intersect(rect2);
    }
}
