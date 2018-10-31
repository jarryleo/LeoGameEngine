package cn.leo.engine.cell;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;

import cn.leo.engine.common.AssetsUtil;
import cn.leo.engine.scene.Scene;
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

    public static ImageCell create(Bitmap bitmap) {
        return new ImageCell(bitmap);
    }

    private ImageCell(Bitmap bitmap) {
        mBitmap = bitmap;
    }

    public static ImageCell create(Scene scene, String assetsPicFileName) {
        return new ImageCell(scene, assetsPicFileName);
    }

    /**
     * 从资源文件夹加载图片
     *
     * @param scene             场景
     * @param assetsPicFileName 文件名
     */
    private ImageCell(Scene scene, String assetsPicFileName) {
        Bitmap bitmapFromAsset = AssetsUtil.getBitmapFromAsset(scene.getContext(), assetsPicFileName);
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
        canvas.drawBitmap(mBitmap, 0, 0, getPaint());
    }

    @Override
    public ImageCell setWidth(int width) {
        super.setWidth(width);
        setScaleX(getWidthInPx() * 1f / mBitmap.getWidth());
        return this;
    }


    @Override
    public ImageCell setHeight(int height) {
        super.setHeight(height);
        setScaleY(getHeightInPx() * 1f / mBitmap.getHeight());
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
