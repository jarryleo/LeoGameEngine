package cn.leo.leogameengine.game;

import android.content.Context;

import cn.leo.engine.cell.ImageCell;
import cn.leo.engine.layer.BaseLayer;
import cn.leo.engine.scene.BaseScene;

/**
 * @author : Jarry Leo
 * @date : 2018/10/22 17:32
 */
public class FirstScene extends BaseScene {
    /**
     * 上下文构造
     *
     * @param context 上下文
     */
    public FirstScene(Context context) {
        super(context);
        initResource();
    }

    private void initResource() {
        //创建帧
        BaseLayer layer = new BaseLayer();
        //创建元素
        ImageCell cell = new ImageCell(null);
        //元素添加到帧
        layer.addCell(cell);
        //帧添加到场景
        addLayer(layer);
    }


}
