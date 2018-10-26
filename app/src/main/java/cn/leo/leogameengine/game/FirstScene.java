package cn.leo.leogameengine.game;

import android.graphics.Paint;
import android.view.MotionEvent;

import cn.leo.engine.LeoEngine;
import cn.leo.engine.cell.BaseCell;
import cn.leo.engine.cell.CellRecycler;
import cn.leo.engine.cell.ImageCell;
import cn.leo.engine.cell.TextCell;
import cn.leo.engine.cell.animation.AnimCell;
import cn.leo.engine.cell.animation.AnimClip;
import cn.leo.engine.cell.animation.AnimFrame;
import cn.leo.engine.control.CellControl;
import cn.leo.engine.control.Scheduler;
import cn.leo.engine.layer.BaseLayer;
import cn.leo.engine.listener.CellEventListener;
import cn.leo.engine.listener.CellOnTouchListener;
import cn.leo.engine.scene.BaseScene;

/**
 * @author : Jarry Leo
 * @date : 2018/10/22 17:32
 */
public class FirstScene extends BaseScene {


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

    }


    /**
     * 玩家动画
     */
    private void createPlayerAnim(BaseLayer layer) {
        //创建动画片段
        AnimClip animClip = AnimClip.create()
                .addFrame(new AnimFrame(this, "pic/hero1.png", 100))
                .addFrame(new AnimFrame(this, "pic/hero2.png", 100))
                .setLoop(true);
        //创建动画元素
        AnimCell animCell = AnimCell.create()
                .setAnimClip(animClip, true)
                //设置元素大小
                .setWidth(60)
                .setHeight(80)
                //设置元素位置
                .setCenterToX(getWidth() / 2)
                .setCenterToY(getHeight() - 120);
        //元素添加到图层
        layer.addCell(animCell);
        //元素添加到控制器,便于其它位置获取飞机元素
        getCellControl().addCell("player", animCell);
        //飞机触摸事件监听
        getTouchControl().setCellOnTouch(animCell, new CellOnTouchListener() {
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
        final CellControl.CellProperty player = getCellControl().getCellProperty("player").get(0);

        final CellRecycler<ImageCell> bulletPool = new CellRecycler<ImageCell>() {
            @Override
            public ImageCell buildCell() {
                //创建子弹对象
                final ImageCell bullet = ImageCell.create(FirstScene.this, "pic/bullet1.png")
                        .setWidth(5, true);
                getCellControl().addCell("bullet", bullet);
                getCellControl().setYSpeed("bullet", -300);
                layer.addCell(bullet);
                return bullet;
            }
        };
        getTimerControl().subscribe(new Scheduler() {
            @Override
            public void event() {
                float x = player.getCell().getX();
                float y = player.getCell().getY();
                ImageCell cell = bulletPool.getCell();
                cell.setCenterToX(x + 30).setCenterToY(y);
                //子弹事件监控
                getCellControl().setCellEventListener("bullet", new CellEventListener<ImageCell>() {
                    @Override
                    public void onCellMove(ImageCell cell, float lastX, float newX, float lastY, float newY, float lastRotation, float newRotation) {
                        if (newY < -cell.getHeight()) {
                            cell.recycle();
                        }
                    }
                });

            }
        }, 200, -1, 1000);

    }

    /**
     * 敌人动画
     */
    private void createEnemyAnim(BaseLayer layer) {
        //创建动画片段
        AnimClip animClip = AnimClip.create()
                .addFrame(new AnimFrame(this, "pic/enemy3_n1.png", 200))
                .addFrame(new AnimFrame(this, "pic/enemy3_n2.png", 200))
                .setLoop(true);
        //创建动画单元
        AnimCell animCell = AnimCell.create()
                .setAnimClip(animClip, true)
                .setWidth(120)
                .setHeight(180)
                .setCenterToX(getWidth() / 2);
        layer.addCell(animCell);
    }

    /**
     * 文字
     */
    private void createText(BaseLayer layer) {
        TextCell textCell = TextCell.create("飞机大战")
                .setTextAlign(Paint.Align.CENTER)
                .setTextSize(30)
                .setX(getWidth() / 2)
                .setY(getHeight() / 2)
                .setZ(2000)
                .setRotate(30);
        layer.addCell(textCell);
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
        getCellControl().addCell("bg", bg1);
        getCellControl().addCell("bg", bg2);
        //向下滚动,速度每秒100dp
        getCellControl().setYSpeed("bg", 100);
        //监听滚动,一张到底后拼接到另一张开头,以做到无限循环
        getCellControl().setCellEventListener("bg", new CellEventListener<ImageCell>() {
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
