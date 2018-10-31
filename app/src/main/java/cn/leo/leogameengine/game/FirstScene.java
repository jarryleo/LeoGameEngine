package cn.leo.leogameengine.game;

import android.graphics.Paint;
import android.view.MotionEvent;

import java.util.Random;

import cn.leo.engine.LeoEngine;
import cn.leo.engine.cell.BaseCell;
import cn.leo.engine.cell.ImageCell;
import cn.leo.engine.cell.TextCell;
import cn.leo.engine.cell.animation.AnimCell;
import cn.leo.engine.cell.animation.AnimClip;
import cn.leo.engine.control.CellProperty;
import cn.leo.engine.control.CellRecycler;
import cn.leo.engine.control.Scheduler;
import cn.leo.engine.control.TimerControl;
import cn.leo.engine.layer.BaseLayer;
import cn.leo.engine.listener.CellEventListener;
import cn.leo.engine.listener.CellOnTouchListener;
import cn.leo.engine.path.LinearPath;
import cn.leo.engine.physics.CollisionDetection;
import cn.leo.engine.scene.Scene;
import cn.leo.leogameengine.R;

/**
 * @author : Jarry Leo
 * @date : 2018/10/22 17:32
 */
public class FirstScene extends Scene {

    private float mDx;
    private float mDy;

    /**
     * 场景构造
     *
     * @param leoEngine 引擎
     */
    public FirstScene(LeoEngine leoEngine) {
        super(leoEngine);
    }


    @Override
    public void initScene() {
        //创建背景图层
        createBackGround();
        //创建角色图层
        BaseLayer layer = new BaseLayer();
        //添加文字
        createText(layer);
        //添加敌机动画
        createEnemyAnim(layer);
        //添加玩家动画
        createPlayerAnim(layer);
        //添加子弹
        createBullet(layer);
        //帧添加到场景
        addLayer(layer);
        //加载声音
        loadSounds(R.raw.bullet);
    }


    /**
     * 玩家动画
     */
    private void createPlayerAnim(BaseLayer layer) {
        //创建动画元素
        AnimCell animCell = AnimCell.create()
                .setAnimClip(AnimClip.create(this)
                        .addFrame("pic/hero1.png", 100)
                        .addFrame("pic/hero2.png", 100)
                        .setLoop(true), true)
                //设置元素大小
                .setWidth(60)
                .setHeight(80)
                //设置元素位置
                .setCenterToX(getWidth() / 2)
                .setCenterToY(getHeight() - 120);
        //元素添加到图层
        layer.addCell(animCell);
        //元素添加到控制器,便于其它位置获取飞机元素
        addCellToControl("player", animCell);
        //飞机触摸事件监听
        setCellOnTouch(animCell, new CellOnTouchListener() {
            @Override
            public boolean onTouch(BaseCell cell, CellMotionEvent event) {
                int action = event.getAction();
                float x = event.getX();
                float y = event.getY();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        mDx = x - cell.getX();
                        mDy = y - cell.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                    case MotionEvent.ACTION_UP:
                        cell.moveTo(x - mDx, y - mDy);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

    }

    /**
     * 子弹
     */
    private void createBullet(final BaseLayer layer) {

        //获取玩家位置,给子弹初始坐标
        final CellProperty player = getCellProperty("player").get(0);
        //子弹复用池
        final CellRecycler<ImageCell> bulletPool = new CellRecycler<ImageCell>() {
            @Override
            public ImageCell buildCell() {
                //创建子弹对象
                final ImageCell bullet = ImageCell.create(FirstScene.this, "pic/bullet1.png")
                        .setWidth(5, true);
                addCellToControl("bullet", bullet);
                setCellYSpeed("bullet", -300);
                layer.addCell(bullet);
                return bullet;
            }
        };
        //获取敌机位置
        final CellProperty enemy = getCellProperty("enemy").get(0);
        subscribe(new Scheduler() {
            @Override
            public void event() {
                //播放子弹声音
                //playSound(R.raw.bullet, false);
                //发射子弹
                float x = player.getCell().getX();
                float y = player.getCell().getY();
                ImageCell cell = bulletPool.getCell();
                cell.setCenterToX(x + 30).setCenterToY(y);
                //子弹事件监控
                setCellEventListener("bullet", new CellEventListener<ImageCell>() {
                    @Override
                    public void onCellMove(ImageCell cell, float lastX, float newX, float lastY, float newY, float lastRotation, float newRotation) {
                        if (CollisionDetection.isCollision(enemy.getCell(), cell, 5)) {
                            //击中敌机
                            cell.recycle();
                        }
                        if (newY < -cell.getHeight()) {
                            cell.recycle();
                        }
                    }
                });

            }
        }, 200, TimerControl.REPEAT_FOREVER, 1000);

    }

    /**
     * 敌人动画
     */
    private void createEnemyAnim(BaseLayer layer) {
        //创建动画单元
        final AnimCell animCell = AnimCell.create()
                .setAnimClip(AnimClip.create(this)
                        .addFrame("pic/enemy3_n1.png", 200)
                        .addFrame("pic/enemy3_n2.png", 200)
                        .setLoop(true), true)
                .setWidth(120)
                .setHeight(180)
                .setCenterToX(getWidth() / 2);
        layer.addCell(animCell);

        addCellToControl("enemy", animCell);
        //轨迹移动
        final Random random = new Random();
        final LinearPath path = new LinearPath();
        path.setInterval(5000);
        path.setTargetY(300);
        path.setTargetRotate(360);
        path.setTargetX(random.nextInt(300));
        setCellPath("enemy", path);
        //监控轨迹
        setCellEventListener("enemy", new CellEventListener() {
            @Override
            public void onCellMoveFinished(BaseCell cell) {
                path.setTargetY(random.nextInt(300));
                path.setTargetX(random.nextInt(300));
                path.setTargetRotate(random.nextInt(360));
            }
        });

    }

    /**
     * 文字
     */
    private void createText(BaseLayer layer) {
        layer.addCell(TextCell.create("飞机大战")
                .setTextAlign(Paint.Align.CENTER)
                .setTextSize(30)
                .setX(getWidth() / 2)
                .setZ(2000));
    }

    /**
     * 背景
     */
    private void createBackGround() {
        //创建背景图层
        BaseLayer backGround = new BaseLayer();
        final ImageCell bg1 = new ImageCell(this, "pic/background.png");
        //图片按宽高比填充屏幕
        bg1.setWidth(getWidth(), true);
        backGround.addCell(bg1);
        //克隆一张背景图和上面的拼接加长,以便滚动
        final ImageCell bg2 = bg1.clone();
        bg2.setY(-bg1.getHeight());
        backGround.addCell(bg2);
        addLayer(backGround);
        //控制2张背景图
        addCellToControl("bg", bg1);
        addCellToControl("bg", bg2);
        //向下滚动,速度每秒100dp
        setCellYSpeed("bg", 100);
        //监听滚动,一张到底后拼接到另一张开头,以做到无限循环
        setCellEventListener("bg", new CellEventListener<ImageCell>() {
            @Override
            public void onCellMove(ImageCell cell, float lastX, float newX, float lastY, float newY, float lastRotation, float newRotation) {
                BaseCell top = bg1.getY() < bg2.getY() ? bg1 : bg2;
                if (newY > getHeight()) {
                    cell.setY(top.getY() - cell.getHeight() + 1);
                }
            }
        });
    }

}
