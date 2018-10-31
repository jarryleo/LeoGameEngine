package cn.leo.engine.scene;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import cn.leo.engine.LeoEngine;
import cn.leo.engine.cell.BaseCell;
import cn.leo.engine.common.SystemTime;
import cn.leo.engine.control.CellControl;
import cn.leo.engine.control.CellControlImpl;
import cn.leo.engine.control.CellProperty;
import cn.leo.engine.control.Scheduler;
import cn.leo.engine.control.TimerControl;
import cn.leo.engine.control.TimerControlImpl;
import cn.leo.engine.control.TouchControl;
import cn.leo.engine.control.TouchControlImpl;
import cn.leo.engine.control.VoiceControl;
import cn.leo.engine.control.VoiceControlImpl;
import cn.leo.engine.layer.Layer;
import cn.leo.engine.listener.CellEventListener;
import cn.leo.engine.listener.CellOnClickListener;
import cn.leo.engine.listener.CellOnTouchListener;
import cn.leo.engine.path.BasePath;
import cn.leo.engine.screen.ScreenUtil;

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
public abstract class Scene implements CellControl, TimerControl, TouchControl, VoiceControl {
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
    private TimerControl mTimerControl;

    /**
     * 场景开始时间
     */
    private long mStartTime;
    /**
     * 图层集合
     */
    private List<Layer> mLayers = new ArrayList<>();
    /**
     * 场景运行时间
     */
    private long mPassTimeMills;
    /**
     * 场景是否初始化
     */
    private boolean mHasInit = false;

    /**
     * 场景构造
     *
     * @param leoEngine 引擎
     */
    public Scene(LeoEngine leoEngine) {
        mEngine = leoEngine;
        mContext = leoEngine.getContext();
        //初始化默认控制器
        mCellControl = new CellControlImpl();
        mTimerControl = new TimerControlImpl();
        mTouchControl = new TouchControlImpl(mContext);
        mVoiceControl = new VoiceControlImpl(mContext);
        mStartTime = SystemTime.now();
    }

    public void addLayer(Layer layer) {
        mLayers.add(layer);
    }

    public void removeLayer(Layer layer) {
        mLayers.remove(layer);
    }

    public void clearLayer() {
        mLayers.clear();
    }

    @Override
    public void onFrame() {

    }

    /**
     * @hide
     */
    public void dispatchDraw(@NonNull Canvas canvas) {
        SystemTime.setTime();
        onFrame();
        if (mCellControl != null) {
            mCellControl.onFrame();
        }
        if (mTimerControl != null) {
            mTimerControl.onFrame();
        }
        if (mHasInit) {
            for (Layer layer : mLayers) {
                layer.dispatchDraw(canvas);
            }
        } else {
            mHasInit = true;
            initScene();
        }
        //场景运行时间
        mPassTimeMills = SystemTime.now() - mStartTime;
        mFPS.showFps(canvas);
    }

    /**
     * 初始化场景
     */
    protected abstract void initScene();

    private Fps mFPS = new Fps();

    /**
     * @hide
     */
    public TouchControlImpl getTouchControl() {
        return (TouchControlImpl) mTouchControl;
    }


    private class Fps {
        private Paint mPaint;
        private long mLastDrawTime;
        private long mLastShowFps;
        private int mFps;

        Fps() {
            mPaint = new Paint();
            mPaint.setTextSize(ScreenUtil.dp2px(16));
        }

        /**
         * Debug时候显示FPS
         */
        private void showFps(@NonNull Canvas canvas) {
            long l = SystemTime.now() - mLastDrawTime;
            if (SystemTime.now() - mLastShowFps > 1000) {
                mFps = (int) (1000 / l);
                mLastShowFps = SystemTime.now();
            }
            canvas.drawText("FPS:" + mFps, 10, 20, mPaint);
            mLastDrawTime = SystemTime.now();
        }
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
     * 销毁场景,回收资源
     */
    @Override
    public void onDestroy() {
        for (Layer layer : mLayers) {
            layer.onDestroy();
        }
        clearLayer();
        mCellControl.onDestroy();
        mTimerControl.onDestroy();
        mVoiceControl.onDestroy();
        mTouchControl.onDestroy();
    }


    /**
     * 以下元素控制器代理方法
     */

    @Override
    public CellProperty addCellToControl(String cellName, BaseCell cell) {
        return mCellControl.addCellToControl(cellName, cell);
    }

    @Override
    public void removeCellFromControl(String cellName, BaseCell cell) {
        mCellControl.removeCellFromControl(cellName, cell);
    }

    @Override
    public void setCellEventListener(String cellName, CellEventListener cellEventListener) {
        mCellControl.setCellEventListener(cellName, cellEventListener);
    }

    @Override
    public void setCellXSpeed(String cellName, float xSpeed) {
        mCellControl.setCellXSpeed(cellName, xSpeed);
    }

    @Override
    public void setCellYSpeed(String cellName, float ySpeed) {
        mCellControl.setCellYSpeed(cellName, ySpeed);
    }

    @Override
    public void setCellPath(String cellName, BasePath path) {
        mCellControl.setCellPath(cellName, path);
    }

    @Override
    public List<CellProperty> getCellProperty(String cellName) {
        return mCellControl.getCellProperty(cellName);
    }

    /**
     * 以下时间控制器方法代理
     */
    @Override
    public void subscribe(Scheduler scheduler, long interval, int repeat, long delay) {
        mTimerControl.subscribe(scheduler, interval, repeat, delay);
    }

    @Override
    public void subscribeOnce(Scheduler scheduler, long delay) {
        mTimerControl.subscribeOnce(scheduler, delay);
    }

    @Override
    public void unsubscribe(Scheduler scheduler) {
        mTimerControl.unsubscribe(scheduler);
    }

    /**
     * 以下触摸事件控制器代理方法
     */
    @Override
    public void setCellOnClick(BaseCell cell, CellOnClickListener onClickListener) {
        mTouchControl.setCellOnClick(cell, onClickListener);
    }

    @Override
    public void setCellOnTouch(BaseCell cell, CellOnTouchListener onTouchListener) {
        mTouchControl.setCellOnTouch(cell, onTouchListener);
    }

    @Override
    public void setOnTouchListener(CellOnTouchListener onTouchListener) {
        mTouchControl.setOnTouchListener(onTouchListener);
    }

    /**
     * 以下声音控制器代理方法
     */
    @Override
    public void loadSounds(int... rsId) {
        mVoiceControl.loadSounds(rsId);
    }

    @Override
    public void playSound(int rsID, boolean loop) {
        mVoiceControl.playSound(rsID, loop);
    }

    @Override
    public void soundOff() {
        mVoiceControl.soundOff();
    }

    @Override
    public void soundOn() {
        mVoiceControl.soundOn();
    }

    @Override
    public void stopSound(int rsID) {
        mVoiceControl.stopSound(rsID);
    }

    @Override
    public void pauseSound(int rsID) {
        mVoiceControl.pauseSound(rsID);
    }

    @Override
    public void resumeSound(int rsID) {
        mVoiceControl.resumeSound(rsID);
    }
}
