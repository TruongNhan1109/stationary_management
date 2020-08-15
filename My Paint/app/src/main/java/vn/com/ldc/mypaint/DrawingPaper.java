package vn.com.ldc.mypaint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;



public class DrawingPaper extends View {

    // Khai báo các thuộc tính
    private android.graphics.Path drawPath;    // Vẽ đường thẳng

    private Paint drawPaint; // Vẽ màu
    private Paint canvasPaint; // Xóa nét vẽ
    private int paintColor = Color.rgb(0, 0, 0);
    private Canvas drawCanvas;  // Canvas
    private Bitmap canvasBitmap;   // Canvas bitmap
    private float brushSize;      // Kích thước cọ vẽ
    private float lastBrushSize;
    private boolean erase = false;  // Trạng thái xóa





    // Phương thức khởi tạo
    public DrawingPaper(Context context, AttributeSet attrs) {
        super(context, attrs);

        setupDrawing();
    }

    // Khởi tạo bản vẽ lúc ban đầu
    private void setupDrawing() {

        // Chuẩn bị màu và kích thước cho các thuộc tính lúc ban đầu

        // Kích thước bút vẽ
        brushSize = 2;

        // Kích thước bút vẽ được chọn lần cuối
        lastBrushSize = brushSize;

        drawPaint = new Paint();
        drawPath = new Path();

        // Thiêt lập màu vẽ
        drawPaint.setColor(paintColor);

        drawPaint.setAntiAlias(true);

        drawPaint.setStrokeWidth(brushSize);

        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
        canvasPaint = new Paint(Paint.DITHER_FLAG);


    }

    // Kích thước chỉ định để xem
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);
    }

    // Vẽ trên giao diện
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
        canvas.drawPath(drawPath, drawPaint);
    }

    // Phương thức sử lý sự kiện khi người dùng chạm
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();

        //respond to down, move and up events
        switch (event.getAction()) {

            // Sự kiện khi bắt đầu chạm vào view
            case MotionEvent.ACTION_DOWN:
                drawPath.moveTo(touchX, touchY);
                break;

             // Sự kiện khi di chuyển trên view đó
            case MotionEvent.ACTION_MOVE:
                drawPath.lineTo(touchX, touchY);
                break;

            // Trả về khi nhấc tay ra khỏi view
            case MotionEvent.ACTION_UP:
                drawPath.lineTo(touchX, touchY);
                drawCanvas.drawPath(drawPath, drawPaint);
                drawPath.reset();
                break;

            default:
                return false;
        }
        //redraw
        invalidate();
        return true;

    }

    // Phương thức cập nhật màu sắc nếu thay đổi
    public void setColor(int newColor) {
        invalidate();

       // paintColor = Color.parseColor(newColor);
        paintColor = newColor;
        drawPaint.setColor(paintColor);
    }

    // Thiêt lập kích thước cọ
    public void setBrushSize(float newSize) {
        float pixelAmount = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                newSize, getResources().getDisplayMetrics());
        brushSize = pixelAmount;
        drawPaint.setStrokeWidth(brushSize);
    }


    // Lấy kích thước cọ cuối
    public void setLastBrushSize(float lastSize){
        lastBrushSize=lastSize;
    }
    public float getLastBrushSize(){
        return lastBrushSize;
    }

    // Thiết lập tẩy
    public void setErase(boolean isErase){
        erase = isErase;

        if(erase) {

          drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        }
        else {
            drawPaint.setXfermode(null);
        }
    }

    // Bắt đầu bản vẽ mới
    //start new drawing
    public void startNew(){
        drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        invalidate();
    }





}
