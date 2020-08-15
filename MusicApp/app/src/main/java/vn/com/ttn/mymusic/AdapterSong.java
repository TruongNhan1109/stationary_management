package vn.com.ttn.mymusic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import vn.com.ldc.mymusic.R;

public class AdapterSong extends RecyclerView.Adapter<AdapterSong.SongViewHolder> {

    private ArrayList<Song> listSong;
    private Context context;
    private LayoutInflater inflater;

    public AdapterSong(Context context,  ArrayList<Song> list) {
        this.context = context;
        this.listSong = list;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View item = inflater.inflate(R.layout.item_list_song, viewGroup, false);
        return new SongViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder songViewHolder, int i) {
        final Song song = listSong.get(i);

        songViewHolder.tvNameSong.setText(song.getNameSong());

        // Bắt sự kiện click vào một item
        songViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickedListener.onItemClick(song.getIdSong());
            }
        });

    }

    @Override
    public int getItemCount() {
        return listSong.size();
    }

    // View Holder Song
    class SongViewHolder extends RecyclerView.ViewHolder {

        private TextView tvNameSong;

        public SongViewHolder(View itemView) {
            super(itemView);

            tvNameSong = (TextView) itemView.findViewById(R.id.tvListNameSong);
        }

    }


    // Khai báo một phương thức  để ta có thể gọi nó bên ngoài để lấy được dữ liệu
    public interface OnItemClickedListener {
        void onItemClick(int idSong);
    }

    // Khai báo một biến interface
    private OnItemClickedListener onItemClickedListener;

    // Tạo setter cho biến interface ta vừa tạo
    public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener) {

        this.onItemClickedListener = onItemClickedListener;
    }

}
