package vn.com.ttn.mymusic;

import android.content.Intent;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.animators.adapters.SlideInBottomAnimationAdapter;
import vn.com.ldc.mymusic.R;


public class ListActivity extends AppCompatActivity {

    ArrayList<Song> listSong;
    AdapterSong adapterSong;
    RecyclerView rvListSong;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Hiển thị nút back
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setControl();

        // Xử lý sự kiện khi click vào một item trong list
        adapterSong.setOnItemClickedListener(new AdapterSong.OnItemClickedListener() {
            @Override
            public void onItemClick(int idSong) {
                Intent intent = getIntent();
                intent.putExtra("id", idSong);
                setResult(1, intent);
                finish();
            }
        });



    }


    // Phuong thức set control
    public void setControl() {
        rvListSong = (RecyclerView) findViewById(R.id.rvListSong);
        listSong = new ArrayList<>();
        listSong = MainActivity.listSong;
        adapterSong = new AdapterSong(this, listSong);

        LinearLayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        rvListSong.setLayoutManager(layout);
        rvListSong.setAdapter(new SlideInBottomAnimationAdapter(adapterSong));


    }


    // Bắt sự kiên trên tool bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                break;

        }

        return super.onOptionsItemSelected(item);
    }

}
