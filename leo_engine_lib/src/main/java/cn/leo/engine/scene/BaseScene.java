package cn.leo.engine.scene;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import cn.leo.engine.control.BaseControl;
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
    /**
     * 上下文
     */
    private Context mContext;
    /**
     * 场景控制器
     */
    private BaseControl mControl;

    /**
     * 图层集合
     */
    private List<BaseLayer> mLayers = new ArrayList<>();

    /**
     * 上下文构造
     * @param context 上下文
     */
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

    /**
     * 设置场景控制器
     * @param control 控制器
     */
    public void setControl(BaseControl control) {
        mControl = control;
    }

    /**
     * 引擎获取场景控制器
     */
    public BaseControl getControl() {
        return mControl;
    }
}
