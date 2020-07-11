package com.ttn.stationarymanagement.presentation.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.ttn.stationarymanagement.R;
import com.ttn.stationarymanagement.data.local.WorkWithDb;
import com.ttn.stationarymanagement.data.local.model.CapPhat;
import com.ttn.stationarymanagement.data.local.model.NhanVien;
import com.ttn.stationarymanagement.data.local.model.PhongBan;
import com.ttn.stationarymanagement.presentation.baseview.BaseFragment;
import com.ttn.stationarymanagement.utils.CustomToast;
import com.ttn.stationarymanagement.utils.SortMapStaft;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DepartmentStatisticFragment extends BaseFragment {


    @BindView(R.id.barchart)
    BarChart chart;

    private CompositeDisposable compositeDisposable;

    public static DepartmentStatisticFragment newInstance() {
        Bundle args = new Bundle();
        DepartmentStatisticFragment fragment = new DepartmentStatisticFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_department_statistic, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setControls();
        getData();

    }

    private void setControls() {

        compositeDisposable = new CompositeDisposable();
    }

    private void getData() {

        Map<Long, List<CapPhat>> groupByStaftId = new HashMap<>();
        Map<Long, Integer> groupStaftByDepartment = new HashMap<>();

        Observable<List<CapPhat>> getAllBills = Observable.just(WorkWithDb.getInstance().getAllAllocation());
        compositeDisposable.add(getAllBills.subscribeOn(Schedulers.newThread()).flatMap(capPhats -> Observable.fromIterable(capPhats))
                .observeOn(AndroidSchedulers.mainThread()).subscribe(capPhat -> {

                    if (groupByStaftId.get(capPhat.getMaNV()) == null) {

                        List<CapPhat> listBills = new ArrayList<>();
                        listBills.add(capPhat);
                        groupByStaftId.put(capPhat.getMaNV(), listBills);

                    } else {

                        List<CapPhat> listBills = groupByStaftId.get(capPhat.getMaNV());
                        listBills.add(capPhat);

                    }
        }, throwable -> CustomToast.showToastError(getContext(), "Đã xảy ra lỗi", Toast.LENGTH_SHORT)
        , () -> {

                    for(Map.Entry<Long, List<CapPhat>> entry: groupByStaftId.entrySet()) {
                        NhanVien nhanVien = WorkWithDb.getInstance().getStaftById(entry.getKey());

                        if (nhanVien == null) {
                            continue;
                        }

                        int total = 0;
                        for(CapPhat capPhat: entry.getValue()) {
                            total += capPhat.getSoLuong();
                        }

                        if (groupStaftByDepartment.get(nhanVien.getMaPB()) == null) {
                            groupStaftByDepartment.put(nhanVien.getMaPB(), total);
                        } else {
                            int currentTotal = groupStaftByDepartment.get(nhanVien.getMaPB());
                            currentTotal += total;
                            groupStaftByDepartment.put(nhanVien.getMaPB(), currentTotal);
                        }

                    }

                    SortMapStaft sortMapStaft = new SortMapStaft(groupStaftByDepartment);
                    Map<Long, Integer> result = new TreeMap<Long, Integer>(sortMapStaft);
                    result.putAll(groupStaftByDepartment);

                    ArrayList listRooms = new ArrayList();
                    List<String> listNameDepartment = new ArrayList<>();

                    int index = 0;
                    for(Map.Entry<Long, Integer> entry: result.entrySet()) {

                        PhongBan phongBan = WorkWithDb.getInstance().getDepartmentById(entry.getKey());
                        listRooms.add(new BarEntry(entry.getValue(), index++));

                        if (phongBan != null) {
                            listNameDepartment.add(phongBan.getTenPB());
                        }

                        if (index == 7) {
                            break;
                        }
                    }

                    BarDataSet bardataset = new BarDataSet(listRooms, "Số lượng cấp phát theo phòng ban");
                    chart.animateY(3000);

                    BarData data = new BarData(listNameDepartment, bardataset);
                    bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
                    chart.setBackgroundColor(Color.WHITE);
                    chart.setNoDataText("Chưa có số liệu");
                    chart.setData(data);

                }));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }
}
