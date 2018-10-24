package cn.leo.leogameengine.game;

import android.graphics.Paint;

import java.util.List;

import cn.leo.engine.LeoEngine;
import cn.leo.engine.cell.AnimCell;
import cn.leo.engine.cell.ImageCell;
import cn.leo.engine.cell.TextCell;
import cn.leo.engine.control.CellControl;
import cn.leo.engine.layer.BaseLayer;
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
        //设置元素控制器
        setCellControl(new CellControl());
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
        animCell.setHeight(60);
        //设置元素位置
        animCell.setX((getWidth() / 2) - (animCell.getWidthInDp() / 2));
        animCell.setY(getHeight() - animCell.getHeightInDp() * 2);
        //元素添加到帧
        layer.addCell(animCell);
        //元素克隆
        AnimCell clone = (AnimCell) animCell.clone();
        layer.addCell(clone);
        clone.setX(20);
        clone.setRotate(-30);
        //控制元素移动
        getCellControl().addCell("player", animCell);
        List<CellControl.CellProperty> player = getCellControl().getCellProperty("player");
        player.get(0).setYSpeed(-100);
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
        animCell.setRotate(60);
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
        ImageCell bg = new ImageCell(getContext(), "pic/background.png");
        bg.setWidth(getWidth());
        bg.setHeight(getHeight());
        backGround.addCell(bg);
        addLayer(backGround);
    }

}
