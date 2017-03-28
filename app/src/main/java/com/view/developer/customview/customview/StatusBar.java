package com.view.developer.customview.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.view.developer.customview.R;

/**
 * Created by Developer on 2017/1/11.
 */

public class StatusBar extends View {

    private Paint mPaint;

    private float density;

    private int textSelectedColor;//文字选中的颜色
    private int textUnSelectedColor;//文字未选中的颜色

    private int pointSelectedColor;//点选中的颜色
    private int pointUnselectedColor;//点未选中的颜色

    private int lineColor;//横线颜色

    private int pointNumber;//点的数量

    private int width;//控件的宽

    private int pointRadius = 2;//圆点的半径
    private float lineHeight = 1;//线高
    private int margin = 15;//文字到点之间的距离
    private String[] info;//对应的信息
    private int padding = 50;//padding距离
    private int normalTextSize = 10;//正常状态下字体大小
    private int highlightTextSize = 12;//高亮状态下字体大小
    private int textHeight = 0;//文本高度
    private int currentStep = 1;//当前step
    private int dyOfText = 6;//文字偏移位置

    public StatusBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.statusBar);
        textSelectedColor = typedArray.getColor(R.styleable.statusBar_textSelectedColor, 0xffff725b);
        textUnSelectedColor = typedArray.getColor(R.styleable.statusBar_textUnselectedColor, 0xff8e8e8e);
        pointSelectedColor = typedArray.getColor(R.styleable.statusBar_pointSelectedColor, 0xffff725b);
        pointUnselectedColor = typedArray.getColor(R.styleable.statusBar_pointUnselectedColor, 0xff8e8e8e);
        lineColor = typedArray.getColor(R.styleable.statusBar_lineColor, 0xffe9e9e9);
        pointNumber = typedArray.getInteger(R.styleable.statusBar_pointNumber, 2);
        typedArray.recycle();
        density = context.getResources().getDisplayMetrics().density;
        pointRadius = (int) (pointRadius * density);
        lineHeight = (int) (lineHeight * density);
        margin = (int) (margin * density);
        padding = (int) (padding * density);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextAlign(Paint.Align.LEFT);
        mPaint.setTextSize(highlightTextSize * density);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (null != info && info.length > 0) {
            StringBuilder stringBuilder = new StringBuilder("");
            for (String str : info) {
                stringBuilder.append(str);
            }
            Rect rect = new Rect();
            mPaint.getTextBounds(stringBuilder.toString(), 0, stringBuilder.length(), rect);
            textHeight = rect.height();
        }
        setMeasuredDimension(widthMeasureSpec, textHeight + dyOfText + margin + pointRadius * 2);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = this.getMeasuredWidth() - padding;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawLine(canvas, pointRadius, pointRadius + margin + textHeight, width, pointRadius + margin + textHeight);
        for (int i = 0; i < pointNumber; i++) {
            if (i == 0) {
                drawPoint(canvas, pointRadius, pointRadius + margin + textHeight, i);
                if (null != info[i] && i < info.length) {
                    drawText(canvas, pointRadius, textHeight + dyOfText, info[i], i);
                }
                continue;
            }
            if (i + 1 == pointNumber) {
                drawPoint(canvas, width - pointRadius, pointRadius + margin + textHeight, i);
                if (null != info[i] && i < info.length) {
                    drawText(canvas, width - pointRadius, textHeight + dyOfText, info[i], i);
                }
                continue;
            }
            int tempPointNumber = pointNumber - 2;
            if (tempPointNumber > 0) {
                int tempWidth = width / (tempPointNumber + 1);
                int cx = i * tempWidth;
                drawPoint(canvas, cx, pointRadius + margin + textHeight, i);
                if (null != info[i] && i < info.length) {
                    drawText(canvas, cx, textHeight + dyOfText, info[i], i);
                }
            }

        }
    }

    /**
     * 绘制文字
     *
     * @param canvas
     * @param x
     * @param y
     * @param text
     */
    private void drawText(Canvas canvas, int x, int y, String text, int i) {
        float textWidth;
        if (i + 1 == currentStep) {
            mPaint.setTextSize(highlightTextSize * density);
            textWidth = mPaint.measureText(info[i]);
            mPaint.setColor(textSelectedColor);
        } else {
            mPaint.setTextSize(normalTextSize * density);
            textWidth = mPaint.measureText(info[i]);
            mPaint.setColor(textUnSelectedColor);
        }
        canvas.drawText(text, x - textWidth / 2 + padding / 2, y, mPaint);
    }

    /**
     * 画点
     *
     * @param canvas
     * @param cx
     * @param cy
     */
    private void drawPoint(Canvas canvas, int cx, int cy, int i) {
        i = i + 1;
        if (i == currentStep) {
            mPaint.setColor(pointSelectedColor);
        } else {
            mPaint.setColor(pointUnselectedColor);
        }
        canvas.drawCircle(cx + padding / 2, cy, pointRadius, mPaint);
    }

    /**
     * 画线
     *
     * @param canvas
     * @param startX
     * @param startY
     * @param endX
     * @param endY
     */
    private void drawLine(Canvas canvas, int startX, int startY, int endX, int endY) {
        mPaint.setColor(lineColor);
        mPaint.setStrokeWidth(lineHeight);
        canvas.drawLine(startX + padding / 2, startY, endX + padding / 2, endY, mPaint);
    }

    /**
     * 设置显示的文本信息
     *
     * @param info
     */
    public void setInfo(String[] info) {
        this.info = info;
        pointNumber = this.info.length;
        currentStep = 1;
        this.postInvalidate();
    }

    /**
     * 设置阶段
     *
     * @param step
     */
    public void setStep(int step) {
        if (currentStep == step) {
            return;
        }
        currentStep = step;
        this.postInvalidate();
    }

    /**
     * 设置point数量
     *
     * @param pointNumber
     */
    public void setPointNumber(int pointNumber) {
        this.pointNumber = pointNumber;
        this.postInvalidate();
    }
}
