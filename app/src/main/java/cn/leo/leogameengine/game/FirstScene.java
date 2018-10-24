package cn.leo.leogameengine.game;

import android.graphics.Paint;
import android.view.MotionEvent;

import java.util.List;

import cn.leo.engine.LeoEngine;
import cn.leo.engine.cell.AnimCell;
import cn.leo.engine.cell.BaseCell;
import cn.leo.engine.cell.ImageCell;
import cn.leo.engine.cell.TextCell;
import cn.leo.engine.control.CellControl;
import cn.leo.engine.layer.BaseLayer;
import cn.leo.engine.listener.CellEventListener;
import cn.leo.engine.listener.CellOnTouchListener;
import cn.leo.engine.scene.BaseScene;

/**
 * @author : Jarry Leo
 * @date : 2018/10/22 17:32
 */
public class FirstScene extends BaseScene {


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
        //添加玩家动画
        createPlayerAnim(layer);
        //添加文字
        createText(layer);
        //添加敌机动画
        createEnemyAnim(layer);
        //帧添加到场景
        addLayer(layer);

    }

    /**
     * 玩家动画
     */
    private void createPlayerAnim(BaseLayer layer) {
        //创建动画片段
        AnimCell.AnimClip animClip = new AnimCell.AnimClip();
        animClip.addFrame(new AnimCell.AnimFrame(getContext(), "pic/hero1.png", 100));
        animClip.addFrame(new AnimCell.AnimFrame(getContext(), "pic/hero2.png", 100));
        animClip.setLoop(true);
        AnimCell animCell = new AnimCell();
        animCell.setAnimClip(animClip, true);
        //设置元素大小
        animCell.setWidth(60);
        animCell.setHeight(80);
        //设置元素位置
        animCell.setX((getWidth() / 2) - (animCell.getWidthInDp() / 2));
        animCell.setY(getHeight() - animCell.getHeightInDp() * 2);
        //元素添加到帧
        layer.addCell(animCell);

        //元素添加到控制器
        getCellControl().addCell("player", animCell);

        //点击背景控制
        List<CellControl.CellProperty> players = getCellControl().getCellProperty("player");
        final CellControl.CellProperty player = players.get(0);
        getTouchControl().setOnTouchListener(new CellOnTouchListener() {
            @Override
            public void onTouch(BaseCell cell, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                        float x = event.getX();
                        float y = event.getY();
                        BaseCell playerCell = player.getCell();
                        playerCell.centerMoveToPx(x, y);
                        break;
                    case MotionEvent.ACTION_UP:
                        player.setYSpeed(0);
                        player.setXSpeed(0);
                        break;
                    default:
                        break;
                }
            }
        });

    }

    /**
     * 敌人动画
     */
    private void createEnemyAnim(BaseLayer layer) {
        //创建动画片段
        AnimCell.AnimClip animClip = new AnimCell.AnimClip();
        animClip.addFrame(new AnimCell.AnimFrame(getContext(), "pic/enemy3_n1.png", 200));
        animClip.addFrame(new AnimCell.AnimFrame(getContext(), "pic/enemy3_n2.png", 200));
        animClip.setLoop(true);
        //创建动画单元
        AnimCell animCell = new AnimCell();
        animCell.setAnimClip(animClip, true);
        animCell.setX((getWidth() / 2) - (animCell.getWidthInDp() / 2));
        layer.addCell(animCell);
    }

    /**
     * 文字
     */
    private void createText(BaseLayer layer) {
        TextCell textCell = new TextCell("飞机大战");
        textCell.setTextAlign(Paint.Align.CENTER);
        textCell.setTextSize(30);
        textCell.setX(getWidth() / 2);
        textCell.setY(getHeight() / 2);
        textCell.setZ(2000);
        textCell.setRotate(30);
        layer.addCell(textCell);
    }

    /**
     * 背景
     */
    private void createBackGround() {
        BaseLayer backGround = new BaseLayer();
        final ImageCell bg1 = new ImageCell(getContext(), "pic/background.png");
        float ratio = bg1.getHeightInPx() * 1f / bg1.getWidthInPx();

        bg1.setWidth(getWidth());
        bg1.setHeight((int) (getWidth() * ratio));
        backGround.addCell(bg1);

        final ImageCell bg2 = (ImageCell) bg1.clone();
        bg2.setY(-bg1.getHeightInDp());
        backGround.addCell(bg2);

        addLayer(backGround);
        getCellControl().addCell("bg1", bg1);
        getCellControl().addCell("bg2", bg2);

        CellControl.CellProperty bg11 = getCellControl().getCellProperty("bg1").get(0);
        CellControl.CellProperty bg22 = getCellControl().getCellProperty("bg2").get(0);

        bg11.setYSpeed(50);
        bg22.setYSpeed(50);

        CellEventListener cellEventListener = new CellEventListener() {
            @Override
            public void onCellMove(BaseCell cell, float lastX, float newX, float lastY, float newY, float lastRotation, float newRotation) {
                BaseCell low = bg1.getYInDp() < bg2.getYInDp() ? bg1 : bg2;
                if (newY > getHeight()) {
                    cell.setY(low.getYInDp() - cell.getHeightInDp());
                }
            }
        };

        bg11.setCellEventListener(cellEventListener);
        bg22.setCellEventListener(cellEventListener);

    }

}
