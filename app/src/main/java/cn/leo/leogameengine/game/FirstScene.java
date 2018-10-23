package cn.leo.leogameengine.game;

import android.graphics.Paint;

import cn.leo.engine.LeoEngine;
import cn.leo.engine.cell.AnimCell;
import cn.leo.engine.cell.ImageCell;
import cn.leo.engine.cell.TextCell;
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
        //背景层
        BaseLayer backGround = new BaseLayer();
        ImageCell bg = new ImageCell(getContext(), "pic/background.png");
        bg.setWidth(getWidth());
        bg.setHeight(getHeight());
        backGround.addCell(bg);
        addLayer(backGround);

        //创建帧
        BaseLayer layer = new BaseLayer();

        //创建图片元素
        ImageCell cell = new ImageCell(getContext(), "pic/hero1.png");
        //设置元素大小
        cell.setWidth(60);
        cell.setHeight(60);
        //设置元素位置
        cell.setX((getWidth() / 2) - (cell.getWidthInDp() / 2));
        cell.setY(getHeight() - cell.getHeightInDp() * 2);
        //元素添加到帧
        layer.addCell(cell);

        //添加文字
        TextCell textCell = new TextCell("飞机大战");
        textCell.setTextAlign(Paint.Align.CENTER);
        textCell.setTextSize(30);
        textCell.setX(getWidth() / 2);
        textCell.setY(getHeight() / 2);
        textCell.setZ(2000);
        layer.addCell(textCell);

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
        //帧添加到场景
        addLayer(layer);
    }

}
