package vn.com.ttn.mymusic;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Bundle;
import android.transition.Slide;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import vn.com.ldc.mymusic.R;

public class MainActivity extends AppCompatActivity {

    // Khai bâo các đối tượng

    private TextView tvNameSong;
    private TextView tvTimeStart;
    private TextView tvTimeTotal;
    private SeekBar sbTime;
    private ImageButton btnPrev;
    private ImageButton btnPlay;
    private  ImageButton btnNext;
    private ImageButton btnList;
    private TextView tvStatus;
    LinearLayout layout_transition;

    static ArrayList <Song> listSong;
    int index = 0;
    MediaPlayer mediaPlayer;

    Animation animationRound;
    Animation animationSlideUp;
    Animation animationFadeIn;
    Animation animationMove;

   // ImageView imvDisk;

    ImageButton btnDisk;




    // Phương thức oncreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setControl();

        importSong();

        setEvent();


    }

    // Phương thức set control
    public void setControl() {

        tvNameSong = (TextView) findViewById(R.id.tvNameSong);
        tvTimeStart = (TextView) findViewById(R.id.tvTimeStart);
        tvTimeTotal = (TextView)findViewById(R.id.tvTimeTotal);

        btnNext = (ImageButton) findViewById(R.id.btnNext);
        btnPlay = (ImageButton) findViewById(R.id.btnPlay);
        btnPrev = (ImageButton) findViewById(R.id.btnPrev);
        sbTime = (SeekBar) findViewById(R.id.sbTime);
        btnList = (ImageButton) findViewById(R.id.btnList);
        listSong = new ArrayList<Song>();

        // Load animation
        animationRound = AnimationUtils.loadAnimation(this, R.anim.disround);
        animationSlideUp = AnimationUtils.loadAnimation(this, R.anim.anim_slide_up);
        animationFadeIn = AnimationUtils.loadAnimation(this, R.anim.anim_fade_in);
        animationMove = AnimationUtils.loadAnimation(this, R.anim.anim_move);


        tvStatus = (TextView) findViewById(R.id.tvStatus) ;
       // imvDisk = (ImageView) findViewById(R.id.ivCd);
        btnDisk = (ImageButton)  findViewById(R.id.btnDisk);
        layout_transition = (LinearLayout) findViewById(R.id.layout_transition);




    }

    // Phuong thức thêm nhạc vào danh sách
    public void importSong() {

       listSong.add(new Song(0,"Fox rain", R.raw.fox_rain));
        listSong.add(new Song(1,"Give me your love", R.raw.givemeyourlove));
        listSong.add(new Song(2,"How to make windows", R.raw.howtomake));
        listSong.add(new Song(3,"Vô tình", R.raw.vo_tinh));
        listSong.add(new Song(4,"See you again", R.raw.se_you_again));
        listSong.add(new Song(5,"Nơi ta chờ em", R.raw.noi_ta_cho_em));
        listSong.add(new Song(6,"Có chắc là yêu đây", R.raw.cochaclayeuday));
        listSong.add(new Song(7,"Em của ngày hôm qua ", R.raw.emcuangayhomqua));
        listSong.add(new Song(8,"Sau tất cả", R.raw.sautatca));

        // Khởi tạo giá trị ban đầu cho media play
        mediaPlayer = MediaPlayer.create(MainActivity.this, listSong.get(index).getFile());


    }

    // Phương thức sử lý các sự kiện
   public void setEvent() {

        // Sự kiện nút play
       btnPlay.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               if (mediaPlayer.isPlaying()) {
                   mediaPlayer.pause();
                   btnPlay.setImageResource(R.drawable.ic_play);

                  //imvDisk.clearAnimation();
                   btnDisk.clearAnimation();

                   // Cập nhật trạng thái status
                   tvStatus.setVisibility(View.INVISIBLE);
                   tvStatus.setText("Pause");

                   TransitionManager.beginDelayedTransition(layout_transition,new  Slide(Gravity.END));
                   tvStatus.setVisibility(View.VISIBLE);
                   tvNameSong.clearAnimation();


               } else {

                   mediaPlayer.start();
                   setTimeTotal();
                   tvNameSong.setText(listSong.get(index).getNameSong());
                   btnPlay.setImageResource(R.drawable.ic_pause);
                   updateTime();

                   // Cập nhập trạng thái status
                   tvStatus.setVisibility(View.INVISIBLE);
                   tvStatus.setText("Now play");

                   TransitionManager.beginDelayedTransition(layout_transition,new  Slide(Gravity.END));
                   tvStatus.setVisibility(View.VISIBLE);


                   // Set animation cho hinh
                   //imvDisk.startAnimation(animationRound);
                   btnDisk.startAnimation(animationRound);
                   tvNameSong.startAnimation(animationMove);


               }


           }
       });


       // Sự kiện nút next
       btnNext.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               index++;

               mediaPlayer.stop();
               if (index > listSong.size() - 1) {
                   index = 0;
               }

               mediaPlayer = MediaPlayer.create(MainActivity.this, listSong.get(index).getFile());

               tvNameSong.startAnimation(animationMove);
               tvNameSong.setText(listSong.get(index).getNameSong());

               btnPlay.setImageResource(R.drawable.ic_pause);

               mediaPlayer.start();


               setTimeTotal();

               updateTime();

               //imvDisk.startAnimation(animationRound);
               btnDisk.startAnimation(animationRound);


               // Cập nhật trạng thái status
               tvStatus.setVisibility(View.INVISIBLE);
               tvStatus.setText("Next song");

               TransitionManager.beginDelayedTransition(layout_transition,new  Slide(Gravity.END));
               tvStatus.setVisibility(View.VISIBLE);

               // Cập nhật status thao tác
               updateStatus();


           }
       });


       // Sự kiện nút back
       btnPrev.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               index--;

               mediaPlayer.stop();
               if (index < 0) {
                   index = listSong.size()-1;
               }

               mediaPlayer = MediaPlayer.create(MainActivity.this, listSong.get(index).getFile());


               tvNameSong.startAnimation(animationMove);
               tvNameSong.setText(listSong.get(index).getNameSong());

               mediaPlayer.start();
               btnPlay.setImageResource(R.drawable.ic_pause);
               setTimeTotal();
               updateTime();

               // Bắt đầu animation
             //  imvDisk.startAnimation(animationRound);
               btnDisk.startAnimation(animationRound);

               // Cập nhật trạng thái status nút bấm
               tvStatus.setVisibility(View.INVISIBLE);
               tvStatus.setText("Prev song");

               TransitionManager.beginDelayedTransition(layout_transition,new  Slide(Gravity.END));
               tvStatus.setVisibility(View.VISIBLE);

               // Cập nhật status
               updateStatus();

           }


       });

       // Sự kiện seekbar time
       sbTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
           @Override
           public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

           }

           @Override
           public void onStartTrackingTouch(SeekBar seekBar) {

           }

           @Override
           public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(sbTime.getProgress());
           }
       });


       // Sụ kiện nút list
       btnList.setOnClickListener(new View.OnClickListener() {

           @Override
           public void onClick(View view) {

               Intent intent = new Intent(MainActivity.this, ListActivity.class);
               startActivityForResult(intent, 1);

           }
       });


       // Sự kiện nút đĩa
       btnDisk.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               if (mediaPlayer.isPlaying()) {
                   mediaPlayer.pause();
                   btnPlay.setImageResource(R.drawable.ic_play);

                   //imvDisk.clearAnimation();
                   btnDisk.clearAnimation();

                   // Cập nhật trạng thái status
                   tvStatus.setVisibility(View.INVISIBLE);
                   tvStatus.setText("Pause");

                   TransitionManager.beginDelayedTransition(layout_transition,new  Slide(Gravity.END));
                   tvStatus.setVisibility(View.VISIBLE);
                   tvNameSong.clearAnimation();


               } else {

                   mediaPlayer.start();
                   setTimeTotal();
                   tvNameSong.setText(listSong.get(index).getNameSong());
                   btnPlay.setImageResource(R.drawable.ic_pause);
                   updateTime();

                   // Cập nhập trạng thái status
                   tvStatus.setVisibility(View.INVISIBLE);
                   tvStatus.setText("Now play");

                   TransitionManager.beginDelayedTransition(layout_transition,new  Slide(Gravity.END));
                   tvStatus.setVisibility(View.VISIBLE);


                   // Set animation cho hinh
                   //imvDisk.startAnimation(animationRound);
                   btnDisk.startAnimation(animationRound);
                   tvNameSong.startAnimation(animationMove);
               }
           }
       });
   }

   // Tính tổng thời gian của một bài hát
   public void setTimeTotal() {
       SimpleDateFormat formatHour = new SimpleDateFormat("mm:ss");

        tvTimeTotal.setText(formatHour.format(mediaPlayer.getDuration()) + "");

        // Gán giá trị cho seekbar
       sbTime.setMax(mediaPlayer.getDuration());
   }

   // Cập nhật thời gian thực của bài đang phát
   public void updateTime() {
       final Handler handler = new Handler();
       handler.postDelayed(new Runnable() {
           @Override
           public void run() {

               SimpleDateFormat formatHour = new SimpleDateFormat("mm:ss");
               tvTimeStart.setText(formatHour.format(mediaPlayer.getCurrentPosition()) + "");

               // Cập nhật tiến trình seekbar
               sbTime.setProgress(mediaPlayer.getCurrentPosition());

               // Kiểm tra thời gian bài hát
               mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                   @Override
                   public void onCompletion(MediaPlayer mp) {
                       index++;
                       mediaPlayer.stop();
                       if (index > listSong.size() - 1) {
                           index = 0;
                       }

                       mediaPlayer = MediaPlayer.create(MainActivity.this, listSong.get(index).getFile());
                       tvNameSong.setText(listSong.get(index).getNameSong());

                       btnPlay.setImageResource(R.drawable.ic_pause);

                       mediaPlayer.start();
                       setTimeTotal();
                       updateTime();
                   }
               });

               handler.postDelayed(this, 500);


           }
       }, 100);


   }

   // Cập nhật trạng thái nhãn sau khi nhấn nút
   public void updateStatus() {
       TimerTask timerTask = new TimerTask() {
           @Override
           public void run() {
               MainActivity.this.runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       tvStatus.setText("Now play");
                   }
               });
           }
       };

       Timer timer = new Timer();
       timer.schedule(timerTask, 3000);
   }


   // Nhần kết quả trả về từ màn hình khác
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {


        super.onActivityResult(requestCode, resultCode, data);

        // Kiểm tra nếu dữ liệu trả về rỗng
        if (data == null) {
            return;
        }

        // Nhận id bài hát được gửi về
        if (requestCode == 1 && resultCode == 1) {

            index = data.getIntExtra("id", 0);

            mediaPlayer.stop();
            mediaPlayer = MediaPlayer.create(MainActivity.this, listSong.get(index).getFile());

            tvNameSong.startAnimation(animationFadeIn);
            tvNameSong.setText(listSong.get(index).getNameSong());

            btnPlay.setImageResource(R.drawable.ic_pause);

            mediaPlayer.start();
            setTimeTotal();
            updateTime();
          //  imvDisk.startAnimation(animationRound);
            btnDisk.startAnimation(animationRound);


            // Cập nhật trạng thái status
            tvStatus.setVisibility(View.INVISIBLE);
            tvStatus.setText("Now play");

            TransitionManager.beginDelayedTransition(layout_transition,new  Slide(Gravity.END));
            tvStatus.setVisibility(View.VISIBLE);


        }
    }
}
