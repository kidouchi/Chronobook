package kidouchi.chronobook.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by iuy407 on 1/5/16.
 */
public class RoundedCornersImageView extends ImageView {

    private static float radius = 36.0f;

    public RoundedCornersImageView(Context context) {
        super(context);
    }

    public RoundedCornersImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RoundedCornersImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public RoundedCornersImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Path path = new Path();
        RectF rect = new RectF(0, 0, this.getWidth(), this.getHeight());
        // 'CW' --> Clockwise
        path.addRoundRect(rect, radius, radius, Path.Direction.CW);
        canvas.clipPath(path);
        super.onDraw(canvas);
    }
}
