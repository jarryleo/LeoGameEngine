package cn.leo.engine.cell;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

import cn.leo.engine.screen.ScreenUtil;

/**
 * @author : Jarry Leo
 * @date : 2018/10/18 15:46
 * 文字元素
 */
public class TextCell extends BaseCell {
    /**
     * 要绘制的文字内容
     */
    private String mText;
    /**
     * 每行多少个字,用于换行
     */
    private int mLineOfChars = 50;
    /**
     * 文字大小
     */
    private int mTextSize = ScreenUtil.dp2px(16);
    /**
     * 文字颜色
     */
    private int mTextColor = Color.WHITE;
    /**
     * 文字对象
     */
    private StaticLayout mLayout;
    /**
     * 属性是否变化,不变化绘制相同对象,防止每次绘制都创建文字对象
     */
    private boolean mChanged = true;

    public TextCell() {
    }

    public TextCell(String text) {
        mText = text;
    }

    @Override
    protected Paint initPaint() {
        TextPaint textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(mTextColor);
        textPaint.setTextSize(mTextSize);
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setShadowLayer(1, 1, 1, Color.BLACK);
        return textPaint;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        if (mChanged || mLayout == null) {
            mChanged = false;
            mLayout = new StaticLayout(
                    mText,
                    (TextPaint) getPaint(),
                    mLineOfChars,
                    Layout.Alignment.ALIGN_NORMAL,
                    1.0F,
                    0.0F,
                    true);
        }
        canvas.save();
        canvas.translate(getX(), getY());
        mLayout.draw(canvas);
        canvas.restore();
    }

    @Override
    public int getCellType() {
        return TYPE_STRING;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
        mChanged = true;
    }

    public int getLineOfChars() {
        return mLineOfChars;
    }

    public void setLineOfChars(int lineOfChars) {
        mLineOfChars = lineOfChars;
        mChanged = true;
    }

    public int getTextSize() {
        return mTextSize;
    }

    public void setTextSize(int textSize) {
        mTextSize = ScreenUtil.dp2px(textSize);
        getPaint().setTextSize(mTextSize);
        mChanged = true;
    }

    public int getTextColor() {
        return mTextColor;
    }

    public void setTextColor(int textColor) {
        mTextColor = textColor;
        getPaint().setColor(mTextSize);
        mChanged = true;
    }

    /**
     * 设置字体对齐方式
     *
     * @param align 对齐方式
     */

    public void setTextAlign(Paint.Align align) {
        getPaint().setTextAlign(align);
        mChanged = true;
    }
}
