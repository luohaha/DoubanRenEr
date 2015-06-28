package com.lyx.doubanrener.doubanrener.MaterialDesign;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by root on 15-6-28.
 */
public class RoundImageView extends ImageView {

    private Path mPath = new Path();

    public interface LockScreenLayoutListener
    {
        public void onUnLock();
    }

    public RoundImageView(Context context) {
        this(context, null);
    }

    public RoundImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        float cx = getMeasuredWidth() / 2;
        float cy = getMeasuredHeight() / 2;
        float cr = cx < cy ? cx : cy;

        mPath.reset();
        mPath.addCircle(cx, cy, cr, Path.Direction.CCW);
        canvas.clipPath(mPath);
        super.onDraw(canvas);
            /*
            paint.setStrokeWidth(cr/5);
            paint.setAntiAlias(true);
            paint.setStyle(Style.STROKE);
            paint.setColor(Color.WHITE);

            canvas.drawCircle(cx, cy, 49, paint);
            paint.setAlpha((int) (255*0.71));
            canvas.drawCircle(cx, cy, 50, paint);
            canvas.drawCircle(cx, cy, cr, gradientPaint);

            paint.setStrokeWidth(10);
            paint.setColor(Color.BLACK);
            paint.setAlpha((int) (255*0.51));
            canvas.drawCircle(cx, cy, 52, paint);

            paint.setXfermode(new PorterDuffXfermode(Mode.SRC_OUT));
            paint.setColor(Color.RED);
            canvas.drawCircle(cx, cy, 53, paint);
                */
    }

}
