package cn.leo.engine;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import cn.leo.engine.control.TouchControlImpl;
import cn.leo.engine.scene.Scene;
import cn.leo.engine.screen.ScreenAdapter;
import cn.leo.engine.screen.ScreenUtil;

/**
 * @author : Jarry Leo
 * @date : 2018/10/18 13:33
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class LeoEngine extends SurfaceView implements Runnable {

    private SurfaceHolder mHolder;
    private Context mContext;
    /**
     * 游戏窗口可见状态
     */
    private volatile boolean mGameWindowIsVisible;
    /**
     * 游戏窗口宽度,单位dp
     */
    private int mGameWindowWidthInDp;
    /**
     * 游戏窗口高度,单位dp
     */
    private int mGameWindowHeightInDp;
    /**
     * 引擎执行场景
     */
    private Scene mScene;

    public LeoEngine(Context context) {
        this(context, null);
    }

    public LeoEngine(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LeoEngine(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        //拿取父类holder
        mHolder = getHolder();
        //本类上下文
        mContext = getContext();
        //本引擎view置顶显示
        setZOrderOnTop(true);
        //设置本类回调
        mHolder.addCallback(mCallBack);
        //屏幕适配
        ScreenAdapter.adaptScreen((Activity) mContext, 0);
        //创建引擎线程
        Thread engineThread = new Thread(this, "EngineThread");
        engineThread.start();
    }


    /**
     * 本类holderBack回调
     */
    final SurfaceHolder.Callback mCallBack = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            //窗口可见
            mGameWindowIsVisible = true;
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            //窗口改变状态
            //游戏可见区域宽
            mGameWindowWidthInDp = ScreenUtil.px2dp(width);
            //游戏可见区域高
            mGameWindowHeightInDp = ScreenUtil.px2dp(height);
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            //窗口不可见
            mGameWindowIsVisible = false;
        }
    };

    /**
     * 加载场景
     *
     * @param scene 场景
     */
    public void loadScene(Scene scene) {
        if (mScene != null) {
            mScene.onDestroy();
        }
        mScene = scene;
        drawStep();
        //清理上一次场景缓存
        System.gc();
    }

    /**
     * 引擎循环
     */
    private void loop() {
        if (mGameWindowIsVisible) {
            drawStep();
        }
    }

    @Override
    public void run() {
        for (; ; ) {
            SystemClock.sleep(10);
            loop();
        }
    }

    /**
     * 绘制一帧图像
     */
    public void drawStep() {
        core();
    }

    /**
     * 图像绘制
     */
    private void core() {
        if (mGameWindowIsVisible) {
            Canvas canvas = mHolder.lockCanvas();
            drawFrame(canvas);
            if (canvas != null) {
                mHolder.unlockCanvasAndPost(canvas);
            }
        }
    }

    /**
     * 绘制场景
     *
     * @param canvas 画布
     */
    private void drawFrame(Canvas canvas) {
        if (canvas == null) {
            return;
        }
        //清空画布
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        //绘制场景
        if (mScene != null) {
            mScene.dispatchDraw(canvas);
        }
    }

    /**
     * 获取游戏区域宽度值dp
     */
    public int getGameWindowWidthInDp() {
        return mGameWindowWidthInDp;
    }

    /**
     * 获取游戏区域高度值dp
     */
    public int getGameWindowHeightInDp() {
        return mGameWindowHeightInDp;
    }

    /**
     * 触摸事件交给场景控制器
     *
     * @param event 事件
     * @return true 消费事件
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        TouchControlImpl touchControl = mScene.getTouchControl();
        if (touchControl != null) {
            touchControl.onTouchEvent(event);
        }
        return true;
    }

    /**
     * 设置全屏
     */
    public LeoEngine setFullScreen() {
        ((Activity) mContext).getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        return this;
    }

    /**
     * 设置竖屏
     */
    public LeoEngine setPortrait() {
        ((Activity) mContext).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        return this;
    }

    /**
     * 设置横屏
     */
    public LeoEngine setLandscape() {
        ((Activity) mContext).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        return this;
    }


}
