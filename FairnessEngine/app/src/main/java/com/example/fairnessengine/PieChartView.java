package com.example.fairnessengine;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import java.util.ArrayList;
import java.util.List;

public class PieChartView extends View {
    private Paint paint;
    private RectF rectF;
    private List<Float> values = new ArrayList<>();
    private List<Integer> colors = new ArrayList<>();
    private String centerText = "";

    public PieChartView(Context context) {
        super(context);
        init();
    }

    public PieChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        rectF = new RectF();
    }

    public void setData(List<Float> values, List<Integer> colors) {
        this.values = values;
        this.colors = colors;
        invalidate();
    }

    public void setCenterText(String text) {
        this.centerText = text;
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        float padding = 16f;
        float min = Math.min(w, h) - padding * 2;
        float cx = w / 2f;
        float cy = h / 2f;
        rectF.set(cx - min / 2, cy - min / 2, cx + min / 2, cy + min / 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (values.isEmpty()) {
            paint.setColor(Color.DKGRAY);
            canvas.drawArc(rectF, 0, 360, true, paint);
            return;
        }

        float total = 0;
        for (float v : values) total += v;

        if (total == 0) {
            paint.setColor(Color.DKGRAY);
            canvas.drawArc(rectF, 0, 360, true, paint);
            return;
        }

        float currentAngle = -90f;
        for (int i = 0; i < values.size(); i++) {
            float sweepAngle = (values.get(i) / total) * 360f;
            paint.setColor(colors.get(i));
            canvas.drawArc(rectF, currentAngle, sweepAngle, true, paint);
            currentAngle += sweepAngle;
        }
        
        // Draw inner circle for donut shape
        paint.setColor(Color.parseColor("#2B222A")); // surface_color
        float cx = getWidth() / 2f;
        float cy = getHeight() / 2f;
        float innerRadius = (rectF.width() / 2f) * 0.6f;
        canvas.drawCircle(cx, cy, innerRadius, paint);

        if (centerText != null && !centerText.isEmpty()) {
            paint.setColor(Color.WHITE);
            paint.setTextSize(48f);
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setFakeBoldText(true);
            float textY = cy - ((paint.descent() + paint.ascent()) / 2f);
            canvas.drawText(centerText, cx, textY, paint);
        }
    }
}
