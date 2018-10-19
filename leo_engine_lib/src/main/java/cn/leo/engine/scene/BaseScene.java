package cn.leo.engine.scene;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import cn.leo.engine.layer.BaseLayer;

/**
 * @author : Jarry Leo
 * @date : 2018/10/18 14:48
 * <p>
 * 场景类,一个场景有多个图层,一个图层多个元素;
 * 场景还包含声音,和操作控制;
 * 一个关卡可以看做一个场景;
 * 引擎对场景负责,场景载入引擎后,
 * 引擎负责加载声音和画面,并接受场景的控制器操控;
 * 场景可销毁!
 */
public class BaseScene {
    private Context mContext;
    private List<BaseLayer> mLayers = new ArrayList<>();

    public BaseScene(Context context) {
        mContext = context;
    }

    public void addLayer(BaseLayer layer) {
        mLayers.add(layer);
    }

    public void removeLayer(BaseLayer layer) {
        mLayers.remove(layer);
    }

    public void clearLayer() {
        mLayers.clear();
    }

    public void dispatchDraw(@NonNull Canvas canvas) {
        for (BaseLayer layer : mLayers) {
            layer.dispatchDraw(canvas);
        }
    }
}
