package com.ttn.stationarymanagement.presentation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ttn.stationarymanagement.R;
import com.ttn.stationarymanagement.presentation.model.HomeModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AppFunctionAdapter extends RecyclerView.Adapter<AppFunctionAdapter.AppFunctionViewHolder> {

    private Context mContext;
    private List<HomeModel> listFunctions;
    private LayoutInflater layoutInflater;

    public AppFunctionAdapter(Context context, List<HomeModel> list) {

        this.mContext = context;
        this.listFunctions= list;
        layoutInflater = layoutInflater.from(mContext);

    }

    @NonNull
    @Override
    public AppFunctionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.item_app_function, parent, false);
        return new AppFunctionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AppFunctionViewHolder holder, int position) {
        HomeModel functionItem = listFunctions.get(position);

        holder.tvName.setText(functionItem.getSettingName());
        holder.ivIcon.setImageResource(functionItem.getIcon());

        holder.itemView.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onItemClick(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listFunctions.size();
    }

    public class  AppFunctionViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_item_app_function_name)
        TextView tvName;

        @BindView(R.id.iv_item_app_function_icon)
        ImageView ivIcon;

        public AppFunctionViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public  interface AddFuntionAdapterListener {
        public void onItemClick(int position);
    }

    private AddFuntionAdapterListener mListener;

    public void setListener(AddFuntionAdapterListener listener){
        this.mListener = listener;
    }
}
