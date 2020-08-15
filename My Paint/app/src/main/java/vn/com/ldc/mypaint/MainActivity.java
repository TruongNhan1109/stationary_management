package vn.com.ldc.mypaint;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.IOException;
import java.util.UUID;


public class MainActivity extends AppCompatActivity {

    private DrawingPaper drawPaper;
    private ImageButton btnClear;
    private  ImageButton btnErase;
    private ImageButton btnDraw;
    private SeekBar sbSize;
    private ImageButton btnZoom;
    private ImageButton btnPalette;
    private ImageButton btnPhoto;
    int DefaultColor;
    private TextView tvStatus;
    private ImageButton btnSave;


    ScaleGestureDetector scaleGestureDetector;


    // Phương thức onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setControl();

        setFunction();


        // Khởi tạo đối tượng định nghĩa thao tác scale
        scaleGestureDetector = new ScaleGestureDetector(this, new MyGesture  ());



        // Xin cấp quyền khi khởi chạy ứng dụng
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }


    }


    // Set control
    public void setControl() {
        drawPaper = (DrawingPaper) findViewById(R.id.drawing);
       btnDraw = (ImageButton) findViewById(R.id.btnBrush) ;
        btnClear = (ImageButton) findViewById(R.id.btnClear);
        btnErase = (ImageButton) findViewById(R.id.btnErase);
        sbSize = (SeekBar) findViewById(R.id.sbSizeBrush);
        btnPalette = (ImageButton) findViewById(R.id.btnPalette);
        btnZoom = (ImageButton) findViewById(R.id.btnZoom);
        tvStatus = (TextView) findViewById(R.id.tvStatus);
        btnSave = (ImageButton) findViewById(R.id.btnSave);
        btnPhoto = (ImageButton) findViewById(R.id.btnPhoto);
    }

    // Set function
    public  void setFunction() {

        // Sự kiện khi chọn màu
        btnPalette.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenColorPickerDialog(false);
            }
        });


        // Sự kiện khi thay đổi kích thước cọ bằng seekbar
        sbSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                drawPaper.setBrushSize(i);
                Toast.makeText(getApplicationContext(), "Kích thước nét vẽ: "  + i , Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        // Sự kiện khi nhấn vào bút vẽ
        btnDraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btnDraw.setImageResource(R.drawable.ic_brush_blue);
                btnErase.setImageResource(R.drawable.ic_format_paint);
                btnZoom.setImageResource(R.drawable.ic_zoompage_black);

                // Ẩn trạng thái không dùng tẩy
                drawPaper.setErase(false);

                // Thiết lập kích thước cọ vẽ
                drawPaper.setBrushSize(sbSize.getProgress());

                // Thiết lập tiêu đề status
                tvStatus.setText("Chế độ vẽ");

                // Hủy trạng thái ghi đè sự kiện on touch
                drawPaper.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        return false;
                    }
                });

            }
        });

        // Sự kiện khi nhấn vào tẩy
        btnErase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btnDraw.setImageResource(R.drawable.ic_brush_black);
                btnErase.setImageResource(R.drawable.ic_format_blue);
                btnZoom.setImageResource(R.drawable.ic_zoompage_black);

                drawPaper.setErase(true);

                // Thiết lập tiêu đề status
                tvStatus.setText("Chế độ tẩy");


                // Hủy trạng thái ghi đè sự kiện on touch
                drawPaper.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        return false;
                    }
                });

                // Thiết lập kích thước tẩy
                drawPaper.setBrushSize(13);

            }
        });



        // Sự kiện khi nhấn nút zoom
        btnZoom.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {


                // Set trạng thái cho các nút
                btnDraw.setImageResource(R.drawable.ic_brush_black);
                btnErase.setImageResource(R.drawable.ic_format_paint);
                btnZoom.setImageResource(R.drawable.ic_zoompage_blue);

                drawPaper.setErase(false);

                // Thiết lập tiêu đề status
                tvStatus.setText("Chế độ thu phóng");


                // Ghi đè phương thức chạm
                drawPaper.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        scaleGestureDetector.onTouchEvent(motionEvent);
                        return true;
                    }
                });

            }
        });

        // Sự kiện khi nhấn giữ nút zoom: Để khôi phục lại tỉ lệ ban đầu
        btnZoom.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                drawPaper.setScaleY(1f);
                drawPaper.setScaleX(1f);
                Toast.makeText(getApplicationContext(), "Tỉ lệ đã trở về mặc định", Toast.LENGTH_SHORT).show();
                return true;
            }
        });


        // Sự kiện nút clear
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("Tạo bản vẽ mới");
                dialog.setMessage("Bạn có muốn tạo bản vẽ mới không?");

                // Sự kiện khi chọn nút trong dialog
                dialog.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        drawPaper.startNew();

                        // Set lại các giá trị ban đầu
                        drawPaper.setColor(Color.rgb(0, 0, 0));
                        drawPaper.setBrushSize(1);
                        sbSize.setProgress(1);
                        drawPaper.setErase(false);

                        btnDraw.setImageResource(R.drawable.ic_brush_blue);
                        btnErase.setImageResource(R.drawable.ic_format_paint);
                        btnZoom.setImageResource(R.drawable.ic_zoompage_black);
                        drawPaper.setBackgroundColor(Color.rgb(255, 255, 255));

                        // Thiết lập lại tỉ lệ bản vẽ
                        drawPaper.setScaleX(1f);
                        drawPaper.setScaleY(1f);

                        // Thiết lập lại tiêu đề
                        tvStatus.setText("Chế độ vẽ");

                        dialog.setCancelable(true);

                    }
                });

                dialog.setNegativeButton("Thôi khỏi", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialog.setCancelable(true);
                    }
                });

                dialog.show();

            }
        });


        // Sự kiện nút chọn ảnh
        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();

                // Chỉ định kiểu file cần hiển thị
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);

                // Hiển thị các ứng dụng có thể xử lý ảnh
                startActivityForResult(Intent.createChooser(intent, "Chọn hỉnh ảnh"), 1);


            }
        });

        // Sự kiện khi nhấn nút lưu
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder saveDialog = new AlertDialog.Builder(MainActivity.this);

                saveDialog.setTitle("Lưu hình ảnh");
                saveDialog.setMessage("Bạn có muốn lưu bản vẻ này không?");

                // Sự kiện khi chọn Lưu
                saveDialog.setPositiveButton("Lưu", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Lưu bản vẽ
                        drawPaper.setDrawingCacheEnabled(true);

                        // Địa chỉ lưu
                        String imgSaved = MediaStore.Images.Media.insertImage(getContentResolver(), drawPaper.getDrawingCache(), UUID.randomUUID().toString() +".png", "My paint");

                        // Thông báo kết quả lưu
                        if (imgSaved != null) {
                            Toast.makeText(getApplicationContext(), "Lưu thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), ":( Đã xảy ra sự cố khi lưu", Toast.LENGTH_SHORT).show();
                        }

                        // Xóa cache
                        drawPaper.destroyDrawingCache();

                    }
                });

                saveDialog.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        saveDialog.setCancelable(true);
                    }
                });

                saveDialog.show();

            }
        });


    }


    private void OpenColorPickerDialog(boolean AlphaSupport) {

        AmbilWarnaDialog ambilWarnaDialog = new AmbilWarnaDialog(MainActivity.this, DefaultColor, AlphaSupport, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onOk(AmbilWarnaDialog ambilWarnaDialog, int color) {

                DefaultColor = color;

                // Set lại màu vẽ
                drawPaper.setColor(color);


            }

            // Sự kiện nhấn nút cancel
            @Override
            public void onCancel(AmbilWarnaDialog ambilWarnaDialog) {

              //  Toast.makeText(MainActivity.this, "Đóng cửa sổ chọn màu", Toast.LENGTH_SHORT).show();
            }
        });
        ambilWarnaDialog.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        // Nếu kết quả trả về là rỗng
        if (data == null) {
            return;
        }

        // Nhận kết quả từ thư viện chọn ảnh
        if (requestCode == 1 && resultCode == RESULT_OK) {
            

            // Lấy địa chỉ hình ảnh
            Uri uri = data.getData();

            // Lấy hình ảnh
            try {

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

              //  BitmapDrawable background = new BitmapDrawable(getResources(), bitmap);
                Drawable drawable = new BitmapDrawable(getResources(), bitmap);

                // Set hình ảnh cho đối tượng cần set
                drawPaper.setBackground(drawable);



            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    class MyGesture extends  ScaleGestureDetector.SimpleOnScaleGestureListener {

        float scale = 1.0f;
        float onScaleStart  = 0;
        float onScaleEnd = 0;

        @Override
        public boolean onScale(ScaleGestureDetector detector) {

            scale *= detector.getScaleFactor();
            drawPaper.setScaleX(scale);
            drawPaper.setScaleY(scale);
            return super.onScale(detector);
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            onScaleStart = scale;
            return super.onScaleBegin(detector);
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {

            onScaleEnd = scale;
            super.onScaleEnd(detector);
        }
    }





}
