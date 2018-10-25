package cn.leo.engine.cell;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;

import cn.leo.engine.common.AssetsUtil;
import cn.leo.engine.scene.BaseScene;
import cn.leo.engine.screen.ScreenUtil;

/**
 * @author : Jarry Leo
 * @date : 2018/10/18 14:29
 * 图片元素
 */
public class ImageCell extends BaseCell<ImageCell> {
    /**
     * bitmap图像
     */
    private Bitmap mBitmap;


    private Rect mSource;
    private Rect mTarget;


    public static ImageCell create(Bitmap bitmap) {
        return new ImageCell(bitmap);
    }

    public ImageCell(Bitmap bitmap) {
        mBitmap = bitmap;
    }

    public static ImageCell create(BaseScene baseScene, String assetsPicFileName) {
        return new ImageCell(baseScene, assetsPicFileName);
    }

    /**
     * 从资源文件夹加载图片
     *
     * @param baseScene         场景
     * @param assetsPicFileName 文件名
     */
    public ImageCell(BaseScene baseScene, String assetsPicFileName) {
        Bitmap bitmapFromAsset = AssetsUtil.getBitmapFromAsset(baseScene.getContext(), assetsPicFileName);
        if (bitmapFromAsset == null) {
            throw new IllegalArgumentException("\"" + assetsPicFileName + "\" are not exist in assets folder");
        }
        setBitmap(bitmapFromAsset);
    }

    @Override
    protected Paint initPaint() {
        return new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        if (mBitmap == null) {
            return;
        }
        canvas.save();
        if (getWidth() == 0 || getHeight() == 0) {
            canvas.rotate(getRotate(), getXInPx() + getWidthInPx() / 2, getYInPx() + getHeightInPx() / 2);
            canvas.drawBitmap(mBitmap, getXInPx(), getYInPx(), getPaint());
        } else {
            canvas.translate(getXInPx(), getYInPx());
            canvas.rotate(getRotate());
            canvas.drawBitmap(mBitmap, mSource, mTarget, getPaint());
        }
        canvas.restore();
    }

    @Override
    public ImageCell setWidth(int width) {
        super.setWidth(width);
        mTarget = new Rect(0, 0, getWidthInPx(), getHeightInPx());
        return this;
    }


    @Override
    public ImageCell setHeight(int height) {
        super.setHeight(height);
        mTarget = new Rect(0, 0, getWidthInPx(), getHeightInPx());
        return this;
    }

    /**
     * 设置宽度,是否等比设置
     *
     * @param width      宽度
     * @param equalRatio 是否等比
     */
    public ImageCell setWidth(int width, boolean equalRatio) {
        if (equalRatio) {
            float ratio = getHeightInPx() * 1f / getWidthInPx();
            setHeight((int) (width * ratio));
        }
        setWidth(width);
        return this;
    }

    /**
     * 设置高度,是否等比设置
     *
     * @param height     高度
     * @param equalRatio 是否等比
     */
    public ImageCell setHeight(int height, boolean equalRatio) {
        if (equalRatio) {
            float ratio = getHeightInPx() * 1f / getWidthInPx();
            setWidth((int) (height / ratio));
        }
        setHeight(height);
        return this;
    }

    @Override
    public int getCellType() {
        return TYPE_IMAGE;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public ImageCell setBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
        mSource = new Rect(0, 0, mBitmap.getWidth(), mBitmap.getHeight());
        if (getWidth() == 0) {
            setWidth(ScreenUtil.px2dp(bitmap.getWidth()));
        }
        if (getHeight() == 0) {
            setHeight(ScreenUtil.px2dp(bitmap.getHeight()));
        }
        return this;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mBitmap.recycle();
        mBitmap = null;
    }
}
