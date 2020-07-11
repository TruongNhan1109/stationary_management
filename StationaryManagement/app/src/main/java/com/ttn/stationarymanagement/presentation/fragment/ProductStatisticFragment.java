package com.ttn.stationarymanagement.presentation.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.chart.common.listener.Event;
import com.anychart.chart.common.listener.ListenersInterface;
import com.anychart.charts.Pie;
import com.anychart.enums.Align;
import com.anychart.enums.LegendLayout;
import com.ttn.stationarymanagement.R;
import com.ttn.stationarymanagement.data.local.WorkWithDb;
import com.ttn.stationarymanagement.data.local.model.VanPhongPham;
import com.ttn.stationarymanagement.presentation.baseview.BaseFragment;
import com.ttn.stationarymanagement.utils.CustomToast;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ProductStatisticFragment extends BaseFragment {

    @BindView(R.id.chart_product_statistic)
    AnyChartView productChart;

    @BindView(R.id.progress_bar)
    ProgressBar pvBar;

    private List<VanPhongPham> listProductTopUse;
    private List<DataEntry> listDataChart;


    public static ProductStatisticFragment newInstance() {
        Bundle args = new Bundle();
        ProductStatisticFragment fragment = new ProductStatisticFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_statistic, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setControls();
        getTopProducts();
    }


    private void getTopProducts() {

        Observable<List<VanPhongPham>> getTopProduct = Observable.create(r -> {
            List<VanPhongPham> listTopProducts = WorkWithDb.getInstance().getTopProducts();
            r.onNext(listTopProducts);
            r.onComplete();

        });

        getTopProduct.subscribeOn(Schedulers.newThread()).flatMap(vanPhongPhams -> Observable.fromIterable(vanPhongPhams)
        ).observeOn(AndroidSchedulers.mainThread()).subscribe(vanPhongPham -> {

            listDataChart.add(new ValueDataEntry(vanPhongPham.getTenSP(), vanPhongPham.getDaDung()));

        }, throwable -> {

            CustomToast.showToastError(getContext(), "Đã xảy ra lỗi", Toast.LENGTH_SHORT);

        }, () -> {

            CustomToast.showToastSuccesstion(getContext(), "Đã hoàn tất", Toast.LENGTH_SHORT);
            setChart();

        });

    }

    private void setControls() {
        productChart.setProgressBar(pvBar);

        listProductTopUse = new ArrayList<>();
        listDataChart = new  ArrayList<>();
    }

    public void setChart() {

        Pie pieProduct = AnyChart.pie();
        pieProduct.data(listDataChart);
        pieProduct.title("Fruits imported in 2015 (in kg)");
        pieProduct.labels().position("outside");

        pieProduct.legend().title().enabled(true);
        pieProduct.legend().title()
                .text("Retail channels")
                .padding(0d, 0d, 10d, 0d);

        pieProduct.legend()
                .position("center-bottom")
                .itemsLayout(LegendLayout.HORIZONTAL)
                .align(Align.CENTER);

        productChart.setChart(pieProduct);

    }
}
