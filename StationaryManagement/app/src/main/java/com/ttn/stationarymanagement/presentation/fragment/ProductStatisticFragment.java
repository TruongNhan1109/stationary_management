package com.ttn.stationarymanagement.presentation.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ProductStatisticFragment extends BaseFragment {

    @BindView(R.id.chart_product_statistic)
    AnyChartView productChart;

    @BindView(R.id.progress_bar)
    ProgressBar pvBar;

    @BindView(R.id.tv_fragment_product_statistic_notify)
    TextView tvNotify;

    private List<VanPhongPham> listProductTopUse;
    private List<DataEntry> listDataChart;

    private CompositeDisposable compositeDisposable;


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

        compositeDisposable.add( getTopProduct.subscribeOn(Schedulers.newThread()).flatMap(vanPhongPhams -> Observable.fromIterable(vanPhongPhams)
        ).observeOn(AndroidSchedulers.mainThread()).subscribe(vanPhongPham -> {
            listDataChart.add(new ValueDataEntry(vanPhongPham.getTenSP(), vanPhongPham.getDaDung()));
        }, throwable -> {
            CustomToast.showToastError(getContext(), getResources().getString(R.string.occurre_error) ,Toast.LENGTH_SHORT);
        }, () -> {

            if (listDataChart.size() > 0) {
                tvNotify.setVisibility(View.GONE);
                productChart.setVisibility(View.VISIBLE);
                pvBar.setVisibility(View.VISIBLE);

                setChart();
            } else {
                tvNotify.setVisibility(View.VISIBLE);
                productChart.setVisibility(View.GONE);
                pvBar.setVisibility(View.GONE);
            }

        }));

    }

    private void setControls() {

        compositeDisposable = new CompositeDisposable();
        productChart.setProgressBar(pvBar);
        listProductTopUse = new ArrayList<>();
        listDataChart = new  ArrayList<>();

    }

    public void setChart() {

        Pie pieProduct = AnyChart.pie();
        pieProduct.data(listDataChart);
        pieProduct.title(getResources().getString(R.string.most_used_products));
        pieProduct.labels().position("outside");

        pieProduct.legend().title().enabled(true);
        pieProduct.legend().title()
                .text(getResources().getString(R.string.products))
                .padding(0d, 0d, 10d, 0d);

        pieProduct.legend()
                .position("center-bottom")
                .itemsLayout(LegendLayout.HORIZONTAL)
                .align(Align.CENTER);

        productChart.setChart(pieProduct);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }
}
