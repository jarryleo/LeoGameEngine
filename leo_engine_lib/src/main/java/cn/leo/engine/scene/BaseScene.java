package cn.leo.engine.scene;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import cn.leo.engine.LeoEngine;
import cn.leo.engine.control.CellControl;
import cn.leo.engine.control.TouchControl;
import cn.leo.engine.control.VoiceControl;
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
public abstract class BaseScene {
    /**
     * 上下文
     */
    private Context mContext;
    /**
     * 游戏引擎
     */
    private LeoEngine mEngine;
    /**
     * 场景控制器
     */
    private TouchControl mTouchControl;
    private VoiceControl mVoiceControl;
    private CellControl mCellControl;
    /**
     * 场景开始时间
     */
    private long mStartTime;
    /**
     * 图层集合
     */
    private List<BaseLayer> mLayers = new ArrayList<>();
    /**
     * 场景是否初始化
     */
    private boolean mHasInit = false;
    /**
     * 场景运行时间
     */
    private long mPassTimeMills;

    /**
     * 场景构造
     *
     * @param leoEngine 引擎
     */
    public BaseScene(LeoEngine leoEngine) {
        mEngine = leoEngine;
        mContext = leoEngine.getContext();
        //初始化默认控制器
        mCellControl = new CellControl();
        mTouchControl = new TouchControl(mContext);
        mVoiceControl = new VoiceControl(mContext, 20);
        mStartTime = System.currentTimeMillis();
    }

    /**
     * 初始化场景
     */
    public abstract void initScene();

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
        if (mCellControl != null) {
            mCellControl.onFrame();
        }
        if (mHasInit) {
            for (BaseLayer layer : mLayers) {
                layer.dispatchDraw(canvas);
            }
        } else {
            mHasInit = true;
            initScene();
        }
        //场景运行时间
        mPassTimeMills = System.currentTimeMillis() - mStartTime;
        onScenePassTime(mPassTimeMills);
        showFps(canvas);
    }

    private Paint mPaint = new Paint();
    private long mLastDrawTime;
    private long mLastShowFps;
    private int mFps;

    /**
     * Debug时候显示FPS
     */
    private void showFps(@NonNull Canvas canvas) {
        long l = System.currentTimeMillis() - mLastDrawTime;
        if (System.currentTimeMillis() - mLastShowFps > 1000) {
            mFps = (int) (1000 / l);
            mLastShowFps = System.currentTimeMillis();
        }
        canvas.drawText("FPS:" + mFps, 10, 20, mPaint);
        mLastDrawTime = System.currentTimeMillis();
    }

    /**
     * 子类如果想知道场景运行时间,重写此方法
     * 画面每执行一帧都会回调此方法
     *
     * @param passTimeMills 场景运行时间
     * @callsuper
     */
    protected void onScenePassTime(long passTimeMills) {

    }

    /**
     * 获取场景运行时长
     *
     * @return 场景运行时长, 毫秒
     */
    public long getPassTimeMills() {
        return mPassTimeMills;
    }

    public Context getContext() {
        return mContext;
    }

    /**
     * 场景开场
     */
    public void start() {
        mEngine.loadScene(this);
    }

    /**
     * 获取显示区域宽度 单位dp
     */
    public int getWidth() {
        return mEngine.getGameWindowWidthInDp();
    }

    /**
     * 获取显示区域高度 单位dp
     */
    public int getHeight() {
        return mEngine.getGameWindowHeightInDp();
    }

    /**
     * 设置触摸控制器
     *
     * @param touchControl 触摸控制器
     */
    public void setTouchControl(TouchControl touchControl) {
        mTouchControl = touchControl;
    }

    /**
     * 引擎获取触摸控制器
     */
    public TouchControl getTouchControl() {
        return mTouchControl;
    }

    /**
     * 设置声音控制器
     *
     * @param voiceControl 声音控制器
     */
    public void setVoiceControl(VoiceControl voiceControl) {
        mVoiceControl = voiceControl;
    }

    /**
     * 获取声音控制器
     */
    public VoiceControl getVoiceControl() {
        return mVoiceControl;
    }

    /**
     * 设置元素控制器
     *
     * @param cellControl 元素控制器
     */
    public void setCellControl(CellControl cellControl) {
        mCellControl = cellControl;
    }

    /**
     * 获取元素控制器
     */
    public CellControl getCellControl() {
        return mCellControl;
    }

    /**
     * 销毁场景,回收资源
     */
    public void onDestroy() {
        for (BaseLayer layer : mLayers) {
            layer.onDestroy();
        }
        clearLayer();
        if (getTouchControl() != null) {
            getTouchControl().onDestroy();
        }
        if (getVoiceControl() != null) {
            getVoiceControl().onDestroy();
        }
        if (getCellControl() != null) {
            getCellControl().onDestroy();
        }
    }
}
