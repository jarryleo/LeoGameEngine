package cn.leo.engine.cell;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

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
     * 每行多少个字
     */
    private int mLineOfChars = 200;
    /**
     * 文字大小
     */
    private int mTextSize;
    /**
     * 文字颜色
     */
    private int mTextColor;

    @Override
    protected Paint initPaint() {
        return new TextPaint();
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
}
