package com.ttn.stationarymanagement.presentation.dialog_fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;
import com.ttn.stationarymanagement.R;
import com.ttn.stationarymanagement.data.local.model.VanPhongPham;
import com.ttn.stationarymanagement.presentation.baseview.FullScreenDialog;
import com.ttn.stationarymanagement.utils.CustomToast;
import com.ttn.stationarymanagement.utils.GetDataToCommunicate;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShowDetailProductDialog extends FullScreenDialog {

    @BindView(R.id.tv_dialog_show_detail_product_name_product)
    TextView tvNameProduct;

    @BindView(R.id.ibt_dialog_show_detail_product_close)
    ImageButton ibtRemove;

    @BindView(R.id.iv_dialog_show_detail_product_image)
    ImageView ivPicture;

    @BindView(R.id.tv_dialog_show_detail_product_amount)
    TextView tvAmount;

    @BindView(R.id.tv_dialog_show_detail_product_price)
    TextView tvPrice;

    @BindView(R.id.tv_dialog_show_detail_product_unit)
    TextView tvUnit;

    @BindView(R.id.tv_dialog_show_detail_product_note)
    TextView tvNote;

    private VanPhongPham product;

    public static ShowDetailProductDialog newInstance() {
        ShowDetailProductDialog fragment = new ShowDetailProductDialog();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.DialogTheme);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_show_detail_product, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (product != null) {

            tvNameProduct.setText(product.getTenSP());
            tvAmount.setText(product.getSoLuong() + "");

            if (!TextUtils.isEmpty(product.getAnh())) {
                Picasso.get().load(new File(product.getAnh())).error(R.mipmap.app_icon).fit().centerInside().into(ivPicture);
            } else {
                ivPicture.setImageResource(R.drawable.ic_part_color_24);
            }

            tvPrice.setText("Đơn giá: " + GetDataToCommunicate.changeToPrice(product.getDonGia()));

            tvUnit.setText(!TextUtils.isEmpty(product.getDonVi()) ? "Đơn vị: " + product.getDonVi()  : "");

            if (!TextUtils.isEmpty(product.getGhiChu())) {
                tvNote.setVisibility(View.VISIBLE);
                tvNote.setText("Ghi chú: " + product.getGhiChu());
            } else {
                tvNote.setVisibility(View.GONE);
            }

        } else {
            CustomToast.showToastError(getContext(), "Không lấy được thông tin sản phẩm", Toast.LENGTH_SHORT);
            dismiss();
        }

        ibtRemove.setOnClickListener(v -> dismiss());
    }


    public void setProduct(VanPhongPham product) {
        this.product = product;
    }
}
