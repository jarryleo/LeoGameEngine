package cn.leo.leogameengine.game;

import cn.leo.engine.LeoEngine;
import cn.leo.engine.cell.ImageCell;
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
        //创建帧
        BaseLayer layer = new BaseLayer();
        //创建元素
        ImageCell cell = new ImageCell(getContext(), "pic/hero1.png");
        cell.setWidth(60);
        cell.setHeight(60);
        cell.setX(getWidth() / 2 - 30);
        cell.setY(getHeight() / 2 - 30);
        //元素添加到帧
        layer.addCell(cell);
        //帧添加到场景
        addLayer(layer);
    }

}
