package cn.leo.engine;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import cn.leo.engine.scene.BaseScene;
import cn.leo.engine.screen.ScreenAdapter;
import cn.leo.engine.screen.ScreenUtil;

/**
 * @author : Jarry Leo
 * @date : 2018/10/18 13:33
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class LeoEngine extends SurfaceView {

    private SurfaceHolder mHolder;
    private Context mContext;
    //游戏窗口可见状态
    private boolean mGameWindowIsVisiable;
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
    private BaseScene mScene;


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
    }


    /**
     * 本类holderBack回调
     */
    final SurfaceHolder.Callback mCallBack = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            //窗口可见
            mGameWindowIsVisiable = true;
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
            mGameWindowIsVisiable = false;
        }
    };

    /**
     * 加载场景
     *
     * @param scene 场景
     */
    public void loadScene(BaseScene scene) {
        mScene = scene;
        drawStep();
    }

    public void drawStep() {
        core();
    }

    private void core() {
        Canvas canvas = mHolder.lockCanvas();
        drawFrame(canvas);
        mHolder.unlockCanvasAndPost(canvas);
    }

    private void drawFrame(Canvas canvas) {
        //清空画布
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        //绘制场景
        if (mScene != null) {
            mScene.dispatchDraw(canvas);
        }
    }

    public int getGameWindowWidthInDp() {
        return mGameWindowWidthInDp;
    }

    public int getGameWindowHeightInDp() {
        return mGameWindowHeightInDp;
    }
}
