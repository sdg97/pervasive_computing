
package it.unibo.vuzix.barcode_scan;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.vuzix.sdk.barcode.Location;

/**
 * An image view specifically to draw the barcode to our screen
 */
@SuppressLint("AppCompatCustomView")
public class ScanResultImageView extends ImageView {

    private Location location;
    private Paint locationPaint;

    public ScanResultImageView(Context context) {
        this(context, null);
    }

    public ScanResultImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * The constructor taking all available parameters. Called directly, or by other constructors
     *
     * @param context The Context in which we are operating
     * @param attrs - The AttributeSet or null
     * @param defStyleAttr The int style, or zero
     */
    public ScanResultImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setAdjustViewBounds(true);

        // Setup a Paint object to draw on the scanned barcode with these attributes
        locationPaint = new Paint();
        locationPaint.setColor(Color.MAGENTA);
        locationPaint.setStrokeWidth(5);
        locationPaint.setStyle(Paint.Style.STROKE);
        locationPaint.setStrokeCap(Paint.Cap.ROUND);
        locationPaint.setAntiAlias(true);
    }

    /**
     * Accessor to get the location of the barcode within the image
     * @return Location of barcode
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Mutator to set the location of the barcode within the image, and re-draw accordingly
     *
     * @param location  Location object defining the recognized barcode within the image
     */
    public void setLocation(Location location) {
        this.location = location;
        invalidate();
    }

    /**
     * Override this to draw a box around the barcode at the location it was found
     *
     * @param canvas Canvas upon which to draw
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (location != null) {
            Drawable d = getDrawable();
            if (d instanceof BitmapDrawable) {
                Bitmap b = ((BitmapDrawable)d).getBitmap();
                float scale = (float)getWidth() / b.getWidth();
                drawLine(canvas, location.getPoint1(), location.getPoint2(), scale);
                drawLine(canvas, location.getPoint2(), location.getPoint3(), scale);
                drawLine(canvas, location.getPoint3(), location.getPoint4(), scale);
                drawLine(canvas, location.getPoint4(), location.getPoint1(), scale);
            }
        }
    }

    /**
     * Utility to draw a single line onto the canvas
     *
     * @param canvas Canvas upon which to draw
     * @param p1 Point to start the line
     * @param p2 Point to end the line
     * @param scale float by which to scale the line. Converts from high-res bitmap dimensions to Canvas dimensions
     */
    private void drawLine(Canvas canvas, PointF p1, PointF p2, float scale) {
        canvas.drawLine(p1.x * scale, p1.y * scale, p2.x * scale, p2.y * scale, locationPaint);
    }
}
