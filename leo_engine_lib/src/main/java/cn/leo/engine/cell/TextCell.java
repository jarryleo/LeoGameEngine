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
    private int mLineOfChars = 200;
    /**
     * 文字大小
     */
    private int mTextSize = 16;
    /**
     * 文字颜色
     */
    private int mTextColor = Color.WHITE;

    @Override
    protected Paint initPaint() {
        TextPaint textPaint = new TextPaint();
        textPaint.setShadowLayer(1, 1, 1, Color.BLACK);
        return textPaint;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        StaticLayout layout = new StaticLayout(
                mText,
                (TextPaint) getPaint(),
                mLineOfChars,
                Layout.Alignment.ALIGN_NORMAL,
                1.0F,
                0.0F,
                true);
        canvas.save();
        canvas.translate(getX(), getY());
        layout.draw(canvas);
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
    }

    public int getLineOfChars() {
        return mLineOfChars;
    }

    public void setLineOfChars(int lineOfChars) {
        mLineOfChars = lineOfChars;
    }

    public int getTextSize() {
        return mTextSize;
    }

    public void setTextSize(int textSize) {
        mTextSize = ScreenUtil.dp2px(textSize);
    }

    public int getTextColor() {
        return mTextColor;
    }

    public void setTextColor(int textColor) {
        mTextColor = textColor;
    }
}
