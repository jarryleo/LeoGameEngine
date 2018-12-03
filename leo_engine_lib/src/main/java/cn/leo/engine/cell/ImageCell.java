package cn.leo.engine.cell;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Picture;
import android.support.annotation.NonNull;

import cn.leo.engine.common.PicturePool;
import cn.leo.engine.scene.Scene;
import cn.leo.engine.screen.ScreenUtil;

/**
 * @author : Jarry Leo
 * @date : 2018/10/18 14:29
 * 图片元素
 */
public class ImageCell extends BaseCell<ImageCell> {
    /**
     * 上下文
     */
    private Context mContext;
    private String mFileName;
    private int mPicWidth;
    private int mPicHeight;


    public static ImageCell create(Scene scene, String assetsPicFileName) {
        return new ImageCell(scene, assetsPicFileName);
    }

    public void setImage(String assetsPicFileName) {
        mFileName = assetsPicFileName;
        PicturePool.put(mContext, mFileName, getPaint());
        Picture picture = PicturePool.getPicture(mFileName);
        mPicWidth = picture.getWidth();
        mPicHeight = picture.getHeight();
        setSize();
    }


    /**
     * 从资源文件夹加载图片
     *
     * @param scene             场景
     * @param assetsPicFileName 文件名
     */
    private ImageCell(Scene scene, String assetsPicFileName) {
        mContext = scene.getContext();
        setImage(assetsPicFileName);
    }

    @Override
    protected Paint initPaint() {
        return new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        canvas.drawPicture(PicturePool.getPicture(mFileName));
    }

    @Override
    public ImageCell setWidth(int width) {
        super.setWidth(width);
        setScaleX(getWidthInPx() * 1f / mPicWidth);
        return this;
    }


    @Override
    public ImageCell setHeight(int height) {
        super.setHeight(height);
        setScaleY(getHeightInPx() * 1f / mPicHeight);
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


    private void setSize() {
        if (getWidth() == 0) {
            setWidth(ScreenUtil.px2dp(mPicWidth));
        }
        if (getHeight() == 0) {
            setHeight(ScreenUtil.px2dp(mPicHeight));
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        PicturePool.destroy(mFileName);
    }
}
