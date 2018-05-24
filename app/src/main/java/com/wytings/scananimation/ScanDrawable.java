package com.wytings.scananimation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.TypedValue;


/**
 * @author Rex Wei
 * Created at 2018/5/24 17:27
 */

public class ScanDrawable extends Drawable implements Animatable {

    private final RectF rectF = new RectF();
    private final Path path = new Path();
    private final Paint paint;
    private final Bitmap scanShape;
    private final float overPadding;
    private final float strokeWidth;

    private float heightOffset = 0;
    private boolean isRunning = true;

    public ScanDrawable(Context context, int overPaddingDp) {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        scanShape = BitmapFactory.decodeResource(context.getResources(), R.drawable.scan_shape);

        overPadding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, overPaddingDp, context.getResources().getDisplayMetrics());
        strokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, context.getResources().getDisplayMetrics());
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        final long start = System.currentTimeMillis();
        final int width = canvas.getWidth();
        final int height = canvas.getHeight();
        final float length = overPadding;

        paint.setStrokeWidth(0);
        paint.setColor(Color.RED);
        rectF.set(0, 0, width, height);
        rectF.inset(overPadding, overPadding);
        canvas.drawRect(rectF, paint);

        path.reset();
        paint.setStrokeWidth(strokeWidth);

        path.moveTo(rectF.left, rectF.top + length);
        path.lineTo(rectF.left, rectF.top);
        path.lineTo(rectF.left + length, rectF.top);
        canvas.drawPath(path, paint);

        path.moveTo(rectF.right - length, rectF.top);
        path.lineTo(rectF.right, rectF.top);
        path.lineTo(rectF.right, rectF.top + length);
        canvas.drawPath(path, paint);

        path.moveTo(rectF.right, rectF.bottom - length);
        path.lineTo(rectF.right, rectF.bottom);
        path.lineTo(rectF.right - length, rectF.bottom);
        canvas.drawPath(path, paint);

        path.moveTo(rectF.left, rectF.bottom - length);
        path.lineTo(rectF.left, rectF.bottom);
        path.lineTo(rectF.left + length, rectF.bottom);
        canvas.drawPath(path, paint);


        canvas.drawBitmap(scanShape, null, getShapeRecF(width, height), paint);

        if (isRunning) {
            invalidateSelf();
        }
        Log.i("ScanDrawable", String.format("canvas width = %s, height = %s, spent time = %s, offset = %s", canvas.getWidth(), canvas.getHeight(), (System.currentTimeMillis() - start), heightOffset));
    }

    private RectF getShapeRecF(int width, int height) {
        float shapeHeight = width * scanShape.getHeight() / scanShape.getWidth();
        rectF.set(0, -shapeHeight, width, 0);

        heightOffset += 5f;
        if (heightOffset > height + shapeHeight) {
            heightOffset = 0;
        }
        rectF.offset(0, heightOffset);

        return rectF;
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSPARENT;
    }

    @Override
    public void start() {
        if (!isRunning()) {
            isRunning = true;
        }
    }

    @Override
    public void stop() {
        isRunning = false;
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }
}
